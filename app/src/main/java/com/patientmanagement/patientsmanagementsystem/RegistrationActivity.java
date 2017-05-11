package com.patientmanagement.patientsmanagementsystem;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.JSONParser;
import patientsmanagement.patientmanagement.patientsmanagementsystem.R;
import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.Person;

public class RegistrationActivity extends AppCompatActivity {

    private static final String REGISTER_URL = "http://patientmanagement.medi-bd.com/patientmanagement/patientinfo.php";
    private static final String GETPHONE_URL = "http://patientmanagement.medi-bd.com/patientmanagement/allpatientmobileno.php";
    private static final String GETDIVISION_URL = "http://patientmanagement.medi-bd.com/patientmanagement/getdivision.php";
    private static final String GETJONE_URL = "http://patientmanagement.medi-bd.com/patientmanagement/getjone.php";
    private static final String GETDISTRICT_URL = "http://patientmanagement.medi-bd.com/patientmanagement/getdistrict.php";
    private static final String GETIDDISTRCIT_URL = "http://patientmanagement.medi-bd.com/patientmanagement/getiddistrict.php";
    private static final String GETTHANA_URL = "http://patientmanagement.medi-bd.com/patientmanagement/getThana.php";
    private int PICK_IMAGE_REQUEST = 1;
    public static final String KEY_NAME = "name";
    public static final String KEY_PHONE = "phone";
    public static final String TAG_DIVID = "div_id";
    public static final String TAG_DIVISIONID = "division_id";
    public static final String TAG_JONALID = "jonal_id";
    public static final String KEY_ID = "id";
    public static final String KEY_DISNAME = "name";
    public static final String TAG_DISTRICTNAMEVALUE = "name";
    public static final String KEY_Image = "image";
    public static final String KEY_Age = "age";
    public static final String KEY_Gender = "gender";
    public static final String KEY_Address = "address";
    public static final String KEY_PatientMobNo = "mobileno";
    public static final String KEY_Disease = "disease";
    public static final String KEY_EncryptedPassword = "encryptedpassword";
    public static final String KEY_BLOODGROUP = "bloodgroup";
    public static final String KEY_DATE = "date";
    public static final String TAG_ALLCONTACT = "alldoctor";
    public static final String TAG_ALLDVISION = "division";
    public static final String TAG_TH = "th";
    public static final String TAG_JONENAME = "name";
    public static final String TAG_DISTRICTNAME = "name";
    public static final String TAG_JONEID = "id";
    public static final String TAG_DISTRICTID = "id";
    public static final String TAG_THANAID = "id";
    public static final String TAG_DIVIDGET = "id";
    public static final String TAG_THANANAME = "name";
    Button signup;
    ImageView photoimage;
    TextView uploadimage,loginback,captureimage;
    EditText patientname, patientaddress, patientage, patientgender, patientmobileno, diseasename,bloodgroup,password;
    Spinner districtspinner,divisionspinner,thanaspinner,jonespinner;
    String id,divisionname,districtname,districtid;
    String name,districtnamevalue;
    String image;
    String address;
    String age,jonal_id;
    String gender,thanatid,thananame;
    String mobileno;
    String disease,div,dis,jon,tn;
    String epassword;
    String phone,division_id;
    String blood,jonename;
    String div_id;
    private JSONArray jsonArray;
    private final int requestCode = 20;
    private static final int CAMERA_REQUEST = 1888;
    private static final String TAG_SUCCESS = "success";
    JSONParser jsonParser = new JSONParser();
    private Uri filePath;
    private Bitmap bitmap;
    private Calendar calendar;
    private ProgressDialog pDialog;
    private int year, month, day;
    AlertDialog.Builder alertdialogbuilder,alertdialogbuilder1;
    Person person;
    ArrayList<String> alist,jonelist,dislist,thanalist;
    ArrayAdapter<String> spinnerArrayAdapter,joneadapter,districtadapter,thanaadapter;
    String[] AlertDialogItems = new String[]{
            "Male",
            "Female"
    };

    boolean[] Selectedtruefalse = new boolean[]{
            false,
            false
    };


    String[] Bloodgroups = new String[]{
            "O+",
            "O-",
            "A+",
            "A-",
            "B+",
            "B-",
            "AB+",
            "AB-"
    };

    boolean[] Selectedtruefalseblood = new boolean[]{
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false
    };

    List<String> ItemsIntoList,ItemsIntoList1,DisList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Patient Registration");
        setSupportActionBar(toolbar);

        new DownloadJSON().execute();
        new getDivision().execute();

        alist = new ArrayList<String>();
        DisList = new ArrayList<String>();
        thanalist = new ArrayList<String>();

