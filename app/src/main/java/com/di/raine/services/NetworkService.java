package com.di.raine.services;

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
import com.di.raine.services.auth.LoginService;
import com.di.raine.services.auth.LogoutService;
import com.di.raine.services.comments.PostComment;
import com.di.raine.services.comments.RequestComments;
import com.di.raine.services.product.requests.RequestCategories;
import com.di.raine.services.product.requests.RequestSpecificCategory;
import com.di.raine.services.product.requests.RequestSpecificProductBranches;
import com.di.raine.services.product.requests.SearchRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

public class NetworkService extends Service {
    private final static String TAG = "NetworkService";
    private static final int cacheSize = 10 * 1024 * 1024;
    private static Context mContext;
    private final IBinder mBinder = new NetworkBinder();
    public boolean loggedIn = false;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private String username, password;

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
        this.password = password;
        this.username = username;
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
        RequestCategories requestCategories = new RequestCategories(onSuccessListener, onErrorListener);
        getRequestQueue().add(requestCategories);
    }

    public void getCategory(int id, Response.Listener<String> listener, Response.ErrorListener onErrorListener) {
        RequestSpecificCategory getCategory = new RequestSpecificCategory(id, listener, onErrorListener);
        getRequestQueue().add(getCategory);
    }

    public void searchProducts(String query, Response.Listener<String> listener, Response.ErrorListener onErrorListener){
        SearchRequest searchRequest = new SearchRequest(query,  listener, onErrorListener);
        getRequestQueue().add(searchRequest);
    }


    public void requestProductBranches (String id, Response.Listener<String> listener, Response.ErrorListener onErrorListener){
        RequestSpecificProductBranches requestSpecificProductBranches= new RequestSpecificProductBranches(id,listener,onErrorListener);
        getRequestQueue().add(requestSpecificProductBranches);
    }

    public void requestComments ( String branchid, Response.Listener<String> listener, Response.ErrorListener errorListener ){
        getRequestQueue().add(new RequestComments(branchid,listener,errorListener));
    }

    public void postComment(String branchId, String comment, int rating,  Response.Listener<String> listener, Response.ErrorListener errorListener){
        comment= comment.replaceAll(" ","%20");


        URI url  = null;
        try {
            url =  new URI( Endpoint.endpoint+ "/webserv/api/v0/comment/add"+"?branch="+branchId +"&text="+comment+"&rating="+rating );
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        try {
            getRequestQueue().add(new PostComment(url,branchId,comment,rating, listener,errorListener));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public class NetworkBinder extends Binder {
        public NetworkService getService() {
            // Return this instance of LocalService so clients can call public methods
            return NetworkService.this;
        }
    }


}