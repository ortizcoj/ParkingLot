package com.example.juanc.parkinglotdemo4;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class Sockets {

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

}
