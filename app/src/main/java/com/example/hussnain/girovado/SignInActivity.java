package com.example.hussnain.girovado;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import butterknife.ButterKnife;
import butterknife.BindView;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @BindView(R.id.input_name) EditText nameText;
    @BindView(R.id.input_email) EditText userText;
    @BindView(R.id.input_password) EditText passwordText;
    @BindView(R.id.btn_signup) Button signupButton;
    @BindView(R.id.link_login) TextView loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(SignInActivity.this,StartActivity.class));
    }
    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignInActivity.this,
                R.style.Theme_AppCompat_Light_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = nameText.getText().toString();
        String userID = userText.getText().toString();
        String password = passwordText.getText().toString();

        User user = new User(name,userID, password, true);

        saveToDB(name,userID,password);
        startActivity(new Intent(SignInActivity.this,MainActivity.class));
        this.finish();
        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void saveToDB(String name, String user, String password) {
        try {

            DBConnection dbConnection = new DBConnection();
            String query = "INSERT INTO dbo.User_app " +
                    "VALUES ('"+user+"','" +password+"','"+name+"', 0 )";
            dbConnection.getStatement().executeUpdate(query);
            dbConnection.close();
        }
        catch (SQLException e){
            e.printStackTrace();

        }
        catch (ClassNotFoundException c){}


    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = nameText.getText().toString();
        String user = userText.getText().toString();
        String password = passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            nameText.setError("at least 3 characters");
            valid = false;
        } else {
            nameText.setError(null);
        }

        if (user.isEmpty() || user.length() < 3) {
            userText.setError("At least 3 characters");
            valid = false;
        } else {
            try {
                DBConnection dbConnection = new DBConnection();
                String query = "Select userID from dbo.User_app where userID = '" + user + "'";
                ResultSet rs = dbConnection.getStatement().executeQuery(query);
                if (rs.next()) {
                    userText.setError("Username already used");
                    valid = false;
                }
                else
                    userText.setError(null);
                dbConnection.close();
            }
            catch (SQLException s){
                s.printStackTrace();
            }
            catch (ClassNotFoundException c){}
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError("Between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }
}
