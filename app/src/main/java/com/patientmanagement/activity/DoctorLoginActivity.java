package com.patientmanagement.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.JSONParser;
import patientsmanagement.patientmanagement.patientsmanagementsystem.R;

public class DoctorLoginActivity extends AppCompatActivity {

    private static final String LOGIN_URL = "http://darumadhaka.com/patientmanagement/doctorlogin.php";
    private static final String DOCTORIDGET_URL = "http://darumadhaka.com/patientmanagement/doctoridget.php";
    TextView Signup;
    Button login;
    String mobile,password,doctorid;
    EditText mobileEdt,passwordEdt;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_DOCTORID = "doctorId";
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctorlogin);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        mobileEdt = (EditText) findViewById(R.id.edtMobileNo);
        passwordEdt = (EditText) findViewById(R.id.edtPassword);

        login = (Button) findViewById(R.id.doctorsigninButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mobile = mobileEdt.getText().toString();
               password = passwordEdt.getText().toString();

                new DoctorIdget().execute();
                new AttemptLogin().execute();
            }
        });

        Signup = (TextView) findViewById(R.id.signup);
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorLoginActivity.this,DoctorRegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    private class AttemptLogin extends AsyncTask<String, String, String> {

        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DoctorLoginActivity.this);
            pDialog.setMessage("Attempting for login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            int success;
            try
            {
                List<NameValuePair> paramss = new ArrayList<NameValuePair>();
                paramss.add(new BasicNameValuePair("phone",mobile));
                paramss.add(new BasicNameValuePair("password",password));
                Log.d("request!", "starting");
                JSONObject json = jsonParser.makeHttpRequest( LOGIN_URL, "POST", paramss);

                success = json.getInt(TAG_SUCCESS);
                if (success == 1)
                {
                    Log.d("Successfully Login!", json.toString());

                    Intent ii = new Intent(DoctorLoginActivity.this,DoctorDashboard.class);
                    ii.putExtra(TAG_DOCTORID,doctorid);
                    startActivity(ii);

                    return json.getString(TAG_MESSAGE);
                }

                else {
                    return json.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } return null;
        }

        protected void onPostExecute(String message)
        {
            pDialog.dismiss();
            if (message != null)
            {
                Toast.makeText(DoctorLoginActivity.this, message, Toast.LENGTH_LONG).show(); } }
    }

    private class DoctorIdget extends AsyncTask<String, String, String> {

        boolean failure = false;
        @Override
        protected String doInBackground(String... params) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONParser jP = new JSONParser();
                    List<NameValuePair> param = new ArrayList<NameValuePair>();
                    param.add(new BasicNameValuePair("phone", mobile));
                    JSONObject json = jP.makeHttpRequest(DOCTORIDGET_URL, "GET", param);

                    Log.e("Response: ", "> " + json);

                    JSONArray than = null;
                    try {
                        than = json.getJSONArray("th");
                        for (int x = 0; x < than.length(); x++) {
                            JSONObject catObj11 = than.getJSONObject(x);
                            doctorid = catObj11.getString(TAG_DOCTORID);
                            Log.d("id",doctorid);
                            Toast.makeText(DoctorLoginActivity.this, ""+doctorid, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            });

            return null;
        }
    }
}
