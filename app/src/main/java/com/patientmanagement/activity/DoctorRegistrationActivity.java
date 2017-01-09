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
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
    //private static final String REGISTER_URL = "http://darumadhaka.com/patientmanagement/AppSchedule.php";
    private int PICK_IMAGE_REQUEST = 1;
    private static final String TAG_SUCCESS = "success";

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

    String name,image,address,phone,expertise,chamberday,chambertime,startime,endtime,doctorfee,followupfee,password;
    TextView signup,uploadimage;
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

        signup = (TextView) findViewById(R.id.signup);

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

                        int a = 0;
                        while (a < Selectedtruefalse.length) {
                            boolean value = Selectedtruefalse[a];

                            if (value) {
                                expertiseEdt.setText("" + ItemsIntoList.get(a).toString());
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

                        int a = 0;
                        while (a < Selectedtruefalseforday.length) {
                            boolean value = Selectedtruefalseforday[a];

                            if (value) {
                                //Toast.makeText(DoctorRegistrationActivity.this, ""+dayIntoList.get(a).toString(), Toast.LENGTH_SHORT).show();
                                chamberdayEdt.setText("" + dayIntoList.get(a).toString());
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

                image = getStringImage(bitmap);
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

                Log.d("output",name+address+phone+expertise+chamberday+chambertime+doctorfee+followupfee+password);
                new CreateNewUser().execute();
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

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    //Toast.makeText(DoctorRegistrationActivity.this, ""+name+""+age, Toast.LENGTH_SHORT).show();

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

                    // getting JSON Object
                    // Note that create product url accepts POST method
                    JSONObject json = jsonParser.makeHttpRequest(REGISTER_URL, "POST", params);
                    //Log.d("json",json.toString());
                    // check log cat fro response
                    //Log.d("Create Response", json.toString());

                    // check for success tag
                    try {

                        int success = json.getInt(TAG_SUCCESS);

                        Toast.makeText(DoctorRegistrationActivity.this, "" + success, Toast.LENGTH_SHORT).show();
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
                }
            });
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
}



