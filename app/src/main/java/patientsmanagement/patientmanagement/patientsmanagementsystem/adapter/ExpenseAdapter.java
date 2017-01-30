package patientsmanagement.patientmanagement.patientsmanagementsystem.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import patientsmanagement.patientmanagement.patientsmanagementsystem.R;
import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.Medicine;
import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.Person;

/**
 * Created by Administrator on 1/30/2017.
 */
public class ExpenseAdapter extends ArrayAdapter<Person> {

    private Context context;
    private List<Person> personArray = null;
    private LayoutInflater inflater = null;

    public ExpenseAdapter(Context context, int resourceid, List<Person> personArray) {
        super(context, resourceid, personArray);
        this.personArray = personArray;
        this.context = context;
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
    public Person getItem(int position) {
        return personArray.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewholder;

        final Person items = personArray.get(position);
        final int temp = position;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.expense, null);
            viewholder = new ViewHolder();
            viewholder.name = (TextView) convertView.findViewById(R.id.names);
            viewholder.disease = (TextView) convertView.findViewById(R.id.diseases);
            viewholder.mobileno = (TextView) convertView.findViewById(R.id.mobilenos);
            viewholder.date = (TextView) convertView.findViewById(R.id.dates);
            viewholder.time = (TextView) convertView.findViewById(R.id.times);

            convertView.setTag(viewholder);
        }

        else  {

            viewholder = (ViewHolder)convertView.getTag();
        }

        viewholder.name.setText(items.getName());
        viewholder.disease.setText(items.getDisease());
        viewholder.mobileno.setText(items.getMobileno());
        viewholder.date.setText(items.getDate());
        viewholder.time.setText(items.getTime());

        return convertView;
    }
    private static class ViewHolder {
        public TextView name,disease,mobileno,date,time;
    }
}

