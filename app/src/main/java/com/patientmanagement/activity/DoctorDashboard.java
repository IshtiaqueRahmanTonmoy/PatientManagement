package com.patientmanagement.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import patientsmanagement.patientmanagement.patientsmanagementsystem.R;

public class DoctorDashboard extends AppCompatActivity {

    private String doctorId;
    private static final String TAG_DOCTORID = "doctorId";
    private Button appoinmentList,viewSpecificPrescription,visitHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            doctorId = b.getString(TAG_DOCTORID);
            // Toast.makeText(PatientAppoinmentList.this, ""+doctorid, Toast.LENGTH_SHORT).show();
        }

        appoinmentList = (Button) findViewById(R.id.listofAppoinment);

        appoinmentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(DoctorDashboard.this,PatientAppoinmentList.class);
                ii.putExtra(TAG_DOCTORID, doctorId);
                startActivity(ii);
            }
        });
    }
}
