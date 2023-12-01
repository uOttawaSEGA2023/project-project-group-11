package com.example.seg2105project;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.SearchView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class BookAppointment extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        SearchView searchSpecialty = findViewById(R.id.specialtySearch);
        searchSpecialty.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            public boolean onQueryTextSubmit(String query){
                handleSearch(query);
                return true;
            }
        }
    }

    public void onClick(View view){
        // Button to return to welcome page
        if(view.getId() == R.id.button4){
            Intent back = new Intent(BookAppointment.this, WelcomePageActivity.class);
            startActivity(back);
        }
    }

}