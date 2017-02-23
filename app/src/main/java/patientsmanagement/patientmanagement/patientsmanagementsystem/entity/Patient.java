package patientsmanagement.patientmanagement.patientsmanagementsystem.entity;

/**
 * Created by Administrator on 2/22/2017.
 */
public class Patient {
    String name,address,phone,blood;

    public Patient(String name,String address,String phone,String blood){
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.blood = blood;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBlood() {
        return blood;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }
}
