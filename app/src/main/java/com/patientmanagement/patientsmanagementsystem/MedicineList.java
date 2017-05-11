package com.patientmanagement.patientsmanagementsystem;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import patientsmanagement.patientmanagement.patientsmanagementsystem.R;
import patientsmanagement.patientmanagement.patientsmanagementsystem.adapter.MedicineAdapter;
import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.JSONParser;
import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.Medicine;

public class MedicineList extends AppCompatActivity {

    Context context;
    ListView listview;
    List<Medicine> medicinelist;
    private MedicineAdapter medicineadapter;
    private Medicine medicine;
    String medicineName,medicineUnit;
    Intent intent;
    List<Medicine> cartList,lists;
    ArrayList<Medicine> resultpass = new ArrayList<>();
    Button submitPrescription;
    private JSONParser jsonParser = new JSONParser();
    private static final String TAG_MediUnitId = "MediUnitId";
    private static final String TAG_SUCCESS = "success";

    private static String REGISTER_SAVEAPPOINMENT = "http://patientmanagement.medi-bd.com/patientmanagement/saveprescription.php";
    private static String url_getname = "http://patientmanagement.medi-bd.com/patientmanagement/getnameappoinment.php";
    private static final String REGISTER_URL = "http://patientmanagement.medi-bd.com/patientmanagement/prescriptionpatient.php";
    private static final String TAG_PHONE = "phone";

    private static final String TAG_PATIENTPRESCRIPTIONNO = "PatientPrescriptionNo";
    private static final String TAG_DOCTORID = "DoctorID";
    private static final String TAG_PATIENTIDVAL = "patientid";
    private static final String TAG_DATE = "Date";
    private static final String TAG_NAMEAPPOINT = "nameapp";
    private static final String TAG_MEDINFOID = "MediInfoID";
    private static final String TAG_UNITID = "MediUnitID";
    private static final String TAG_Quantity = "Quantity";
    private static final String TAG_TimeDuration = "TimeDuration";
    private static final String TAG_AfterOrBefore = "AfterOrBefore";
    private static final String TAG_Frequeancy = "Frequeancy";
    private static final String TAG_SUGGESTION = "Suggestion";

