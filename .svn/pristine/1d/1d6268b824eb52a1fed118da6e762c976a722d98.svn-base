package patientsmanagement.patientmanagement.patientsmanagementsystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import patientsmanagement.patientmanagement.patientsmanagementsystem.R;
import java.util.ArrayList;
import java.util.List;

import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.Doctor;
import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.Person;

/**
 * Created by rajohns on 2/8/15.
 *
 */
public class PersonAdapter extends ArrayAdapter<Person> {

    private Context context;
    private List<Person> personArray = null;
    private LayoutInflater inflater = null;
    private static final int TYPE_PERSON = 0;
    private static final int TYPE_DIVIDER = 1;

    public PersonAdapter(Context context, int resourceid, List<Person> personArray) {
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
    public int getViewTypeCount() {
        // TYPE_PERSON and TYPE_DIVIDER
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof Person) {
            return TYPE_PERSON;
        }

        return TYPE_DIVIDER;
    }

    @Override
    public boolean isEnabled(int position) {
        return (getItemViewType(position) == TYPE_PERSON);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);

        if (convertView == null) {

            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            switch (type) {
                case TYPE_PERSON:
                    convertView = inflater.inflate(R.layout.row_item, parent, false);
                    break;
                case TYPE_DIVIDER:
                    convertView = inflater.inflate(R.layout.row_header, parent, false);
                    break;
            }
        }

        switch (type) {
            case TYPE_PERSON:
                Person person = (Person)getItem(position);
                TextView name = (TextView)convertView.findViewById(R.id.tvDoctor);
                TextView disease = (TextView)convertView.findViewById(R.id.tvDisease);
                TextView mobileno = (TextView)convertView.findViewById(R.id.tvMobileNo);
                TextView time = (TextView)convertView.findViewById(R.id.tvTimeofappoinment);

                name.setText(person.getName());
                disease.setText(person.getDisease());
                mobileno.setText(person.getMobileno());
                time.setText(person.getTime());

                break;
            case TYPE_DIVIDER:
                TextView title = (TextView)convertView.findViewById(R.id.headerTitle);
                //Person person1 = (Person)getItem(position);
                title.setText("list");
                break;
        }

        return convertView;
    }
}
