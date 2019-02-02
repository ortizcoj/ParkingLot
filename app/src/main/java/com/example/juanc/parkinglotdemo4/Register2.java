package com.example.juanc.parkinglotdemo4;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    byte[] password;

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
                //TODO decide when to make the registration.
                //Here or after gone to Menu screen (I suppose here)
                registration();
                Intent intent = new Intent(getApplicationContext(), Menu.class);
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
        email = extras.getString("Email");
        password = extras.getByteArray("Password");
        setContentView(R.layout.register_2_layout);

        mImageView = findViewById(R.id.map);
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
                Intent intent = new Intent(getApplicationContext(), Menu.class);
                startActivity(intent);
            }
        });
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void registration() {
        User userInfo = new User(nameET.getText().toString(), email, password, brandET.getText().toString(), modelET.getText().toString(), colorET.getText().toString());
        Log.e("USERNAME", userInfo.getName());
        Log.e("USERPASSWORD", userInfo.getPassword().toString());
        Log.e("USEREMAIL", userInfo.getEmail());
        Log.e("USERBRAND", userInfo.getCarMake());
        Log.e("USERMODEL", userInfo.getCarModel());
        Log.e("USERCOLOR", userInfo.getCarColor());

        Log.e("eagleride", "about to send stuff");

        final OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n    \"name\": \"" + userInfo.getName() + "\",\n    " +
                "\"email\": \"" + userInfo.getEmail() + "\",\n    \"password\": \"" + password + "\",\n    \"carMake\": \""
                + userInfo.getCarMake() + "\",\n    \"carModel\": \"" + userInfo.getCarModel() + "\",\n    \"carColor\": \""
                + userInfo.getCarColor() + "\"\n}");
        final Request request = new Request.Builder()
                .url("https://eagleride2019.herokuapp.com/createUser")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        Thread thread = new Thread(new Runnable(){
            public void run() {

                try {
                    okhttp3.Response response = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        });

        thread.start();
    }
}
