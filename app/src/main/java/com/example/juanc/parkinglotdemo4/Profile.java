package com.example.juanc.parkinglotdemo4;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.juanc.parkinglotdemo4.Map.LotDisplay;
import com.example.juanc.parkinglotdemo4.Network.Networking;
import com.example.juanc.parkinglotdemo4.Network.User;
import com.example.juanc.parkinglotdemo4.RegisterLogin.Menu;
import com.example.juanc.parkinglotdemo4.Request.RiderDriverRequest;
import com.example.juanc.parkinglotdemo4.Security.Hashing;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;

public class Profile extends AppCompatActivity {

    private ImageView mImageView;
    private ImageView citationLot;
    private TextView email;
    private TextView profileText;
    private TextView name;
    private TextView brand;
    private TextView model;
    private TextView color;
    private Button updateButton;
    private EditText emailField;
    private EditText nameET;
    private EditText brandET;
    private EditText modelET;
    private EditText colorET;
    private Button confirm;
    private String password = "";
    private Button changePasswordButton;
    private Button logout;
    private String tkn = "";
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

    private boolean setUpRideMainUI() {
        mImageView.setVisibility(View.GONE);
        citationLot.setVisibility(View.GONE);
        emailField.setVisibility(View.VISIBLE);
        email.setVisibility(View.VISIBLE);
        name.setVisibility(View.VISIBLE);
        nameET.setVisibility(View.VISIBLE);
        brand.setVisibility(View.VISIBLE);
        brandET.setVisibility(View.VISIBLE);
        model.setVisibility(View.VISIBLE);
        modelET.setVisibility(View.VISIBLE);
        color.setVisibility(View.VISIBLE);
        colorET.setVisibility(View.VISIBLE);
        profileText.setVisibility(View.VISIBLE);
        Bundle extras = getIntent().getExtras();
        emailField.setText(extras.getString("Email"));
        nameET.setText(extras.getString("Name"));
        colorET.setText(extras.getString("carColor"));
        brandET.setText(extras.getString("carMake"));
        modelET.setText(extras.getString("carModel"));
        disableButtons();
        changePasswordButton.setVisibility(View.VISIBLE);
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
        logout.setVisibility(View.VISIBLE);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Menu.class);
                intent.putExtra("Registration", "Logged out");
                startActivity(intent);
            }
        });
        updateButton.setVisibility(View.VISIBLE);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmUpdateMethod();
            }
        });
        return true;
    }

    private void changePassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        builder.setTitle("Change Password");

        final TextView old1 = new TextView(this);
        old1.setText(R.string.EnterPassword);
        final TextView old2 = new TextView(this);
        old2.setText(R.string.ConfirmPassword);
        final TextView new1 = new TextView(this);
        new1.setText(R.string.NewPassword);
        final EditText inputOld = new EditText(this);
        final EditText inputOld2 = new EditText(this);
        final EditText inputNew = new EditText(this);

        inputOld.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(old1);
        layout.addView(inputOld);
        inputOld2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(old2);
        layout.addView(inputOld2);
        inputNew.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(new1);
        layout.addView(inputNew);

        builder.setView(layout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String oldPassword1 = inputOld.getText().toString();
                String oldPassword2 = inputOld2.getText().toString();
                try {
                    Hashing security = new Hashing();
                    if (oldPassword1.equals(oldPassword2) && security.hash(oldPassword1).equals(password)){
                        password = inputNew.getText().toString();
                        updatePassword();

                        Toast.makeText(getApplicationContext(), "Password Updated", Toast.LENGTH_LONG).show();
                    }
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void updatePassword() {
        Networking networking = new Networking();
        Hashing security = new Hashing();
        try {
            networking.updatePassword(emailField.getText().toString(), security.hash(password), tkn);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void confirmUpdateMethod() {
        nameET.setEnabled(true);
        brandET.setEnabled(true);
        modelET.setEnabled(true);
        colorET.setEnabled(true);
        confirm.setVisibility(View.VISIBLE);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });
    }

    private void updateUser() {
        User userInfo = new User(nameET.getText().toString(), emailField.getText().toString(), password, brandET.getText().toString(), modelET.getText().toString(), colorET.getText().toString());

        Networking networking = new Networking();
        networking.updateUser(userInfo, tkn);

        emailField.setText(userInfo.getEmail());
        nameET.setText(userInfo.getName());
        colorET.setText(userInfo.getCarColor());
        brandET.setText(userInfo.getCarMake());
        modelET.setText(userInfo.getCarModel());

        disableButtons();
        confirm.setVisibility(View.GONE);

        Toast.makeText(getApplicationContext(), "User updated", Toast.LENGTH_LONG).show();
    }

    private boolean setUpMapMainUI() {
        emailField.setVisibility(View.GONE);
        email.setVisibility(View.GONE);
        name.setVisibility(View.GONE);
        nameET.setVisibility(View.GONE);
        brand.setVisibility(View.GONE);
        brandET.setVisibility(View.GONE);
        model.setVisibility(View.GONE);
        modelET.setVisibility(View.GONE);
        color.setVisibility(View.GONE);
        colorET.setVisibility(View.GONE);
        updateButton.setVisibility(View.GONE);
        profileText.setVisibility(View.GONE);
        confirm.setVisibility(View.GONE);
        changePasswordButton.setVisibility(View.GONE);
        logout.setVisibility(View.GONE);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);
        Bundle extras = getIntent().getExtras();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mImageView = findViewById(R.id.map);
        mImageView.setImageResource(R.drawable.map);
        mImageView.setVisibility(View.INVISIBLE);
        citationLot = findViewById(R.id.citation);
        citationLot.setVisibility(View.INVISIBLE);
        email = findViewById(R.id.emailTV);
        emailField = findViewById(R.id.email);
        profileText = findViewById(R.id.profile);
        profileText.setTypeface(null, Typeface.BOLD);
        name = findViewById(R.id.nameTV);
        nameET = findViewById(R.id.name);
        brand = findViewById(R.id.brandTV);
        brandET = findViewById(R.id.brandET);
        model = findViewById(R.id.modelTV);
        modelET = findViewById(R.id.modelET);
        color = findViewById(R.id.colorTV);
        colorET = findViewById(R.id.colorET);
        confirm = findViewById(R.id.confirmButton);
        confirm.setVisibility(View.INVISIBLE);
        emailField.setText(extras.getString("Email"));
        nameET.setText(extras.getString("Name"));
        colorET.setText(extras.getString("carColor"));
        brandET.setText(extras.getString("carMake"));
        modelET.setText(extras.getString("carModel"));
        password = extras.getString("Password");
        tkn = extras.getString("token");
        disableButtons();
        changePasswordButton = findViewById(R.id.changePasswordButton);
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
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
        updateButton = findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmUpdateMethod();
            }
        });

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

    private void disableButtons() {
        emailField.setEnabled(false);
        nameET.setEnabled(false);
        brandET.setEnabled(false);
        modelET.setEnabled(false);
        colorET.setEnabled(false);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), RiderDriverRequest.class);

        Hashing security = new Hashing();
        try {
            intent.putExtra("carColor", colorET.getText().toString());
            intent.putExtra("carMake", brandET.getText().toString());
            intent.putExtra("carModel", modelET.getText().toString());
            intent.putExtra("Name", nameET.getText().toString());
            intent.putExtra("Password", security.hash(password));
            intent.putExtra("Email", emailField.getText().toString());
            intent.putExtra("token", tkn);
            startActivity(intent);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}