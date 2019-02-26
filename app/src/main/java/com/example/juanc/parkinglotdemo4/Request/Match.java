package com.example.juanc.parkinglotdemo4.Request;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.juanc.parkinglotdemo4.Network.Request;
import com.example.juanc.parkinglotdemo4.R;
import com.example.juanc.parkinglotdemo4.Sockets;
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
        complete.setVisibility(View.VISIBLE);
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final JSONObject body = new JSONObject();
                try {
                    body.put("email", email);
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            realSocket.emit("get_completed_match", body);
                        }
                    });
                    thread.start();
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
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            realSocket.emit("get_cancelled_match", body);
                        }
                    });
                    thread.start();
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
        realSocket = extras.getParcelable("socket");
//        createSocket();

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
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            realSocket.emit("get_completed_match", body);
                        }
                    });
                    thread.start();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final JSONObject body = new JSONObject();
                try {
                    body.put("email", email);
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            realSocket.emit("get_cancelled_match", body);
                        }
                    });
                    thread.start();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        email = extras.getString("Email");
        name.setText("Name: " + extras.getString("Name"));
        String time1 = extras.getString("Time");
        time1 = time1.substring(0,1) + ":" + time1.substring(2,3);
        time.setText("Time : " + time1);
        pickupLot.setText("Pick up lot: " + extras.getString("Pickup"));
        dropoffLot.setText("Drop off lot: " + extras.getString("Dropoff"));
        if (extras.getString("carColor")!=null){
            color.setText("Car color: " + extras.getString("carColor"));
            brand.setText("Car make: " + extras.getString("carMake"));
            model.setText("Car model : " + extras.getString("carModel"));
        }
    }

    private void createSocket(){
        Sockets socket = new Sockets();
        realSocket = socket.getSocket();
        realSocket.connect();
    }
}
