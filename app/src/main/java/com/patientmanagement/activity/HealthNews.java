package com.patientmanagement.activity;

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

public class HealthNews extends AppCompatActivity {
    private JSONParser jParser=new JSONParser();
    private static String url_health_news = "http://darumadhaka.com/patientmanagement/healthnews.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_HEALTHNEWS = "healthport";
    private static final String TAG_ID = "id";
    private static final String TAG_HEADING = "heading";
    private JSONArray jsonarray;
    private ArrayList<String> alist;
    private ArrayAdapter<String> listAdapter;
    String id,heading,idvalue;
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_news);
        listview=(ListView)findViewById(R.id.healthNewslistview);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int idval=position+1;
                idvalue=String.valueOf(idval);
                Intent i =new Intent(HealthNews.this, HealthNewsDetails.class);
                i.putExtra("IdValue", idvalue);
                startActivity(i);
            }
        });

        alist = new ArrayList<String>();
        new DownloadJSON().execute();
    }

    protected class DownloadJSON extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    List<NameValuePair> param=new ArrayList<NameValuePair>();
                    JSONObject json=jParser.makeHttpRequest(url_health_news,"GET",param);
                    try{
                        int success=json.getInt(TAG_SUCCESS);
                        if ((success==1)){
                        jsonarray=json.getJSONArray(TAG_HEALTHNEWS);
                            for (int i=0;i<jsonarray.length();i++)
                            {
                                JSONObject c=jsonarray.getJSONObject(i);
                                HashMap<String,String>map=new HashMap<String, String>();
                                id=c.getString(TAG_ID);
                                heading=c.getString(TAG_HEADING);

                                alist.add(heading);
                                listAdapter=new ArrayAdapter<String>(HealthNews.this,R.layout.simplerow,alist);
                                listview.setAdapter(listAdapter);
                            }
                        }
                        else {

                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            });
            return null;
        }
    }
}
