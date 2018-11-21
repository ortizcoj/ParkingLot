package com.example.juanc.parkinglotdemo4;

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

public class Menu extends AppCompatActivity {

    private ImageView mImageView;
    private ImageView citationLot;
    private TextView emailTV;
    private TextView passwordTV;
    private EditText emailED;
    private EditText passwordED;
    private Button register;
    private Button login;

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
        emailED.setVisibility(View.VISIBLE);
        emailTV.setVisibility(View.VISIBLE);
        passwordED.setVisibility(View.VISIBLE);
        passwordTV.setVisibility(View.VISIBLE);
        login.setVisibility(View.VISIBLE);
        register.setVisibility(View.VISIBLE);
        register.setX(50);
        mImageView.setVisibility(View.GONE);
        citationLot.setVisibility(View.GONE);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        register = findViewById(R.id.registerButton);
        register.setX(50);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
