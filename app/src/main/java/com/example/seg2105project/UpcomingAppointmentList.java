package com.example.seg2105project;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class UpcomingAppointmentList extends ArrayAdapter<Appointment> {
    private Activity context;
    List<Appointment> appointments;

    public UpcomingAppointmentList(Activity context, List<Appointment> appointments) {
        super(context, R.layout.appointment_view, appointments);
        this.context = context;
        this.appointments = appointments;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.appointment_view, null, true);

        TextView textViewPatient = (TextView) listViewItem.findViewById(R.id.patientView);
        TextView textViewDate = (TextView) listViewItem.findViewById(R.id.dateView);
        TextView textViewStartTime = (TextView) listViewItem.findViewById(R.id.startTimeView);
        TextView textViewEndTime = (TextView) listViewItem.findViewById(R.id.endTimeView);

        Appointment appointment = appointments.get(position);

        textViewPatient.setText("Patient: " + appointment.getPatient());
        textViewDate.setText("Date: " + appointment.getDate());
        textViewStartTime.setText("Start Time: " + appointment.getStartTime());
        textViewEndTime.setText("End Time: " + appointment.getEndTime());
        return listViewItem;
    }
}
