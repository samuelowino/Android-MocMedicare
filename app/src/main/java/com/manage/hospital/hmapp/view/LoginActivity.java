package com.manage.hospital.hmapp.view;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.manage.hospital.hmapp.R;
import com.manage.hospital.hmapp.database.contracts.DatabaseContract;
import com.manage.hospital.hmapp.database.helpers.DaktariDatabaseOpenHelper;
import com.manage.hospital.hmapp.model.User;
import com.manage.hospital.hmapp.view.doctor.DoctorMainActivity;
import com.manage.hospital.hmapp.view.patient.PatientMainActivity;


public class LoginActivity extends Activity {

    private static final int ROLE_DOCTOR = 143;
    private static final int ROLE_PATIENT = 141;

    EditText userEmailEditText, passwordEditText;

    Button loginButton;
    AlertDialogManager alert = new AlertDialogManager();
    SessionManager session;

    ProgressBar loginProgressBar;

    FirebaseAuth firebaseAuth;
    SQLiteOpenHelper dbOpenHelper;
    SQLiteDatabase daktariDatabase;

    String userEmail;
    String password;
    RadioButton doctorRadioButton;
    RadioButton patientRadioButton;
    TextView signUpWidget;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new SessionManager(getApplicationContext());

        userEmailEditText = (EditText) findViewById(R.id.user_email_edit_text);
        passwordEditText = (EditText) findViewById(R.id.password_edit_text);
        doctorRadioButton = (RadioButton) findViewById(R.id.doctor_radio_button);
        patientRadioButton = (RadioButton) findViewById(R.id.patient_radio_button);
        signUpWidget = (TextView) findViewById(R.id.sign_up_view);

        loginProgressBar = (ProgressBar) findViewById(R.id.login_progress_bar);
        firebaseAuth = FirebaseAuth.getInstance();
        dbOpenHelper = new DaktariDatabaseOpenHelper(getApplicationContext());
        daktariDatabase = dbOpenHelper.getReadableDatabase();

        Toolbar toolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setActionBar(toolbar);

        if (getActionBar() != null) {
            getActionBar().setTitle(getResources().getString(R.string.app_name));
        }

        loginButton = (Button) findViewById(R.id.login);


        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                loginProgressBar.setVisibility(View.VISIBLE);
                passwordEditText.setEnabled(false);
                userEmailEditText.setEnabled(false);

                userEmail = userEmailEditText.getText().toString();
                password = passwordEditText.getText().toString();

                if (doctorRadioButton.isChecked()) {
                    new DoctorAuthAsynTask().doInBackground();
                } else if (patientRadioButton.isChecked()) {
                    new PatientAuthAsyncTask().doInBackground();
                } else
                    Toast.makeText(getApplicationContext(), "Kindly select role (Patient|Doctor)", Toast.LENGTH_SHORT).show();

            }

        });

        signUpWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        daktariDatabase.close();
    }

    public User getCachedUser() {
        User user = new User();
        Cursor cursor = daktariDatabase.rawQuery("SELECT * FROM " + DatabaseContract.UserEntry.TABLE_NAME, null);
        while (cursor.moveToNext()) {
            user.setUserEmail(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserEntry.COLUMN_NAME_EMAIL)));
            user.setUserUuid(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserEntry.COLUMN_NAME_UUID)));
            user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserEntry.COLUMN_NAME_ROLE)));
            user.setUserPassword(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserEntry.COLUMN_NAME_HASHED_PASSWORD)));
        }

        cursor.close();

        return user;
    }

    public boolean isUserAuthenticated() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        return firebaseUser != null;
    }

    public boolean signUpUser(String userEmail, String userPassword) {
        firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        loginProgressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "RegistrationActivity Successful", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "RegistrationActivity failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return firebaseAuth.getCurrentUser() != null;
    }

    public void signInUser(String userEmail, String password) {

        User user = getCachedUser();

        Log.e(getClass().getSimpleName(), "User | " + user);

        if (user.getUserEmail() != null) {
            if (user.getUserEmail().equalsIgnoreCase(userEmail) && user.getUserPassword().equalsIgnoreCase(password)) {
                loginProgressBar.setVisibility(View.GONE);

                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();

                if (user.getRole().contains("patient")) {
                    gotoHome(ROLE_PATIENT);
                } else if (user.getRole().contains("doctor")) {
                    gotoHome(ROLE_DOCTOR);
                }
                return;
            }
        }

        firebaseAuth.signInWithEmailAndPassword(userEmail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        loginProgressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            loginProgressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                            loginButton.setText("Login Successful");
                        } else {
                            loginProgressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                            loginButton.setText("Authentication Failed, retry...");
                            passwordEditText.setEnabled(true);
                            userEmailEditText.setEnabled(true);
                            return;
                        }
                    }
                });

        if (user.getRole().contains("patient")) {
            gotoHome(ROLE_PATIENT);
        } else if (user.getRole().contains("doctor")) {
            gotoHome(ROLE_DOCTOR);
        }


    }

//    public User getFirebaseUser() {
//        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//        if (firebaseUser != null) {
//            String name = firebaseUser.getDisplayName();
//            String email = firebaseUser.getEmail();
//            String uuid = firebaseUser.getUid();
//            Uri photoUri = firebaseUser.getPhotoUrl();
//
//            User user = new User();
//            user.setUserEmail(email);
//            user.setRole(false);
//            user.setUserUuid(uuid);
//            user.setPhotoUri(photoUri.toString());
//
//            return user;
//
//        } else
//            return null;
//    }

    public class DoctorAuthAsynTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            signInUser(userEmail, password);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public class PatientAuthAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            signInUser(userEmail, password);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public void gotoHome(int role) {
        if (ROLE_DOCTOR == role) {
            Intent doc_intent = new Intent(LoginActivity.this, DoctorMainActivity.class);
            startActivity(doc_intent);
        } else if (ROLE_PATIENT == role) {
            Intent pat_intent = new Intent(LoginActivity.this, PatientMainActivity.class);
            startActivity(pat_intent);
        }

    }

    public void finishLoginActivity(View V) {
        LoginActivity.this.finish();
        ;
    }


}

