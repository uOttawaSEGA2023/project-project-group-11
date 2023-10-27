package com.example.seg2105project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class AdminInboxActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_inbox);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // display pending list
        RegistrationRequestManager.getList("pending", new SimpleCallback<ArrayList<User>>() {
            @Override
            public void callback(ArrayList<User> data) {
                recyclerView.setAdapter(new ListAdapter(getApplicationContext(), data));
            }
        });
    }
}