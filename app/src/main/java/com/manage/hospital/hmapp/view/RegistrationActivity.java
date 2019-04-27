package com.manage.hospital.hmapp.view;

import android.app.DatePickerDialog;
//import android.app.DialogFragment;
import android.content.ContentValues;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.manage.hospital.hmapp.R;
import com.manage.hospital.hmapp.database.contracts.UserContract;
import com.manage.hospital.hmapp.database.helpers.DaktariDatabaseOpenHelper;
import com.manage.hospital.hmapp.view.doctor.DoctorRegistration;
import com.manage.hospital.hmapp.view.patient.PatientRegistration;


public class RegistrationActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    SQLiteDatabase daktariDatabase;
    SQLiteOpenHelper dbOpenHelper;

    String firstName;
    String lastName;
    String dateOfBirth;
    String email;
    String gender;
    String password;
    String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page_common);

        Toolbar toolbar = (Toolbar) findViewById(R.id.reg_page_common_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("Register");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        progressBar = (ProgressBar)findViewById(R.id.registration_common_progress_banr);
        firebaseAuth = FirebaseAuth.getInstance();
        dbOpenHelper = new DaktariDatabaseOpenHelper(getApplicationContext());
        daktariDatabase = dbOpenHelper.getWritableDatabase();
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
        EditText firstNameText = (EditText) findViewById(R.id.fname);
        EditText lastNameEditText = (EditText) findViewById(R.id.lname);
        EditText dob = (EditText) findViewById(R.id.DOB);
        EditText emailAddressEditText = (EditText) findViewById(R.id.email);
        EditText genderEditText = (EditText) findViewById(R.id.gender_edit_text);
        EditText passwordEditText = (EditText) findViewById(R.id.password_edit_text);
        RadioButton doctorRadioButton = (RadioButton) findViewById(R.id.radio_doctor);
        RadioButton patientRadioButton = (RadioButton) findViewById(R.id.radio_patient);

         firstName = firstNameText.getText().toString();
         lastName = lastNameEditText.getText().toString();
         dateOfBirth = dob.getText().toString();
         email = emailAddressEditText.getText().toString();
         gender = genderEditText.getText().toString();
         password = passwordEditText.getText().toString();

         if (doctorRadioButton.isChecked()){
             role = "doctor";
         }else if (patientRadioButton.isChecked()){
             role = "patient";
         }

        signUpUser(email,password);


    }

    public void cacheUserDetails(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserContract.UserEntry.COLUMN_NAME_FIRST_NAME,firstName);
        contentValues.put(UserContract.UserEntry.COLUMN_NAME_LAST_NAME,lastName);
        contentValues.put(UserContract.UserEntry.COLUMN_NAME_GENDER,gender);
        contentValues.put(UserContract.UserEntry.COLUMN_NAME_DATE_OF_BIRTH, dateOfBirth);
        contentValues.put(UserContract.UserEntry.COLUMN_NAME_HASHED_PASSWORD,password);
        contentValues.put(UserContract.UserEntry.COLUMN_NAME_ROLE,role);
        contentValues.put(UserContract.UserEntry.COLUMN_NAME_UUID,firebaseAuth.getCurrentUser().getUid());

        long rowId = daktariDatabase.insertOrThrow(UserContract.UserEntry.TABLE_NAME,null,contentValues);

    }

    public boolean signUpUser(String userEmail,String userPassword){
        firebaseAuth.createUserWithEmailAndPassword(userEmail,userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()){
                            cacheUserDetails();
                            Toast.makeText(getApplicationContext(),"Registration Successful",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"Registration failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return firebaseAuth.getCurrentUser() != null;
    }

    public void gotoRegistrationDoc(String fname, String lname, String DOB, String email, String gender, String contactNo) {
        Intent intent = new Intent(RegistrationActivity.this, DoctorRegistration.class);
        intent.putExtra("str", 0);
        intent.putExtra("fname", fname);
        intent.putExtra("lname", lname);
        intent.putExtra("dateOfBirth", DOB);
        intent.putExtra("email", email);
        intent.putExtra("gender", gender);
        intent.putExtra("contactNo", contactNo);
        startActivity(intent);
    }

    public void gotoRegistrationPatient(String fname, String lname, String DOB, String email, String gender, String contactNo) {
        Intent intent = new Intent(RegistrationActivity.this, PatientRegistration.class);
        intent.putExtra("str", 1);
        intent.putExtra("fname", fname);
        intent.putExtra("lname", lname);
        intent.putExtra("dateOfBirth", DOB);
        intent.putExtra("email", email);
        intent.putExtra("gender", gender);
        intent.putExtra("contactNo", contactNo);
        startActivity(intent);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(RegistrationActivity.this,HomeActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void finishRegistration(View V) {
        RegistrationActivity.this.finish();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        EditText dob = (EditText) findViewById(R.id.DOB);
        dob.setText(month + 1 + "/" + dayOfMonth + "/" + year);
    }
}
