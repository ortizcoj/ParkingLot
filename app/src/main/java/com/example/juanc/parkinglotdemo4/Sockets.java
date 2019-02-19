package com.example.juanc.parkinglotdemo4;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class Sockets implements Parcelable{

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("https://eagleride2019.herokuapp.com/rideSharing");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Parcelable.Creator<Sockets> CREATOR
            = new Parcelable.Creator<Sockets>() {
        public Sockets createFromParcel(Parcel in) {
            return new Sockets();
        }

        public Sockets[] newArray(int size) {
            return new Sockets[size];
        }
    };

}
