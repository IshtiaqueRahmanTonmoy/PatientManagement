package com.patientmanagement.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import patientsmanagement.patientmanagement.patientsmanagementsystem.R;

public class PatientDashboard extends AppCompatActivity {

    private Button appoinmentSchedule;
    private Button healthTips,healthNews;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            phone =(String) b.get("mobile");
            //Toast.makeText(DoctorListActivity.this, ""+phone, Toast.LENGTH_SHORT).show();
        }

        appoinmentSchedule = (Button)findViewById(R.id.appoinmentScheduleButton);
        healthTips=(Button)findViewById(R.id.helthTipsButton);
        healthNews = (Button) findViewById(R.id.helthNewsButton);

        appoinmentSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(PatientDashboard.this,DoctorListActivity.class);
                ii.putExtra("mobile",phone);
                finish();
                startActivity(ii);
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
}
