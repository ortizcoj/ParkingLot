package com.example.juanc.parkinglotdemo4.Map;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.juanc.parkinglotdemo4.R;

public class LotDisplay extends AppCompatActivity {

    private int numberOfSpots = 10;
    private Spot[] spots = new Spot[numberOfSpots];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        populateSpotsArray();
        setAllSpotsAvailable();
//        spots[5].setVisibility(View.VISIBLE);
//        spots[2].setVisibility(View.VISIBLE);
        spots[0].setVisibility(View.VISIBLE);
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
}
