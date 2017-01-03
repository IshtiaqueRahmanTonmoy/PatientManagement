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
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.JSONParser;
import patientsmanagement.patientmanagement.patientsmanagementsystem.R;

public class MainActivity extends AppCompatActivity {

    Button Login;
    TextView Signup,SignupasDoctor;
    EditText mobilenoEdt,passwordEdt;
    private static final String LOGIN_URL = "http://darumadhaka.com/patientmanagement/patientlogin.php";
    String phone,encryptedpassword;
    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Login = (Button) findViewById(R.id.signinButton);
        Signup = (TextView) findViewById(R.id.signup);
        SignupasDoctor = (TextView)findViewById(R.id.signupasDoctor);

        mobilenoEdt = (EditText)findViewById(R.id.edtMobileNo);
        passwordEdt = (EditText) findViewById(R.id.edtPassword);


        SignupasDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,DoctorLoginActivity.class);
                startActivity(intent);
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = mobilenoEdt.getText().toString();
                encryptedpassword = passwordEdt.getText().toString();

                new AttemptLogin().execute();
            }
        });

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RegistrationActivity.class);
                startActivity(intent);
            }
        });

    }

    private class AttemptLogin extends AsyncTask<String, String, String> {

        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
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
                paramss.add(new BasicNameValuePair("phone", phone));
                paramss.add(new BasicNameValuePair("encryptedpassword", encryptedpassword));
                Log.d("request!", "starting");
                JSONObject json = jsonParser.makeHttpRequest( LOGIN_URL, "POST", paramss);

                success = json.getInt(TAG_SUCCESS);
                if (success == 1)
                 {
                     Log.d("Successfully Login int!", json.toString());
                     Intent ii = new Intent(MainActivity.this,PatientDashboard.class);
                     ii.putExtra("mobile",phone);
                     finish();
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

        protected void onPostExecute(String message) {
            pDialog.dismiss();
            if (message != null)
            {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show(); } }
    }
}