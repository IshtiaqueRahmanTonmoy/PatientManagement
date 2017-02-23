package patientsmanagement.patientmanagement.patientsmanagementsystem.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import patientsmanagement.patientmanagement.patientsmanagementsystem.R;
import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.Patient;
import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.Person;

/**
 * Created by Administrator on 2/22/2017.
 */
public class BloodAdapter extends ArrayAdapter<Patient> {

    private Context context;
    private List<Patient> personArray = null;
    private LayoutInflater inflater = null;
    private ArrayList<Patient> arraylist;

    public BloodAdapter(Context context, int resourceid, List<Patient> personArray) {
        super(context, resourceid, personArray);
        this.personArray = personArray;
        this.context = context;

        this.arraylist = new ArrayList<Patient>();
        this.arraylist.addAll(personArray);
    }

    @Override
    public int getCount() {
        return personArray.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Patient getItem(int position) {
        return personArray.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewholder;

        final Patient items = personArray.get(position);
        final int temp = position;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.bloodcustom, null);
            viewholder = new ViewHolder();
            viewholder.name = (TextView) convertView.findViewById(R.id.code);
            viewholder.address = (TextView) convertView.findViewById(R.id.name);
            viewholder.phone = (TextView) convertView.findViewById(R.id.continent);
            viewholder.bloodgroup = (TextView) convertView.findViewById(R.id.region);

            convertView.setTag(viewholder);
        }

        else  {

            viewholder = (ViewHolder)convertView.getTag();
        }

        viewholder.name.setText(items.getName());
        viewholder.address.setText(items.getAddress());
        viewholder.phone.setText(items.getPhone());
        viewholder.bloodgroup.setText(items.getBlood());

        return convertView;
    }
    private static class ViewHolder {
        public TextView name,address,phone,bloodgroup;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        personArray.clear();
        if (charText.length() == 0) {
            personArray.addAll(arraylist);
        } else {
            for (Patient wp : arraylist) {
                if (wp.getBlood().toLowerCase(Locale.getDefault()).contains(charText) || wp.getName().toLowerCase(Locale.getDefault()).contains(charText)|| wp.getAddress().toLowerCase(Locale.getDefault()).contains(charText)) {
                    personArray.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}
