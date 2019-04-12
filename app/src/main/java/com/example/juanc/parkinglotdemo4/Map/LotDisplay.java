package com.example.juanc.parkinglotdemo4.Map;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.example.juanc.parkinglotdemo4.MapSocket;
import com.example.juanc.parkinglotdemo4.R;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

public class LotDisplay extends AppCompatActivity {
    private Socket realSocket;
    private LinearLayout layout;
    private int imgNumber = 0;
    private boolean spot1 = false;
    private boolean spot2 = false;
    private boolean spot3 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        layout = findViewById(R.id.map);
        layout.setBackgroundResource(R.drawable.parkingmap);

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

                        if (occupied.toLowerCase().equals("true")) {
                            if (Integer.valueOf(spot) == 2 && !spot3){
                                imgNumber += 4;
                                spot3 = true;
                            }
                            else if (Integer.valueOf(spot) == 1 && !spot2){
                                imgNumber += 2;
                                spot2 = true;
                            } else if (Integer.valueOf(spot) == 0 && !spot1){
                                imgNumber += 1;
                                spot1 = true;
                            }
                        } else if (occupied.toLowerCase().equals("false")) {
                            if (Integer.valueOf(spot) == 2 && spot3){
                                imgNumber -= 4;
                                spot3 = false;
                            }
                            else if (Integer.valueOf(spot) == 1 && spot2){
                                imgNumber -= 2;
                                spot2 = false;
                            } else if (Integer.valueOf(spot) == 0 && spot1){
                                imgNumber -= 1;
                                spot1 = false;
                            }
                        }

                        switch (imgNumber){
                            case 0:{
                                layout.setBackgroundResource(R.drawable.parkingmap);
                                break;
                            }case 1:{
                                layout.setBackgroundResource(R.drawable.parkingmap1);
                                break;
                            } case 2:{
                                layout.setBackgroundResource(R.drawable.parkingmap2);
                                break;
                            }case 3:{
                                layout.setBackgroundResource(R.drawable.parkingmap3);
                                break;
                            } case 4:{
                                layout.setBackgroundResource(R.drawable.parkingmap4);
                                break;
                            }case 5:{
                                layout.setBackgroundResource(R.drawable.parkingmap5);
                                break;
                            } case 6:{
                                layout.setBackgroundResource(R.drawable.parkingmap6);
                                break;
                            }case 7:{
                                layout.setBackgroundResource(R.drawable.parkingmap7);
                                break;
                            }
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
                                if (num == 2){
                                    imgNumber += 4;
                                    spot3 = true;
                                }
                                else if (num == 1){
                                    imgNumber += 2;
                                    spot2 = true;
                                } else if (num == 0){
                                    imgNumber += 1;
                                    spot1 = true;

                                }
                            } else if (occupied.toLowerCase().equals("false")) {
                            }

                        }

                        switch (imgNumber){
                            case 0:{
                                layout.setBackgroundResource(R.drawable.parkingmap);
                                break;
                            }case 1:{
                                layout.setBackgroundResource(R.drawable.parkingmap1);
                                break;
                            } case 2:{
                                layout.setBackgroundResource(R.drawable.parkingmap2);
                                break;
                            }case 3:{
                                layout.setBackgroundResource(R.drawable.parkingmap3);
                                break;
                            } case 4:{
                                layout.setBackgroundResource(R.drawable.parkingmap4);
                                break;
                            }case 5:{
                                layout.setBackgroundResource(R.drawable.parkingmap5);
                                break;
                            } case 6:{
                                layout.setBackgroundResource(R.drawable.parkingmap6);
                                break;
                            }case 7:{
                                layout.setBackgroundResource(R.drawable.parkingmap7);
                                break;
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
        realSocket.disconnect();
        super.onBackPressed();
    }
}
