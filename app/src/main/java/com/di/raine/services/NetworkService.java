package com.di.raine.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class NetworkService extends Service {
    private Context mContext;
    private final static String TAG = "NetworkService";
    private String intentTAG;
    private NetworkManager networkManager;
    public boolean loggedIn = false;


    private final IBinder mBinder = new NetworkBinder();

    public class NetworkBinder extends Binder {
        public NetworkService getService() {
            // Return this instance of LocalService so clients can call public methods
            return NetworkService.this;
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        networkManager = NetworkManager.getInstance(getApplicationContext());
        networkManager.getRequestQueue();
        return mBinder;
    }

    public String sendLoginRequest(String username, String password) {
        loggedIn = true;
        //TODO network manager must check if logged in
        return networkManager.sendLoginRequest(username,password);
    }

    public String sendLogoutRequest() {
        if(loggedIn)
        return networkManager.sendLogoutRequest();
        return "";
    }

}
