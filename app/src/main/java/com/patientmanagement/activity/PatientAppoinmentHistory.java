package com.patientmanagement.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import patientsmanagement.patientmanagement.patientsmanagementsystem.adapter.AppoinmentListAdapter;
import patientsmanagement.patientmanagement.patientsmanagementsystem.R;
import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.JSONParser;
import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.Person;

public class PatientAppoinmentHistory extends AppCompatActivity {

    ListView listview;
    EditText editText;
    String patientid,phone,pname,disease,mobileno,date,time;
    private ProgressDialog pDialog;
    private static final String TAG_PHONE = "phone";
    private JSONParser jParser;
    private static String url_getname = "http://darumadhaka.com/patientmanagement/getnameappoinment.php";
    private static String url_getappoinmentdetail= "http://darumadhaka.com/patientmanagement/appoinmentList.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_TH = "th";
    private static final String TAG_NAMEAPPOINT = "nameapp";
    private static final String TAG_PATIENTID = "patientid";

    private static final String TAG_PATIENTNAME = "name";
    private static final String TAG_DISEASE = "disease";
    private static final String TAG_MOBILENO = "mobileno";
    private static final String TAG_DATE = "date";
    private static final String TAG_TIME = "time";
    private Person person;
    private ArrayList<Person> alist;
    private AppoinmentListAdapter adapter;
    int pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_appoinment_history);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        alist = new ArrayList<Person>();
        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        jParser = new JSONParser();

        if(b!=null)
        {
            phone =(String) b.get("mobile");
            new LoadName().execute();
            new AppoinmentDetail().execute();
            //Toast.makeText(DoctorListActivity.this, ""+phone, Toast.LENGTH_SHORT).show();
        }

        listview = (ListView) findViewById(R.id.healthNewslistview);
        editText = (EditText) findViewById(R.id.etSearch);


    }

    private class LoadName extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair(TAG_PHONE, phone));

                        // getting product details by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jParser.makeHttpRequest(
                                url_getname, "GET", params);

                        // check your log for json response
                        Log.d("Single Product Details", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received product details
                            JSONArray productObj = json
                                    .getJSONArray(TAG_NAMEAPPOINT); // JSON Array

                            JSONObject lnews = productObj.getJSONObject(0);
                            pid = lnews.getInt(TAG_PATIENTID);
                            patientid = String.valueOf(pid);

                        } else {
                            // product with pid not found
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            return null;
        }
    }

    private class AppoinmentDetail extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(PatientAppoinmentHistory.this);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair(TAG_PATIENTID,  patientid));

                        // getting product details by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jParser.makeHttpRequest(
                                url_getappoinmentdetail, "GET", params);

                        // check your log for json response
                        Log.d("Single Product Details", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received product details
                            JSONArray productObj = json
                                    .getJSONArray(TAG_TH); // JSON Array

                            for (int x = 0; x < productObj.length(); x++) {
                                JSONObject lnews = productObj.getJSONObject(x);
                                pname = lnews.getString(TAG_PATIENTNAME);
                                disease = lnews.getString(TAG_DISEASE);
                                mobileno = lnews.getString(TAG_MOBILENO);
                                date = lnews.getString(TAG_DATE);
                                time = lnews.getString(TAG_TIME);

                                person = new Person(pname, disease, mobileno, date, time);
                                alist.add(person);

                                adapter = new AppoinmentListAdapter(PatientAppoinmentHistory.this, R.layout.activity_patient_appoinment_history, alist);
                                listview.setAdapter(adapter);
                            }
                           // Log.d("result",pname+disease+mobileno+date+time);
                        } else {
                            // product with pid not found
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();

        }
    }
}
