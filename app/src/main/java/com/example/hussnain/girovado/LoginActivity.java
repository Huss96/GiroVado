package com.example.hussnain.girovado;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @BindView(R.id.input_email) EditText userText;
    @BindView(R.id.input_password) EditText passwordText;
    @BindView(R.id.btn_login) Button loginButton;
    @BindView(R.id.link_signup) TextView signupLink;
    @BindView(R.id.link_home) TextView linkHome;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                //overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        linkHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, StartActivity.class));
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.Theme_AppCompat_Light_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String userID = userText.getText().toString();
        String password = passwordText.getText().toString();
        try{
            DBConnection dbConnection = new DBConnection();
            String query = "Select name from dbo.User_app where userID = '" + userID + "' and password = '" + password + "'";
            String name = null;
            ResultSet rs = dbConnection.getStatement().executeQuery(query);
            if(rs.next()){
                name = rs.getString(1);
            }
            User user = new User(name, userID, password, true);

        }
        catch (SQLException s){
            s.printStackTrace();
        }
        catch(ClassNotFoundException c){
            c.printStackTrace();
        }

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);

        startActivity(new Intent(LoginActivity.this,MainActivity.class));

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LoginActivity.this,StartActivity.class));
    }

    public void onLoginSuccess() {
        loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        loginButton.setEnabled(true);
    }

    //check if the user insert a valid username and password, password should be between 4 and 10 alphanumeric
    public boolean validate() {
        boolean valid = true;

        String user = userText.getText().toString();
        String password = passwordText.getText().toString();

        if (user.isEmpty() || user.length() < 3) {
            userText.setError("At least 3 ");
            valid = false;
        } else {
            try {
                DBConnection dbConnection = new DBConnection();
                String query = "Select userID from dbo.User_app where userID = '" + user + "'";
                ResultSet rs = dbConnection.getStatement().executeQuery(query);
                if (!rs.isBeforeFirst()) {
                    userText.setError("Username not found");
                    valid = false;
                } else
                    userText.setError(null);
                dbConnection.close();
            }
            catch (SQLException s){
                s.printStackTrace();
            }
            catch (ClassNotFoundException c){
                c.printStackTrace();
            }
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError("Between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            try {
                DBConnection dbConnection = new DBConnection();
                String query = "Select password from dbo.User_app where userID = '" + user + "' and password = '" + password + "'";
                ResultSet rs = dbConnection.getStatement().executeQuery(query);
                if (!rs.isBeforeFirst()) {
                    passwordText.setError("Wrong password");
                    valid = false;
                } else
                    userText.setError(null);
                dbConnection.close();
            }
            catch (SQLException s){
                s.printStackTrace();
            }
            catch (ClassNotFoundException c){
                c.printStackTrace();
            }
        }

        return valid;
    }
}