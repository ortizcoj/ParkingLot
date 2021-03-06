package com.example.juanc.parkinglotdemo4.RegisterLogin;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.juanc.parkinglotdemo4.Map.LotDisplay;
import com.example.juanc.parkinglotdemo4.MapSocket;
import com.example.juanc.parkinglotdemo4.Network.Networking;
import com.example.juanc.parkinglotdemo4.Network.User;
import com.example.juanc.parkinglotdemo4.R;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

public class Register2 extends AppCompatActivity {

    private ImageView mImageView;
    private ImageView citationLot;
    private TextView name;
    private TextView brand;
    private TextView model;
    private TextView color;
    private TextView registerText;
    private Button registerButton;
    private EditText nameET;
    private EditText brandET;
    private EditText modelET;
    private EditText colorET;
    private String email;
    private String password;
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
        name.setVisibility(View.GONE);
        nameET.setVisibility(View.GONE);
        brand.setVisibility(View.GONE);
        brandET.setVisibility(View.GONE);
        model.setVisibility(View.GONE);
        modelET.setVisibility(View.GONE);
        color.setVisibility(View.GONE);
        colorET.setVisibility(View.GONE);
        registerButton.setVisibility(View.GONE);
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
        return true;
    }

    private boolean setUpRideMainUI() {
        name.setVisibility(View.VISIBLE);
        nameET.setVisibility(View.VISIBLE);
        brand.setVisibility(View.VISIBLE);
        brandET.setVisibility(View.VISIBLE);
        model.setVisibility(View.VISIBLE);
        modelET.setVisibility(View.VISIBLE);
        color.setVisibility(View.VISIBLE);
        colorET.setVisibility(View.VISIBLE);
        registerButton.setVisibility(View.VISIBLE);
        registerButton.setX(50);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registration();
            }
        });
        mImageView.setVisibility(View.GONE);
        citationLot.setVisibility(View.GONE);
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        email = extras.getString("Email");
        password = extras.getString("Password");
        setContentView(R.layout.register_2_layout);

        mImageView = findViewById(R.id.map);
        mImageView.setImageResource(R.drawable.map);
        mImageView.setVisibility(View.INVISIBLE);
        citationLot = findViewById(R.id.citation);
        citationLot.setVisibility(View.INVISIBLE);
        registerText = findViewById(R.id.register);
        registerText.setTypeface(null, Typeface.BOLD);
        name = findViewById(R.id.nameTV);
        nameET = findViewById(R.id.name);
        brand = findViewById(R.id.brandTV);
        brandET = findViewById(R.id.brandET);
        model = findViewById(R.id.modelTV);
        modelET = findViewById(R.id.modelET);
        color = findViewById(R.id.colorTV);
        colorET = findViewById(R.id.colorET);
        registerButton = findViewById(R.id.registerButton);
        registerButton.setX(50);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registration();
            }
        });
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



        createSocket();

        realSocket.emit("get_lot_count");
    }


    private void createSocket(){
        MapSocket socket = new MapSocket();
        realSocket = socket.getSocket();
        realSocket.on("lot_count", onNewMessage);
        realSocket.on("first_state_count", onNewMessage);
        realSocket.connect();
    }

    @Override
    protected void onResume() {
        realSocket.connect();
        super.onResume();
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

    private void registration() {
        User userInfo = new User(nameET.getText().toString(), email, password, brandET.getText().toString(), modelET.getText().toString(), colorET.getText().toString());

        Networking networking = new Networking();
        networking.registerUser(userInfo, getApplicationContext());
    }
}
