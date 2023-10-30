package com.example.seg2105project;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * The RequestInfoDisplay_Activity class represents an activity for displaying information about a registration request.
 * It provides a user interface for viewing details related to a registration request.
 */
public class RequestInfoDisplay_Activity extends AppCompatActivity {
    TextView userType, firstName, lastName, email, phoneNumber, postalAddress, postalCode, city,
    province, country, healthCardNumberText, employeeNumberText, specialtiesText;
    User user;
    // represents the request type: 0 represents pending, 1 represents rejected
    int requestType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_info_display);

        String type = getIntent().getExtras().getString("Type");
        user = (User) getIntent().getExtras().getSerializable("User");
        Address address = user.getAddress();
        requestType = getIntent().getExtras().getInt("RequestType");

        userType = findViewById(R.id.userType);
        userType.setText(type+ " Registration Overview");

        // type-specific variables
        LinearLayout specialties, employeeNumber, healthCardNumber;
        specialties = findViewById(R.id.specialties);
        employeeNumber = findViewById(R.id.employeeNumber);
        healthCardNumber = findViewById(R.id.healthCardNumber);

        // grabbing textviews from activity_request_info_diplsay.xml
        firstName = findViewById(R.id.textView45);
        lastName = findViewById(R.id.textView46);
        email = findViewById(R.id.textView48);
        phoneNumber = findViewById(R.id.textView49);
        postalAddress = findViewById(R.id.textView50);
        postalCode = findViewById(R.id.textView51);
        city = findViewById(R.id.textView52);
        province = findViewById(R.id.textView53);
        country = findViewById(R.id.textView54);
        healthCardNumberText = findViewById(R.id.textView55);
        employeeNumberText = findViewById(R.id.textView56);
        specialtiesText = findViewById(R.id.textView57);

        // setting text for generic fields
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        email.setText(user.getEmail());
        phoneNumber.setText(user.getPhoneNumber());
        postalAddress.setText(address.getPostalAddress());
        postalCode.setText(address.getPostalCode());
        city.setText(address.getCity());
        province.setText(address.getProvince());
        country.setText(address.getCountry());

        // display certain TextViews depending on user type
        if(type.equals("Doctor")){
            employeeNumberText.setText(((Doctor) user).getEmployeeNumber());
            ArrayList<String> specialtyList = ((Doctor) user).getSpecialties();
            String specialtyString = "";
            for(String s : specialtyList){
                if(specialtyString.equals("")){
                    specialtyString = s;
                }else{
                    specialtyString = specialtyString + ", "  + s;
                }
            }
            specialtiesText.setText(specialtyString);
            specialties.setVisibility(View.VISIBLE);
            employeeNumber.setVisibility(View.VISIBLE);
            healthCardNumber.setVisibility(View.INVISIBLE);
        }
        else{
            healthCardNumberText.setText(((Patient) user).getHealthCardNumber());
            specialties.setVisibility(View.INVISIBLE);
            employeeNumber.setVisibility(View.INVISIBLE);
            healthCardNumber.setVisibility(View.VISIBLE);
        }
    }
    public void onClickChange(View view){
        // if on pending request
        if(requestType == 0) {
            if(view.getId() == R.id.backtoInbox){
                Intent backtoInbox = new Intent(RequestInfoDisplay_Activity.this, AdminInboxActivity.class);
                startActivity(backtoInbox);
            }
            else if(view.getId() == R.id.acceptUser){
                RegistrationRequestManager.transferData(user, "pending", "users", new SimpleCallback<String>() {
                    @Override
                    public void callback(String data) {
                        Toast.makeText(RequestInfoDisplay_Activity.this,"User Accepted!", Toast.LENGTH_SHORT).show();
                        Intent backtoInbox = new Intent(RequestInfoDisplay_Activity.this, AdminInboxActivity.class);
                        startActivity(backtoInbox);
                    }
                });
            }
            else if(view.getId() == R.id.rejectUser){
                RegistrationRequestManager.transferData(user, "pending", "rejected", new SimpleCallback<String>() {
                    @Override
                    public void callback(String data) {
                        Toast.makeText(RequestInfoDisplay_Activity.this,"User Rejected!", Toast.LENGTH_SHORT).show();
                        Intent backtoInbox = new Intent(RequestInfoDisplay_Activity.this, AdminInboxActivity.class);
                        startActivity(backtoInbox);
                    }
                });
            }
        // if on rejected request
        }else if (requestType == 1){
            if(view.getId() == R.id.backtoInbox){
                Intent backtoInbox = new Intent(RequestInfoDisplay_Activity.this, AdminInboxActivity.class);
                startActivity(backtoInbox);
            }
            else if(view.getId() == R.id.acceptUser){
                RegistrationRequestManager.transferData(user, "rejected", "users", new SimpleCallback<String>() {
                    @Override
                    public void callback(String data) {
                        Toast.makeText(RequestInfoDisplay_Activity.this,"User Accepted!", Toast.LENGTH_SHORT).show();
                        Intent backtoInbox = new Intent(RequestInfoDisplay_Activity.this, AdminInboxActivity.class);
                        startActivity(backtoInbox);
                    }
                });
            }
            else if(view.getId() == R.id.rejectUser){
                RegistrationRequestManager.transferData(user, "rejected", "rejected", new SimpleCallback<String>() {
                    @Override
                    public void callback(String data) {
                        Toast.makeText(RequestInfoDisplay_Activity.this,"User Rejected!", Toast.LENGTH_SHORT).show();
                        Intent backtoInbox = new Intent(RequestInfoDisplay_Activity.this, AdminInboxActivity.class);
                        startActivity(backtoInbox);
                    }
                });
            }
        }
    }

}