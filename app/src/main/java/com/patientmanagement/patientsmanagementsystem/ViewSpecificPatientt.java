package com.patientmanagement.patientsmanagementsystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import patientsmanagement.patientmanagement.patientsmanagementsystem.R;
import patientsmanagement.patientmanagement.patientsmanagementsystem.adapter.MedicineListAdapter;
import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.JSONParser;
import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.Medicine;

/**
 * Created by Administrator on 2/2/2017.
 */
public class ViewSpecificPatientt  extends AppCompatActivity {

    private ListView listview;
    private static final String TAG_DOCTORID = "doctorId";
    private static final String TAG_DOCTORIDVAL = "DoctorID";
    private static final String TAG_PRESCRIPTIONNO = "PatientPrescriptionNo";
    private static final String TAG_GETPRESCRIPTIONNO = "doctorId";
    private static final String TAG_MEDICINENAME = "MediName";
    private Medicine medicine;
    private static final String TAG_MEDINFOID = "MediInfoID";
    private static final String TAG_MediUnitID = "MediUnitID";
    private static final String TAG_Quantity = "Quantity";
    private static final String TAG_TimeDuration = "TimeDuration";
    private static final String TAG_AfterOrBefore = "AfterOrBefore";
    private static final String TAG_Frequeancy = "Frequeancy";
    private static final String TAG_Followupdate = "Followupdate";
    private static final String TAG_Suggestion = "Suggestion";
    private ArrayList<Medicine> medicinelist;
    private MedicineListAdapter mediadapter = null;
    private String doctorId,prescriptionno,medininfoid,mediunitid,quantity,timeduration,afterbefore,frequency,followupdate,suggestion,medicinename;
    private static String url_search = "http://darumadhaka.com/patientmanagement/patientprescription.php";
    private static String url_getall = "http://darumadhaka.com/patientmanagement/getallprescriptions.php";
    private static String url_getmedicine = "http://darumadhaka.com/patientmanagement/medicinenamegetbyinfo.php";
    JSONParser jP = new JSONParser();
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_specific);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        listview = (ListView) findViewById(R.id.viewspecificprescription);
        medicinelist = new ArrayList<Medicine>();
        if(b!=null)
        {
            doctorId = b.getString(TAG_DOCTORID);
            Toast.makeText(ViewSpecificPatientt.this, ""+doctorId, Toast.LENGTH_SHORT).show();
            new getPrescriptionNo().execute();
            new getPrescriptionHistory().execute();
            new getMedicineName().execute();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }


    public class getPrescriptionNo extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            //arealist = new ArrayList<String>();
                    List<NameValuePair> param = new ArrayList<NameValuePair>();
                    param.add(new BasicNameValuePair(TAG_DOCTORIDVAL, doctorId));
                    JSONObject json = jP.makeHttpRequest(url_search, "GET", param);

                    Log.e("Response: ", "> " + json);

                    JSONArray than = null;
                    try {
                        than = json.getJSONArray("th");
                        for (int x = 0; x < than.length(); x++) {
                            JSONObject catObj11 = than.getJSONObject(x);
                            prescriptionno = catObj11.getString(TAG_PRESCRIPTIONNO);
                            //Toast.makeText(ViewSpecificPatientt.this, ""+prescriptionno, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

            return null;
        }
    }

    public class getPrescriptionHistory extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            //arealist = new ArrayList<String>();

                   List<NameValuePair> param = new ArrayList<NameValuePair>();
                    param.add(new BasicNameValuePair(TAG_PRESCRIPTIONNO,  prescriptionno));
                    JSONObject json = jP.makeHttpRequest(url_getall, "GET", param);

                    Log.e("Response: ", "> " + json);

                    JSONArray than = null;
                    try {
                        than = json.getJSONArray("th");
                        for (int x = 0; x < than.length(); x++) {
                            JSONObject catObj11 = than.getJSONObject(x);
                            medininfoid = catObj11.getString(TAG_MEDINFOID);
                            // Toast.makeText(ViewSpecificPatientt.this, ""+medicinename, Toast.LENGTH_SHORT).show();
                            //Log.d("medicinename",medicinename);

                            mediunitid = catObj11.getString(TAG_MediUnitID);
                            quantity = catObj11.getString(TAG_Quantity);
                            timeduration = catObj11.getString(TAG_TimeDuration);
                            afterbefore = catObj11.getString(TAG_AfterOrBefore);
                            frequency = catObj11.getString(TAG_Frequeancy);
                            followupdate = catObj11.getString(TAG_Followupdate);
                            suggestion = catObj11.getString(TAG_Suggestion);

                            medicine = new Medicine(medicinename,mediunitid,quantity,timeduration,afterbefore,frequency,followupdate,suggestion);
                            medicinelist.add(medicine);


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
              return null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            mediadapter = new MedicineListAdapter(ViewSpecificPatientt.this, R.layout.activity_view_specific, medicinelist);
            listview.setAdapter(mediadapter);

        }


    }

    public class getMedicineName extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            //arealist = new ArrayList<String>();
                    List<NameValuePair> param = new ArrayList<NameValuePair>();
                    param.add(new BasicNameValuePair(TAG_MEDINFOID, medininfoid));
                    JSONObject json = jP.makeHttpRequest(url_getmedicine, "GET", param);

                    Log.e("Response: ", "> " + json);

                    JSONArray than = null;
                    try {
                        than = json.getJSONArray("th");
                        for (int x = 0; x < than.length(); x++) {
                            JSONObject catObj11 = than.getJSONObject(x);
                            medicinename = catObj11.getString(TAG_MEDICINENAME);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

            return null;
        }
    }

}
