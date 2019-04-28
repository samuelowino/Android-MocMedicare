package com.manage.hospital.hmapp.view;

import android.app.DatePickerDialog;
//import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.manage.hospital.hmapp.R;
import com.manage.hospital.hmapp.database.contracts.DatabaseContract;
import com.manage.hospital.hmapp.database.helpers.DaktariDatabaseOpenHelper;
import com.manage.hospital.hmapp.model.User;
import com.manage.hospital.hmapp.view.doctor.DoctorRegistration;
import com.manage.hospital.hmapp.view.patient.PatientRegistrationActivity;

public class RegistrationActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    ProgressBar progressBar;
    SQLiteDatabase daktariDatabase;
    SQLiteOpenHelper dbOpenHelper;
    SharedPreferences sharedPreferences;

    EditText firstNameText;
    EditText lastNameEditText;
    EditText dobEditText;
    EditText emailAddressEditText;
    EditText genderEditText;
    EditText passwordEditText;

    String firstName;
    String lastName;
    String dateOfBirth;
    String email;
    String gender;
    String password;
    String role;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page_common);

        Toolbar toolbar = (Toolbar) findViewById(R.id.reg_page_common_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Register");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        firstNameText = (EditText) findViewById(R.id.fname);
        lastNameEditText = (EditText) findViewById(R.id.lname);
        dobEditText = (EditText) findViewById(R.id.DOB);
        emailAddressEditText = (EditText) findViewById(R.id.email);
        genderEditText = (EditText) findViewById(R.id.gender_edit_text);
        passwordEditText = (EditText) findViewById(R.id.password_edit_text);

        progressBar = (ProgressBar) findViewById(R.id.registration_common_progress_banr);
        firebaseAuth = FirebaseAuth.getInstance();
        dbOpenHelper = new DaktariDatabaseOpenHelper(getApplicationContext());
        daktariDatabase = dbOpenHelper.getWritableDatabase();
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.global_preference_file_name), Context.MODE_PRIVATE);

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

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void completeSignUp(View v) {

        progressBar.setVisibility(View.VISIBLE);
        int str;

        RadioButton doctorRadioButton = (RadioButton) findViewById(R.id.radio_doctor);
        RadioButton patientRadioButton = (RadioButton) findViewById(R.id.radio_patient);

        firstNameText.setEnabled(false);
        lastNameEditText.setEnabled(false);
        dobEditText.setEnabled(false);
        emailAddressEditText.setEnabled(false);
        genderEditText.setEnabled(false);
        passwordEditText.setEnabled(false);


        firstName = firstNameText.getText().toString();
        lastName = lastNameEditText.getText().toString();
        dateOfBirth = dobEditText.getText().toString();
        email = emailAddressEditText.getText().toString();
        gender = genderEditText.getText().toString();
        password = passwordEditText.getText().toString();

        if (doctorRadioButton.isChecked()) {
            role = "doctor";
        } else if (patientRadioButton.isChecked()) {
            role = "patient";
        }

        signUpUser(email, password);

    }

    public void cacheUserDetails() {
        user = new User();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.UserEntry.COLUMN_NAME_FIRST_NAME, firstName);
        contentValues.put(DatabaseContract.UserEntry.COLUMN_NAME_LAST_NAME, lastName);
        contentValues.put(DatabaseContract.UserEntry.COLUMN_NAME_GENDER, gender);
        contentValues.put(DatabaseContract.UserEntry.COLUMN_NAME_EMAIL, email);
        contentValues.put(DatabaseContract.UserEntry.COLUMN_NAME_DATE_OF_BIRTH, dateOfBirth);
        contentValues.put(DatabaseContract.UserEntry.COLUMN_NAME_HASHED_PASSWORD, password);
        contentValues.put(DatabaseContract.UserEntry.COLUMN_NAME_ROLE, role);
        contentValues.put(DatabaseContract.UserEntry.COLUMN_NAME_UUID, firebaseAuth.getCurrentUser().getUid());

        user.setUserUuid(firebaseAuth.getCurrentUser().getUid());
        user.setUserEmail(email);
        user.setRole(role);
        user.setUserPassword(password);
        user.setUuid(firebaseAuth.getCurrentUser().getUid());
        user.setFirstName(firstName);
        user.setLastName(lastName);

        long rowId = daktariDatabase.insertOrThrow(DatabaseContract.UserEntry.TABLE_NAME, null, contentValues);

    }

    public boolean signUpUser(final String userEmail, String userPassword) {
        firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            cacheUserDetails();
                            Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor sharedPrefsEditor = sharedPreferences.edit();
                            sharedPrefsEditor.putBoolean(getResources().getString(R.string.user_auth_pref_key), true);
                            sharedPrefsEditor.apply();
                            writeUserOnRemoteDatabase();

                            if (user.getRole().equalsIgnoreCase("patient")){
                                Intent intent  = new Intent(RegistrationActivity.this, PatientRegistrationActivity.class);
                                intent.putExtra("first_name",user.getFirstName());
                                intent.putExtra("last_name",user.getLastName());
                                intent.putExtra("uuid",user.getUuid());
                                intent.putExtra("dobEditText", dobEditText.getText().toString());
                                intent.putExtra("email",user.getUserEmail());
                                intent.putExtra("contact","07xxxx");
                                intent.putExtra("gender",genderEditText.getText().toString());
                                startActivity(intent);
                            }else if (user.getRole().equalsIgnoreCase("doctor")){
                                Intent intent = new Intent(RegistrationActivity.this, DoctorRegistration.class);
                                startActivity(intent);
                            }

                        } else {
                            firstNameText.setEnabled(true);
                            lastNameEditText.setEnabled(true);
                            dobEditText.setEnabled(true);
                            emailAddressEditText.setEnabled(true);
                            genderEditText.setEnabled(true);
                            passwordEditText.setEnabled(true);
                            Toast.makeText(getApplicationContext(), "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return firebaseAuth.getCurrentUser() != null;
    }

    public void writeUserOnRemoteDatabase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference userNodeReference = firebaseDatabase.getReference();
        userNodeReference.child("users").child(user.getUuid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"User Record Saved",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"User Record Save Failed",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(RegistrationActivity.this, HomeActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        daktariDatabase.close();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        EditText dob = (EditText) findViewById(R.id.DOB);
        dob.setText(month + 1 + "/" + dayOfMonth + "/" + year);
    }
}
