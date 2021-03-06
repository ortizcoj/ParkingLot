package com.example.juanc.parkinglotdemo4.Network;

import android.content.Context;
import android.content.Intent;
import android.util.Base64;

import com.example.juanc.parkinglotdemo4.RegisterLogin.Menu;
import com.example.juanc.parkinglotdemo4.Request.RiderDriverRequest;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Networking {
    public void registerUser(User userInfo, final Context context) {

        final OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n    \"name\": \"" + userInfo.getName() + "\",\n    " +
                "\"email\": \"" + userInfo.getEmail() + "\",\n    \"password\": \"" + userInfo.getPassword()
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
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, Menu.class);
                        intent.putExtra("Registration", "New user created");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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

        String string = loginInfo.getEmail() + ":" + loginInfo.getPassword();
        final String basicAuth = "Basic " + Base64.encodeToString(string.getBytes(), Base64.NO_WRAP);

        final Request request = new Request.Builder()
                .url("https://eagleride2019.herokuapp.com/loginUser")
                .get()
                .addHeader("Authorization", basicAuth)
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
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, RiderDriverRequest.class);
                        intent.putExtra("carColor", jsonData.split(",")[0].split(":")[1].split("\"")[1]);
                        intent.putExtra("carMake", jsonData.split(",")[1].split(":")[1].split("\"")[1]);
                        intent.putExtra("carModel", jsonData.split(",")[2].split(":")[1].split("\"")[1]);
                        intent.putExtra("Name", jsonData.split(",")[3].split(":")[1].split("\"")[1]);
                        intent.putExtra("Email", loginInfo.getEmail());
                        intent.putExtra("Password", loginInfo.getPassword());
                        intent.putExtra("Registration", "Welcome");
                        intent.putExtra("token", jsonData.split(",")[4].split(":")[1].split("\"")[1]);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        });

        thread.start();
    }

    public void updateUser(User userInfo, String auth) {
        final OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        String data = "{\"name\":\"" + userInfo.getName() + "\", \"carModel\":\"" +
                userInfo.getCarModel() + "\", \"carColor\":\"" + userInfo.getCarColor() + "\", \"carMake\":\"" + userInfo.getCarMake() + "\"}";
        RequestBody body1 = RequestBody.create(mediaType, data);

        final Request request = new Request.Builder()
                .url("https://eagleride2019.herokuapp.com/updateUser")
                .addHeader("Content-Type", "application/json")
                .addHeader("x-access-token", auth)
                .post(body1)
                .build();

        Thread thread = new Thread(new Runnable(){
            public void run() {

                try {
                    okhttp3.Response response = client.newCall(request).execute();
                    String jsonData = response.body().string();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        });

        thread.start();
    }

    public void updatePassword(String email, String password, String auth) {
        final OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");

        RequestBody body = RequestBody.create(mediaType, "{\"password\": \"" + password+ "\"\n}");

        final Request request = new Request.Builder()
                .url("https://eagleride2019.herokuapp.com/updatePassword")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("x-access-token", auth)
                .build();

        final Thread thread = new Thread(new Runnable(){
            public void run() {

                try {
                    okhttp3.Response response = client.newCall(request).execute();
                    String jsonData = response.body().string();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        });

        thread.start();
    }
}
