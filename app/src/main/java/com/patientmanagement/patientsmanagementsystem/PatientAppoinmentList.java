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
import java.util.List;

import patientsmanagement.patientmanagement.patientsmanagementsystem.R;
import patientsmanagement.patientmanagement.patientsmanagementsystem.adapter.PersonAdapter;
import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.JSONParser;
import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.Person;

/**
 * Created by Administrator on 12/15/2016.
 */
public class PatientAppoinmentList extends AppCompatActivity {

    private TextView tvValue;
    private ProgressDialog pDialog;
    private static final String TAG_NAME = "name";
    private static final String TAG_DISEASE = "disease";
    private static final String TAG_MOBILENO = "mobileno";
    private static final String TAG_TIME = "time";
    private static final String TAG_DOCTORID = "doctorId";
    private static String url_search = "http://patientmanagement.medi-bd.com/patientmanagement/searchallpatientbyid.php";
    private Person person;
    private ArrayList<Person> personlist;
    private PersonAdapter personadapter = null;
    JSONParser jP = new JSONParser();
    String name,disease,mobileno,time;
    String doctorId;
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_appoint_list);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        personlist = new ArrayList<Person>();
        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        loadAppoinmentList();
        listview = (ListView) findViewById(R.id.listView);
        if(b!=null)
        {
            doctorId = b.getString(TAG_DOCTORID);
            //Toast.makeText(PatientAppoinmentList.this, ""+doctorId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
    private void loadAppoinmentList() {
        new PatientAppList().execute();
    }

    public class PatientAppList extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(PatientAppoinmentList.this);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

                    List<NameValuePair> param = new ArrayList<NameValuePair>();
                    param.add(new BasicNameValuePair(TAG_DOCTORID, doctorId));
                    JSONObject json = jP.makeHttpRequest(url_search, "GET", param);

                    Log.e("Response: ", "> " + json);

                    JSONArray than = null;
                    try {
                        than = json.getJSONArray("th");
                        for (int x = 0; x < than.length(); x++) {
                            JSONObject catObj11 = than.getJSONObject(x);
                            name = catObj11.getString(TAG_NAME);
                            disease = catObj11.getString(TAG_DISEASE);
                            mobileno = catObj11.getString(TAG_MOBILENO);
                            time = catObj11.getString(TAG_TIME);

                            person = new Person(name,disease,mobileno,time);
                            personlist.add(person);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            personadapter = new PersonAdapter(PatientAppoinmentList.this, R.layout.patient_appoint_list, personlist);
            listview.setAdapter(personadapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String mobilenumber = personlist.get(position).getMobileno();
                    //Toast.makeText(getApplicationContext(), "" + member_name,
                    //       Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PatientAppoinmentList.this,PatientDetailActivity.class);
                    intent.putExtra("mobile",mobilenumber);
                    intent.putExtra("doctorid",doctorId);
                    startActivity(intent);
                    finish();
                }
            });

        }

    }
}
