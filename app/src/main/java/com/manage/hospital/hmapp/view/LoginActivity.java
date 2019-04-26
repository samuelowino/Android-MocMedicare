package com.manage.hospital.hmapp.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.Toolbar;

import com.manage.hospital.hmapp.R;
import com.manage.hospital.hmapp.data.LoginInfo;
import com.manage.hospital.hmapp.utility.ConfigConstant;
import com.manage.hospital.hmapp.utility.encryptPasscode;
import com.manage.hospital.hmapp.view.doctor.DoctorMainActivity;
import com.manage.hospital.hmapp.view.patient.PatientMainActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class LoginActivity extends Activity {

    EditText txtUsername, txtPassword;

    Button btnLogin;
    AlertDialogManager alert = new AlertDialogManager();
    SessionManager session;

    ProgressBar loginProgressBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        System.out.println("Inside login activity");
        //System.setProperty("http.keepAlive", "false");

        session = new SessionManager(getApplicationContext());

        txtUsername = (EditText) findViewById(R.id.uname_login);
        txtPassword = (EditText) findViewById(R.id.password_login);
        RadioButton rd1 = (RadioButton) findViewById(R.id.doc_rd);
        RadioButton rd2 = (RadioButton) findViewById(R.id.patient_rd);

        loginProgressBar = (ProgressBar) findViewById(R.id.login_progress_bar);

        //Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        Toolbar toolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setActionBar(toolbar);

        if (getActionBar() != null) {
            getActionBar().setTitle(getResources().getString(R.string.app_name));
        }

        btnLogin = (Button) findViewById(R.id.login);


        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                loginProgressBar.setVisibility(View.VISIBLE);
                txtPassword.setEnabled(false);
                txtUsername.setEnabled(false);

                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();
                Bundle bundle = getIntent().getExtras();
                RadioButton rd1 = (RadioButton) findViewById(R.id.doc_rd);
                RadioButton rd2 = (RadioButton) findViewById(R.id.patient_rd);


                LoginInfo loginInfo = new LoginInfo();

                if (username.trim().length() > 0 && password.trim().length() > 0) {
                    loginInfo.setUsername(username);
                    loginInfo.setPassword(password);
                    if (rd1.isChecked())
                        new AsyncTaskLoginDoc().execute(loginInfo);
                    else
                        new AsyncTaskLoginPatient().execute(loginInfo);
                } else {

                    alert.showAlertDialog(LoginActivity.this, "Login failed..", "Please enter username and password", false);
                }

            }

        });
    }

    public class AsyncTaskLoginDoc extends AsyncTask<LoginInfo, String, LoginInfo> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Authenticating...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected LoginInfo doInBackground(LoginInfo... params) {
            return null;
        }

        @Override
        protected void onPostExecute(LoginInfo L) {
            super.onPostExecute(L);
            Intent i = new Intent(getApplicationContext(), DoctorMainActivity.class); //ToDo doctor dashboard
            startActivity(i);
            finish();
        }

    }

    public class AsyncTaskLoginPatient extends AsyncTask<LoginInfo, Void, LoginInfo> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected LoginInfo doInBackground(LoginInfo... params) {

            return null;
        }

        @Override
        protected void onPostExecute(LoginInfo L) {
            super.onPostExecute(L);

            Intent i = new Intent(getApplicationContext(), PatientMainActivity.class); //ToDo Patient dashboard
            startActivity(i);
            finish();
        }

    }

    public void gotoHome(View V) {
        Intent intent = new Intent(LoginActivity.this, LauncherActivity.class);
        startActivity(intent);
    }

    public void finishLoginActivity(View V) {
        LoginActivity.this.finish();
        ;
    }

}

