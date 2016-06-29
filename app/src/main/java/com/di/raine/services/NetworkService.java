package com.di.raine.services;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.di.raine.services.user.LoginService;
import com.di.raine.services.user.LogoutService;

public class NetworkService extends Service {
    private final static String TAG = "NetworkService";
    public boolean loggedIn = false;
    private RequestQueue mRequestQueue;
    private static Context mContext;
    private ImageLoader mImageLoader;
    private static final int cacheSize = 10 * 1024 * 1024;
    private final IBinder mBinder = new NetworkBinder();


    public class NetworkBinder extends Binder {
        public NetworkService getService() {
            // Return this instance of LocalService so clients can call public methods
            return NetworkService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mRequestQueue = getRequestQueue();
        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(cacheSize);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    public void sendAnotherRequest(Response.Listener<String> successListener, Response.ErrorListener errorListener) {
        LoginService ser = new LoginService();
        StringRequest req = ser.new LoginRequest(Request.Method.POST, LoginService.loginUrl, successListener, errorListener);
        mRequestQueue.add(req);
    }


    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
            mRequestQueue.start();
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void sendLoginRequest(String username, String password,
                                 Response.Listener successListener, Response.ErrorListener errorListener) {
        LoginService loginService = new LoginService();
        StringRequest req = loginService.attemptLogin(username, password, successListener, errorListener);
        getRequestQueue().add(req);
    }

    public void sendLogoutRequest() {
        if (loggedIn) {
            LogoutService logoutService = new LogoutService();
            StringRequest req = logoutService.attemptLogout();
            getRequestQueue().add(req);
        }

    }
}
