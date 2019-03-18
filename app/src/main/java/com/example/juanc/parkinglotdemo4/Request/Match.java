package com.example.juanc.parkinglotdemo4.Request;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.juanc.parkinglotdemo4.R;
import com.example.juanc.parkinglotdemo4.RegisterLogin.Menu;
import com.example.juanc.parkinglotdemo4.Sockets;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

public class Match extends AppCompatActivity {
    private TextView time;
    private TextView pickupLot;
    private TextView dropoffLot;
    private TextView name;
    private TextView brand;
    private TextView model;
    private TextView color;
    private String email;
    private ImageView mImageView;
    private ImageView citationLot;
    private Button complete;
    private Button cancel;
    private Socket realSocket;
    private boolean rideComplete = false;
    private boolean rideCancel = false;
    private boolean newMatch = false;
    private String matchTime = null;
    private String sPickup = "";
    private String sDropoff = "";
    private String carMake = "";
    private String carModel = "";
    private String carColor = null;
    private String matchName = "";
    private Button logout;

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
        name.setVisibility(View.VISIBLE);
        time.setVisibility(View.VISIBLE);
        pickupLot.setVisibility(View.VISIBLE);
        dropoffLot.setVisibility(View.VISIBLE);
        color.setVisibility(View.VISIBLE);
        brand.setVisibility(View.VISIBLE);
        model.setVisibility(View.VISIBLE);
        logout.setVisibility(View.VISIBLE);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Menu.class);
                intent.putExtra("Registration", "Logged out");
                startActivity(intent);
            }
        });
        complete.setVisibility(View.VISIBLE);
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final JSONObject body = new JSONObject();
                try {
                    body.put("email", email);
//                    Thread thread = new Thread(new Runnable() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            realSocket.emit("send_completed_match", body);
                            rideCompleted("Ride completed! Hope you enjoyed our service");
                        }
                    });
//                    thread.start();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        cancel.setVisibility(View.VISIBLE);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final JSONObject body = new JSONObject();
                try {
                    body.put("email", email);
//                    Thread thread = new Thread(new Runnable() {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            realSocket.emit("send_cancelled_match", body);
                            rideCompleted("You cancelled the ride");
                        }
                    });
//                    thread.start();
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
        name.setVisibility(View.GONE);
        time.setVisibility(View.GONE);
        pickupLot.setVisibility(View.GONE);
        dropoffLot.setVisibility(View.GONE);
        color.setVisibility(View.GONE);
        brand.setVisibility(View.GONE);
        model.setVisibility(View.GONE);
        complete.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);
        logout.setVisibility(View.GONE);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_layout);
        Bundle extras = getIntent().getExtras();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mImageView = findViewById(R.id.map);
        mImageView.setVisibility(View.INVISIBLE);
        citationLot = findViewById(R.id.citation);
        citationLot.setVisibility(View.INVISIBLE);

        name = findViewById(R.id.name);
        time = findViewById(R.id.time);
        pickupLot = findViewById(R.id.pickup);
        dropoffLot = findViewById(R.id.dropoff);
        color = findViewById(R.id.color);
        brand = findViewById(R.id.brand);
        model = findViewById(R.id.model);
        complete = findViewById(R.id.complete);
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final JSONObject body = new JSONObject();
                try {
                    body.put("email", email);
//                    Thread thread = new Thread(new Runnable() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            realSocket.emit("send_completed_match", body);
                            rideCompleted("Ride completed! Hope you enjoyed our service");
                        }
                    });
//                    thread.start();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                    Thread thread = new Thread(new Runnable() {
                            realSocket.emit("get_cancelled_match", body);
                            rideCompleted("You cancelled the ride");

                        }
                    });
//                    thread.start();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(getApplicationContext(), RiderDriverRequest.class);
                startActivity(intent);

            }
        });

        email = extras.getString("Email");
        matchName = extras.getString("Name");
        String time1 = extras.getString("Time");
        matchTime = time1.substring(0,2) + ":" + time1.substring(2,4);
        sPickup = extras.getString("Pickup");
        sDropoff = extras.getString("Dropoff");
        if (extras.getString("carColor")!=null) {
            carColor = extras.getString("carColor");
            carMake = extras.getString("carMake");
            carModel = extras.getString("carModel");
        }

        updateFields();

        createSocket();
        sendNewID();

//        waitForAction();

//        while(!rideComplete || !rideCancel){
//            if (rideCancel){
//                  rideCancelled();
//            }
//            if (rideComplete){
//                rideCompleted();
//            }
//
//        }
    }

    private void rideCancelled() {
        Toast.makeText(getApplicationContext(), "Your ride was cancelled, looking for a " +
                "new match", Toast.LENGTH_LONG).show();
    }

    private void rideCompleted(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

        Intent intent = new Intent(getApplicationContext(), RiderDriverRequest.class);
        startActivity(intent);
    }

    private void updateFields() {
        name.setText("Name: " + matchName);
        time.setText("Time : " + matchTime);
        pickupLot.setText("Pick up lot: " + sPickup);
        dropoffLot.setText("Drop off lot: " + sDropoff);
        if (carColor!=null){
            color.setText("Car color: " + carColor);
            brand.setText("Car make: " + carMake);
            model.setText("Car model : " + carModel);
        }
    }

    private void waitForAction() {
        while (true){
            if (newMatch){
                updateFields();
                newMatch=false;
            }
        }
    }

    private void sendNewID() {
        final JSONObject sessionBody = new JSONObject();
        try {
            sessionBody.put("email", email);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    realSocket.emit("update_match_session_id", sessionBody);
                }
            });
            thread.start();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void createSocket(){
        Sockets socket = new Sockets();
        realSocket = socket.getSocket();
        realSocket.on("match_cancelled", onMatchCancelled);
        realSocket.on("get_rider_match", onNewMessage);
        realSocket.on("get_driver_match", onNewMessage);
        realSocket.connect();
    }

    @Override
    protected void onDestroy() {
        realSocket.close();
        super.onDestroy();
    }

    private Emitter.Listener onMatchCancelled = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    rideCancelled();
                }
            });
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
//            Thread thread = new Thread(new Runnable() {
//                //TODO set new text
                @Override
                public void run() {
                    JSONObject json = new JSONObject();
                    try {
                        json.put("Stuff", args[0]);

                        matchTime = ((String) args[0]).split("\"")[7];
                        sPickup = ((String) args[0]).split("\"")[11];
                        sDropoff = ((String) args[0]).split("\"")[15];
                        if (((String) args[0]).contains("carMake")){
                            carMake = ((String) args[0]).split("\"")[19];
                            carModel = ((String) args[0]).split("\"")[23];
                            carColor = ((String) args[0]).split("\"")[27];
                        }
                        matchName = ((String) args[0]).split("\"")[3];

                        updateFields();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
//            thread.start();
        }
    };
}
