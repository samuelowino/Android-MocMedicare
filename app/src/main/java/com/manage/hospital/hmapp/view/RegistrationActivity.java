package com.manage.hospital.hmapp.view;

import android.app.DatePickerDialog;
//import android.app.DialogFragment;
import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;

import com.manage.hospital.hmapp.R;
import com.manage.hospital.hmapp.view.doctor.DoctorRegistration;
import com.manage.hospital.hmapp.view.patient.PatientRegistration;


public class RegistrationActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
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

    public void gotoNext(View v) {
        int str;
        EditText firstNameText = (EditText) findViewById(R.id.fname);
        EditText lastNameEditText = (EditText) findViewById(R.id.lname);
        EditText dob = (EditText) findViewById(R.id.DOB);
        EditText eid = (EditText) findViewById(R.id.email);
        EditText gen = (EditText) findViewById(R.id.gender_edit_text);
        EditText ph = (EditText) findViewById(R.id.phone_no_edit_text);
        RadioButton doctorRadioButton = (RadioButton) findViewById(R.id.radio_doctor);
        RadioButton patientRadioButton = (RadioButton) findViewById(R.id.radio_patient);

        String fname = firstNameText.getText().toString();
        String lname = lastNameEditText.getText().toString();
        String DOB = dob.getText().toString();
        String email = eid.getText().toString();
        String gender = gen.getText().toString();
        String contactNo = ph.getText().toString();


        if (doctorRadioButton.isChecked())
            gotoRegistrationDoc(fname, lname, DOB, email, gender, contactNo);
        else
            gotoRegistrationPatient(fname, lname, DOB, email, gender, contactNo);

        System.out.println("Fname = " + fname);


    }

    public void gotoRegistrationDoc(String fname, String lname, String DOB, String email, String gender, String contactNo) {
        Intent intent = new Intent(RegistrationActivity.this, DoctorRegistration.class);
        intent.putExtra("str", 0);
        intent.putExtra("fname", fname);
        intent.putExtra("lname", lname);
        intent.putExtra("DOB", DOB);
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
        intent.putExtra("DOB", DOB);
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
