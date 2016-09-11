package com.di.raine.services.auth;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.di.raine.activities.LoginActivity;
import com.di.raine.services.Endpoint;

import java.util.HashMap;
import java.util.Map;


public class LoginService {


    public static final String loginUrl =  Endpoint.endpoint + "/webserv/api/login";
    private String username;
    private String password;
    private Context ctx;

    public StringRequest attemptLogin(Context ctx, String username, String password, Response.Listener successListener, Response.ErrorListener errorListener) {
        this.username = username;
        this.password = password;
        this.ctx = ctx;
        return new LoginRequest(Request.Method.POST,
                loginUrl, successListener, errorListener);
    }


    public class LoginRequest extends StringRequest {

        public LoginRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
            super(method, url, listener, errorListener);
        }


        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> pars = new HashMap<String, String>();
            pars.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            pars.put("Accept", "application/xhtml+xml");
            pars.put("Accept", "application/json");
            return pars;
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", username);
            params.put("password", password);
            return params;
        }
        
        @Override
        protected Response<String> parseNetworkResponse(NetworkResponse response) {
            // since we don't know which of the two underlying network vehicles
            // will Volley use, we have to handle and store session cookies manually

            LoginActivity.myapp.checkSessionCookie(response.headers);

            return super.parseNetworkResponse(response);
        }
    }





}
