package com.example.juanc.parkinglotdemo4.RegisterLogin;

import android.annotation.SuppressLint;
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
import com.example.juanc.parkinglotdemo4.Network.LoginInfo;
import com.example.juanc.parkinglotdemo4.Network.Networking;
import com.example.juanc.parkinglotdemo4.Network.User;
import com.example.juanc.parkinglotdemo4.R;
import com.example.juanc.parkinglotdemo4.Request.RiderDriverRequest;
import com.example.juanc.parkinglotdemo4.Security.Hashing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

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
        mImageView.setVisibility(View.VISIBLE);
        citationLot.setVisibility(View.VISIBLE);
//        citationLot.setX(200);
//        citationLot.setY(850);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            regUser = extras.getString("Registration");
            Toast.makeText(getApplicationContext(), regUser, Toast.LENGTH_LONG).show();
        }
        setContentView(R.layout.menu_layout);

        mImageView = findViewById(R.id.map);
        mImageView.setVisibility(View.INVISIBLE);
        citationLot = findViewById(R.id.citation);
        citationLot.setVisibility(View.INVISIBLE);
        emailED = findViewById(R.id.email);
        emailTV = findViewById(R.id.emailTV);
        passwordED = findViewById(R.id.passwordET);
        passwordTV = findViewById(R.id.passwordTV);
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
}
