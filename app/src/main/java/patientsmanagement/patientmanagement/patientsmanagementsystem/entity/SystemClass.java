package patientsmanagement.patientmanagement.patientsmanagementsystem.entity;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 10/24/2016.
 */
public class SystemClass {
    List<Medicine> list = new ArrayList<Medicine>();

    public SystemClass(String medname,String medunit){
        list.add(new Medicine(medname,medunit));
    }

    public SystemClass() {

    }

    public List<Medicine> getMedicine() {
        return this.list;
    }

    public void setMedicine(List<Medicine> list) {
        this.list = list;
    }
}
