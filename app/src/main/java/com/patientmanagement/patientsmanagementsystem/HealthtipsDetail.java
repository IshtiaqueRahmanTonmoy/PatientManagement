package com.patientmanagement.patientsmanagementsystem;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import patientsmanagement.patientmanagement.patientsmanagementsystem.R;
import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.JSONParser;

public class HealthtipsDetail extends AppCompatActivity {

    private ProgressDialog pDialog;
    private JSONParser jsonParser = new JSONParser();
    private static final String TAG_ID = "id";
    private static final String TAG_DETAIL = "details";
    private static final String DOCTORDETAILGET_URL = "http://patientmanagement.medi-bd.com/patientmanagement/healthtipsdetail.php";
    private static final String TAG_HEALTHDETAIL_LIST = "htip";
    private static final String TAG_SUCCESS = "success";
    private JSONArray jsonarray;
    private String id,detail;
    TextView text;
    String outd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthtips_detail);

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Health Details");
        setSupportActionBar(toolbar);

        getDetail();

        text = (TextView) findViewById(R.id.detail);
        Intent i = getIntent();
        id = i.getStringExtra("IdValue");
        Log.e("idvalue",id);

    }

    private void getDetail() {
        new getDetail().execute();
    }

    private class getDetail extends AsyncTask<String, String, String> {

        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HealthtipsDetail.this);
            pDialog.setMessage("Searching...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            // updating UI from Background Thread

                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> param = new ArrayList<NameValuePair>();
                        param.add(new BasicNameValuePair(TAG_ID, id));

                        // getting product details by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                DOCTORDETAILGET_URL, "GET", param);

                        // check your log for json response
                        Log.d("Single Product Details", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received product details
                            JSONArray patientObj = json
                                    .getJSONArray(TAG_HEALTHDETAIL_LIST); // JSON Array

                            // get first product object from JSON Array
                            JSONObject healthpo = patientObj.getJSONObject(0);

                            // product with this pid found
                            // Edit Text

                            outd = removeHTML(healthpo.getString(TAG_DETAIL));
                            Log.d("outputvalue",outd);


                            //main.... text.setText(healthpo.getString(TAG_DETAILS));


                        } else {
                            // product with pid not found
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
            text.setText(outd);

        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private String removeHTML(String tagDetails) {
        return Html.fromHtml(tagDetails).toString();
    }
}
