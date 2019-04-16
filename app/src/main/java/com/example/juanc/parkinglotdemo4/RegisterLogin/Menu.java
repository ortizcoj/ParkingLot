package com.example.juanc.parkinglotdemo4.RegisterLogin;

import android.content.Intent;
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
import android.widget.Toast;

import com.example.juanc.parkinglotdemo4.Map.LotDisplay;
import com.example.juanc.parkinglotdemo4.MapSocket;
import com.example.juanc.parkinglotdemo4.Network.LoginInfo;
import com.example.juanc.parkinglotdemo4.Network.Networking;
import com.example.juanc.parkinglotdemo4.R;
import com.example.juanc.parkinglotdemo4.Security.Hashing;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;

public class Menu extends AppCompatActivity {

    private ImageView mImageView;
    private ImageView citationLot;
    private TextView emailTV;
    private TextView passwordTV;
    private EditText emailED;
    private EditText passwordED;
    private Button register;
    private Button login;
    private String regUser;
    private TextView loginTV;
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
        emailED.setVisibility(View.GONE);
        emailTV.setVisibility(View.GONE);
        passwordED.setVisibility(View.GONE);
        passwordTV.setVisibility(View.GONE);
        login.setVisibility(View.GONE);
        register.setVisibility(View.GONE);
        loginTV.setVisibility(View.GONE);
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
        emailED.setVisibility(View.VISIBLE);
        emailTV.setVisibility(View.VISIBLE);
        passwordED.setVisibility(View.VISIBLE);
        passwordTV.setVisibility(View.VISIBLE);
        loginTV.setVisibility(View.VISIBLE);
        login.setVisibility(View.VISIBLE);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registration();
            }
        });
        register.setVisibility(View.VISIBLE);
        register.setX(50);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });
        mImageView.setVisibility(View.GONE);
        citationLot.setVisibility(View.GONE);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) throws NullPointerException{
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        try {
            regUser = extras.getString("Registration");
            Toast.makeText(getApplicationContext(), regUser, Toast.LENGTH_LONG).show();
        } catch (NullPointerException e){
            Toast.makeText(getApplicationContext(), "Welcome!", Toast.LENGTH_LONG).show();

        }
        setContentView(R.layout.menu_layout);

        mImageView = findViewById(R.id.map);
        mImageView.setImageResource(R.drawable.map);
        mImageView.setVisibility(View.INVISIBLE);
        citationLot = findViewById(R.id.citation);
        citationLot.setVisibility(View.INVISIBLE);
        emailED = findViewById(R.id.email);
        emailTV = findViewById(R.id.emailTV);
        passwordED = findViewById(R.id.passwordET);
        passwordTV = findViewById(R.id.passwordTV);
        loginTV = findViewById(R.id.login);
        login = findViewById(R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registration();
            }
        });
        register = findViewById(R.id.registerButton);
        register.setX(50);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
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
    protected void onRestart() {
        realSocket.connect();
        super.onRestart();
    }

    private void registration() {
        try {
            Hashing security = new Hashing();
            String hashpass = security.hash(passwordED.getText().toString());
            LoginInfo loginInfo = new LoginInfo(emailED.getText().toString(), hashpass);

            Networking networking = new Networking();
            networking.loginUser(loginInfo, getApplicationContext());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
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
}