        photoimage = (ImageView) findViewById(R.id.patientimage);
        //captureimage = (TextView) findViewById(R.id.imagecapture);
        uploadimage = (TextView) findViewById(R.id.imageupload);
        //signup = (TextView) findViewById(R.id.signup);
        signup = (Button) findViewById(R.id.submit);
        loginback = (TextView) findViewById(R.id.login);

        patientname = (EditText) findViewById(R.id.name);
        patientaddress = (EditText) findViewById(R.id.address);
        patientage = (EditText) findViewById(R.id.age);
        patientgender = (EditText) findViewById(R.id.gender);
        patientmobileno = (EditText) findViewById(R.id.mobileno);
        diseasename = (EditText) findViewById(R.id.disease);
        bloodgroup = (EditText) findViewById(R.id.bloodgroup);
        password = (EditText) findViewById(R.id.password);

        divisionspinner = (Spinner) findViewById(R.id.division);
        districtspinner = (Spinner) findViewById(R.id.district);


        patientgender.setFocusable(false);
        bloodgroup.setFocusable(false);

        patientgender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertdialogbuilder = new AlertDialog.Builder(RegistrationActivity.this);

                ItemsIntoList = Arrays.asList(AlertDialogItems);

                alertdialogbuilder.setMultiChoiceItems(AlertDialogItems, Selectedtruefalse, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    }
                });

                alertdialogbuilder.setCancelable(false);
                alertdialogbuilder.setTitle("Select Gender Here");

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
                                patientgender.setText("" + responseText);
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

        bloodgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertdialogbuilder1 = new AlertDialog.Builder(RegistrationActivity.this);

                ItemsIntoList1 = Arrays.asList(Bloodgroups);

                alertdialogbuilder1.setMultiChoiceItems(Bloodgroups, Selectedtruefalseblood, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    }
                });

                alertdialogbuilder1.setCancelable(false);
                alertdialogbuilder1.setTitle("Select Blood Groups Here");

                alertdialogbuilder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        StringBuffer responseText = new StringBuffer();
                        int a = 0;
                        while (a < Selectedtruefalseblood.length) {
                            boolean value = Selectedtruefalseblood[a];

                            if (value) {
                                String namess = ItemsIntoList1.get(a);
                                //Toast.makeText(RegistrationActivity.this, ""+names, Toast.LENGTH_SHORT).show();
                                responseText.append(namess);
                                bloodgroup.setText("" + responseText);
                            }
                            a++;
                        }
                    }
                });

                alertdialogbuilder1.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = alertdialogbuilder1.create();
                dialog.show();
            }
        });

        uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = patientname.getText().toString();
                if(bitmap!=null) {
                    image = getStringImage(bitmap);
                }
                else{
                    image = "";
                }
                address = patientaddress.getText().toString();
                age = patientage.getText().toString();
                gender = patientgender.getText().toString();
                mobileno = patientmobileno.getText().toString();
                disease = diseasename.getText().toString();
                blood = bloodgroup.getText().toString();
                epassword = password.getText().toString();
                div = divisionspinner.getSelectedItem().toString();
                dis = districtspinner.getSelectedItem().toString();
                //jon = jonespinner.getSelectedItem().toString();

                    if(name.length()==0){
                        patientname.setError("Field cannot be null");
                    }
                    if(address.length()==0){
                        patientaddress.setError("Field cannot be null");
                    }
                    if(age.length()==0){
                        patientage.setError("Field cannot be null");
                    }
                    if(gender.length()==0){
                        patientgender.setError("Field cannot be null");
                    }
                    if(mobileno.length()==0){
                        patientmobileno.setError("Field cannot be null");
                    }
                    if(disease.length()==0){
                        diseasename.setError("Field cannot be null");
                    }
                   if(blood.length()==0){
                    bloodgroup.setError("Field cannot be null");
                   }
                    if(epassword.length()==0){
                        password.setError("Field cannot be null");
                    }

                    patientname.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            patientname.setError(null);
                        }
                    });

                patientaddress.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        patientaddress.setError(null);
                    }
                });

                patientage.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        patientage.setError(null);
                    }
                });

                patientgender.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        patientgender.setError(null);
                    }
                });

                patientmobileno.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        //Toast.makeText(RegistrationActivity.this, ""+alist.size(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                       patientmobileno.setError(null);
                        //String search = "A";
                        String search = patientmobileno.getText().toString();
                        for(String str: alist) {
                            if(str.trim().contains(search)){
                                //Toast.makeText(RegistrationActivity.this, "This name already contains", Toast.LENGTH_SHORT).show();
                                patientmobileno.setError("This mobile no is already in used");
                            }
                        }
                    }
                });

                diseasename.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        diseasename.setError(null);
                    }
                });

                password.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        password.setError(null);
                    }
                });

                //new CreateNewUser().execute();

                if(!name.isEmpty() && !address.isEmpty() && !age.isEmpty() && !gender.isEmpty() && !mobileno.isEmpty() && !disease.isEmpty() && !blood.isEmpty() && !epassword.isEmpty() && !div.isEmpty() && !dis.isEmpty()){
                    //Toast.makeText(RegistrationActivity.this, "field is not null", Toast.LENGTH_SHORT).show();

                    new CreateNewUser().execute();
                    //Toast.makeText(RegistrationActivity.this, "div"+div+"dis"+dis+"jone"+jonename+""+jon+"thana"+tn, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                photoimage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    class CreateNewUser extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegistrationActivity.this);
            pDialog.setMessage("Creating User..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        protected String doInBackground(String... args) {

                  List<NameValuePair> params = new ArrayList<NameValuePair>();

                  params.add(new BasicNameValuePair(KEY_NAME, name));
                  params.add(new BasicNameValuePair(KEY_Image, image));
                  params.add(new BasicNameValuePair(KEY_Age, age));
                  params.add(new BasicNameValuePair(KEY_Gender, gender));
                  params.add(new BasicNameValuePair(KEY_Address, address));
                  params.add(new BasicNameValuePair(KEY_PatientMobNo, mobileno));
                  params.add(new BasicNameValuePair(KEY_Disease, disease));
                  params.add(new BasicNameValuePair(KEY_BLOODGROUP, blood));
                  params.add(new BasicNameValuePair(KEY_EncryptedPassword, epassword));

                  JSONObject json = jsonParser.makeHttpRequest(REGISTER_URL, "POST", params);

                  try {

                      int success = json.getInt(TAG_SUCCESS);

                     // Toast.makeText(RegistrationActivity.this, "" + success, Toast.LENGTH_SHORT).show();
                      if (success == 1) {
                          // successfully created product

                          RegistrationActivity.this.runOnUiThread(new Runnable() {
                              public void run() {
                                  Toast.makeText(RegistrationActivity.this.getBaseContext(), "Registration completed..", Toast.LENGTH_LONG).show();
                                  Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                  startActivity(i);
                                  finish();
                              }
                          });
                      } else {
                          // failed to create product
                      }
                  } catch (JSONException e) {
                      e.printStackTrace();
                  }

                  return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }
    }


    private class DownloadJSON extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
                    List<NameValuePair>param = new ArrayList<NameValuePair>();
                    JSONObject json= jsonParser.makeHttpRequest(GETPHONE_URL,"GET",param);

                    try {
                        // Checking for SUCCESS TAG
                        int success = json.getInt(TAG_SUCCESS);

                        if (success == 1) {
                            // products found
                            // Getting Array of Products
                            jsonArray = json.getJSONArray(TAG_ALLCONTACT);

                            // looping through All Products
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject c = jsonArray.getJSONObject(i);
                                // Storing each json item in variable
                                phone = c.getString(KEY_PHONE);
                                alist.add(phone);

                            }
                        } else {
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            return null;
        }
    }

    private class getDivision extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

                    List<NameValuePair>param = new ArrayList<NameValuePair>();
                    JSONObject json= jsonParser.makeHttpRequest(GETDIVISION_URL,"GET",param);

                    try {
                        // Checking for SUCCESS TAG
                        int success = json.getInt(TAG_SUCCESS);

                        if (success == 1) {
                            // products found
                            // Getting Array of Products
                            jsonArray = json.getJSONArray(TAG_ALLDVISION);

                            // looping through All Products
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject c = jsonArray.getJSONObject(i);
                                // Storing each json item in variable
                                id = c.getString(KEY_ID);
                                divisionname = c.getString(KEY_DISNAME);
                                DisList.add(divisionname);

                               spinnerArrayAdapter = new ArrayAdapter<String>(
                                        RegistrationActivity.this,R.layout.spinner_item,DisList);

                            }
                        } else {
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            divisionspinner.setAdapter(spinnerArrayAdapter);
            divisionspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
               @Override
               public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                   div_id = String.valueOf(divisionspinner.getSelectedItemPosition() + 1);
                   //Toast.makeText(RegistrationActivity.this, ""+Hold, Toast.LENGTH_SHORT).show();
                   new getJone().execute();

               }

               @Override
               public void onNothingSelected(AdapterView<?> parent) {

               }
           });
        }
    }



    private class getJone extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            int success;
            try {
                jonelist = new ArrayList<String>();
                // Building Parameters
                List<NameValuePair> paramss = new ArrayList<NameValuePair>();
                paramss.add(new BasicNameValuePair(TAG_DIVID, div_id));

                // getting product details by making HTTP request
                // Note that product details url will use GET request
                JSONObject json = jsonParser.makeHttpRequest(
                        GETJONE_URL, "GET", paramss);

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // successfully received product details
                    JSONArray productObj = json
                            .getJSONArray(TAG_TH); // JSON Array

                    JSONObject lnews = productObj.getJSONObject(0);
                    jonal_id = lnews.getString(TAG_JONEID);
                    jonename = lnews.getString(TAG_JONENAME);
                    jonelist.add(jonename);
                    joneadapter = new ArrayAdapter<String>(
                            RegistrationActivity.this,R.layout.spinner_item,jonelist);


                } else {
                    // product with pid not found
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //jonespinner.setAdapter(joneadapter);
            new getDistrict().execute();
        }
    }


    private class getDistrict extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            int success;
            try {
                dislist = new ArrayList<>();
                // Building Parameters
                List<NameValuePair> paramss = new ArrayList<NameValuePair>();
                paramss.add(new BasicNameValuePair(TAG_JONALID, jonal_id));

                // getting product details by making HTTP request
                // Note that product details url will use GET request
                JSONObject json = jsonParser.makeHttpRequest(
                        GETDISTRICT_URL, "GET", paramss);

                // json success tag
                success = json.getInt(TAG_SUCCESS
                );
                if (success == 1) {
                    // successfully received product details
                    JSONArray productObj = json
                            .getJSONArray(TAG_TH); // JSON Array
                    for (int i=0;i<productObj.length();i++)
                    {
                        JSONObject lnews = productObj.getJSONObject(i);
                        districtid = lnews.getString(TAG_DISTRICTID);
                        districtname = lnews.getString(TAG_DISTRICTNAME);
                        //Toast.makeText(RegistrationActivity.this, ""+districtid, Toast.LENGTH_SHORT).show();
                        dislist.add(districtname);

                    }

                } else {
                    // product with pid not found
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Toast.makeText(RegistrationActivity.this, ""+div_id, Toast.LENGTH_SHORT).show();
            districtadapter = new ArrayAdapter<String>(
                    RegistrationActivity.this,R.layout.spinner_item,dislist);
            districtspinner.setAdapter(districtadapter);
            districtspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    districtnamevalue = districtspinner.getSelectedItem().toString();
                    new getDistId().execute();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            //new getThana().execute();

        }
    }

    private class getDistId extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            int success;
            try {

                // Building Parameters
                List<NameValuePair> paramss = new ArrayList<NameValuePair>();
                paramss.add(new BasicNameValuePair(TAG_DISTRICTNAMEVALUE, '"'+districtnamevalue+'"'));

                // getting product details by making HTTP request
                // Note that product details url will use GET request
                JSONObject json = jsonParser.makeHttpRequest(
                        GETIDDISTRCIT_URL, "GET", paramss);

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // successfully received product details
                    JSONArray productObj = json
                            .getJSONArray(TAG_TH); // JSON Array
                    for(int x=0;x<productObj.length();x++) {
                        JSONObject lnews = productObj.getJSONObject(x);
                        division_id = lnews.getString(TAG_DIVIDGET);
                    }
                } else {
                    // product with pid not found
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Toast.makeText(RegistrationActivity.this, ""+division_id, Toast.LENGTH_SHORT).show();
            //new getThana().execute();
        }
    }

    /*
    private class getThana extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            int success;
            try {
                thanalist = new ArrayList<>();
                // Building Parameters
                List<NameValuePair> paramss = new ArrayList<NameValuePair>();
                paramss.add(new BasicNameValuePair(TAG_DIVISIONID, division_id));

                // getting product details by making HTTP request
                // Note that product details url will use GET request
                JSONObject json = jsonParser.makeHttpRequest(
                        GETTHANA_URL, "GET", paramss);

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // successfully received product details
                    JSONArray productObj = json
                            .getJSONArray(TAG_TH); // JSON Array
                    for (int i=0;i<productObj.length();i++)
                    {
                        JSONObject lnews = productObj.getJSONObject(i);
                        thanatid = lnews.getString(TAG_THANAID);
                        thananame = lnews.getString(TAG_THANANAME);
                        thanalist.add(thananame);
                    }

                } else {
                    // product with pid not found
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //Toast.makeText(RegistrationActivity.this, ""+thananame, Toast.LENGTH_SHORT).show();
            thanaadapter = new ArrayAdapter<String>(
                    RegistrationActivity.this,R.layout.spinner_item,thanalist);
            thanaspinner.setAdapter(thanaadapter);
            thanaspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //new getThana().execute();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }
    */
}