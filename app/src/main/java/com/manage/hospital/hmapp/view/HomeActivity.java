package com.manage.hospital.hmapp.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.manage.hospital.hmapp.R;


public class HomeActivity extends Activity {
    AlertDialogManager alert = new AlertDialogManager();

    SharedPreferences sharedPreferences;
    boolean isUserRegistered;
    Button registerButton;
    Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sharedPreferences = this.getSharedPreferences(getResources().getString(R.string.global_preference_file_name), Context.MODE_PRIVATE);
        isUserRegistered = sharedPreferences.getBoolean(getResources().getString(R.string.user_auth_pref_key),false);
        registerButton = (Button) findViewById(R.id.register_button);
        loginButton = (Button) findViewById(R.id.login_button);

        if (isUserRegistered){
            loginButton.setVisibility(View.VISIBLE);
            loginButton.setEnabled(true);
        }else {
            registerButton.setVisibility(View.VISIBLE);
            registerButton.setEnabled(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void gotoRegister(View v) {
        Intent intent = new Intent(HomeActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }

    public void gotoLogin(View v) {
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
