package com.patientmanagement.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.JSONParser;

/**
 * Created by Administrator on 12/21/2016.
 */
public class PatientDetailActivity extends AppCompatActivity {

    String phone;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static final String DOCTORDETAILGET_URL = "http://darumadhaka.com/patientmanagement/searchallpatientinfo.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_DOCTORLIST = "patientdetail";
    private JSONArray jsonarray;

    private static final String TAG_IMAGE = "image";
    private static final String TAG_NAME = "name";
    private static final String TAG_AGE = "age";
    private static final String TAG_GENDER = "gender";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_PHONE = "phone";
    private static final String TAG_DISEASE = "disease";

    ImageView image;
    TextView name,age,gender,address,mobphone,disease;
    Bitmap bmp1;
    String namevalue,agevalue,gendervalue,addressvalue,mobphonevalue,diseasevalue,doctorid;
    Button addPrescription,viewPrescription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientlogin_detail);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null) {
            phone =(String) b.get("mobile");
            doctorid = (String) b.get("doctorid");
            //Toast.makeText(DoctorListActivity.this, ""+phone, Toast.LENGTH_SHORT).show();
        }

        image = (ImageView) findViewById(R.id.imagev1);
        name = (TextView) findViewById(R.id.tvPatientnamevalue);
        age = (TextView) findViewById(R.id.tvAgevalue);
        gender = (TextView) findViewById(R.id.tvGendervalue);
        address = (TextView) findViewById(R.id.tvPatientaddressvalue);
        mobphone = (TextView) findViewById(R.id.tvPatientPhonevalue);
        disease = (TextView) findViewById(R.id.tvPatientdiseasevalue);

        addPrescription = (Button) findViewById(R.id.btnAddPrescription);
        viewPrescription = (Button) findViewById(R.id.btnViewPrescription);

        addPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent(PatientDetailActivity.this,AddPrescriptionActivity.class);
                 intent.putExtra("mobilephonevalue",mobphonevalue);
                 intent.putExtra("doctorid",doctorid);
                 startActivity(intent);
            }
        });

        viewPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientDetailActivity.this,ViewPrescriptionActivity.class);
                startActivity(intent);
            }
        });

        getPatientDetail();
    }

    private void getPatientDetail() {
        new getPatientDetail().execute();
    }

    private class getPatientDetail extends AsyncTask<String, String, String> {

        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PatientDetailActivity.this);
            pDialog.setMessage("Getting patient details...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair(TAG_PHONE, phone));
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
                        String imagevalue = c.getString(TAG_IMAGE);

                        byte[] byteArray =  Base64.decode(imagevalue, Base64.DEFAULT) ;
                        bmp1 = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                        namevalue = c.getString(TAG_NAME);
                        agevalue = c.getString(TAG_AGE);
                        gendervalue = c.getString(TAG_GENDER);
                        addressvalue = c.getString(TAG_ADDRESS);
                        mobphonevalue = c.getString(TAG_PHONE);
                        diseasevalue = c.getString(TAG_DISEASE);
                    }
                } else {
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String message) {
               pDialog.dismiss();

               image.setImageBitmap(bmp1);
               name.setText(namevalue);
               age.setText(agevalue);
               gender.setText(gendervalue);
               address.setText(addressvalue);
               mobphone.setText(mobphonevalue);
               disease.setText(diseasevalue);
        }
    }
}
