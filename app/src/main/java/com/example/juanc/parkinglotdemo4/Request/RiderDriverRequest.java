package com.example.juanc.parkinglotdemo4.Request;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.juanc.parkinglotdemo4.Map.LotDisplay;
import com.example.juanc.parkinglotdemo4.Network.Request;
import com.example.juanc.parkinglotdemo4.Profile;
import com.example.juanc.parkinglotdemo4.R;
import com.example.juanc.parkinglotdemo4.RegisterLogin.Menu;

import org.json.JSONException;
import org.json.JSONObject;

public class RiderDriverRequest extends AppCompatActivity {

    private TextView eagleRide;
    private TextView requestType;
    private TextView timeTV;
    private TextView dropoff;
    private TextView pickup;
    private Switch requestSwitch;
    private Spinner dropoffDropDown;
    private Spinner pickupDropDown;
    private Button next;
    private Button profile;
    private ImageView mImageView;
    private ImageView citationLot;
    private Button firstTimeButton;
    private Button secondTimeButton;
    private String pickupHourEarly;
    private String pickupMinEarly;
    private String pickupHourLate;
    private String pickupMinLate;
    private Button logout;
    private TextView and;
    String email;
    String startTime = "";
    String endTime = "";
    private String name = "";
    private String carColor = "";
    private String carMake = "";
    private String carModel = "";
    private String password = "";
    private boolean sent = false;

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
        dropoffDropDown.setVisibility(View.GONE);
        pickupDropDown.setVisibility(View.GONE);
        and.setVisibility(View.GONE);
        next.setVisibility(View.GONE);
        mImageView.setVisibility(View.VISIBLE);
        citationLot.setVisibility(View.VISIBLE);
        firstTimeButton.setVisibility(View.GONE);
        secondTimeButton.setVisibility(View.GONE);
        profile.setVisibility(View.GONE);
        logout.setVisibility(View.GONE);
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
        firstTimeButton.setVisibility(View.VISIBLE);
        secondTimeButton.setVisibility(View.VISIBLE);
        dropoff.setVisibility(View.VISIBLE);
        pickup.setVisibility(View.VISIBLE);
        pickupDropDown.setVisibility(View.VISIBLE);
        and.setVisibility(View.VISIBLE);
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

        logout.setVisibility(View.VISIBLE);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Menu.class);
                intent.putExtra("Registration", "Logged out");
                startActivity(intent);
            }
        });

        profile.setVisibility(View.VISIBLE);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Profile.class);
                intent.putExtra("carColor", carColor);
                intent.putExtra("carMake", carMake);
                intent.putExtra("carModel", carModel);
                intent.putExtra("Name", name);
                intent.putExtra("Email", email);
                intent.putExtra("Password", password);
                startActivity(intent);
            }
        });
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
        dropoffDropDown.setVisibility(View.VISIBLE);
        next.setVisibility(View.VISIBLE);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertToTimes();
                openWaiting();
            }
        });
        mImageView.setVisibility(View.GONE);
        citationLot.setVisibility(View.GONE);
        return true;
    }

    private void openWaiting() {
        Intent intent = new Intent(getApplicationContext(), WaitingToMatch.class);
        intent.putExtra("Email", email);
        intent.putExtra("Time1", startTime);
        intent.putExtra("Time2", endTime);
        intent.putExtra("Dropoff", dropoffDropDown.getSelectedItem().toString());
        if (!requestSwitch.isChecked()){
            intent.putExtra("Pickup", pickupDropDown.getSelectedItem().toString());
        } else {
            intent.putExtra("Pickup", "0");
        }
        startActivity(intent);
    }

    private void convertToTimes() {
        if (String.valueOf(pickupMinLate).length()==1){
            pickupMinLate = 0 + pickupMinLate;
        }
        if (String.valueOf(pickupHourLate).length()==1){
            pickupHourLate = 0 + pickupHourLate;
        }
        if (String.valueOf(pickupHourEarly).length()==1){
            pickupHourEarly = 0 + pickupHourEarly;
        }
        if (String.valueOf(pickupMinEarly).length()==1){
            pickupMinEarly = 0 + pickupMinEarly;
        }
        startTime = pickupHourEarly + pickupMinEarly;
        endTime = pickupHourLate + pickupMinLate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request);
        Bundle extras = getIntent().getExtras();
        email = extras.getString("Email");
        name = extras.getString("Name");
        carColor = extras.getString("carColor");
        carMake = extras.getString("carMake");
        carModel = extras.getString("carModel");
        password = extras.getString("Password");

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Menu.class);
                intent.putExtra("Registration", "Logged out");
                startActivity(intent);
            }
        });

        profile = findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Profile.class);
                intent.putExtra("carColor", carColor);
                intent.putExtra("carMake", carMake);
                intent.putExtra("carModel", carModel);
                intent.putExtra("Name", name);
                intent.putExtra("Email", email);
                intent.putExtra("Password", password);
                startActivity(intent);
            }
        });
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
        String[] items = new String[]{"Parking circle of Student Union", "Between COA and Flight Line", "Parking between Lehman and COAS", "Behind COAS", "Parking by COB"};
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
        firstTimeButton = findViewById(R.id.timeButton);
        firstTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.time_picker, null, false);

                final TimePicker myTimePicker = (TimePicker) view
                        .findViewById(R.id.myTimePicker);

                new AlertDialog.Builder(RiderDriverRequest.this).setView(view)
                        .setTitle("Set Time")
                        .setPositiveButton("Go", new DialogInterface.OnClickListener() {
                            @TargetApi(11)
                            public void onClick(DialogInterface dialog, int id) {

                                String currentHourText = myTimePicker.getCurrentHour()
                                        .toString();

                                String currentMinuteText = myTimePicker
                                        .getCurrentMinute().toString();

                                pickupHourEarly = currentHourText;
                                pickupMinEarly = currentMinuteText;
                                firstTimeButton.setText(new StringBuilder().append(pickupHourEarly).append(":").append(pickupMinEarly).toString());

                                dialog.cancel();

                            }
                        }).show();
            }
        });

        secondTimeButton = findViewById(R.id.timeButton2);

        secondTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.time_picker, null, false);

                final TimePicker myTimePicker = (TimePicker) view
                        .findViewById(R.id.myTimePicker);

                new AlertDialog.Builder(RiderDriverRequest.this).setView(view)
                        .setTitle("Set Time")
                        .setPositiveButton("Go", new DialogInterface.OnClickListener() {
                            @TargetApi(11)
                            public void onClick(DialogInterface dialog, int id) {

                                String currentHourText = myTimePicker.getCurrentHour()
                                        .toString();

                                String currentMinuteText = myTimePicker
                                        .getCurrentMinute().toString();

                                pickupHourLate = currentHourText;
                                pickupMinLate = currentMinuteText;
                                secondTimeButton.setText(new StringBuilder().append(pickupHourLate).append(":").append(pickupMinLate).toString());

                                dialog.cancel();

                            }

                        }).show();
            }
        });

        dropoffDropDown = findViewById(R.id.dropoffDropDown);
        and = findViewById(R.id.and);

        String[] dropItems = new String[]{"Tomcat", "Voyager", "Concorde", "Cochran", "Earhart", "Enterprise"};
        ArrayAdapter<String> dropAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dropItems);
        dropoffDropDown.setAdapter(dropAdapter);
        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertToTimes();
                openWaiting();
            }
        });

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
}
