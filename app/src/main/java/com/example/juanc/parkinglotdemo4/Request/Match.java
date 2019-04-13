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

import com.example.juanc.parkinglotdemo4.Map.LotDisplay;
import com.example.juanc.parkinglotdemo4.MapSocket;
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
    private String matchTime = null;
    private String sPickup = "";
    private String sDropoff = "";
    private String carMake = "";
    private String carModel = "";
    private String carColor = null;
    private String matchName = "";
    private Button logout;
    private String userCarMake;
    private String userCarModel;
    private String userCarColor;
    private String userName;
    private String userPassword;
    private String time_1 = "";
    private String time2;
    private String dropoff1;
    private String pick_up;
    private Socket mapSocket;

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

    private boolean setUpRideMainUI() {
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            realSocket.emit("send_completed_match", body);
                            rideCompleted("Ride completed! Hope you enjoyed our service");
                        }
                    });
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            realSocket.emit("send_cancelled_match", body);
                            rideCompleted("You cancelled the ride");
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return true;
    }
    
    private boolean setUpMapMainUI() {
        mImageView.setVisibility(View.VISIBLE);
        citationLot.setVisibility(View.VISIBLE);
        citationLot.setAlpha((float) 0.0);
        citationLot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LotDisplay.class);
                startActivity(intent);
            }
        });
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            realSocket.emit("send_completed_match", body);
                            rideCompleted("Ride completed! Hope you enjoyed our service");
                        }
                    });
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
                            realSocket.emit("send_cancelled_match", body);
                            rideCompleted("You cancelled the ride");
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        email = extras.getString("Email");
        matchName = extras.getString("Name");
        String time1 = extras.getString("Time");
        matchTime = time1.substring(0,2) + ":" + time1.substring(2,4);
        pick_up = extras.getString("pick_up");
        sPickup = extras.getString("Pickup");
        sDropoff = extras.getString("Dropoff");
        if (extras.getString("carColor")!=null) {
            carColor = extras.getString("carColor");
            carMake = extras.getString("carMake");
            carModel = extras.getString("carModel");
        }

        userCarColor = extras.getString("userCarColor");
        userCarMake = extras.getString("userCarMake");
        userCarModel = extras.getString("userCarModel");
        userPassword = extras.getString("Password");
        userName = extras.getString("name");
        time_1 = extras.getString("Time1");
        time2 = extras.getString("Time2");
        dropoff1 = extras.getString("Dropoff1");

        updateFields();

        createSocket();
        sendNewID();

        realSocket.emit("get_lot_count");
    }

    private void rideCancelled() {
        Toast.makeText(getApplicationContext(), "Your ride was cancelled, looking for a " +
                "new match", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(getApplicationContext(), WaitingToMatch.class);
        intent.putExtra("carColor", userCarColor);
        intent.putExtra("carMake", userCarMake);
        intent.putExtra("carModel", userCarModel);
        intent.putExtra("Name", userName);
        intent.putExtra("Password", userPassword);
        intent.putExtra("Email", email);
        intent.putExtra("Pickup", pick_up);
        intent.putExtra("Dropoff", dropoff1);
        intent.putExtra("Time1", time_1);
        intent.putExtra("Time2", time2);
        intent.putExtra("NewRide", "0");
        startActivity(intent);
    }

    private void rideCompleted(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

        Intent intent = new Intent(getApplicationContext(), RiderDriverRequest.class);
        intent.putExtra("carColor", userCarColor);
        intent.putExtra("carMake", userCarMake);
        intent.putExtra("carModel", userCarModel);
        intent.putExtra("Name", userName);
        intent.putExtra("Password", userPassword);
        intent.putExtra("Email", email);
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
        MapSocket mSocket = new MapSocket();
        mapSocket = mSocket.getSocket();
        mapSocket.on("lot_count", onNewMessage1);
        mapSocket.on("first_state_count", onNewMessage1);
        mapSocket.connect();
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
        }
    };

    @Override
    protected void onResume() {
        realSocket.connect();
        super.onResume();
    }

    private Emitter.Listener onNewMessage1 = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject json = new JSONObject();
                    try {
                        json.put("Stuff", args[0]);
                        int spot = (int) args[0];

                        if (spot==186){
                            mImageView.setImageResource(R.drawable.map186);
                        } else if (spot==185){
                            mImageView.setImageResource(R.drawable.map185);
                        } else if (spot==184){
                            mImageView.setImageResource(R.drawable.map184);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    @Override
    public void onBackPressed() {
    }
}
