package com.patientmanagement.patientsmanagementsystem;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import patientsmanagement.patientmanagement.patientsmanagementsystem.R;
import patientsmanagement.patientmanagement.patientsmanagementsystem.adapter.PrescriptionAdapter;
import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.JSONParser;
import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.Prescription;

public class ViewPrescriptionActivity extends AppCompatActivity {

    String namevalue,agevalue,gendervalue,docnamevalue,specialityvalue,patientid,prescriptionno,medicinename;
    String medincineinfoid,unit,frequentlty,afterbefore,quantity,timeduration;
    ListView listview;
    TextView doctorName,Speciality,patientname,age,sex;
    String doctorid,patientmobile;
    JSONParser jsonParser = new JSONParser();
    private JSONArray jsonarray;
    private static final String TAG_TH = "th";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_NAME = "name";
    private static final String TAG_AGE = "age";
    private static final String TAG_GENDER = "gender";
    private static final String TAG_PATIENTID = "patientid";
    private static final String TAG_DOCTORLIST = "patientdetail";
    private static final String TAG_DOCTORID = "doctorId";
    private static final String TAG_PHONE = "phone";
    private static final String TAG_DOCNAME = "name";
    private static final String TAG_MEDINAME = "MediName";
    private static final String TAG_SPECIALITY = "expertise";
    private static final String TAG_MEDICINEID = "MediInfoId";
    private static final String MEDICINENAME_URL = "http://patientmanagement.medi-bd.com/patientmanagement/getmedicinename.php";
    private static final String DOCTORDETAILGET_URL = "http://patientmanagement.medi-bd.com/patientmanagement/searchallpatientinfo.php";
    private static final String DOCTORIDGET_URL = "http://patientmanagement.medi-bd.com/patientmanagement/getDocNameSpecial.php";
    private static final String IDGET_URL = "http://patientmanagement.medi-bd.com/patientmanagement/getpatientid.php";
    private static final String url_search = "http://patientmanagement.medi-bd.com/patientmanagement/prescriptionview.php";
    private static final String TAG_PatientPrescriptionNo = "PatientPrescriptionNo";
    private static final String TAG_MediInfoID = "MediInfoID";
    private static final String TAG_UNIT = "MediUnitID";
    private static final String TAG_FREQUENTLTY = "Frequeancy";
    private static final String TAG_AFTERBEFORE = "AfterOrBefore";
    private static final String TAG_QUANTITY = "Quantity";
    private static final String TAG_TIMEDURATION = "TimeDuration";

