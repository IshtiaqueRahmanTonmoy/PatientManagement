package patientsmanagement.patientmanagement.patientsmanagementsystem.entity;

/**
 * Created by Administrator on 1/10/2017.
 */
public class Prescription {

    String medicinename,unit,frequently,afterbefore,quantity,timeduration;

    public String getMedicineinfoname() {
        return medicinename;
    }

    public void setMedicinename(String medicinename) {
        this.medicinename = medicinename;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getFrequently() {
        return frequently;
    }

    public void setFrequently(String frequently) {
        this.frequently = frequently;
    }

    public String getAfterbefore() {
        return afterbefore;
    }

    public void setAfterbefore(String afterbefore) {
        this.afterbefore = afterbefore;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTimeduration() {
        return timeduration;
    }

    public void setTimeduration(String timeduration) {
        this.timeduration = timeduration;
    }

    public Prescription(String medicinename, String unit, String frequentlty, String afterbefore, String quantity, String timeduration) {

           this.medicinename = medicinename;
           this.unit = unit;
           this.frequently = frequentlty;
           this.afterbefore = afterbefore;
           this.quantity = quantity;
           this.timeduration = timeduration;

    }
}
