package com.patientmanagement.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ScrollingTabContainerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import patientsmanagement.patientmanagement.patientsmanagementsystem.R;

import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.JSONParser;
import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.Medicine;
import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.SystemClass;

public class AddPrescriptionActivity extends AppCompatActivity {

    EditText prescriptionNo,quantity,timeduration,frequent,followuptime,Suggestion;
    Spinner Medicinename,MedicineUnit,AfterorBefore;
    TextView name,address;
    Button AddmoreMedicine;
    ImageButton imagebutton;
    JSONParser jP = new JSONParser();
    private static final String TAG_MEDICINEURL = "http://darumadhaka.com/patientmanagement/medicineinfo.php";
    private static final String TAG_MediUnitURL = "http://darumadhaka.com/patientmanagement/medicineunit.php";
    private static final String getmedicineinfo_url = "http://darumadhaka.com/patientmanagement/medicineunitid.php";
    private static final String getmedicineunit_url = "http://darumadhaka.com/patientmanagement/medicineinfoget.php";
    private static final String DOCTORDETAILGET_URL = "http://darumadhaka.com/patientmanagement/searchallpatientinfo.php";
    private static final String TAG_MedicineName = "MediName";
    private static final String TAG_MediUnitId = "MediUnitId";
    private static final String TAG_UnitName = "UnitName";
    private static final String TAG_MEDINFO = "medinfo";
    private static final String TAG_INFOID = "MediInfoId";
    private static final String TAG_PHONE = "phone";
    private static final String TH = "th";
    private static final String TAG_IMAGE = "image";
    private static final String TAG_NAME = "name";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_DOCTORLIST = "patientdetail";
    String MediName,MediUnitId,MediUnitname,Total,Quantity;
    ArrayList<String> medicineList;
    ArrayList<String> medicineUnit;
    ArrayList<String[]> AfterBeforeList;
    private JSONArray jsonarray;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ALLMEDICINE = "allmedicine";
    String values[] = {"Before","After"};
    List<Medicine> medicinesList,cartList;
    SystemClass sys;
    ArrayList<Medicine> result = new ArrayList<Medicine>();
    Medicine medicine;
    String mobnopatient;
    Bitmap bmp1;
    String namevalue,addressvalue;
    String unitname,tduration,MedinnameParam,MedinfoId,MediUnitIdVal,afterbefore,frequently,suggestion,doctorid,followupdate,prescriptionno;
    int i = 0;
    private List<String> ItemsIntoList;
    private AlertDialog.Builder alertdialogbuilder;
    private ProgressDialog pDialog;
    StringBuilder value;
    StringBuilder finalval;
    String[] AlertDialogItems = new String[]{
            "1/0/0",
            "0/1/0",
            "0/0/1",
            "1/0/1",
            "1/1/0",
            "0/1/1",
            "1/1/1"
    };

    boolean[] Selectedtruefalse = new boolean[]{
            false,
            false,
            false,
            false,
            false,
            false,
            false
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prescription);

        i = i +1;
        mobnopatient = getIntent().getExtras().getString("mobilephonevalue");
        doctorid = getIntent().getExtras().getString("doctorid");

        prescriptionno = "00000000".substring(doctorid.length()) + doctorid;
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message-pass"));

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        ItemsIntoList = new ArrayList<>();
        medicine = new Medicine();
        medicineList = new ArrayList<String>();
        medicineUnit = new ArrayList<>();

        imagebutton = (ImageButton) findViewById(R.id.user_profile_photo);
        name = (TextView) findViewById(R.id.user_profile_name);
        address = (TextView) findViewById(R.id.user_profile_short_bio);
        prescriptionNo = (EditText) findViewById(R.id.PrescriptionNo);
        quantity = (EditText) findViewById(R.id.Quantity);
        timeduration = (EditText) findViewById(R.id.Timeduration);
        followuptime = (EditText) findViewById(R.id.NextFollowup);
        frequent = (EditText) findViewById(R.id.Frequent);

        Suggestion = (EditText) findViewById(R.id.addSugestion);
        AddmoreMedicine = (Button) findViewById(R.id.AddmoreMedicine);

        prescriptionNo.setText(""+prescriptionno);

