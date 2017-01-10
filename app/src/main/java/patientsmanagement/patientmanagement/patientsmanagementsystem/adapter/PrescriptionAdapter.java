package patientsmanagement.patientmanagement.patientsmanagementsystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import patientsmanagement.patientmanagement.patientsmanagementsystem.R;
import patientsmanagement.patientmanagement.patientsmanagementsystem.entity.Prescription;

/**
 * Created by Administrator on 1/10/2017.
 */
public class PrescriptionAdapter extends ArrayAdapter<Prescription> {

    Context context;
    List<Prescription> prescriptionlist = null;
    View rowView = null;
    private LayoutInflater inflater = null;

    public PrescriptionAdapter(Context context, int resourceid, ArrayList<Prescription> prescriptionlist) {
        super(context, resourceid, prescriptionlist);
        this.context = context;
        this.prescriptionlist = prescriptionlist;
    }

    @Override
    public int getCount() {
        return prescriptionlist.size();
    }

    @Override
    public Prescription getItem(int position) {
        return prescriptionlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = new ViewHolder();
        Prescription pres = (Prescription) prescriptionlist.get(position);
        View vi = convertView;

        if (vi == null) {
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            vi = inflater.inflate(R.layout.custom_prescription,null);

            holder.medicinename = (TextView) vi.findViewById(R.id.medicinename);
            holder.unit = (TextView) vi.findViewById(R.id.unitmg);
            holder.frequent = (TextView) vi.findViewById(R.id.frequently);
            holder.afterbefore = (TextView) vi.findViewById(R.id.afterOrBefore);
            holder.quantity = (TextView) vi.findViewById(R.id.QuantityMedicine);
            holder.timeduration = (TextView) vi.findViewById(R.id.timeDuration);
            //holder.date = (TextView) vi.findViewById(R.id.date);
            //holder.time = (TextView) vi.findViewById(R.id.time);
            //holder.image = (ImageView) vi.findViewById(R.id.imageview);

            vi.setTag(holder);
        } else {

            holder = (ViewHolder) rowView.getTag();
        }

        holder.medicinename.setText("Medicine Name"+pres.getMedicineinfoname());
        holder.unit.setText("Unit"+pres.getUnit());
        holder.frequent.setText("Frequent"+pres.getFrequently());
        holder.afterbefore.setText("After or Before"+pres.getAfterbefore());
        //holder.image.setImageBitmap();
        holder.quantity.setText("Quantity"+pres.getQuantity());
        holder.timeduration.setText("Time Duration"+pres.getTimeduration());
        //holder.date.setText(ldoc.getChamberday());
        //holder.time.setText(ldoc.getChambertime());

        return vi;
    }

    private class ViewHolder {

        TextView medicinename;
        TextView unit;
        TextView frequent;
        TextView afterbefore;
        TextView quantity;
        TextView timeduration;
        TextView date;
        TextView time;
        ImageView image;
    }
}
