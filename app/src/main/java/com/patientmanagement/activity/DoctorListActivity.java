package com.patientmanagement.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import patientsmanagement.patientmanagement.patientsmanagementsystem.adapter.ListViewAdapter;
import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.JSONParser;
import patientsmanagement.patientmanagement.patientsmanagementsystem.R;
import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.Doctor;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class DoctorListActivity extends AppCompatActivity {

    private static String url_doctorlist = "http://darumadhaka.com/patientmanagement/getalldoctorinfo.php";
    private static String url_getname = "http://darumadhaka.com/patientmanagement/getnameappoinment.php";
    private static final String REGISTER_URL = "http://darumadhaka.com/patientmanagement/appoinmentschedule.php";
    private ProgressDialog pDialog;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_DOCTORLIST = "alldoctor";
    private JSONParser jParser;
    ListViewAdapter adapter;
    private ProgressDialog mProgressDialog;
    private JSONArray jsonarray;
    ListView listview;
    Doctor doctor;
    ArrayList<Doctor> alist;
    private static final String TAG_NAMEAPPOINT = "nameapp";
    private static final String TAG_IMAGE = "image";
    private static final String TAG_NAME = "name";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_PHONE = "phone";
    private static final String TAG_EXPERTISE = "expertise";
    private static final String TAG_CHAMBERDAY = "chamberday";
    private static final String TAG_CHAMBERTIME = "chambertime";
    private static final String TAG_DOCTORFEE = "doctorfee";
    private static final String TAG_FOLLOWUPFEE = "followupfeee";
    private static final String TAG_DISEASE = "disease";
    private static final String TAG_PATIENTID = "patientid";
    private static final String TAG_MOBILENO = "mobileno";
    private static final String TAG_DATE = "date";
    private static final String TAG_TIME = "time";
    private static final String TAG_DOCTORID = "doctorId";
    String phone,patientid,formattedDate;
    int pid,doctorid;
    String name,docid,disease,time;
    String delegate = "hh:mm aaa";

    Calendar c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        c = Calendar.getInstance();

        //Toast.makeText(DoctorListActivity.this, ""+formattedDate, Toast.LENGTH_SHORT).show();

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        time = (String) DateFormat.format(delegate,Calendar.getInstance().getTime());
        Log.d("time",time);

        if(b!=null)
        {
            phone =(String) b.get("mobile");
            new LoadName().execute();
            //Toast.makeText(DoctorListActivity.this, ""+phone, Toast.LENGTH_SHORT).show();
        }

        listview = (ListView) findViewById(R.id.listview);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Doctor clickedObj = (Doctor) parent.getItemAtPosition(position);
                //phone = clickedObj.getPhone();
                doctorid = position + 1;
                docid = String.valueOf(doctorid);

                //Toast.makeText(DoctorListActivity.this, "Appoinment confirmed..."+mobilenumber, Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(DoctorListActivity.this);
                AlertDialog dialog = builder.create();
                dialog.setTitle("Do you want to appoint with this doctor?");
                dialog.setMessage("Confirm your appoinment.");
                dialog.setButton("Yes",listenerAccept);
                dialog.setButton2("No", listenerDoesNotAccept);
                dialog.setCancelable(false);
                dialog.show();
                //String name = clickedObj.getName().toString();
                //Toast.makeText(DoctorListActivity.this, ""+name, Toast.LENGTH_SHORT).show();
            }
        });

        jParser = new JSONParser();
        listview = (ListView) findViewById(R.id.listview);
        alist = new ArrayList<Doctor>();
        new DownloadJSON().execute();
    }

    DialogInterface.OnClickListener listenerAccept = new DialogInterface.OnClickListener() {

        public void onClick(DialogInterface dialog, int which) {
            new CreateAppoinment().execute();
        }
    };

    DialogInterface.OnClickListener listenerDoesNotAccept = new DialogInterface.OnClickListener() {

        public void onClick(DialogInterface dialog, int which) {
            Toast.makeText(DoctorListActivity.this, "You canceled appoinment", Toast.LENGTH_SHORT).show();
        }
    };

    private class DownloadJSON extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    List<NameValuePair> param =
                            new ArrayList<NameValuePair>();
                    // getting JSON string from URL
                    JSONObject json = jParser.makeHttpRequest(url_doctorlist, "GET", param);

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
                                String image = c.getString(TAG_IMAGE);

                                byte[] byteArray =  Base64.decode(image, Base64.DEFAULT) ;
                                Bitmap bmp1 = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);


                                String name = c.getString(TAG_NAME);
                                String address = c.getString(TAG_ADDRESS);
                                String phone = c.getString(TAG_PHONE);
                                String expertise = c.getString(TAG_EXPERTISE);
                                String chamberday = c.getString(TAG_CHAMBERDAY);
                                String chambertime = c.getString(TAG_CHAMBERTIME);
                                String doctorfee = c.getString(TAG_DOCTORFEE);
                                String followupfee = c.getString(TAG_FOLLOWUPFEE);

                                doctor = new Doctor(name,bmp1,address,phone,expertise,chamberday,chambertime,doctorfee,followupfee);
                                alist.add(doctor);

                                adapter = new ListViewAdapter(DoctorListActivity.this, R.layout.activity_doctor_list, alist);
                                listview.setAdapter(adapter);
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


    class CreateAppoinment extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DoctorListActivity.this);
            pDialog.setMessage("Creating Appoinment..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        protected String doInBackground(String... args) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                    formattedDate = df.format(c.getTime());

                    Toast.makeText(DoctorListActivity.this, ""+formattedDate, Toast.LENGTH_SHORT).show();
                    List<NameValuePair> params = new ArrayList<NameValuePair>();

                    //Log.d("output",patientid+name+mobile+serial+formattedDate+docid);
                    params.add(new BasicNameValuePair(TAG_PATIENTID, patientid));
                    params.add(new BasicNameValuePair(TAG_NAME, name));
                    params.add(new BasicNameValuePair(TAG_DISEASE, disease));
                    params.add(new BasicNameValuePair(TAG_MOBILENO, phone));
                    params.add(new BasicNameValuePair(TAG_DATE, formattedDate));
                    params.add(new BasicNameValuePair(TAG_TIME, time));
                    params.add(new BasicNameValuePair(TAG_DOCTORID, docid));

                    Log.d("param",params.toString());
                    // getting JSON Object
                    // Note that create product url accepts POST method
                    JSONObject json = jParser.makeHttpRequest(REGISTER_URL, "POST", params);
                    Log.d("json",json.toString());
                    // check log cat fro response
                    //Log.d("Create Response", json.toString());

                    // check for success tag
                    try {

                        int success = json.getInt(TAG_SUCCESS);

                        Toast.makeText(DoctorListActivity.this, "" + success, Toast.LENGTH_SHORT).show();
                        if (success == 1) {
                            // successfully created product
                            Toast.makeText(DoctorListActivity.this, "Successfully created appoinment", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), PatientDashboard.class);
                            startActivity(i);

                            // closing this screen
                            finish();
                        } else {
                            // failed to create product
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }

    private class LoadName extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(DoctorListActivity.this);
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
                            name = lnews.getString(TAG_NAME);
                            disease = lnews.getString(TAG_DISEASE);

                            Log.d("valuessss",patientid+name+disease+phone);
                            Toast.makeText(DoctorListActivity.this, "id"+ patientid + "name"+ name+"consult for"+disease, Toast.LENGTH_SHORT).show();
                            //main.... text.setText(healthpo.getString(TAG_DETAILS));


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