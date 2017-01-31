package com.patientmanagement.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.util.List;

import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.JSONParser;
import patientsmanagement.patientmanagement.patientsmanagementsystem.R;

/**
 * Created by Administrator on 12/4/2016.
 */
public class DoctorRegistrationActivity extends AppCompatActivity {

    private static final String REGISTER_URL = "http://darumadhaka.com/patientmanagement/doctorregistration.php";
    private static final String GETPHONE_URL = "http://darumadhaka.com/patientmanagement/getalldoctorno.php";
    private int PICK_IMAGE_REQUEST = 1;
    private static final String TAG_SUCCESS = "success";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_NAME = "name";
    public static final String KEY_Image = "image";
    public static final String KEY_Address = "address";
    public static final String KEY_Phone = "phone";
    public static final String KEY_Expertise = "expertise";
    public static final String KEY_Chamberday = "chamberday";
    public static final String KEY_Chambertime = "chambertime";
    public static final String KEY_Doctorfee = "doctorfee";
    public static final String KEY_Followupfee = "followupfeee";
    public static final String KEY_Password = "password";
    public static final String TAG_ALLCONTACT = "alldoctor";
    String name,image,address,phone,expertise,chamberday,chambertime,startime,endtime,doctorfee,followupfee,password;
    TextView uploadimage;
    //Button uploadimage;
    private JSONArray jsonArray;
    EditText nameEdt,addressEdt,phoneEdt,expertiseEdt, chamberdayEdt,startEdt, endEdt,doctorfeeEdt,followupfeeEdt,passwordEdt;
    ImageView doctorimagepic;
    AlertDialog.Builder alertdialogbuilder;
    private int CalendarHour, CalendarMinute;
    String format;
    Calendar calendar;
    TimePickerDialog timepickerdialog;
    JSONParser jsonParser = new JSONParser();
    private Uri filePath;
    private Bitmap bitmap;
    private ProgressDialog pDialog;
    Button signup;
    StringBuffer responseText = new StringBuffer();
    ArrayList<String> alist;
    String[] AlertDialogItems = new String[]{
            "E.N.T Specialist",
            "Child Health specialist",
            "Orthopaedic Surgeon",
            "Cardiologist"
    };

    String[] AlertDialogItemsforDay = new String[]{
            "Friday",
            "Saturday",
            "Sunday",
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday"
    };

    List<String> ItemsIntoList,dayIntoList;

    boolean[] Selectedtruefalse = new boolean[]{
            false,
            false,
            false,
            false,
    };

    boolean[] Selectedtruefalseforday = new boolean[]{
            false,
            false,
            false,
            false,
            false,
            false,
            false
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctorregistration);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        new DownloadJSON().execute();

        alist = new ArrayList<String>();
       // uploadimage = (Button) findViewById(R.id.imageupload);
        uploadimage = (TextView) findViewById(R.id.imageupload);
        nameEdt = (EditText) findViewById(R.id.name);
        addressEdt = (EditText) findViewById(R.id.address);
        phoneEdt = (EditText) findViewById(R.id.mobileno);
        expertiseEdt = (EditText) findViewById(R.id.expertise);
        chamberdayEdt = (EditText) findViewById(R.id.chamberday);
        startEdt = (EditText) findViewById(R.id.chamberstarttime);
        endEdt = (EditText) findViewById(R.id.chamberendtime);
        doctorfeeEdt = (EditText) findViewById(R.id.doctorfee);
        followupfeeEdt = (EditText) findViewById(R.id.followupfee);
        passwordEdt = (EditText) findViewById(R.id.password);
        doctorimagepic = (ImageView) findViewById(R.id.doctorimage);

        signup = (Button) findViewById(R.id.submit);
        //signup = (TextView) findViewById(R.id.signup);

        expertiseEdt.setFocusable(false);
        chamberdayEdt.setFocusable(false);
        startEdt.setFocusable(false);
        endEdt.setFocusable(false);

        uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        expertiseEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertdialogbuilder = new AlertDialog.Builder(DoctorRegistrationActivity.this);

                ItemsIntoList = Arrays.asList(AlertDialogItems);

