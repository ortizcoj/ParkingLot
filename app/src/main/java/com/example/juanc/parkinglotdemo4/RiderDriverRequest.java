package com.example.juanc.parkinglotdemo4;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;

public class RiderDriverRequest extends AppCompatActivity {

    private TextView eagleRide;
    private TextView requestType;
    private TextView timeTV;
    private TextView dropoff;
    private TextView pickup;
    private Switch requestSwitch;
    private Spinner timeDropDown;
    private Spinner dropoffDropDown;
    private Spinner pickupDropDown;
    private Button next;
    private ImageView mImageView;
    private ImageView citationLot;
    int[] times;
    private boolean isUserInteracting;
    private ProgressBar pb;

    //TODO add a profile selectable icon

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return setUpRideMainUI();
                case R.id.navigation_dashboard:
                    return setUpMapMainUI();
            }
            return false;
        }
    };

    private boolean setUpMapMainUI() {
        eagleRide.setVisibility(View.GONE);
        requestType.setVisibility(View.GONE);
        timeTV.setVisibility(View.GONE);
        dropoff.setVisibility(View.GONE);
        pickup.setVisibility(View.GONE);
        requestSwitch.setVisibility(View.GONE);
        timeDropDown.setVisibility(View.GONE);
        dropoffDropDown.setVisibility(View.GONE);
        pickupDropDown.setVisibility(View.GONE);
        next.setVisibility(View.GONE);
        mImageView.setVisibility(View.VISIBLE);
        citationLot.setVisibility(View.VISIBLE);
        citationLot.setX(200);
        citationLot.setY(850);
        citationLot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LotDisplay.class);
                startActivity(intent);
            }
        });
        return true;
    }

    private boolean setUpRideMainUI() {
        eagleRide.setVisibility(View.VISIBLE);
        requestType.setVisibility(View.VISIBLE);
        timeTV.setVisibility(View.VISIBLE);
        dropoff.setVisibility(View.VISIBLE);
        pickup.setVisibility(View.VISIBLE);
        pickupDropDown.setVisibility(View.VISIBLE);
        requestSwitch.setVisibility(View.VISIBLE);
        requestSwitch.setTextOn("Driver");
        requestSwitch.setTextOff("Rider");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            requestSwitch.setShowText(true);
        }
        if (!requestSwitch.isChecked()){
            pickup.setVisibility(View.VISIBLE);
            pickupDropDown.setVisibility(View.VISIBLE);
        } else {
            pickup.setVisibility(View.INVISIBLE);
            pickupDropDown.setVisibility(View.INVISIBLE);
        }
        requestSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!requestSwitch.isChecked()){
                    pickup.setVisibility(View.VISIBLE);
                    pickupDropDown.setVisibility(View.VISIBLE);
                } else {
                    pickup.setVisibility(View.INVISIBLE);
                    pickupDropDown.setVisibility(View.INVISIBLE);
                }
            }
        });
        timeDropDown.setVisibility(View.VISIBLE);
        dropoffDropDown.setVisibility(View.VISIBLE);
        next.setVisibility(View.VISIBLE);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RiderDriverRequest.this, "Looking for a match", Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.VISIBLE);
//                if (!requestSwitch.isChecked()) {
//                    RideRequest ride = new RideRequest(Time.valueOf(String.valueOf(timeDropDown.getText())), String.valueOf(dropoffDropDown.getSelectedItem().toString()), String.valueOf(pickupDropDown.getSelectedItem().toString()));
//                    //TODO add to pool
//                } else {
//                    DriveRequest drive = new DriveRequest(Time.valueOf(String.valueOf(timeDropDown.getText())), String.valueOf(dropoffDropDown.getSelectedItem().toString()));
//                    //TODO add to pool
//                }
            }
        });
        mImageView.setVisibility(View.GONE);
        citationLot.setVisibility(View.GONE);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request);

        mImageView = findViewById(R.id.map);
        mImageView.setVisibility(View.INVISIBLE);
        citationLot = findViewById(R.id.citation);
        citationLot.setVisibility(View.INVISIBLE);
        eagleRide = findViewById(R.id.eagleRide);
        requestType = findViewById(R.id.requestType);
        requestSwitch = findViewById(R.id.requestSwitch);
        requestSwitch.setTextOn("Driver");
        requestSwitch.setTextOff("Rider");
        pickup = findViewById(R.id.pickup);
        pickupDropDown = findViewById(R.id.pickupDropDown);
        //TODO get pickup locations
        String[] items = new String[]{"1", "2", "three"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        pickupDropDown.setAdapter(adapter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            requestSwitch.setShowText(true);
        }
        requestSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!requestSwitch.isChecked()){
                    pickup.setVisibility(View.VISIBLE);
                    pickupDropDown.setVisibility(View.VISIBLE);
                } else {
                    pickup.setVisibility(View.INVISIBLE);
                    pickupDropDown.setVisibility(View.INVISIBLE);
                }
            }
        });
        timeTV = findViewById(R.id.time);
        dropoff = findViewById(R.id.dropoff);
        timeDropDown = findViewById(R.id.timeDropDown);
        pb = findViewById(R.id.ProgressBar);
        pb.setVisibility(View.GONE);

        //TODO put times?
        times = new int[]{1,2,3,4,5,6,7,8,9,10,11,12};
        CustomAdapter mCustomAdapter = new CustomAdapter(RiderDriverRequest.this, times);
        timeDropDown.setAdapter(mCustomAdapter);

        timeDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (isUserInteracting) {
                    Toast.makeText(RiderDriverRequest.this, times[i], Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dropoffDropDown = findViewById(R.id.dropoffDropDown);

        //TODO put the actual names
        String[] dropItems = new String[]{"Tomcat", "Voyager", "Concord"};
        ArrayAdapter<String> dropAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dropItems);
        dropoffDropDown.setAdapter(dropAdapter);
        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RiderDriverRequest.this, "Looking for a match", Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.VISIBLE);
//                if (!requestSwitch.isChecked()) {
//                    RideRequest ride = new RideRequest(Time.valueOf(String.valueOf(timeDropDown.getText())), String.valueOf(dropoffDropDown.getSelectedItem().toString()), String.valueOf(pickupDropDown.getSelectedItem().toString()));
//                    //TODO add to pool
//                } else {
//                    DriveRequest drive = new DriveRequest(Time.valueOf(String.valueOf(timeDropDown.getText())), String.valueOf(dropoffDropDown.getSelectedItem().toString()));
//                    //TODO add to pool
//                }
            }
        });

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        isUserInteracting = true;
    }
}
