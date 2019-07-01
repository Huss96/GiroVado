package com.example.hussnain.girovado;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
public class StartActivity extends AppCompatActivity {

    @BindView(R.id.login) Button login;
    @BindView(R.id.noLogin) Button noLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this,LoginActivity.class));
                }
        });
        noLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noLogin();
                }
        });
    }
    public void noLogin(){
            User user = new User("User","user","user", false);
            startActivity(new Intent(StartActivity.this,MainActivity.class));
            this.finish();
    }

}
