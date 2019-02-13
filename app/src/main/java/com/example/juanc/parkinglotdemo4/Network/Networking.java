package com.example.juanc.parkinglotdemo4.Network;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.juanc.parkinglotdemo4.RegisterLogin.Menu;
import com.example.juanc.parkinglotdemo4.Request.RiderDriverRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Networking {
    public void registerUser(User userInfo, final Context context) {

        final OkHttpClient client = new OkHttpClient();

        userInfo.setPassword("[B@55082b1".getBytes());

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
                    String jsonData = response.body().string();
                    if (jsonData.equals("false\n")){
                        Intent intent = new Intent(context, Menu.class);
                        intent.putExtra("Registration", "There is an existing user with that email address");
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, Menu.class);
                        intent.putExtra("Registration", "New user created");
                        context.startActivity(intent);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        });

        thread.start();
    }

    public void loginUser(final LoginInfo loginInfo, final Context context) {

        final OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
//        RequestBody body = RequestBody.create(mediaType, "{\n    \"email\": \"" + loginInfo.getEmail() + "\",\n  " +
//                        "  \"password\": \"" + loginInfo.getPassword().toString() + "\"\n}");

        RequestBody body = RequestBody.create(mediaType, "{\n    \"email\": \"" + loginInfo.getEmail() + "\",\n  " +
                "  \"password\": \"" + "[B@55082b1" + "\"\n}");

        final Request request = new Request.Builder()
                .url("https://eagleride2019.herokuapp.com/loginUser")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        Thread thread = new Thread(new Runnable(){
            public void run() {

                try {
                    okhttp3.Response response = client.newCall(request).execute();
                    String jsonData = response.body().string();
                    if (jsonData.equals("false\n")){
                        Intent intent = new Intent(context, Menu.class);
                        intent.putExtra("Registration", "Invalid password or email");
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, RiderDriverRequest.class);
                        intent.putExtra("carColor", jsonData.split("\"")[3]);
                        intent.putExtra("carMake", jsonData.split("\"")[7]);
                        intent.putExtra("carModel", jsonData.split("\"")[11]);
                        intent.putExtra("Name", jsonData.split("\"")[15]);
                        intent.putExtra("Email", loginInfo.getEmail());
                        intent.putExtra("Password", loginInfo.getPassword().toString());
                        intent.putExtra("Registration", "Welcome");
                        context.startActivity(intent);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        });

        thread.start();
    }

    public void updateUser(User userInfo, final Context context) {
        final OkHttpClient client = new OkHttpClient();

        userInfo.setPassword("[B@55082b1".getBytes());

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n    \"name\": \"" + userInfo.getName() + "\",\n    " +
                "\"email\": \"" + userInfo.getEmail() + "\",\n    \"password\": \"" + userInfo.getPassword().toString()
                + "\",\n    \"carMake\": \"" + userInfo.getCarMake() + "\",\n    \"carModel\": \"" + userInfo.getCarModel()
                + "\",\n    \"carColor\": \"" + userInfo.getCarColor() + "\"\n}");

        final Request request = new Request.Builder()
                .url("https://eagleride2019.herokuapp.com/updateUser")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        Thread thread = new Thread(new Runnable(){
            public void run() {

                try {
                    okhttp3.Response response = client.newCall(request).execute();
                    String jsonData = response.body().string();
                    if (jsonData.equals("false\n")){
                        Intent intent = new Intent(context, Menu.class);
                        intent.putExtra("Registration", "There is an existing user with that email address");
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, Menu.class);
                        intent.putExtra("Registration", "New user created");
                        context.startActivity(intent);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        });

        thread.start();
    }
}
