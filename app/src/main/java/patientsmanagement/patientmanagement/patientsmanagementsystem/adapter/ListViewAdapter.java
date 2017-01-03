package patientsmanagement.patientmanagement.patientsmanagementsystem.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import patientsmanagement.patientmanagement.patientsmanagementsystem.R;
import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.Doctor;

/**
 * Created by Administrator on 12/7/2016.
 */
public class ListViewAdapter extends ArrayAdapter<Doctor> {

    Context context;
    List<Doctor> alist = null;
    View rowView = null;
    private LayoutInflater inflater = null;

    public ListViewAdapter(Context context, int resourceid, List<Doctor> alist) {
        super(context, resourceid, alist);
        this.context = context;
        this.alist = alist;
    }

    @Override
    public int getCount() {
        return alist.size();
    }

    @Override
    public Doctor getItem(int position) {
        return alist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = new ViewHolder();
        Doctor ldoc = (Doctor) alist.get(position);
        View vi = convertView;

        if (vi == null) {
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            vi = inflater.inflate(R.layout.doctorcustomlist,null);

            holder.name = (TextView) vi.findViewById(R.id.tvDoctor);
            holder.address = (TextView) vi.findViewById(R.id.tvAddress);
            holder.mobileno = (TextView) vi.findViewById(R.id.tvMobileNo);
            holder.expertise = (TextView) vi.findViewById(R.id.tvExpertise);
            holder.doctorfee = (TextView) vi.findViewById(R.id.tvDoctorFee);
            holder.followupfee = (TextView) vi.findViewById(R.id.tvFollowupFee);
            holder.date = (TextView) vi.findViewById(R.id.tvDate);
            holder.time = (TextView) vi.findViewById(R.id.tvTime);
            holder.image = (ImageView) vi.findViewById(R.id.ivIcon);

            vi.setTag(holder);
        } else {

            holder = (ViewHolder) rowView.getTag();
        }

        holder.name.setText(ldoc.getName());
        holder.address.setText(ldoc.getAddress());
        holder.mobileno.setText(ldoc.getPhone());
        holder.expertise.setText(ldoc.getExpertise());
        holder.image.setImageBitmap(ldoc.getImage());
        holder.doctorfee.setText(ldoc.getDoctorfee());
        holder.followupfee.setText(ldoc.getFollowupfeee());
        holder.date.setText(ldoc.getChamberday());
        holder.time.setText(ldoc.getChambertime());

        return vi;
    }

    private class ViewHolder {

        TextView name;
        TextView address;
        TextView mobileno;
        TextView expertise;
        TextView doctorfee;
        TextView followupfee;
        TextView date;
        TextView time;
        ImageView image;
    }
}
