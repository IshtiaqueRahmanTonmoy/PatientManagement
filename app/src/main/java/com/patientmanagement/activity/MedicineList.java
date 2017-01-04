package com.patientmanagement.activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import patientsmanagement.patientmanagement.patientsmanagementsystem.R;
import patientsmanagement.patientmanagement.patientsmanagementsystem.adapter.MedicineAdapter;
import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.JSONParser;
import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.Medicine;

public class MedicineList extends AppCompatActivity {

    Context context;
    ListView listview;
    List<Medicine> medicinelist;
    private MedicineAdapter medicineadapter;
    private Medicine medicine;
    String medicineName,medicineUnit;
    Intent intent;
    List<Medicine> cartList,lists;
    ArrayList<Medicine> resultpass = new ArrayList<>();
    Button submitPrescription;
    private JSONParser jsonParser = new JSONParser();
    private static final String TAG_MediUnitId = "MediUnitId";
    private static final String TAG_SUCCESS = "success";
    String medinfoid,mediunitid,quantity,timeduration,afterbefore,frequently,suggestion,patientmobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_list);

        listview = (ListView) findViewById(R.id.listview);
        medicinelist = new ArrayList<Medicine>();
        lists = new ArrayList<Medicine>();
        cartList = new ArrayList<Medicine>();
        intent = getIntent();
        submitPrescription = (Button) findViewById(R.id.SubmitMedicine);

        submitPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));

        String carListAsString = getIntent().getStringExtra("listget");

        medinfoid = getIntent().getExtras().getString("medinfoid");
        mediunitid = getIntent().getExtras().getString("mediunitidval");
        quantity = getIntent().getExtras().getString("quantity");
        timeduration = getIntent().getExtras().getString("timeduration");
        afterbefore = getIntent().getExtras().getString("afterbefore");
        frequently = getIntent().getExtras().getString("frequently");
        suggestion = getIntent().getExtras().getString("suggestion");

        Gson gson = new Gson();
        Type type = new TypeToken<List<Medicine>>(){}.getType();
        cartList = gson.fromJson(carListAsString, type);

        for(int i=0;i<cartList.size();i++){
            Log.d("list",cartList.get(i).getTimeduration());
        }

        medicineadapter = new MedicineAdapter(MedicineList.this,R.layout.activity_medicine_list,cartList);
        listview.setAdapter(medicineadapter);

        medicineadapter.notifyDataSetChanged();


        submitPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        for(int i=0;i<cartList.size();i++){

            String mname = cartList.get(i).getMedicineName();
            String munit = cartList.get(i).getUnit();
            resultpass.add(i,new Medicine(mname,munit));
        }

        Intent intentpass = new Intent("custom-message-pass");
        String listSerializedToJson = new Gson().toJson(resultpass);

        Log.d("listserialize",listSerializedToJson);
        intentpass.putExtra("listget", listSerializedToJson);
        //startActivity(intent);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intentpass);
    }


    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            medicineName = intent.getStringExtra("name");
            medicineUnit = intent.getStringExtra("unit");

            //Log.d("quant",quantity);
            lists.add(new Medicine(medicineName,medicineUnit));
        }
    };
}
