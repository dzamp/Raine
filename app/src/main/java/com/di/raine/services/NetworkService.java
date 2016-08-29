package com.di.raine.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.LruCache;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.di.raine.services.auth.LoginService;
import com.di.raine.services.auth.LogoutService;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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

    public void requestCategories(Response.Listener<JSONArray> onSuccessListener, Response.ErrorListener onErrorListener) {
        RequestCategories requestCategories = new RequestCategories("http://cello.jamwide.com/webserv/api/v0/category/list", onSuccessListener, onErrorListener);
        getRequestQueue().add(requestCategories);
    }


    public final class RequestCategories extends JsonArrayRequest {

        public RequestCategories(String url, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
            super(url, listener, errorListener);
        }


        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> pars = new HashMap<String, String>();
            pars.put("Accept", "application/xhtml+xml");
            pars.put("Accept", "application/json");
            return pars;
        }


    }
    public void getCategory(int id, Response.Listener<String> listener, Response.ErrorListener onErrorListener){
        System.out.println(id);
        GetCategory getCategory= new GetCategory("http://cello.jamwide.com/webserv/api/v0/product/list?category="+id,
                listener, onErrorListener);
        getRequestQueue().add(getCategory);


    }
    public final class GetCategory extends StringRequest {
        int id ;
        private Map<String, String> mParams= new HashMap<>();

        public GetCategory( String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
            super( url,  listener, errorListener);
            this.id = id;
            mParams.put("category",Integer.toString(id));
        }


        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> pars = new HashMap<String, String>();
            pars.put("Accept", "application/xhtml+xml");
            pars.put("Accept", "application/json");
            return pars;
        }


        @Override
        public Map<String, String> getParams() {
            return mParams;
        }
    }

}