package com.example.juanc.parkinglotdemo4;

import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Networking {
    public void registerUser(User userInfo) {
        Log.e("eagleride", "about to send stuff");

        final OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n    \"name\": \"" + userInfo.getName() + "\",\n    " +
                "\"email\": \"" + userInfo.getEmail() + "\",\n    \"password\": \"" + userInfo.getPassword().toString()
                + "\",\n    \"carMake\": \"" + userInfo.getCarMake() + "\",\n    \"carModel\": \"" + userInfo.getCarModel()
                + "\",\n    \"carColor\": \"" + userInfo.getCarColor() + "\"\n}");

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
