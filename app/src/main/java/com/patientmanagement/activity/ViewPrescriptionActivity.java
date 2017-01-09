package com.patientmanagement.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import patientsmanagement.patientmanagement.patientsmanagementsystem.R;

public class ViewPrescriptionActivity extends AppCompatActivity {

    ListView listview;
    TextView doctorName,Speciality,patientname,age,sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_prescription);

        doctorName = (TextView) findViewById(R.id.doctorName);
        Speciality = (TextView) findViewById(R.id.speciality);
        patientname = (TextView) findViewById(R.id.patientnamae);
        age = (TextView) findViewById(R.id.agevalue);
        sex = (TextView) findViewById(R.id.sexvalue);

        listview = (ListView) findViewById(R.id.listView);

    }
}