    int docid,pid;
    String medinfoid,mediunitid,quantity,timeduration,afterbefore,frequently,suggestion,doctorid,patientmobile,prescriptionno,followup,currentdate,patientid;
    private ProgressDialog pDialog;
    int i=0,pno=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_list);

        new LoadName().execute();

        if (Build.VERSION.SDK_INT>9){
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Medicine List");
        setSupportActionBar(toolbar);

        listview = (ListView) findViewById(R.id.listview);
        medicinelist = new ArrayList<Medicine>();
        lists = new ArrayList<Medicine>();
        cartList = new ArrayList<Medicine>();
        intent = getIntent();
        submitPrescription = (Button) findViewById(R.id.SubmitMedicine);

        pno = getIntent().getIntExtra("prescriptinno",0);
        medinfoid = getIntent().getExtras().getString("medinfoid");
        mediunitid = getIntent().getExtras().getString("mediunitidval");
        quantity = getIntent().getExtras().getString("quantity");
        timeduration = getIntent().getExtras().getString("timeduration");
        afterbefore = getIntent().getExtras().getString("afterbefore");
        frequently = getIntent().getExtras().getString("frequently");
        suggestion = getIntent().getExtras().getString("suggestion");
        prescriptionno = getIntent().getExtras().getString("prescriptionno");
        docid = Integer.parseInt(getIntent().getExtras().getString("doctorid"));
        patientmobile = getIntent().getExtras().getString("mobnopatient");
        followup = getIntent().getExtras().getString("followup");
        //Log.d("patientmobno",patientmobile);

        DateFormat dateFormat = new SimpleDateFormat("MMMM dd yyyy");
        java.util.Date date = new java.util.Date();
        currentdate = dateFormat.format(date);


        //System.out.println("Current Date : " + dateFormat.format(date));

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));

        String carListAsString = getIntent().getStringExtra("listget");

        Gson gson = new Gson();
        Type type = new TypeToken<List<Medicine>>(){}.getType();
        cartList = gson.fromJson(carListAsString, type);

        medicineadapter = new MedicineAdapter(MedicineList.this,R.layout.activity_medicine_list,cartList);
        listview.setAdapter(medicineadapter);

        medicineadapter.notifyDataSetChanged();

        submitPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String patientpres = prescriptionno;
                for(Medicine s : cartList){
                    String medfoval = s.getMedinfo().toString();
                    String unit = s.getUnit().toString();
                    String qtys = s.getQuantity().toString();
                    String timedur = s.getTimeduration().toString();
                    String afbefore = s.getAfterbefore().toString();
                    String freq = s.getFrequently().toString();
                    String sugg = s.getSuggestion().toString();

                    //new SaveIntoDb().execute();
                    SaveIntoDb insert = new SaveIntoDb(patientpres,medfoval,unit,qtys,timedur,afbefore,freq,sugg);
                    insert.execute("",null);
                    //Log.d("idval",id);
                }

                new PrescriptionPatient().execute();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        for(int i=0;i<cartList.size();i++){

            String mname = cartList.get(i).getMedicineName();
            String munit = cartList.get(i).getUnit();
            resultpass.add(i,new Medicine(mname,munit));
        }

        Intent intentpass = new Intent("custom-message-pass");
        String listSerializedToJson = new Gson().toJson(resultpass);

        Log.d("listserialize",listSerializedToJson);
        intentpass.putExtra("listget", listSerializedToJson);
        //startActivity(intent);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intentpass);
    }


    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            medicineName = intent.getStringExtra("name");
            medicineUnit = intent.getStringExtra("unit");

            //Log.d("quant",quantity);
            lists.add(new Medicine(medicineName,medicineUnit));
        }
    };


    class PrescriptionPatient extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MedicineList.this);
            pDialog.setMessage("Adding a prescription..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        protected String doInBackground(String... args) {

                    Log.d("value for paramter", String.valueOf(pid));
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair(TAG_PATIENTPRESCRIPTIONNO, prescriptionno));
                    params.add(new BasicNameValuePair(TAG_DOCTORID,""+docid));
                    params.add(new BasicNameValuePair(TAG_PATIENTIDVAL,""+pid));
                    params.add(new BasicNameValuePair(TAG_DATE, currentdate));

                    // getting JSON Object
                    // Note that create product url accepts POST method
                    JSONObject json = jsonParser.makeHttpRequest(REGISTER_URL, "POST", params);
                    //Toast.makeText(MedicineList.this, ""+pid, Toast.LENGTH_SHORT).show();
                    //Log.d("json",json.toString());
                    // check log cat fro response
                    //Log.d("Create Response", json.toString());

                    // check for success tag
                    try {

                        int success = json.getInt(TAG_SUCCESS);

                        //Toast.makeText(MedicineList.this, "" + success, Toast.LENGTH_SHORT).show();
                        if (success == 1) {
                            // successfully created product

                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);

                            // closing this screen
                            finish();
                        } else {
                            // failed to create product
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
        protected String doInBackground(String... params) {
            // updating UI from Background Thread

            // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> paramss = new ArrayList<NameValuePair>();
                        paramss.add(new BasicNameValuePair(TAG_PHONE, patientmobile));

                        // getting product details by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_getname, "GET", paramss);

                        // check your log for json response
                        Log.d("Single Product Details", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received product details
                            JSONArray productObj = json
                                    .getJSONArray(TAG_NAMEAPPOINT); // JSON Array

                            JSONObject lnews = productObj.getJSONObject(0);
                            pid = lnews.getInt(TAG_PATIENTIDVAL);
                           // Toast.makeText(MedicineList.this, ""+pid, Toast.LENGTH_SHORT).show();
                           // patientid = String.valueOf(pid);

                        } else {
                            // product with pid not found
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

            return null;
        }
    }

    class SaveIntoDb extends AsyncTask<String, String, String> {

        String  patientprescriptionno,medinfo,unit,qtys,timedur,afbefore,freq,sugg;

        public SaveIntoDb(String patientprescriptionno,String medinfo, String unit, String qtys, String timedur, String afbefore, String freq, String sugg) {
            this.patientprescriptionno = patientprescriptionno;
            this.medinfo = medinfo;
            this.unit = unit;
            this.qtys = qtys;
            this.timedur = timedur;
            this.afbefore = afbefore;
            this.freq = freq;
            this.sugg = sugg;

            Log.d("values",patientprescriptionno+medinfo+unit+qtys+timedur+afbefore+freq+sugg);
        }

        protected String doInBackground(String... args) {

                    String patno = String.valueOf(pid);
                    Log.d("value for paramter", String.valueOf(pid));
                        List<NameValuePair> params = new ArrayList<NameValuePair>();

                        params.add(new BasicNameValuePair(TAG_PATIENTPRESCRIPTIONNO, patientprescriptionno));
                        params.add(new BasicNameValuePair(TAG_MEDINFOID, medinfo));
                        params.add(new BasicNameValuePair(TAG_UNITID, unit));
                        params.add(new BasicNameValuePair(TAG_Quantity,qtys));
                        params.add(new BasicNameValuePair(TAG_TimeDuration,timedur));
                        params.add(new BasicNameValuePair(TAG_AfterOrBefore,afbefore));
                        params.add(new BasicNameValuePair(TAG_Frequeancy,freq));
                        params.add(new BasicNameValuePair(TAG_SUGGESTION,sugg));

                        Log.d("param",params.toString());
                        JSONObject json = jsonParser.makeHttpRequest(REGISTER_SAVEAPPOINMENT, "POST", params);

                        try {

                            int success = json.getInt(TAG_SUCCESS);

                            //Toast.makeText(MedicineList.this, "" + success, Toast.LENGTH_SHORT).show();
                            if (success == 1) {
                                // successfully created product

                                Intent j = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(j);

                                // closing this screen
                                finish();
                            } else {
                                // failed to create product
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

            return null;
        }
    }
}
