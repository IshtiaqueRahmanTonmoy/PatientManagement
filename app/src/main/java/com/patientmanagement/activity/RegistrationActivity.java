package com.patientmanagement.activity;


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
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

public class RegistrationActivity extends AppCompatActivity {

    private static final String REGISTER_URL = "http://darumadhaka.com/patientmanagement/patientinfo.php";

    private int PICK_IMAGE_REQUEST = 1;
    public static final String KEY_NAME = "name";
    public static final String KEY_Image = "image";
    public static final String KEY_Age = "age";
    public static final String KEY_Gender = "gender";
    public static final String KEY_Address = "address";
    public static final String KEY_PatientMobNo = "mobileno";
    public static final String KEY_Disease = "disease";
    public static final String KEY_EncryptedPassword = "encryptedpassword";
    public static final String KEY_DATE = "date";
    Button signup;
    ImageView photoimage;
    TextView uploadimage,loginback,captureimage;
    EditText patientname, patientaddress, patientage, patientgender, patientmobileno, diseasename,password;
    String name;
    String image;
    String address;
    String age;
    String gender;
    String mobileno;
    String disease;
    String epassword;
    String date;
    private final int requestCode = 20;
    private static final int CAMERA_REQUEST = 1888;
    private static final String TAG_SUCCESS = "success";
    JSONParser jsonParser = new JSONParser();
    private Uri filePath;
    private Bitmap bitmap;
    private Calendar calendar;
    private ProgressDialog pDialog;
    private int year, month, day;
    AlertDialog.Builder alertdialogbuilder;

    String[] AlertDialogItems = new String[]{
            "Male",
            "Female"
    };

    boolean[] Selectedtruefalse = new boolean[]{
            false,
            false
    };

    List<String> ItemsIntoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        photoimage = (ImageView) findViewById(R.id.patientimage);
        captureimage = (TextView) findViewById(R.id.imagecapture);
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
        password = (EditText) findViewById(R.id.password);

        captureimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

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

        uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        patientmobileno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSpinnerDialog();
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
                epassword = password.getText().toString();

                new CreateNewUser().execute();
            }
        });
    }

    private void showSpinnerDialog() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        /*
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                photoimage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        */

        if (requestCode == CAMERA_REQUEST) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            photoimage.setImageBitmap(photo);
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
                                  Intent i = new Intent(getApplicationContext(), PatientDashboard.class);
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
}