package com.manage.hospital.hmapp.view.patient;

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
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.manage.hospital.hmapp.model.Patient;
import com.manage.hospital.hmapp.view.LoginActivity;

import java.util.UUID;

@SuppressLint("NewApi")
public class PatientRegistrationActivity extends Activity {


    ProgressBar registrationProgressBar;
    Patient patientInfo;

    FirebaseDatabase firebaseDatabase;
    SQLiteDatabase daktariDatabase;
    SQLiteOpenHelper databaseOpenHelper;

    EditText weightEditText;
    EditText ageEditText;
    EditText homeAddressEditText;
    EditText userNamePatient;
    EditText passwordEditText;
    TextView lastNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_registration);

        lastNameTextView = (TextView) findViewById(R.id.welcome_patient_last_name_textview);
        registrationProgressBar = (ProgressBar) findViewById(R.id.patient_registration_progress_banr);
        registrationProgressBar.setVisibility(View.GONE);

        weightEditText = (EditText) findViewById(R.id.weight);
        ageEditText = (EditText) findViewById(R.id.age);
        homeAddressEditText = (EditText) findViewById(R.id.home);
        userNamePatient = (EditText) findViewById(R.id.uname_patient);

        Toolbar toolbar = (Toolbar)findViewById(R.id.patient_reg_toolbar);
        setActionBar(toolbar);

        if (getActionBar() != null){
            getActionBar().setTitle("Patient Registration");
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Bundle bundleData = getIntent().getExtras();
        String firstName = (String)bundleData.get("first_name");
        String lastName = (String) bundleData.get("last_name");
        String uuid = (String) bundleData.get("uuid");
        String dob = (String) bundleData.get("dob");
        String email = (String) bundleData.get("email");
        String contact = (String) bundleData.get("contact");
        String gender = (String) bundleData.get("gender");

        lastNameTextView.setText(firstName + " " + lastName);

        patientInfo = new Patient();
        patientInfo.setFirstName(firstName);
        patientInfo.setLastName(lastName);
        patientInfo.setContact(contact);
        patientInfo.setGender(gender);
        patientInfo.setEmail(email);
        patientInfo.setUuid(uuid);
        patientInfo.setDob(dob);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseOpenHelper = new DaktariDatabaseOpenHelper(getApplicationContext());
        daktariDatabase = databaseOpenHelper.getWritableDatabase();

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

    public void submitPatientDataInfo(View V) {
        Bundle bundle = getIntent().getExtras();

        String weight = weightEditText.getText().toString();
        String age = ageEditText.getText().toString();
        String homeAddress = homeAddressEditText.getText().toString();

        patientInfo = new Patient();
        patientInfo.setFirstName(bundle.getString("first_name"));
        patientInfo.setLastName(bundle.getString("last_name"));
        patientInfo.setDob(bundle.getString("DOB"));
        patientInfo.setEmail(bundle.getString("email"));
        patientInfo.setContact(bundle.getString("contact"));
        patientInfo.setGender(bundle.getString("gender"));
        patientInfo.setWeight(weight);
        patientInfo.setAge(age);
        patientInfo.setAddress(homeAddress);
        patientInfo.setUuid(UUID.randomUUID().toString());

        new PersistPatientDataAsyncTask().execute();

    }

    public void submitAndPersistPatientDataRemote(){
        DatabaseReference databaseReference = firebaseDatabase.getReference("patients");
        databaseReference.child(patientInfo.uuid).setValue(patientInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public void submitAndPersistPatientDataLocal(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.PatientEntry.COLUMN_NAME_FIRST_NAME,patientInfo.getFirstName());
        contentValues.put(DatabaseContract.PatientEntry.COLUMN_NAME_LAST_NAME,patientInfo.getLastName());
        contentValues.put(DatabaseContract.PatientEntry.COLUMN_NAME_AGE,patientInfo.getAge());
        contentValues.put(DatabaseContract.PatientEntry.COLUMN_NAME_DOB,patientInfo.getDob());
        contentValues.put(DatabaseContract.PatientEntry.COLUMN_NAME_EMAIL,patientInfo.getEmail());
        contentValues.put(DatabaseContract.PatientEntry.COLUMN_NAME_CONTACT,patientInfo.getContact());
        contentValues.put(DatabaseContract.PatientEntry.COLUMN_NAME_GENDER,patientInfo.getGender());
        contentValues.put(DatabaseContract.PatientEntry.COLUMN_NAME_WEIGHT,patientInfo.getWeight());
        contentValues.put(DatabaseContract.PatientEntry.COLUMN_NAME_UUID,patientInfo.getUuid());

        daktariDatabase.insertOrThrow(DatabaseContract.PatientEntry.TABLE_NAME,null,contentValues);

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

    public class PersistPatientDataAsyncTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            registrationProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            submitAndPersistPatientDataLocal();
            submitAndPersistPatientDataRemote();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            registrationProgressBar.setVisibility(View.GONE);
            startActivity( new Intent(PatientRegistrationActivity.this, LoginActivity.class));
        }
    }
}
