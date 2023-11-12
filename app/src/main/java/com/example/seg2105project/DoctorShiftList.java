package com.example.seg2105project;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class DoctorShiftList extends ArrayAdapter<Shift> {
    private Activity context;
    List<Shift> shifts;

    public DoctorShiftList(Activity context, List<Shift> shifts) {
        super(context, R.layout.shift_view, shifts);
        this.context = context;
        this.shifts = shifts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.shift_view, null, true);

        TextView textViewDate = (TextView) listViewItem.findViewById(R.id.date);
        TextView textViewStartTime = (TextView) listViewItem.findViewById(R.id.startTime);
        TextView textViewEndTime = (TextView) listViewItem.findViewById(R.id.endTime);

        Shift shift = shifts.get(position);

        textViewDate.setText("Date: " + shift.getDate());
        textViewStartTime.setText("Start Time: " + shift.getStartTime());
        textViewEndTime.setText("End Time: " + shift.getEndTime());
        return listViewItem;
    }
}