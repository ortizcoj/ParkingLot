package com.example.juanc.parkinglotdemo4.Request;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private String pickupHourEarly;
    private String pickupMinEarly;
    private String pickupHourLate;
    private String pickupMinLate;
    private TextView and;
    String email;
    String startTime = "";
    String endTime = "";
    private static Socket realSocket;
    private String name = "";
    private String carColor = "";
    private String carMake = "";
    private String carModel = "";
    private String password = "";
    private String matchName = null;
    private String matchTime;
    private String pickupLot;
    private String dropoffLot;
    private String matchCarMake = null;
    private String matchCarModel = null;
    private String matchCarColor = null;

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
        realSocket.on("get_rider_match", onNewMessage);
        realSocket.on("get_driver_match", onNewMessage);
        realSocket.connect();
        final JSONObject body = new JSONObject();
        if (!requestSwitch.isChecked()){
            disableButtons();
            sendRiderRequest(request, body);
        } else {
            disableButtons();
            sendDriverRequest(request, body);
        }
        while (matchName==null){

        }

        pb.setVisibility(View.GONE);
        matchMade(matchName, matchTime, pickupLot, dropoffLot, matchCarMake, matchCarModel, matchCarColor);
    }

    private void disableButtons() {
        requestSwitch.setFocusable(false);
        dropoffDropDown.setFocusable(false);
        pickupDropDown.setFocusable(false);
        firstTimeButton.setFocusable(false);
        secondTimeButton.setFocusable(false);
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

                        matchTime = ((String) args[0]).split("\"")[7];
                        pickupLot = ((String) args[0]).split("\"")[11];
                        dropoffLot = ((String) args[0]).split("\"")[15];
                        if (((String) args[0]).contains("carMake")){
                            carMake = ((String) args[0]).split("\"")[19];
                            carModel = ((String) args[0]).split("\"")[23];
                            carColor = ((String) args[0]).split("\"")[27];
                        }
                        matchName = ((String) args[0]).split("\"")[3];
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
    };

    private void sendDriverRequest(Request request, final JSONObject body) {
        try {
            body.put("email", request.getEmail());
            body.put("startTime", request.getStartTime());
            body.put("endTime", request.getEndTime());
            body.put("lot", request.getDropoffLot());
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    realSocket.emit("send_drive_request", body);
                }
            });
            thread.start();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendRiderRequest(Request request, final JSONObject body) {
        try {
            body.put("email", request.getEmail());
            body.put("startTime", request.getStartTime());
            body.put("endTime", request.getEndTime());
            body.put("pickup", request.getPickupLocation());
            body.put("lot", request.getDropoffLot());
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    realSocket.emit("send_ride_request", body);
                }
            });
            thread.start();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

                                Toast.makeText(RiderDriverRequest.this, currentHourText + ":" + currentMinuteText, Toast.LENGTH_SHORT).show();
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
                sendRequest();
                Toast.makeText(RiderDriverRequest.this, "Looking for a match", Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.VISIBLE);
            }
        });

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void matchMade(final String matchName, final String matchTime, final String matchPickupLot,
                           final String matchDropoffLot, final String matchCarMake, final String matchCarModel, final String matchCarColor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        builder.setTitle("MATCH FOUND!");

        final TextView name = new TextView(this);
        name.setText(matchName);
        name.setPadding(0,10,0,10);
        final TextView time = new TextView(this);
        time.setText(matchTime);
        time.setPadding(0,10,0,10);
        final TextView pickupLot = new TextView(this);
        pickupLot.setText(matchPickupLot);
        pickupLot.setPadding(0,10,0,10);
        final TextView dropoffLot = new TextView(this);
        dropoffLot.setText(matchDropoffLot);
        dropoffLot.setPadding(0,10,0,10);

        layout.addView(name);
        layout.addView(time);
        layout.addView(pickupLot);
        layout.addView(dropoffLot);
        if (matchCarMake!=null){
            final TextView carMake = new TextView(this);
            carMake.setText(matchCarMake);
            carMake.setPadding(0,10,0,10);
            final TextView carModel = new TextView(this);
            carModel.setText(matchCarModel);
            carModel.setPadding(0,10,0,10);
            final TextView carColor = new TextView(this);
            carColor.setText(matchCarColor);
            carColor.setPadding(0,10,0,10);

            layout.addView(carMake);
            layout.addView(carModel);
            layout.addView(carColor);
        }

        builder.setView(layout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(getApplicationContext(), Match.class);
                intent.putExtra("Email", email);
                intent.putExtra("carColor", matchCarColor);
                intent.putExtra("carMake", matchCarMake);
                intent.putExtra("carModel", matchCarModel);
                intent.putExtra("Name", matchName);
                intent.putExtra("Time", matchTime);
                intent.putExtra("Pickup", matchPickupLot);
                intent.putExtra("Dropoff", matchDropoffLot);
                startActivity(intent);
            }
        });
        builder.show();
    }
}
