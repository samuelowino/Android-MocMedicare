package com.manage.hospital.hmapp.view.doctor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.manage.hospital.hmapp.R;
import com.manage.hospital.hmapp.database.contracts.DatabaseContract;
import com.manage.hospital.hmapp.database.helpers.DaktariDatabaseOpenHelper;
import com.manage.hospital.hmapp.model.Doctor;
import com.manage.hospital.hmapp.view.AlertDialogManager;
import com.manage.hospital.hmapp.view.LoginActivity;

@SuppressLint("NewApi")
public class DoctorRegistrationActivity extends Activity {


    AlertDialogManager alert = new AlertDialogManager();
    Doctor doctor = new Doctor();

    EditText licenseEditText;
    EditText clinicalAddressEditText;
    EditText userNameEditText;
    EditText passwordEditText;
    Spinner specializationDropdown;
    ProgressBar progressBar;

    String speciality = "None";
    ArrayAdapter<String> doctorStrangeAdapter;

    FirebaseDatabase firebaseDatabase;
    SQLiteOpenHelper databaseOpenHelper;
    SQLiteDatabase daktariDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_registration);

        Bundle bundleData = getIntent().getExtras();
        String firstName = (String)bundleData.get("first_name");
        String lastName = (String) bundleData.get("last_name");
        String uuid = (String) bundleData.get("uuid");
        String dob = (String) bundleData.get("dob");
        String email = (String) bundleData.get("email");
        String contact = (String) bundleData.get("contact");
        String gender = (String) bundleData.get("gender");

        licenseEditText = (EditText) findViewById(R.id.license_edit_text);
        clinicalAddressEditText = (EditText) findViewById(R.id.clinic_address);
        specializationDropdown = (Spinner) findViewById(R.id.speciality_dropDown);
        progressBar = (ProgressBar) findViewById(R.id.doctor_registration_progress_bar);
        TextView lastNameWelcomTextView = (TextView) findViewById(R.id.welcome_label_textview);

        doctorStrangeAdapter = new ArrayAdapter<>(this, R.layout.spinner_custom, getResources().getStringArray(R.array.doctor_specializations_list));
        specializationDropdown.setAdapter(doctorStrangeAdapter);

        doctor.setFirstName(firstName);
        doctor.setLastName(lastName);
        doctor.setUuid(uuid);
        doctor.setDob(dob);
        doctor.setEmail(email);
        doctor.setContact(contact);
        doctor.setGender(gender);

        lastNameWelcomTextView.setText("Dr." + firstName + " " + lastName);

        Toolbar toolbar = (Toolbar) findViewById(R.id.doctor_registration_toolbar);
        setActionBar(toolbar);

        if (getActionBar() != null){
            getActionBar().setTitle("Doctor Register");
            getActionBar().setDisplayShowHomeEnabled(true);
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        specializationDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                speciality = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                speciality = (String) parent.getItemAtPosition(1);
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseOpenHelper = new DaktariDatabaseOpenHelper(getApplicationContext());
        daktariDatabase = databaseOpenHelper.getWritableDatabase();

    }

    public void LoginDoctorRegistration(View V) {

        String license = licenseEditText.getText().toString();
        String clinicalAddress = clinicalAddressEditText.getText().toString();

        doctor.setSpecialization(speciality);
        doctor.setLicense(license);
        doctor.setClincalAddress(clinicalAddress);

        new PersistDoctorDataAsyncTask().execute();

    }

    public void submitAndPersistDoctorDataRemote(){
        DatabaseReference databaseReference = firebaseDatabase.getReference("doctors");
        databaseReference.child(doctor.getUuid()).setValue(doctor).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Registration Successful ",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Registration Failed ",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void submitAndPersistDoctorDataLocal(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.DoctorEntry.COLUMN_NAME_FIRST_NAME,doctor.getFirstName());
        contentValues.put(DatabaseContract.DoctorEntry.COLUMN_NAME_LAST_NAME,doctor.getLastName());
        contentValues.put(DatabaseContract.DoctorEntry.COLUMN_NAME_DOB,doctor.getDob());
        contentValues.put(DatabaseContract.DoctorEntry.COLUMN_NAME_EMAIL,doctor.getEmail());
        contentValues.put(DatabaseContract.DoctorEntry.COLUMN_NAME_CONTACT,doctor.getContact());
        contentValues.put(DatabaseContract.DoctorEntry.COLUMN_NAME_GENDER,doctor.getGender());
        contentValues.put(DatabaseContract.DoctorEntry.COLUMN_NAME_UUID,doctor.getUuid());
        contentValues.put(DatabaseContract.DoctorEntry.COLUMN_NAME_LICENSE,doctor.getLicense());
        contentValues.put(DatabaseContract.DoctorEntry.COLUMN_NAME_SPECIALIZATION,doctor.getSpecialization());

        daktariDatabase.insertOrThrow(DatabaseContract.DoctorEntry.TABLE_NAME,null,contentValues);

    }


    public class PersistDoctorDataAsyncTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            submitAndPersistDoctorDataLocal();
            submitAndPersistDoctorDataRemote();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            startActivity( new Intent(DoctorRegistrationActivity.this, LoginActivity.class));
        }
    }

}
