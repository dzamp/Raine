package com.di.raine.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.di.raine.services.user.LoginService;
import com.di.raine.services.user.LogoutService;

public class NetworkManager {

    private static NetworkManager rManager;
    private RequestQueue mRequestQueue;
    private static Context mContext;
    private ImageLoader mImageLoader;
    private static final int cacheSize = 10 * 1024 * 1024;

    private NetworkManager(Context context) {
        mContext = context;
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

    public static synchronized NetworkManager getInstance(Context context) {
        if (rManager == null) {
            rManager = new NetworkManager(context);
        }
        return rManager;
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

    public String  sendLoginRequest(String username, String password) {
        LoginService loginService =  new LoginService();
        StringRequest req =  loginService.attemptLogin(username,password);
        getRequestQueue().add(req);
        String response  = loginService.getResponse();
        return response;
        //TODO create and populate  user model here
        //TODO change function return value to model
    }

    public String sendLogoutRequest(){
        LogoutService logoutService = new LogoutService();
        StringRequest req =  logoutService.attemptLogout();
        getRequestQueue().add(req);
        String response =  logoutService.getReponse();
        return response;
    }
}
