package com.patientmanagement.patientsmanagementsystem;

import android.app.AlertDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.content.DialogInterface;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import patientsmanagement.patientmanagement.patientsmanagementsystem.R;
import patientsmanagement.patientmanagement.patientsmanagementsystem.adapter.BloodAdapter;
import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.JSONParser;
import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.Patient;

public class SearchBloodActivity extends AppCompatActivity {

    private JSONParser jParser = new JSONParser();
    private JSONArray jsonarray;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_HEALTHTIPS = "blood";
    private ArrayList<Patient> alist;
    String name,address,phone,bloodgroup;
    private static final String TAG_NAME = "name";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_PHONE = "phone";
    private static final String TAG_BLOOD = "bloodgroup";
    ListView listview;
    EditText search;
    String mob;
    private BloodAdapter bloodadapter = null;
    private static final String urlblood = "http://patientmanagement.medi-bd.com/patientmanagement/getblood.php";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_blood);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Search Blood");
        setSupportActionBar(toolbar);

        new GetBlood().execute();
        alist = new ArrayList<Patient>();
        search = (EditText) findViewById(R.id.editText);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        listview = (ListView) findViewById(R.id.listview);

        listview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(search.getWindowToken(), 0);

                return false;
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Intent intent = new Intent(SearchBloodActivity.this,GoogleMapActivity.class);
                //startActivity(intent);
                Patient selectedFromList =(Patient) (listview.getItemAtPosition(position));
                mob = selectedFromList.getPhone();
                //Toast.makeText(SearchBloodActivity.this, ""+mob, Toast.LENGTH_SHORT).show();
                alertMessage();
            }
        });


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //String text = search.getText().toString().toLowerCase(Locale.getDefault());
               // bloodadapter.filter(text);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = search.getText().toString().toLowerCase(Locale.getDefault());
                bloodadapter.filter(text);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void alertMessage() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which)
            {
                switch (which)
                {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Toast.makeText(SearchBloodActivity.this, "Yes Clicked", Toast.LENGTH_LONG).show();
                        Intent in = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mob));
                        try {
                            startActivity(in);
                        } catch (android.content.ActivityNotFoundException ex) {
                            //Toast.makeText(SearchBloodActivity.this, "Could not find an activity to place the call.", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        //Toast.makeText(SearchBloodActivity.this, "No Clicked", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure to call this patient?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
    }

    private class GetBlood extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(SearchBloodActivity.this,
                    "Search for blood..",
                    "Getting blood data..");
        }
        @Override
        protected String doInBackground(String... params) {

                    List<NameValuePair> param = new ArrayList<NameValuePair>();
                    JSONObject json= jParser.makeHttpRequest(urlblood,"GET",param);

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

                                name = c.getString(TAG_NAME);
                                address = c.getString(TAG_ADDRESS);
                                phone = c.getString(TAG_PHONE);
                                bloodgroup = c.getString(TAG_BLOOD);
                                alist.add(new Patient(name,address,phone,bloodgroup));
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
            // execution of result of Long time consuming operation
            progressDialog.dismiss();
            bloodadapter = new BloodAdapter(SearchBloodActivity.this, R.layout.simplerow, alist);
            listview.setAdapter(bloodadapter);
        }

    }
}