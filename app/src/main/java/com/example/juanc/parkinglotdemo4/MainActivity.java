package com.example.alex.eagleride;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private TextView email;
    private TextView password;
    private TextView confirmPassword;
    private TextView registerText;
    private Button registerButton;
    private EditText emailField;
    private EditText passwordField;
    private EditText confirmPasswordField;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        email = findViewById(R.id.textView1);
        password = findViewById(R.id.textView2);
        confirmPassword = findViewById(R.id.textView3);
        registerText = findViewById(R.id.textView4);
        registerButton = findViewById(R.id.button2);
        emailField = findViewById(R.id.editText);
        passwordField = findViewById(R.id.editText2);
        confirmPasswordField = findViewById(R.id.editText3);


    }

}
