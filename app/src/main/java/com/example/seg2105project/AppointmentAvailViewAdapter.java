package com.example.seg2105project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
public class AppointmentAvailViewAdapter extends ArrayAdapter<AppointmentAvailView>{
    public AppointmentAvailViewAdapter(@NonNull Context context, ArrayList<AppointmentAvailView> arrayList){
        super(context, 0, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View currentItemView = convertView;

        if(currentItemView == null){
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.booking_view_appointment, parent, false);

    }
    AppointmentAvailView currentAppointmentPosition = getItem(position);

    TextView docName = currentItemView.findViewById(R.id.doctor_name);
        assert currentAppointmentPosition != null;
        docName.setText(currentAppointmentPosition.getDoctorName());

    TextView specialType = currentItemView.findViewById(R.id.specialty_doctor);
        specialType.setText(currentAppointmentPosition.getSpecialty());

    TextView dateDay = currentItemView.findViewById(R.id.date_day);
        dateDay.setText(currentAppointmentPosition.getDate());

    TextView startT = currentItemView.findViewById(R.id.startTime);
        startT.setText(currentAppointmentPosition.getStartTime());

    TextView endT = currentItemView.findViewById(R.id.endTime);
        endT.setText(currentAppointmentPosition.getEndTime());

        return currentItemView;
    }
}
