package patientsmanagement.patientmanagement.patientsmanagementsystem.entity;

/**
 * Created by Administrator on 12/28/2016.
 */
public class Medicine {
    String medicineName,Unit,quantity;
    String total,timeduration;

    public Medicine() {

    }

    public Medicine(String medicineName, String Unit,String timeduration){
        this.medicineName = medicineName;
        this.Unit = Unit;
        this.timeduration = timeduration;
    }

    public String getTimeduration() {
        return timeduration;
    }

    public void setTimeduration(String timeduration) {
        this.timeduration = timeduration;
    }

    public Medicine(String medicineName, String Unit) {
        this.medicineName = medicineName;
        this.Unit = Unit;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public void setQuantity(int quantity) {
        quantity = quantity;
    }
}
