package com.example.seg2105project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

/**
 * This activity displays and manages registration requests for an admin user.
 * Admins can switch between pending and rejected requests using tabs and view request details.
 */
public class AdminInboxActivity extends AppCompatActivity implements ListViewHolder.OnRequestListener {

    RecyclerView recyclerView;
    TabLayout tabLayout;

    ArrayList<User> listData;
    int onList;

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
                recyclerView.setAdapter(new ListAdapter(getApplicationContext(), data, AdminInboxActivity.this));
                listData = data;
            }
        });

        // Set up TabLayout to switch between pending and rejected lists
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0) {
                    onList = 0;
                    // display pending list
                    RegistrationRequestManager.getList("pending", new SimpleCallback<ArrayList<User>>() {
                        @Override
                        public void callback(ArrayList<User> data) {
                            recyclerView.setAdapter(new ListAdapter(getApplicationContext(), data, AdminInboxActivity.this));
                            listData = data;
                        }
                    });
                } else if (position == 1) {
                    onList = 1;
                    // display rejected list
                    RegistrationRequestManager.getList("rejected", new SimpleCallback<ArrayList<User>>() {
                        @Override
                        public void callback(ArrayList<User> data) {
                            recyclerView.setAdapter(new ListAdapter(getApplicationContext(), data, AdminInboxActivity.this));
                            listData = data;
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

    // When the admin click on a registration request from the list of pending requests or rejected
    // the admin is sent to the page where the information of that registration request is displayed
    // Used implementation of the onClickListener from a tutorial video: https://www.youtube.com/watch?v=69C1ljfDvl0
    @Override
    public void onRequestClick(int position) {
        User userClicked = listData.get(position);
        String type = userClicked.getClass().toString();
        type = type.substring(type.lastIndexOf('.') + 1);
        Intent intent = new Intent(this, RequestInfoDisplay_Activity.class);
        intent.putExtra("User", userClicked);
        intent.putExtra("Type", type);
        intent.putExtra("RequestType", onList);
        startActivity(intent);
    }
}