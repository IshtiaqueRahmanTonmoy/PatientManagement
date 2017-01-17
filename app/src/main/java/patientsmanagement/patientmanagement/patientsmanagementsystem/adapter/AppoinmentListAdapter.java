package patientsmanagement.patientmanagement.patientsmanagementsystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import patientsmanagement.patientmanagement.patientsmanagementsystem.R;
import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.Doctor;
import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.Person;

/**
 * Created by Administrator on 1/17/2017.
 */
public class AppoinmentListAdapter extends ArrayAdapter<Person> {

    Context context;
    List<Person> alist = null;
    View rowView = null;
    private LayoutInflater inflater = null;

    public AppoinmentListAdapter(Context context, int resourceid, List<Person> alist) {
        super(context, resourceid, alist);
        this.context = context;
        this.alist = alist;
    }

    @Override
    public int getCount() {
        return alist.size();
    }

    @Override
    public Person getItem(int position) {
        return alist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = new ViewHolder();
        Person person = (Person) alist.get(position);
        View vi = convertView;

        if (vi == null) {
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            vi = inflater.inflate(R.layout.custom_list_appoinmentdetail, null);
        }
        holder.name = (TextView) vi.findViewById(R.id.names);
        holder.disesase = (TextView) vi.findViewById(R.id.diseases);
        holder.mobileno = (TextView) vi.findViewById(R.id.mobilenos);
        holder.date = (TextView) vi.findViewById(R.id.dates);
        holder.time = (TextView) vi.findViewById(R.id.times);

        holder.name.setText(person.getName());
        holder.disesase.setText(person.getDisease());
        holder.mobileno.setText(person.getMobileno());
        holder.date.setText(person.getDate());
        holder.time.setText(person.getTime());

        vi.setTag(holder);

        return vi;
    }

    private class ViewHolder {

        TextView name;
        TextView disesase;
        TextView mobileno;
        TextView date;
        TextView time;
    }
}
