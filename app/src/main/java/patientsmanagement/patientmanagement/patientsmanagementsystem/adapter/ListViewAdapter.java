package patientsmanagement.patientmanagement.patientsmanagementsystem.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.util.Locale;

import patientsmanagement.patientmanagement.patientsmanagementsystem.R;
import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.Doctor;
import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.Medicine;
import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.Patient;

/**
 * Created by Administrator on 12/7/2016.
 */
public class ListViewAdapter extends ArrayAdapter<Doctor> {

    Context context;
    List<Doctor> alist = null;
    View rowView = null;
    private LayoutInflater inflater = null;
    private ArrayList<Doctor> arraylist;

    public ListViewAdapter(Context context, int resourceid, List<Doctor> alist) {
        super(context, resourceid, alist);
        this.context = context;
        this.alist = alist;

        this.arraylist = new ArrayList<Doctor>();
        this.arraylist.addAll(alist);

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
    public View getView(int position, View vi, ViewGroup parent) {

        final ViewHolder holder;

        final Doctor ldoc = alist.get(position);
        final int temp = position;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (vi == null) {

            vi = mInflater.inflate(R.layout.doctorcustomlist, null);
            holder = new ViewHolder();
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
        }

        else  {

            holder = (ViewHolder)vi.getTag();
        }

        //Toast.makeText(context, ""+alist.get(position).getImage(), Toast.LENGTH_SHORT).show();
        holder.name.setText(ldoc.getName());
        holder.address.setText(ldoc.getAddress());
        holder.mobileno.setText(ldoc.getPhone());
        holder.expertise.setText(ldoc.getExpertise());

        //Bitmap b = Bitmap.createScaledBitmap(ldoc.getImage(), 100, 100, false);
       // holder.image.setImageBitmap(ldoc.getImage());
        holder.image.setImageResource(R.drawable.download);
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
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        alist.clear();
        if (charText.length() == 0) {
            alist.addAll(arraylist);
        } else {
            for (Doctor wp : arraylist) {
                if (wp.getExpertise().toLowerCase(Locale.getDefault()).contains(charText) || wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    alist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}
