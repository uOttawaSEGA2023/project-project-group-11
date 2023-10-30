package com.example.seg2105project;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;




/**
 * The RequestInfoDisplay_Activity class represents an activity for displaying information about a registration request.
 * It provides a user interface for viewing details related to a registration request.
 */
public class RequestInfoDisplay_Activity extends AppCompatActivity {
    TextView userType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_info_display);

        String type = getIntent().getExtras().getString("Type");
        //User user = getIntent().getExtras().getSerializable("User");

        userType = findViewById(R.id.userType);
        userType.setText(type+ " Registration Overview");

        LinearLayout specialties, employeeNumber, healthCardNumber;
        specialties = findViewById(R.id.specialties);
        employeeNumber = findViewById(R.id.employeeNumber);
        healthCardNumber = findViewById(R.id.healthCardNumber);
        if(type.equals("Doctor")){
            specialties.setVisibility(View.VISIBLE);
            employeeNumber.setVisibility(View.VISIBLE);
            healthCardNumber.setVisibility(View.INVISIBLE);
        }
        else{
            specialties.setVisibility(View.INVISIBLE);
            employeeNumber.setVisibility(View.INVISIBLE);
            healthCardNumber.setVisibility(View.VISIBLE);
        }
    }
    public void onClick(View view){
        if(view.getId() == R.id.backtoInbox){
            Intent backtoInbox = new Intent(RequestInfoDisplay_Activity.this, AdminInboxActivity.class);
            startActivity(backtoInbox);
        }
        else if(view.getId() == R.id.acceptUser){

        }
        else if(view.getId() == R.id.rejectUser){

        }
    }

}