                alertdialogbuilder.setMultiChoiceItems(AlertDialogItems, Selectedtruefalse, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                    }
                });

                alertdialogbuilder.setCancelable(false);
                alertdialogbuilder.setTitle("Select Designation Here");

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
                                expertiseEdt.setText("" + responseText);
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

        chamberdayEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertdialogbuilder = new AlertDialog.Builder(DoctorRegistrationActivity.this);

                dayIntoList = Arrays.asList(AlertDialogItemsforDay);

                alertdialogbuilder.setMultiChoiceItems(AlertDialogItemsforDay, Selectedtruefalseforday, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                    }
                });

                alertdialogbuilder.setCancelable(false);
                alertdialogbuilder.setTitle("Select Days Here");

                alertdialogbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        StringBuffer responseTxt = new StringBuffer();
                        int a = 0;
                        while (a < Selectedtruefalseforday.length) {
                            boolean value = Selectedtruefalseforday[a];

                            if (value) {
                                String names = dayIntoList.get(a);
                                responseTxt.append(names);
                                chamberdayEdt.setText("" + responseTxt);
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

        startEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
                CalendarMinute = calendar.get(Calendar.MINUTE);
                timepickerdialog = new TimePickerDialog(DoctorRegistrationActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                if (hourOfDay == 0) {

                                    hourOfDay += 12;

                                    format = "AM";
                                }
                                else if (hourOfDay == 12) {

                                    format = "PM";

                                }
                                else if (hourOfDay > 12) {

                                    hourOfDay -= 12;

                                    format = "PM";

                                }
                                else {

                                    format = "AM";
                                }


                                startEdt.setText(hourOfDay + ":" + minute + format);
                            }
                        }, CalendarHour, CalendarMinute, false);
                timepickerdialog.show();

            }
        });


        endEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendar = Calendar.getInstance();
                CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
                CalendarMinute = calendar.get(Calendar.MINUTE);
                timepickerdialog = new TimePickerDialog(DoctorRegistrationActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                if (hourOfDay == 0) {

                                    hourOfDay += 12;

                                    format = "AM";
                                }
                                else if (hourOfDay == 12) {

                                    format = "PM";

                                }
                                else if (hourOfDay > 12) {

                                    hourOfDay -= 12;

                                    format = "PM";

                                }
                                else {

                                    format = "AM";
                                }


                                endEdt.setText(hourOfDay + ":" + minute + format);
                            }
                        }, CalendarHour, CalendarMinute, false);
                timepickerdialog.show();
            }

        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(bitmap!=null) {
                    image = getStringImage(bitmap);
                }
                else{
                    image = "";
                }

                name = nameEdt.getText().toString();
                address = addressEdt.getText().toString();
                phone = phoneEdt.getText().toString();
                expertise = expertiseEdt.getText().toString();
                chamberday = chamberdayEdt.getText().toString();
                startime = startEdt.getText().toString();
                endtime = endEdt.getText().toString();
                chambertime = startime+"-"+endtime;
                doctorfee = doctorfeeEdt.getText().toString();
                followupfee = followupfeeEdt.getText().toString();
                password = passwordEdt.getText().toString();

                if(name.length()==0){
                    nameEdt.setError("Field cannot be null");
                }
                if(address.length()==0){
                    addressEdt.setError("Field cannot be null");
                }
                if(phone.length()==0){
                    phoneEdt.setError("Field cannot be null");
                }
                if(expertise.length()==0){
                    expertiseEdt.setError("Field cannot be null");
                }
                if(chamberday.length()==0){
                    chamberdayEdt.setError("Field cannot be null");
                }
                if(startime.length()==0){
                    startEdt.setError("Field cannot be null");
                }
                if(endtime.length()==0){
                    endEdt.setError("Field cannot be null");
                }
                if(doctorfee.length()==0){
                    doctorfeeEdt.setError("Field cannot be null");
                }
                if(followupfee.length()==0){
                    followupfeeEdt.setError("Field cannot be null");
                }
                if(password.length()==0){
                    passwordEdt.setError("Field cannot be null");
                }

                nameEdt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        nameEdt.setError(null);
                    }
                });

                addressEdt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        addressEdt.setError(null);
                    }
                });

                phoneEdt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        phoneEdt.setError(null);
                        String search = phoneEdt.getText().toString();
                        for(String str: alist) {
                            if(str.trim().contains(search)){
                                //Toast.makeText(RegistrationActivity.this, "This name already contains", Toast.LENGTH_SHORT).show();
                                phoneEdt.setError("This mobile no is already in used");
                            }
                        }
                    }
                });

                expertiseEdt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        expertiseEdt.setError(null);
                    }
                });

                chamberdayEdt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        chamberdayEdt.setError(null);
                    }
                });

                startEdt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        startEdt.setError(null);
                    }
                });

                endEdt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        endEdt.setError(null);
                    }
                });

                doctorfeeEdt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        doctorfeeEdt.setError(null);
                    }
                });

                followupfeeEdt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        followupfeeEdt.setError(null);
                    }
                });

                passwordEdt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        passwordEdt.setError(null);
                    }
                });

                Log.d("output",name+address+phone+expertise+chamberday+chambertime+doctorfee+followupfee+password);
               // new CreateNewUser().execute();

                if(!name.isEmpty() && !address.isEmpty() && !phone.isEmpty() && !expertise.isEmpty() && !chamberday.isEmpty() && !startime.isEmpty() && !endtime.isEmpty() && !chambertime.isEmpty() && !doctorfee.isEmpty() && !followupfee.isEmpty() && !password.isEmpty()){
                    new CreateNewUser().execute();
                }
            }
        });
    }

    private String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                doctorimagepic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class CreateNewUser extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DoctorRegistrationActivity.this);
            pDialog.setMessage("Creating Doctor Information..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        protected String doInBackground(String... args) {

                    List<NameValuePair> params = new ArrayList<NameValuePair>();

                    params.add(new BasicNameValuePair(KEY_NAME, name));
                    params.add(new BasicNameValuePair(KEY_Image, image));
                    params.add(new BasicNameValuePair(KEY_Address, address));
                    params.add(new BasicNameValuePair(KEY_Phone, phone));
                    params.add(new BasicNameValuePair(KEY_Expertise, expertise));
                    params.add(new BasicNameValuePair(KEY_Chamberday, chamberday));
                    params.add(new BasicNameValuePair(KEY_Chambertime, chambertime));
                    params.add(new BasicNameValuePair(KEY_Doctorfee, doctorfee));
                    params.add(new BasicNameValuePair(KEY_Followupfee, followupfee));
                    params.add(new BasicNameValuePair(KEY_Password, password));

                    JSONObject json = jsonParser.makeHttpRequest(REGISTER_URL, "POST", params);

                    try {
                        int success = json.getInt(TAG_SUCCESS);

                        //Toast.makeText(DoctorRegistrationActivity.this, "" + success, Toast.LENGTH_SHORT).show();
                        if (success == 1) {
                            // successfully created product

                            DoctorRegistrationActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(DoctorRegistrationActivity.this.getBaseContext(), "Registration completed..", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(getApplicationContext(), DoctorLoginActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            });

                            // closing this screen

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

    private class DownloadJSON extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
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

                }
            });
            return null;
        }
    }
}



