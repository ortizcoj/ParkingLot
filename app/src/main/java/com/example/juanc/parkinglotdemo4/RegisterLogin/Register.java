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
import android.widget.Toast;

import com.example.juanc.parkinglotdemo4.Map.LotDisplay;
import com.example.juanc.parkinglotdemo4.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.spec.SecretKeySpec;

public class Register extends AppCompatActivity {

    private ImageView mImageView;
    private ImageView citationLot;
    private TextView email;
    private TextView password;
    private TextView confirmPassword;
    private TextView registerText;
    private Button nextButton;
    private EditText emailField;
    private EditText passwordField;
    private EditText confirmPasswordField;

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
        emailField.setVisibility(View.GONE);
        email.setVisibility(View.GONE);
        passwordField.setVisibility(View.GONE);
        password.setVisibility(View.GONE);
        confirmPassword.setVisibility(View.GONE);
        confirmPasswordField.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);
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
        mImageView.setVisibility(View.GONE);
        citationLot.setVisibility(View.GONE);
        emailField.setVisibility(View.VISIBLE);
        email.setVisibility(View.VISIBLE);
        passwordField.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);
        confirmPassword.setVisibility(View.VISIBLE);
        confirmPasswordField.setVisibility(View.VISIBLE);
        nextButton.setVisibility(View.VISIBLE);
        nextButton.setX(50);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Register2.class);
//                    String pass = hash(passwordField.getText().toString());
                intent.putExtra("Email", emailField.getText().toString());
                intent.putExtra("Password", passwordField.getText().toString());
                startActivity(intent);
            }
        });
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        mImageView = findViewById(R.id.map);
        mImageView.setVisibility(View.INVISIBLE);
        citationLot = findViewById(R.id.citation);
        citationLot.setVisibility(View.INVISIBLE);
        registerText = findViewById(R.id.register);
        registerText.setTypeface(null, Typeface.BOLD);
        email = findViewById(R.id.emailTV);
        password = findViewById(R.id.passwordTV);
        confirmPassword = findViewById(R.id.confirmPasswordTV);
        nextButton = findViewById(R.id.registerButton);
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.passwordET);
        confirmPasswordField = findViewById(R.id.confirmPasswordET);
        nextButton.setX(50);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!emailField.getText().toString().contains("@erau.edu") && !emailField.getText().toString().contains("@my.erau.edu") ){
                    Toast.makeText(Register.this, "Email must be an ERAU email account", Toast.LENGTH_SHORT).show();
                } else if (!passwordField.getText().toString().equals(confirmPasswordField.getText().toString())){
                    Toast.makeText(Register.this, "Passwords aren't matching", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), Register2.class);
                    intent.putExtra("Email", emailField.getText().toString());
//                        String hashpass = hash(passwordField.getText().toString());

                    intent.putExtra("Password", passwordField.getText().toString());
                    startActivity(intent);

                }
            }
        });
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

//    public String hash(String password) throws NoSuchAlgorithmException {
//        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
//        byte[] passBytes = password.getBytes();
//        sha256.update(passBytes);
//        byte[] passHash = sha256.digest(passBytes);
//        String strHashpass = passHash.toString();
//        return strHashpass;
//    }
}