package com.patientmanagement.patientsmanagementsystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import patientsmanagement.patientmanagement.patientsmanagementsystem.R;

public class PatientDashboard extends AppCompatActivity {

    private Button appoinmentSchedule;
    private Button healthTips,healthNews,appoinmentDetail,searchBlood,searchLocation;
    String phone,message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            phone =(String) b.get("mobile");
            message = phone;
            //Toast.makeText(DoctorListActivity.this, ""+phone, Toast.LENGTH_SHORT).show();
        }

        appoinmentSchedule = (Button)findViewById(R.id.appoinmentScheduleButton);
        appoinmentDetail = (Button) findViewById(R.id.appoinmentDetailButton);
        healthTips=(Button)findViewById(R.id.helthTipsButton);
        healthNews = (Button) findViewById(R.id.helthNewsButton);
        searchBlood = (Button) findViewById(R.id.bloodgroupButton);
        searchLocation = (Button) findViewById(R.id.searchlocation);

        appoinmentSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(PatientDashboard.this,DoctorListActivity.class);
                ii.putExtra("mobile",phone);
                startActivity(ii);

            }
        });

        appoinmentDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(PatientDashboard.this,PatientAppoinmentHistory.class);
                ii.putExtra("mobile",message);
                startActivity(ii);

            }
        });

        searchBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientDashboard.this,SearchBloodActivity.class);
                startActivity(intent);

            }
        });

        searchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientDashboard.this,GoogleMapActivity.class);
                startActivity(intent);

            }
        });

        healthNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientDashboard.this,HealthNews.class);
                startActivity(intent);

            }
        });
        healthTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientDashboard.this,HealthTips.class);
                startActivity(intent);

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK) {

            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.quit)
                    .setMessage(R.string.really_quit)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //Stop the activity
                            Intent intent = new Intent(PatientDashboard.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    })
                    .setNegativeButton(R.string.no, null)
                    .show();

            return true;
        }
        else {
            return super.onKeyDown(keyCode, event);
        }

    }
}
