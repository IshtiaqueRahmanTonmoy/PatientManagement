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

/**
 * Created by Administrator on 12/28/2016.
 */
public class MedicineAdapter extends ArrayAdapter<Medicine> {

    Context context;
    List<Medicine> list;

    public MedicineAdapter(Context context, int resource, List<Medicine> list) {
        super(context, resource, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Medicine getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewholder;

        final Medicine items = list.get(position);
        final int temp = position;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.medicine, null);
            viewholder = new ViewHolder();
            viewholder.medicinename = (TextView) convertView.findViewById(R.id.medicinename);
            viewholder.medicineunit = (TextView) convertView.findViewById(R.id.unit);
            viewholder.timeduration = (TextView) convertView.findViewById(R.id.timeduration);

            convertView.setTag(viewholder);
        }

        else  {

            viewholder = (ViewHolder)convertView.getTag();
        }

        viewholder.medicinename.setText(items.getMedicineName());
        viewholder.medicineunit.setText(items.getUnit());
        viewholder.timeduration.setText(items.getTimeduration());

        return convertView;
    }

    private static class ViewHolder {
        public TextView medicinename,medicineunit,timeduration;
    }
}
