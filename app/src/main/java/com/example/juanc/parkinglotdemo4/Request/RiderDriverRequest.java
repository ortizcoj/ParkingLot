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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.juanc.parkinglotdemo4.Map.LotDisplay;
import com.example.juanc.parkinglotdemo4.Network.Request;
import com.example.juanc.parkinglotdemo4.Profile;
import com.example.juanc.parkinglotdemo4.R;
import com.example.juanc.parkinglotdemo4.Sockets;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;

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
    private ProgressBar pb;
    private Button firstTimeButton;
    private Button secondTimeButton;
    private int pickupHourEarly;
    private int pickupMinEarly;
    private int pickupHourLate;
    private int pickupMinLate;
    private TextView and;
    String email;
    String startTime = "";
    String endTime = "";
    private Socket realSocket;

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
        dropoffDropDown.setVisibility(View.GONE);
        pickupDropDown.setVisibility(View.GONE);
        and.setVisibility(View.GONE);
        next.setVisibility(View.GONE);
        firstTimeButton.setVisibility(View.GONE);
        mImageView.setVisibility(View.VISIBLE);
        citationLot.setVisibility(View.VISIBLE);
        firstTimeButton.setVisibility(View.GONE);
        secondTimeButton.setVisibility(View.GONE);
        profile.setVisibility(View.GONE);
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
        profile.setVisibility(View.VISIBLE);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Profile.class);
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
                //TODO decide how to make ride OR drive
                convertToTimes();
                sendRequest();
                Toast.makeText(RiderDriverRequest.this, "Looking for a match", Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.VISIBLE);
            }
        });
        mImageView.setVisibility(View.GONE);
        citationLot.setVisibility(View.GONE);
        return true;
    }

    private void sendRequest() {
        Request request = new Request(email, startTime, endTime, pickupDropDown.getSelectedItem().toString(), dropoffDropDown.getSelectedItem().toString());
        Sockets socket = new Sockets();
        realSocket = socket.getSocket();
        realSocket.on("get_match", onNewMessage);
        realSocket.connect();
        final JSONObject body = new JSONObject();
        try {
            body.put("email", request.getEmail());
            body.put("startTime", request.getStartTime());
            body.put("endTime", request.getEndTime());
            body.put("pickupLocation", request.getPickupLocation());
            body.put("dropoffLot", request.getDropoffLot());
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    realSocket.emit("send_request", body);
                }
            });
            thread.start();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    JSONObject json = new JSONObject();
                    try {
                        json.put("Stuff", args[0]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
    };

    private void convertToTimes() {
        startTime = "" + pickupHourEarly + pickupMinEarly;
        endTime = "" + pickupHourLate + pickupMinLate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request);
        Bundle extras = getIntent().getExtras();
        email = extras.getString("Email");

        profile = findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Profile.class);
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
        pb = findViewById(R.id.ProgressBar);
        pb.setVisibility(View.GONE);
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

                                Toast.makeText(RiderDriverRequest.this, currentHourText + ":" + currentMinuteText, Toast.LENGTH_SHORT).show();
                                pickupHourEarly = Integer.valueOf(currentHourText);
                                pickupMinEarly = Integer.valueOf(currentMinuteText);
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

                                Toast.makeText(RiderDriverRequest.this, currentHourText + ":" + currentMinuteText, Toast.LENGTH_SHORT).show();
                                pickupHourLate = Integer.valueOf(currentHourText);
                                pickupMinLate = Integer.valueOf(currentMinuteText);
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
                //TODO ride or drive request. Differences?
                convertToTimes();
                sendRequest();
                Toast.makeText(RiderDriverRequest.this, "Looking for a match", Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.VISIBLE);
            }
        });

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
}
