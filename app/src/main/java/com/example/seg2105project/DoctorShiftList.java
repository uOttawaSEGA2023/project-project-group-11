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

    /**
     * Get a View that displays the data at the specified position in the data set.
     *
     * @param position    The position of the item within the adapter's data set.
     * @param convertView The old view to reuse, if possible.
     * @param parent      The parent that this view will eventually be attached to.
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the layout for each list item
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.shift_view, null, true);

        // Get references to the TextViews in the layout
        TextView textViewDate = (TextView) listViewItem.findViewById(R.id.date);
        TextView textViewStartTime = (TextView) listViewItem.findViewById(R.id.startTime);
        TextView textViewEndTime = (TextView) listViewItem.findViewById(R.id.endTime);

        // Get the Shift object at the current position
        Shift shift = shifts.get(position);

        // Set the text for each TextView based on the Shift object's properties
        textViewDate.setText("Date: " + shift.getDate());
        textViewStartTime.setText("Start Time: " + shift.getStartTime());
        textViewEndTime.setText("End Time: " + shift.getEndTime());

        // Return the prepared list item view
        return listViewItem;
    }
}