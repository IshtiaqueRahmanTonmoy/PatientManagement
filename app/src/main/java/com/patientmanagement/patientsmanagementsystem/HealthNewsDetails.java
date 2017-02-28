package com.patientmanagement.patientsmanagementsystem;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class HealthNewsDetails extends AppCompatActivity {

    private ProgressDialog pDialog;
    private static final String TAG_ID = "id";
    private static final String TAG_DETAIL = "details";
    private static final String DOCTORDETAILGET_URL = "http://darumadhaka.com/patientmanagement/healthnewsdetail.php";
    private static final String TAG_HEALTHDETAIL_LIST = "healthport";
    private static final String TAG_SUCCESS = "success";
    private JSONParser jsonParser = new JSONParser();
    TextView text;
    private String id;
    String outd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_news_details);
        text=(TextView)findViewById(R.id.healthNewsdetail);

        if (Build.VERSION.SDK_INT>9){
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            getDetail();
            Intent i=getIntent();
            id=i.getStringExtra("IdValue");
            Log.i("idvalue",id);
        }
    }

    private void getDetail() {
        new getDetail().execute();
    }
    private class getDetail extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog=new ProgressDialog(HealthNewsDetails.this);
            pDialog.setMessage("Searching....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(final String... params) {

            int success;
                    try{
                        List<NameValuePair>param=new ArrayList<NameValuePair>();
                        param.add(new BasicNameValuePair(TAG_ID,id));
                        JSONObject json=jsonParser.makeHttpRequest(DOCTORDETAILGET_URL,"GET",param);
                        success=json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            JSONArray patientObj=json.getJSONArray(TAG_HEALTHDETAIL_LIST);
                            JSONObject healthdetails=patientObj.getJSONObject(0);
                            outd =removeHTML(healthdetails.getString(TAG_DETAIL));
                            Log.d("outputvalue",outd);
                        }
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }

            return null;
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            pDialog.dismiss();
            text.setText(outd);
        }
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        private  String removeHTML(String tagDetails){
            return Html.fromHtml(tagDetails).toString();
        }
    }
}
