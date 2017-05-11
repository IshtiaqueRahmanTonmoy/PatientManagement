package com.patientmanagement.patientsmanagementsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
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
    private static final String LOGIN_URL = "http://patientmanagement.medi-bd.com/patientmanagement/patientlogin.php";
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Patient Login");
        setSupportActionBar(toolbar);

        Login = (Button) findViewById(R.id.signinButton);
        Signup = (TextView) findViewById(R.id.signup);
        SignupasDoctor = (TextView)findViewById(R.id.signupasDoctor);

        mobilenoEdt = (EditText)findViewById(R.id.edtMobileNo);
        passwordEdt = (EditText) findViewById(R.id.edtPassword);

        mobilenoEdt.setTypeface(Typeface.SERIF);
        passwordEdt.setTypeface(Typeface.SERIF);


        SignupasDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,DoctorLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             /*
                int a=1,b=4,c=2017,d;
                int x = 121;
                int j = 20;
                int i = 1;
                int n = 1;

                 for(i=1;i<=x;i++) {
                     if (i == 20 * n) {
                         j = i - 19;
                         d = a++;
                         String date = String.valueOf(d)+String.valueOf(b)+String.valueOf(c);
                         Log.d("output", String.valueOf(j) + String.valueOf(i)+date);
                         n++;
                     }
                 }
             */

                phone = mobilenoEdt.getText().toString();
                encryptedpassword = passwordEdt.getText().toString();

                if(phone.matches("") && encryptedpassword.matches("")) {
                    //new AttemptLogin().execute();
                    Toast.makeText(MainActivity.this, "Field cannot be blank..", Toast.LENGTH_SHORT).show();
                }
                else{
                    new AttemptLogin().execute();
                }
            }
        });

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RegistrationActivity.class);
                startActivity(intent);
            }
        });

       // setToolBar();

        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tb.setTitle("");
        tb.setSubtitle("");
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.menu);
        ab.setDisplayHomeAsUpEnabled(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, tb, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    Intent intent = new Intent(MainActivity.this,DoctorListActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_photos) {
                    Intent intent = new Intent(MainActivity.this,SearchBloodActivity.class);
                    startActivity(intent);
                }
                else if (id == R.id.nav_movies) {
                    Intent intent = new Intent(MainActivity.this,HealthTips.class);
                    startActivity(intent);
                }
                else if(id == R.id.nav_notifications){
                    Intent intent = new Intent(MainActivity.this,HealthNews.class);
                    startActivity(intent);
                }
                else if(id == R.id.googlemap){
                    Intent intent = new Intent(MainActivity.this,MapsActivity.class);
                    startActivity(intent);
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    private void setToolBar() {

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