    private PrescriptionAdapter prescriptionadapter = null;
    private ArrayList<Prescription> prescriptionlist;
    Prescription prescriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_prescription);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        prescriptionlist = new ArrayList<Prescription>();
        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null) {
            doctorid = (String) b.get("doctorid");
            patientmobile = (String) b.get("mobilephonevalue");

            new getmedname().execute();
            new idget().execute();
            new getDoctorid().execute();
            new getPatientDetail().execute();
            new getPrescription().execute();
            //Toast.makeText(ViewPrescriptionActivity.this, ""+doctorid, Toast.LENGTH_SHORT).show();
        }

        doctorName = (TextView) findViewById(R.id.doctorName);
        Speciality = (TextView) findViewById(R.id.speciality);
        patientname = (TextView) findViewById(R.id.patientnamae);
        age = (TextView) findViewById(R.id.agevalue);
        sex = (TextView) findViewById(R.id.sexvalue);

        listview = (ListView) findViewById(R.id.listView);

    }


    public class getPrescription extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            //arealist = new ArrayList<String>();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    List<NameValuePair> param = new ArrayList<NameValuePair>();
                    param.add(new BasicNameValuePair(TAG_PatientPrescriptionNo, prescriptionno));
                    JSONObject json = jsonParser.makeHttpRequest(url_search, "GET", param);

                    Log.e("Response: ", "> " + json);

                    JSONArray than = null;
                    try {
                        than = json.getJSONArray("th");
                        for (int x = 0; x < than.length(); x++) {
                            JSONObject catObj11 = than.getJSONObject(x);
                            medincineinfoid = catObj11.getString(TAG_MediInfoID);
                            unit = catObj11.getString(TAG_UNIT);
                            frequentlty = catObj11.getString(TAG_FREQUENTLTY);
                            afterbefore = catObj11.getString(TAG_AFTERBEFORE);
                            quantity = catObj11.getString(TAG_QUANTITY);
                            timeduration = catObj11.getString(TAG_TIMEDURATION);

                            prescriptions = new Prescription(medicinename,unit,frequentlty,afterbefore,quantity,timeduration);
                            prescriptionlist.add(prescriptions);

                            prescriptionadapter = new PrescriptionAdapter(ViewPrescriptionActivity.this, R.layout.activity_view_prescription, prescriptionlist);
                            listview.setAdapter(prescriptionadapter);

                            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                }
                            });
                            //Toast.makeText(PatientAppoinmentList.this, "name"+name+"disease"+disease+"mobileno"+mobileno, Toast.LENGTH_SHORT).show();
                            //Log.d("output",name+disease+mobileno);
                            //arealist.add(name);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            return null;
        }
    }


    private class getmedname extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    List<NameValuePair> param = new ArrayList<NameValuePair>();
                    param.add(new BasicNameValuePair(TAG_MEDICINEID, ""));
                    // getting JSON string from URL
                    JSONObject json = jsonParser.makeHttpRequest(MEDICINENAME_URL, "GET", param);

                    // Check your log cat for JSON reponse
                    Log.d("All Doctors: ", json.toString());

                    try {
                        // Checking for SUCCESS TAG
                        int success = json.getInt(TAG_SUCCESS);

                        if (success == 1) {
                            // products found
                            // Getting Array of Products
                            jsonarray = json.getJSONArray(TAG_TH);

                            // looping through All Products
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject c = jsonarray.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<String, String>();
                                // Storing each json item in variable
                                medicinename = c.getString(TAG_MEDINAME);
                            }
                        } else {
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return null;
        }
    }


    private class idget extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    List<NameValuePair> param = new ArrayList<NameValuePair>();
                    param.add(new BasicNameValuePair(TAG_PHONE, patientmobile));
                    // getting JSON string from URL
                    JSONObject json = jsonParser.makeHttpRequest(IDGET_URL, "GET", param);

                    // Check your log cat for JSON reponse
                    Log.d("All Doctors: ", json.toString());

                    try {
                        // Checking for SUCCESS TAG
                        int success = json.getInt(TAG_SUCCESS);

                        if (success == 1) {
                            // products found
                            // Getting Array of Products
                            jsonarray = json.getJSONArray(TAG_TH);

                            // looping through All Products
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject c = jsonarray.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<String, String>();
                                // Storing each json item in variable

                                patientid = c.getString(TAG_PATIENTID);
                                prescriptionno = "00000000".substring(patientid.length()) + patientid;
                            }
                        } else {
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return null;
        }
    }

    private class getDoctorid extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    List<NameValuePair> param = new ArrayList<NameValuePair>();
                    param.add(new BasicNameValuePair(TAG_DOCTORID, doctorid));
                    // getting JSON string from URL
                    JSONObject json = jsonParser.makeHttpRequest(DOCTORIDGET_URL, "GET", param);

                    // Check your log cat for JSON reponse
                    Log.d("All Doctors: ", json.toString());

                    try {
                        // Checking for SUCCESS TAG
                        int success = json.getInt(TAG_SUCCESS);

                        if (success == 1) {
                            // products found
                            // Getting Array of Products
                            jsonarray = json.getJSONArray(TAG_TH);

                            // looping through All Products
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject c = jsonarray.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<String, String>();
                                // Storing each json item in variable

                                docnamevalue = c.getString(TAG_DOCNAME);
                                specialityvalue = c.getString(TAG_SPECIALITY);

                                doctorName.setText(docnamevalue);
                                Speciality.setText(specialityvalue);
                            }
                        } else {
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return null;
        }
    }

    private class getPatientDetail extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    List<NameValuePair> param = new ArrayList<NameValuePair>();
                    param.add(new BasicNameValuePair(TAG_PHONE, patientmobile));
                    // getting JSON string from URL
                    JSONObject json = jsonParser.makeHttpRequest(DOCTORDETAILGET_URL, "GET", param);

                    // Check your log cat for JSON reponse
                    Log.d("All Doctors: ", json.toString());

                    try {
                        // Checking for SUCCESS TAG
                        int success = json.getInt(TAG_SUCCESS);

                        if (success == 1) {
                            // products found
                            // Getting Array of Products
                            jsonarray = json.getJSONArray(TAG_DOCTORLIST);

                            // looping through All Products
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject c = jsonarray.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<String, String>();
                                // Storing each json item in variable

                                namevalue = c.getString(TAG_NAME);
                                agevalue = c.getString(TAG_AGE);
                                gendervalue = c.getString(TAG_GENDER);

                                patientname.setText(namevalue);
                                age.setText(agevalue);
                                sex.setText(gendervalue);


                            }
                        } else {
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
