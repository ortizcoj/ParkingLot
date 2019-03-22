package com.example.juanc.parkinglotdemo4.Map;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.juanc.parkinglotdemo4.MapSocket;
import com.example.juanc.parkinglotdemo4.R;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

public class LotDisplay extends AppCompatActivity {

    private int numberOfSpots = 10;
    private Spot[] spots = new Spot[numberOfSpots];
    private Socket realSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        populateSpotsArray();
        setAllSpotsAvailable();

        createSocket();

        realSocket.emit("get_map_state");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void populateSpotsArray(){
        Resources resources = getResources();
        String packageName = getPackageName();
        for(int i = 0; i < spots.length; i++){
            int id = resources.getIdentifier("spot" + i, "id", packageName);
            Spot spot = findViewById(id);
            spots[i] = spot;
        }
    }

    public void setAllSpotsAvailable(){
        for(Spot spot: spots){
            spot.setVisibility(View.INVISIBLE);
        }
    }

    private void createSocket(){
        MapSocket socket = new MapSocket();
        realSocket = socket.getSocket();
        realSocket.on("spot_status", onNewMessage1);
        realSocket.on("map_state", onNewMessage);
        realSocket.connect();
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

                        String spot = ((String) args[0]).split(":")[0];
                        String occupied = ((String) args[0]).split(":")[1];

                        if (occupied.toLowerCase().equals("true")){
                            spots[Integer.valueOf(spot)].setVisibility(View.VISIBLE);
                        } else if (occupied.toLowerCase().equals("false")){
                            spots[Integer.valueOf(spot)].setVisibility(View.INVISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

                        String[] spot = ((String) args[0]).split(",");

                        for (String aSpot : spot) {
                            int num = Integer.valueOf(aSpot.split(":")[0]);
                            String occupied = aSpot.split(":")[1];

                            if (occupied.toLowerCase().equals("true")) {
                                spots[num].setVisibility(View.VISIBLE);
                            } else if (occupied.toLowerCase().equals("false")) {
                                spots[num].setVisibility(View.INVISIBLE);
                            }

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
