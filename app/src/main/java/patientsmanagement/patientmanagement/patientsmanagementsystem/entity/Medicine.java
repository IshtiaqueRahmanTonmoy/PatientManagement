package patientsmanagement.patientmanagement.patientsmanagementsystem.entity;

/**
 * Created by Administrator on 12/28/2016.
 */
public class Medicine {
    String medicineName,Unit,afterbefore,Quantity;
    String frequently;
    String timeduration;
    String medinfo;
    String suggestion;
    String  medininfoid;
    String followup;

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }


    public Medicine(String medicineName, String medinfo, String Unit, String unival, String Quantity, String timeduration, String afterbefore, String frequently, String suggestion) {
        this.medicineName = medicineName;
        this.medinfo = medinfo;
        this.Unit = Unit;
        this.unival = unival;
        this.Quantity = Quantity;
        this.timeduration = timeduration;
        this.afterbefore = afterbefore;
        this.frequently = frequently;
        this.suggestion = suggestion;
    }


    public Medicine(String medicineName,String Unit,String Quantity,String timeduration,String afterbefore,String frequently,String followup,String suggestion){
        this.medicineName = medicineName;
        this.Unit = Unit;
        this.Quantity = Quantity;
        this.timeduration = timeduration;
        this.afterbefore = afterbefore;
        this.frequently = frequently;
        this.followup = followup;
        this.suggestion = suggestion;
    }


    public Medicine() {

    }

    public String getUnival() {
        return unival;
    }

    public void setUnival(String unival) {
        this.unival = unival;
    }

    public String getMedinfo() {
        return medinfo;
    }

    public void setMedinfo(String medinfo) {
        this.medinfo = medinfo;
    }

    String unival;

    public String getMedininfoid() {
        return medininfoid;
    }

    public void setMedininfoid(String medininfoid) {
        this.medininfoid = medininfoid;
    }

    public String getFollowup() {
        return followup;
    }

    public void setFollowup(String followup) {
        this.followup = followup;
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
        return Quantity;
    }

    public void setQuantity(String Quantity) {
        this.Quantity = Quantity;
    }

    public String getAfterbefore() {
        return afterbefore;
    }

    public void setAfterbefore(String afterbefore) {
        this.afterbefore = afterbefore;
    }

    public String getFrequently() {
        return frequently;
    }

    public void setFrequently(String frequently) {
        this.frequently = frequently;
    }

    public void setQuantity(int quantity) {
        quantity = quantity;
    }
}
