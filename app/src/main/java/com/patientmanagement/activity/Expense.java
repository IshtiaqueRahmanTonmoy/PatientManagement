package com.patientmanagement.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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
import patientsmanagement.patientmanagement.patientsmanagementsystem.adapter.ExpenseAdapter;
import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.JSONParser;
import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.Person;

public class Expense extends AppCompatActivity {

    private static final String EXPENSE_URL = "http://darumadhaka.com/patientmanagement/getexpense.php";
    private static final String EXPENSECALCULATE_URL = "http://darumadhaka.com/patientmanagement/getdoctorfeefolliowup.php";
    private ListView listview;
    private Button viewprescription;
    private String phone,doctorId,name,mobileno,disease,date,time,appoinmentno,fees,followupfee,settotal;
    private static final String TAG_DOCTORID = "doctorId";
    private Person person;
    private static final String TAG_APPOINMENTNO= "app_scheduleid";
    private static final String TAG_NAME= "name";
    private static final String TAG_MOBILENO = "mobileno";
    private static final String TAG_DISEASE = "disease";
    private static final String TAG_DATE = "date";
    private static final String TAG_TIME = "time";
    private static final String TAG_FEE = "doctorfee";
    private static final String TAG_FOLLOWUPFEE = "followupfeee";
    private ArrayList<Person> personlist;
    private ExpenseAdapter personadapter = null;
    private int numberofappoinment=0;
    float fee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        personlist = new ArrayList<Person>();
        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        listview = (ListView) findViewById(R.id.listview);
        viewprescription = (Button) findViewById(R.id.viewExpense);

        if(b!=null)
        {
            doctorId = b.getString(TAG_DOCTORID);
            new DoctorIdget().execute();
            new Expensecalculate().execute();

            //Toast.makeText(DoctorListActivity.this, ""+phone, Toast.LENGTH_SHORT).show();
        }
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
                    param.add(new BasicNameValuePair(TAG_DOCTORID, doctorId));
                    JSONObject json = jP.makeHttpRequest(EXPENSE_URL, "GET", param);

                    Log.e("Response: ", "> " + json);

                    JSONArray than = null;
                    try {
                        than = json.getJSONArray("th");
                        for (int x = 0; x < than.length(); x++) {
                            JSONObject catObj11 = than.getJSONObject(x);
                            appoinmentno = catObj11.getString(TAG_APPOINMENTNO);
                            numberofappoinment = Integer.parseInt(appoinmentno);
                            Log.d("noofapp", String.valueOf(numberofappoinment));
                            name = catObj11.getString(TAG_NAME);
                            mobileno = catObj11.getString(TAG_MOBILENO);
                            disease = catObj11.getString(TAG_DISEASE);
                            date = catObj11.getString(TAG_DATE);
                            time = catObj11.getString(TAG_TIME);

                            person = new Person(name,mobileno,disease,date,time);
                            personlist.add(person);
                            personadapter = new ExpenseAdapter(Expense.this, R.layout.activity_expense, personlist);
                            listview.setAdapter(personadapter);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            });

            return null;
        }
    }

    private class Expensecalculate extends AsyncTask<String, String, String> {

        boolean failure = false;
        @Override
        protected String doInBackground(String... params) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONParser jP = new JSONParser();
                    List<NameValuePair> param = new ArrayList<NameValuePair>();
                    param.add(new BasicNameValuePair(TAG_DOCTORID, doctorId));
                    JSONObject json = jP.makeHttpRequest(EXPENSECALCULATE_URL, "GET", param);

                    Log.e("Response: ", "> " + json);

                    JSONArray than = null;
                    try {
                        than = json.getJSONArray("th");
                        for (int x = 0; x < than.length(); x++) {
                            JSONObject catObj11 = than.getJSONObject(x);
                            fees = catObj11.getString(TAG_FEE);
                            fee = Float.parseFloat(fees);
                            followupfee = catObj11.getString(TAG_FOLLOWUPFEE);

                            float total = numberofappoinment*fee;
                            settotal = String.valueOf(total);
                            viewprescription.setText(settotal);

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
