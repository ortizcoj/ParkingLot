package com.example.juanc.parkinglotdemo4.Request;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.juanc.parkinglotdemo4.Network.Request;
import com.example.juanc.parkinglotdemo4.R;
import com.example.juanc.parkinglotdemo4.RegisterLogin.Menu;
import com.example.juanc.parkinglotdemo4.Sockets;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

public class WaitingToMatch extends AppCompatActivity {
    private TextView time;
    private TextView pickupLot;
    private TextView dropoffLot;
    private TextView and;
    private TextView time2;
    private String email;
    private ImageView mImageView;
    private ImageView citationLot;
    private Button cancel;
    private Socket realSocket;
    private volatile String matchName = null;
    private String matchTime;
    private String matchCarMake = null;
    private String matchCarModel = null;
    private String matchCarColor = null;
    private String matchPickupLot = "";
    private String matchDropOff = "";
    private Button logout;
    private String userCarMake;
    private String userCarModel;
    private String userCarColor;
    private String userName;
    private String userPassword;
    private String time1;
    private String time_2;

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
        mImageView.setVisibility(View.GONE);
        citationLot.setVisibility(View.GONE);
        time.setVisibility(View.VISIBLE);
        and.setVisibility(View.VISIBLE);
        time2.setVisibility(View.VISIBLE);
        pickupLot.setVisibility(View.VISIBLE);
        dropoffLot.setVisibility(View.VISIBLE);
        logout.setVisibility(View.VISIBLE);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final JSONObject body = new JSONObject();
                try {
                    body.put("email", email);
                    realSocket.emit("send_cancelled_request", body);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getApplicationContext(), Menu.class);
                intent.putExtra("Registration", "Logged out");
                startActivity(intent);
            }
        });
        cancel.setVisibility(View.VISIBLE);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final JSONObject body = new JSONObject();
                try {
                    body.put("email", email);
                    realSocket.emit("send_cancelled_request", body);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return true;
    }

    private boolean setUpRideMainUI() {
        mImageView.setVisibility(View.VISIBLE);
        citationLot.setVisibility(View.VISIBLE);
        time.setVisibility(View.GONE);
        and.setVisibility(View.GONE);
        time2.setVisibility(View.GONE);
        pickupLot.setVisibility(View.GONE);
        dropoffLot.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);
        logout.setVisibility(View.GONE);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting_layout);
        Bundle extras = getIntent().getExtras();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mImageView = findViewById(R.id.map);
        mImageView.setVisibility(View.INVISIBLE);
        citationLot = findViewById(R.id.citation);
        citationLot.setVisibility(View.INVISIBLE);

        time = findViewById(R.id.time);
        and = findViewById(R.id.and);
        time2 = findViewById(R.id.time2);
        pickupLot = findViewById(R.id.pickup);
        dropoffLot = findViewById(R.id.dropoff);
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final JSONObject body = new JSONObject();
                try {
                    body.put("email", email);
                    realSocket.emit("send_cancelled_request", body);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getApplicationContext(), Menu.class);
                intent.putExtra("Registration", "Logged out");
                startActivity(intent);
            }
        });
        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final JSONObject body = new JSONObject();
                try {
                    body.put("email", email);
                    realSocket.emit("send_cancelled_request", body);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(getApplicationContext(), RiderDriverRequest.class);
                intent.putExtra("carColor", userCarColor);
                intent.putExtra("carMake", userCarMake);
                intent.putExtra("carModel", userCarModel);
                intent.putExtra("Name", userName);
                intent.putExtra("Password", userPassword);
                intent.putExtra("Email", email);

                startActivity(intent);

            }
        });

        userCarColor = extras.getString("carColor");
        userCarMake = extras.getString("carMake");
        userCarModel = extras.getString("carModel");
        userPassword = extras.getString("Password");
        userName = extras.getString("Name");
        email = extras.getString("Email");
        time1 = extras.getString("Time1");
        time_2 = extras.getString("Time2");
        if (!time1.contains(":")){
            time1 = time1.substring(0,2) + ":" + time1.substring(2);
            time_2 = time_2.substring(0,2) + ":" + time_2.substring(2);

        }
        time.setText("Between : " + time1);
        time2.setText("" + time_2);
        String pick_up = extras.getString("Pickup");
        String drop_off = extras.getString("Dropoff");
        dropoffLot.setText("Drop off lot: " + drop_off);
        if (!pick_up.equals("0")){
            pickupLot.setText("Pick up lot: " + pick_up);
        } else {
            pickupLot.setVisibility(View.GONE);
        }

        createSocket();
        sendRequest(time1, time_2, pick_up, drop_off);
    }

    private void sendRequest(String startTime, String endTime, String pick, String drop) {
        String firstT = startTime.substring(0,2) + startTime.substring(3,5);
        String secondT = endTime.substring(0,2) + endTime.substring(3,5);
        Request request = new Request(email, firstT, secondT, pick, drop);
        final JSONObject body = new JSONObject();
        if (!pick.equals("0")){
            sendRiderRequest(request, body);
        } else {
            sendDriverRequest(request, body);
        }
    }

    private void createSocket(){
        Sockets socket = new Sockets();
        realSocket = socket.getSocket();
        realSocket.on("get_rider_match", onNewMessage);
        realSocket.on("get_driver_match", onNewMessage);
        realSocket.on("send_cancelled_request", onCancelRequest);
        realSocket.connect();
    }

    @Override
    protected void onDestroy() {
        realSocket.close();
        super.onDestroy();
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject json = new JSONObject();
                    try {
                        json.put("Stuff", args[0]);

                        matchTime = ((String) args[0]).split("\"")[7];
                        matchPickupLot = ((String) args[0]).split("\"")[11];
                        matchDropOff = ((String) args[0]).split("\"")[15];
                        if (((String) args[0]).contains("carMake")) {
                            matchCarMake = ((String) args[0]).split("\"")[19];
                            matchCarModel = ((String) args[0]).split("\"")[23];
                            matchCarColor = ((String) args[0]).split("\"")[27];
                        }
                        matchName = ((String) args[0]).split("\"")[3];

                        matchMade(matchName, matchTime, matchPickupLot, matchDropOff, matchCarMake, matchCarModel, matchCarColor);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    private void matchMade(final String matchName, final String matchTime, final String matchPickupLot,
                           final String matchDropoffLot, final String matchCarMake, final String matchCarModel, final String matchCarColor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        builder.setTitle("MATCH FOUND!");

        final TextView name = createCarMake(matchName);
        final TextView time = createCarMake(matchTime);
        final TextView pickupLot = createCarMake(matchPickupLot);
        final TextView dropoffLot = createCarMake(matchDropoffLot);

        layout.addView(name);
        layout.addView(time);
        layout.addView(pickupLot);
        layout.addView(dropoffLot);

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
                intent.putExtra("userCarModel", userCarModel);
                intent.putExtra("userCarMake", userCarMake);
                intent.putExtra("userCarColor", userCarColor);
                intent.putExtra("Password", userPassword);
                intent.putExtra("name", userName);
                intent.putExtra("Time1", time1);
                intent.putExtra("Time2", time_2);
                startActivity(intent);
            }
        });
        builder.show();
    }

    @NonNull
    private TextView createCarMake(String matchCarMake) {
        final TextView carMake = new TextView(this);
        carMake.setText(matchCarMake);
        carMake.setPadding(0,10,0,10);
        return carMake;
    }

    private Emitter.Listener onCancelRequest = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Toast.makeText(getApplicationContext(), "It's been a while... New request?", Toast.LENGTH_LONG).show();
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
            realSocket.emit("send_ride_request", body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