        cartList = new ArrayList<Medicine>();
        sys = new SystemClass();
        medicinesList = new ArrayList<Medicine>();
        AfterBeforeList = new ArrayList<>();

        Medicinename = (Spinner) findViewById(R.id.MedicineName);
        MedicineUnit = (Spinner) findViewById(R.id.MedicineUnit);
        AfterorBefore = (Spinner) findViewById(R.id.AfterorBefore);

        AfterBeforeList.add(values);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, values);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        AfterorBefore.setAdapter(spinnerArrayAdapter);

        new getPatientDetail().execute();
        new MedicineName().execute();
        new MedicineUnit().execute();

        frequent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertdialogbuilder = new AlertDialog.Builder(AddPrescriptionActivity.this);

                ItemsIntoList = Arrays.asList(AlertDialogItems);

                alertdialogbuilder.setMultiChoiceItems(AlertDialogItems, Selectedtruefalse, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                    }
                });

                alertdialogbuilder.setCancelable(false);
                alertdialogbuilder.setTitle("Select Frequent times");

                alertdialogbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        StringBuffer responseText = new StringBuffer();
                        int a = 0;
                        while (a < Selectedtruefalse.length) {
                            boolean value = Selectedtruefalse[a];

                            if (value) {
                                String names = ItemsIntoList.get(a);
                                responseText.append(names);
                                frequent.setText("" + responseText);
                            }
                            a++;
                        }
                    }
                });

                alertdialogbuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = alertdialogbuilder.create();
                dialog.show();
            }
        });


        Medicinename.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MedinnameParam = Medicinename.getSelectedItem().toString();
                new GetInfoId().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        MedicineUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MediUnitname = MedicineUnit.getSelectedItem().toString();
                new GetMedinUnitId().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        AddmoreMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MediName = Medicinename.getSelectedItem().toString();
                MediUnitname = MedicineUnit.getSelectedItem().toString();
                afterbefore = AfterorBefore.getSelectedItem().toString();
                Quantity = quantity.getText().toString();
                tduration = timeduration.getText().toString();
                frequently = frequent.getText().toString();
                followupdate = followuptime.getText().toString();
                suggestion = Suggestion.getText().toString();

                //Toast.makeText(AddPrescriptionActivity.this, "Quantity"+Quantity, Toast.LENGTH_SHORT).show();
                result.add(new Medicine(MediName,MedinfoId,MediUnitname,MediUnitIdVal,Quantity,tduration,afterbefore,frequently,suggestion));

                //Toast.makeText(AddPrescriptionActivity.this, ""+MediName, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(AddPrescriptionActivity.this, MedicineList.class);
                String listSerializedToJson = new Gson().toJson(result);

                intent.putExtra("listget", listSerializedToJson);
                intent.putExtra("prescriptinno",i);
                intent.putExtra("val",values);
                intent.putExtra("medinfoid",MedinfoId);
                intent.putExtra("mediunitidval",MediUnitIdVal);
                intent.putExtra("quantity",Quantity);
                intent.putExtra("timeduration",tduration);
                intent.putExtra("afterbefore",afterbefore);
                intent.putExtra("frequently",frequently);
                intent.putExtra("suggestion",suggestion);
                intent.putExtra("prescriptionno",prescriptionno);
                intent.putExtra("doctorid",doctorid);
                intent.putExtra("followup",followupdate);
                intent.putExtra("mobnopatient",mobnopatient);
                startActivity(intent);
            }
        });
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            sys.getMedicine().clear();
            String carListAsString = intent.getStringExtra("listget");
            Gson gson = new Gson();
            Type type = new TypeToken<List<Medicine>>(){}.getType();
            cartList = gson.fromJson(carListAsString, type);
            //Log.d("carlist",cartList.get(0.getFoodrate());
            for(int i=0;i<cartList.size();i++){
                String mediname = cartList.get(i).getMedicineName();
                String mediunit = cartList.get(i).getUnit();

                //Toast.makeText(AddPrescriptionActivity.this, ""+quantity, Toast.LENGTH_SHORT).show();
                medicinesList.add(new Medicine(mediname,mediunit));
                sys.setMedicine(medicinesList);
            }
        }
    };

    private class MedicineName extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    List<NameValuePair> param =
                            new ArrayList<NameValuePair>();
                    // getting JSON string from URL
                    JSONObject json = jP.makeHttpRequest(TAG_MEDICINEURL, "GET", param);

                    // Check your log cat for JSON reponse
                    Log.d("All Doctors: ", json.toString());

                    try {
                        // Checking for SUCCESS TAG
                        int success = json.getInt(TAG_SUCCESS);

                        if (success == 1) {
                            // products found
                            // Getting Array of Products
                            jsonarray = json.getJSONArray(TAG_ALLMEDICINE);

                            // looping through All Products
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject c = jsonarray.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<String, String>();
                                // Storing each json item in variable
                                MediName = c.getString(TAG_MedicineName);
                                MediUnitId = c.getString(TAG_MediUnitId);

                                //Toast.makeText(AddPrescriptionActivity.this, ""+MediName+""+MediUnitId, Toast.LENGTH_SHORT).show();
                                medicineList.add(MediName);

                                ArrayAdapter<String> adp= new ArrayAdapter<String>(AddPrescriptionActivity.this,
                                        android.R.layout.simple_list_item_1,medicineList);
                                adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                Medicinename.setAdapter(adp);
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

    public class MedicineUnit extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    List<NameValuePair> param =
                            new ArrayList<NameValuePair>();
                    // getting JSON string from URL
                    JSONObject json = jP.makeHttpRequest(TAG_MediUnitURL, "GET", param);

                    // Check your log cat for JSON reponse
                    Log.d("All Doctors: ", json.toString());

                    try {
                        // Checking for SUCCESS TAG
                        int success = json.getInt(TAG_SUCCESS);

                        if (success == 1) {
                            // products found
                            // Getting Array of Products
                            jsonarray = json.getJSONArray(TAG_ALLMEDICINE);

                            // looping through All Products
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject c = jsonarray.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<String, String>();
                                // Storing each json item in variable
                                unitname = c.getString(TAG_UnitName);
                                //Toast.makeText(AddPrescriptionActivity.this, ""+unitname, Toast.LENGTH_SHORT).show();
                                medicineUnit.add(unitname);

                                ArrayAdapter<String> adp= new ArrayAdapter<String>(AddPrescriptionActivity.this,
                                        android.R.layout.simple_list_item_1,medicineUnit);
                                adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                MedicineUnit.setAdapter(adp);
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

    private class GetInfoId extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(final String... params) {
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair(TAG_MedicineName,"'"+MedinnameParam+"'"));

                        // getting product details by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jP.makeHttpRequest(
                                getmedicineunit_url, "GET", params);

                        // check your log for json response
                        Log.d("Single Product Details", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received product details
                            JSONArray patientObj = json
                                    .getJSONArray(TAG_MEDINFO); // JSON Array

                            // get first product object from JSON Array
                            JSONObject medinfo = patientObj.getJSONObject(0);

                            // product with this pid found
                            // Edit Text
                            MedinfoId = medinfo.getString(TAG_INFOID);
                            Log.d("mediInfoId",MedinfoId);
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
    }

    private class  GetMedinUnitId extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(final String... params) {
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair(TAG_UnitName, MediUnitname));

                        // getting product details by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jP.makeHttpRequest(
                                getmedicineinfo_url , "GET", params);

                        // check your log for json response
                        Log.d("Single Product Details", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received product details
                            JSONArray patientObj = json
                                    .getJSONArray(TH); // JSON Array

                            // get first product object from JSON Array
                            JSONObject mediunit = patientObj.getJSONObject(0);

                            // product with this pid found
                            // Edit Text
                            MediUnitIdVal = mediunit.getString(TAG_MediUnitId);
                            Log.d("mediunitvalue",MediUnitIdVal);

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
    }


    private class getPatientDetail extends AsyncTask<String, String, String> {

        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddPrescriptionActivity.this);
            pDialog.setMessage("Getting prescription...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair(TAG_PHONE,  mobnopatient));
            // getting JSON string from URL
            JSONObject json = jP.makeHttpRequest(DOCTORDETAILGET_URL, "GET", param);

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
                        addressvalue = c.getString(TAG_ADDRESS);
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

            imagebutton.setImageBitmap(bmp1);
            name.setText(namevalue);
            address.setText(addressvalue);
        }
    }
}
