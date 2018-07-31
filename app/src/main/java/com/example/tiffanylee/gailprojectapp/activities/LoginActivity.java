package com.example.tiffanylee.gailprojectapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.tiffanylee.gailprojectapp.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    //Login Credentials Declaration
    EditText email;
    EditText password;

    //Login Button Declaration
    Button login_sigin;

    //SignUp Button Declaration
    Button gail_signup;
    Button fb_signup;

    //Shared Preferences Declaration and Initialization
    SharedPreferences sp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Login Credentials Declaration
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        //Login Button initialization
        login_sigin = (Button) findViewById(R.id.loginBtn);

        //Sign Up Buttons initialization
        gail_signup = (Button) findViewById(R.id.gail_button);
        fb_signup = (Button) findViewById(R.id.fb_button);

        //initialize shared prefrences.
        sp = getSharedPreferences("login",MODE_PRIVATE);

        if(sp.getBoolean("LoggedIn", false)){
            startMainActivity();
        }
        //setup onClickListeners
        clickFunctionalityInitializer();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.loginBtn:
                //if successful change to Main Activity and also add to shared preference
                String enteredEmail = email.getText().toString();
                String enteredPassword = password.getText().toString();

                //if autentication succcesfull also store the user id created in sql... blah
                sp.edit().putBoolean("LoggedIn",true).apply();
                startMainActivity();
                break;

            case R.id.gail_button:
                //Sign up with Gail
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
                break;

            case R.id.fb_button:
                //Sign Up using Facebook..
                //and also add to shared preference
                break;
        }
    }


    private void clickFunctionalityInitializer(){
        login_sigin.setOnClickListener(this);
        gail_signup.setOnClickListener(this);
        fb_signup.setOnClickListener(this);
    }

    private void startMainActivity(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();
    }
}
