package com.di.raine.services;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jim on 27/6/2016.
 */
public class LoginService {
    private static final String url = "https://httpbin.org/get";
    private static final String loginUrl = "http://cello.jamwide.com/webserv/api/login";


    public static void sendGetRequest(Context appContext) {

        RequestQueue queue = Volley.newRequestQueue(appContext);


// Request a string response from the provided URL.
        JsonObjectRequest ObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.
                        //System.out.println("Response is: " + response.substring(0, 500));
                        Log.d("Debug", response.toString());
                        try {
//                            JSONObject obj = response.getJSONObject("origin");
                            String str = (String) response.get("origin");
                            System.out.println(str);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("That didn't work!");
            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

        };
        queue.add(ObjRequest);
    }


    public static void attemptLogin(Context appContext) {
        RequestQueue queue = Volley.newRequestQueue(appContext);
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                loginUrl,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("attemptLogin didn't work");
            }
        }) {


            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                System.out.println("on getParams()");
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", "dimitris");
                params.put("password", "123456");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> pars = new HashMap<String, String>();
                System.out.println("on getHeaders()");
                pars.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                pars.put("Accept", "application/xhtml+xml");
                pars.put("Accept", "application/json");
//                pars.put("username", "dimitris");
//                pars.put("password", "123456");
                return pars;
            }
        };


        queue.add(jsonObjReq);


    }


    //            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("username", "dimitris");
//                params.put("password", "123456");
//
//                return params;
//            }
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                String credentials = "dimitris:123456";
//                String auth = "Basic "
//                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
//                headers.put("Content-Type", "application/json");
//                headers.put("Content-Type", "application/json");
//                headers.put("Authorization", auth);
//                return headers;
//            }



    //            @Override
//            public String getBodyContentType() {
//                return "application/x-www-form-urlencoded; charset=UTF-8";
//            }
//
//
//    @Override
//    protected String getParamsEncoding() {
//        System.out.println("on getParamsEncoding()");
//        return "utf-8";
//    }

}
