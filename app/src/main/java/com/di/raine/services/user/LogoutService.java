package com.di.raine.services.user;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jim on 27/6/2016.
 */
public class LogoutService {

    private static final String loginUrl = "http://cello.jamwide.com/webserv/api/logout";

    private class LoginListener implements Response.Listener<String> {
        @Override
        public void onResponse(String response) {
//            TODO
            System.out.println(response);
        }
    }

    private class LoginResponseListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
//            TODO
            System.out.println("attemptLogin didn't work");
        }
    }


    private class LogoutRequest extends StringRequest {

        public LogoutRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
            super(method, url, listener, errorListener);
        }



    }

    public void attemptLogout(Context appContext) {
        LoginListener listener = new LoginListener();
        LoginResponseListener loginResponseListener = new LoginResponseListener();
        RequestQueue queue = Volley.newRequestQueue(appContext);
        StringRequest jsonObjReq = new LogoutRequest(Request.Method.POST,
                loginUrl, listener, loginResponseListener);
        queue.add(jsonObjReq);


    }


}
