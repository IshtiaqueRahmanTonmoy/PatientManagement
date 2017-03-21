package com.patientmanagement.patientsmanagementsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import patientsmanagement.patientmanagement.patientsmanagementsystem.R;
import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.JSONParser;

public class HealthTips extends AppCompatActivity {

    private JSONParser jParser = new JSONParser();
    private static String url_health_tips = "http://patientmanagement.medi-bd.com/patientmanagement/healthtips.php";
    private static final String TAG_HEALTHTIPS = "htips";
    private static final String TAG_ID = "id";
    private static final String TAG_HEADING = "heading";
    private static final String TAG_DETAILS = "details";
    private static final String TAG_SUCCESS = "success";
    private JSONArray jsonarray;
    private ArrayList<String> alist;
    private ArrayAdapter<String> listAdapter;
    ListView listview;
    String id,heading,idvalue;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_tips);

        listview = (ListView) findViewById(R.id.listview);

       listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               int idval = position + 1;
               idvalue = String.valueOf(idval);
               //Toast.makeText(HealthTips.this, ""+UserInfo, Toast.LENGTH_SHORT).show();
               Intent i =new Intent(HealthTips.this, HealthtipsDetail.class);
               i.putExtra("IdValue", idvalue);
               startActivity(i);
           }
       });
        alist = new ArrayList<String>();
        new DownloadJSON().execute();

    }
    protected class DownloadJSON extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(HealthTips.this);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

                    List<NameValuePair>param = new ArrayList<NameValuePair>();
                    JSONObject json= jParser.makeHttpRequest(url_health_tips,"GET",param);

                    try {
                        // Checking for SUCCESS TAG
                        int success = json.getInt(TAG_SUCCESS);

                        if (success == 1) {
                            // products found
                            // Getting Array of Products
                            jsonarray = json.getJSONArray(TAG_HEALTHTIPS);

                            // looping through All Products
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject c = jsonarray.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<String, String>();
                                // Storing each json item in variable

                                id = c.getString(TAG_ID);
                                heading = c.getString(TAG_HEADING);
                                alist.add(heading);
                            }
                        } else {
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
            listAdapter = new ArrayAdapter<String>(HealthTips.this, R.layout.simplerow, alist);
            listview.setAdapter(listAdapter);

        }

    }
}
