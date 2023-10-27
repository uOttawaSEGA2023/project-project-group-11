package com.example.seg2105project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class AdminInboxActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_inbox);

        // get tablayout
        tabLayout = findViewById(R.id.requestList);

        // get recycler view of lists
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // display pending list
        RegistrationRequestManager.getList("pending", new SimpleCallback<ArrayList<User>>() {
            @Override
            public void callback(ArrayList<User> data) {
                recyclerView.setAdapter(new ListAdapter(getApplicationContext(), data));

            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if(position == 0){
                    // display pending list
                    RegistrationRequestManager.getList("pending", new SimpleCallback<ArrayList<User>>() {
                        @Override
                        public void callback(ArrayList<User> data) {
                            recyclerView.setAdapter(new ListAdapter(getApplicationContext(), data));

                        }
                    });
                }
                else if(position == 1){
                    // display rejected list
                    RegistrationRequestManager.getList("rejected", new SimpleCallback<ArrayList<User>>() {
                        @Override
                        public void callback(ArrayList<User> data) {
                            recyclerView.setAdapter(new ListAdapter(getApplicationContext(), data));

                        }
                    });
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}