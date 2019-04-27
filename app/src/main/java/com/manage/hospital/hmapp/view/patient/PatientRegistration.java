package com.manage.hospital.hmapp.view.patient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.manage.hospital.hmapp.R;
import com.manage.hospital.hmapp.data.PatientInfo;
import com.manage.hospital.hmapp.view.AlertDialogManager;
import com.manage.hospital.hmapp.view.LauncherActivity;
import com.manage.hospital.hmapp.view.SessionManager;
import com.manage.hospital.hmapp.utility.ConfigConstant;
import com.manage.hospital.hmapp.utility.encryptPasscode;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

@SuppressLint("NewApi")
public class PatientRegistration extends Activity {


    ProgressBar registrationProgressBar;
    AlertDialogManager alert = new AlertDialogManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_registration);
        TextView lname = (TextView) findViewById(R.id.lname_patient);
        lname.setText(bundle.getString("lname"));

        registrationProgressBar = (ProgressBar) findViewById(R.id.patient_registration_progress_banr);
        registrationProgressBar.setVisibility(View.GONE);

        Toolbar toolbar = (Toolbar)findViewById(R.id.patient_reg_toolbar);
        setActionBar(toolbar);

        if (getActionBar() != null){
            getActionBar().setTitle("Patient Registration");
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setDisplayHomeAsUpEnabled(true);
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

    public void LoginPatientRegistration(View V) {
        Bundle bundle = getIntent().getExtras();

        EditText we = (EditText) findViewById(R.id.weight);
        EditText ag = (EditText) findViewById(R.id.age);
        EditText ad = (EditText) findViewById(R.id.home);
        EditText un = (EditText) findViewById(R.id.uname_patient);
        EditText pass = (EditText) findViewById(R.id.password_patient);


        String weight = we.getText().toString();
        String age = ag.getText().toString();
        String home = ad.getText().toString();
        String uname = un.getText().toString();
        String password = pass.getText().toString();


        System.out.println("user name = " + uname);

        PatientInfo patientInfo = new PatientInfo();
        patientInfo.setFname(bundle.getString("fname"));
        patientInfo.setLname(bundle.getString("lname"));
        patientInfo.setDOB(bundle.getString("DOB"));
        patientInfo.setEmail(bundle.getString("email"));
        patientInfo.setContact(bundle.getString("contactNo"));
        patientInfo.setGender(bundle.getString("gender"));
        patientInfo.setWeight(weight);
        patientInfo.setAge(age);
        patientInfo.setAddress(home);
        patientInfo.setUsername(uname);
        patientInfo.setPassword(password);

        new AsyncTaskPatient().execute(patientInfo);


    }

    public class AsyncTaskPatient extends AsyncTask<PatientInfo, String, PatientInfo> {

        HttpResponse response;
        SessionManager session;
        String msg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            registrationProgressBar.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(),"Please wait...",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected PatientInfo doInBackground(PatientInfo... params) {

            Log.e(getClass().getSimpleName(),"Asyn Patient Registration... doInBackground");

            try {

                JSONObject requestBody_credential = new JSONObject();
                requestBody_credential.put("username", params[0].Username);
                String password = encryptPasscode.encryptPassword(params[0].Password);
                requestBody_credential.put("password", password);
                String request_credentials = requestBody_credential.toString();
                StringEntity request_param = new StringEntity(request_credentials);

                String Url = ConfigConstant.BASE_URL + ConfigConstant.insertPatientCredential;
                HttpPost post = new HttpPost(Url);
                post.setHeader("Content-Type", "application/json");
                post.setEntity(request_param);
                HttpClient httpClient = new DefaultHttpClient();
                response = httpClient.execute(post);

                Log.e(getClass().getSimpleName(),"http response code " + response.toString());

                System.out.println("Reached after coming back from credentials API");

                if (response.getStatusLine().getStatusCode() != 200) {
                    if (response.getStatusLine().getStatusCode() == 500) {
                        System.out.println("Patient registration failed!");
                    } else
                        throw new RuntimeException("Failed: HTTP error code :" + response.getStatusLine().getStatusCode());
                } else {
                    HttpEntity e = response.getEntity();
                    String i = EntityUtils.toString(e);
                    System.out.println(i);
                    JSONObject j = new JSONObject(i);
                    //int userID = Integer.parseInt(i);
                    int userID = j.getInt("PatientId");
                    params[0].setID(userID);
                }
            } catch (Exception x) {
                throw new RuntimeException("Error from insert Patient credentials", x);
            }

            try {

                JSONObject requestBody = new JSONObject();
                requestBody.put("firstname", params[0].fname);
                requestBody.put("lastname", params[0].lname);
                requestBody.put("DOB", params[0].DOB);
                requestBody.put("emailId", params[0].email);
                requestBody.put("gender", params[0].gender);
                requestBody.put("contactNo", params[0].contact);
                requestBody.put("age", params[0].age);
                requestBody.put("weight", params[0].weight);
                requestBody.put("address", params[0].address);
                requestBody.put("P_ID", params[0].ID);
                String request = requestBody.toString();
                StringEntity request_param = new StringEntity(request);

                String Url = ConfigConstant.BASE_URL + ConfigConstant.insertPatient;
                HttpPost post = new HttpPost(Url);
                post.setHeader("Content-Type", "application/json");
                post.setEntity(request_param);
                HttpClient httpClient = new DefaultHttpClient();
                response = httpClient.execute(post);
                System.out.println("Reached after coming back from patient API");
                if (response.getStatusLine().getStatusCode() != 200) {
                    if (response.getStatusLine().getStatusCode() == 500) {
                        System.out.println("Patient registration failed!");
                    } else if (response.getStatusLine().getStatusCode() == 400) {
                        System.out.println("Patient registration failed!");
                    } else
                        throw new RuntimeException("Failed: HTTP error code :" + response.getStatusLine().getStatusCode());
                } else {
                    HttpEntity e = response.getEntity();
                    String i = EntityUtils.toString(e);
                    JSONObject j = new JSONObject(i);
                    msg = j.getString("string");
                }

            } catch (Exception x) {
                throw new RuntimeException("Error from patient insert API", x);
            }


            return params[0];
        }

        @Override
        protected void onPostExecute(PatientInfo P) {
            super.onPostExecute(P);

            registrationProgressBar.setVisibility(View.GONE);

            Toast.makeText(getApplicationContext(),"Patient registration is completed",Toast.LENGTH_SHORT).show();

            String userID;
            userID = String.valueOf(P.ID);
            if (msg.equals("Request Succeeded!")) {
                session = new SessionManager(getApplicationContext());
                session.createLoginSession(P.Username, userID, "Patient");

                Intent i = new Intent(getApplicationContext(), LauncherActivity.class);
                startActivity(i);
                finish();
            } else {
                alert.showAlertDialog(PatientRegistration.this, "Patient resgistration failed..", "Re-Register", false);
            }
        }
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

    public void finishPatientRegistration(View V) {
        PatientRegistration.this.finish();
        ;
    }
}
