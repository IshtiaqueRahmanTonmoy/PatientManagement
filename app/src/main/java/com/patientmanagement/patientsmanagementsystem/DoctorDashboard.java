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

public class DoctorDashboard extends AppCompatActivity {

    private String doctorId;
    private static final String TAG_DOCTORID = "doctorId";
    private Button appoinmentList,viewSpecificPrescription, vistitHistory,SearchBlood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            doctorId = b.getString(TAG_DOCTORID);
            //Toast.makeText(DoctorDashboard.this, ""+doctorId, Toast.LENGTH_SHORT).show();
        }

        appoinmentList = (Button) findViewById(R.id.listofAppoinment);
        viewSpecificPrescription = (Button) findViewById(R.id.PrescriptionVisit);
        vistitHistory = (Button) findViewById(R.id.visitRate);
        SearchBlood = (Button) findViewById(R.id.bloodgroupButton);

        appoinmentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(DoctorDashboard.this,PatientAppoinmentList.class);
                ii.putExtra(TAG_DOCTORID, doctorId);
                startActivity(ii);
            }
        });

        viewSpecificPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(DoctorDashboard.this,ViewSpecificPatientt.class);
                ii.putExtra(TAG_DOCTORID, doctorId);
                startActivity(ii);
            }
        });

        vistitHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorDashboard.this,Expense.class);
                intent.putExtra(TAG_DOCTORID, doctorId);
                startActivity(intent);
            }
        });

        SearchBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorDashboard.this,SearchBloodActivity.class);
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
                            Intent intent = new Intent(DoctorDashboard.this,MainActivity.class);
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
