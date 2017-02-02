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

import patientsmanagement.patientmanagement.patientsmanagementsystem.R;
import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.Medicine;
import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.Prescription;

/**
 * Created by Administrator on 2/2/2017.
 */
public class MedicineListAdapter extends ArrayAdapter<Medicine> {

    Context context;
    List<Medicine> medilist = null;

    public MedicineListAdapter(Context context, int resourceid, ArrayList<Medicine> medilist) {
        super(context, resourceid, medilist);
        this.context = context;
        this.medilist = medilist;
    }


    @Override
    public int getCount() {
        return medilist.size();
    }

    @Override
    public Medicine getItem(int position) {
        return medilist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewholder;

        final Medicine items = medilist.get(position);
        final int temp = position;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.custom_viewspecific, null);
            viewholder = new ViewHolder();
            viewholder.name = (TextView) convertView.findViewById(R.id.names);
            viewholder.unit = (TextView) convertView.findViewById(R.id.unit);
            viewholder.quantity = (TextView) convertView.findViewById(R.id.quantity);
            viewholder.timeduration = (TextView) convertView.findViewById(R.id.timedurations);
            viewholder.afterbefore = (TextView) convertView.findViewById(R.id.afterbefore);
            viewholder.suggestions = (TextView) convertView.findViewById(R.id.suggestions);
            viewholder.follow = (TextView) convertView.findViewById(R.id.follow);
            viewholder.frequency = (TextView) convertView.findViewById(R.id.frequency);

            convertView.setTag(viewholder);
        }

        else  {

            viewholder = (ViewHolder)convertView.getTag();
        }

        viewholder.name.setText(items.getMedicineName());
        viewholder.unit.setText(items.getUnit());
        viewholder.quantity.setText(items.getQuantity());
        viewholder.timeduration.setText(items.getTimeduration());
        viewholder.afterbefore.setText(items.getAfterbefore());
        viewholder.suggestions.setText(items.getSuggestion());
        viewholder.follow.setText(items.getFollowup());
        viewholder.frequency.setText(items.getFrequently());
        return convertView;
    }

    private static class ViewHolder {
        public TextView name,unit,quantity,timeduration,afterbefore,suggestions,follow,frequency;
    }
}
