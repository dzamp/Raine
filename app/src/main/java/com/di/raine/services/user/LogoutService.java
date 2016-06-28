package com.di.raine.services.user;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class LogoutService {

    private static final String loginUrl = "http://cello.jamwide.com/webserv/api/logout";
    private LoginService.SuccessListener successlistener = new LoginService.SuccessListener();
    private LoginService.ErrorListener errorListener = new LoginService.ErrorListener();

    public String getReponse() { return successlistener.returnResponse(); }

    public VolleyError getError() { return errorListener.getErrorMessage(); }

    private class LogoutRequest extends StringRequest {
        public LogoutRequest(int method, String url, Response.Listener<String> listener,
                             Response.ErrorListener errorListener) {
            super(method, url, listener, errorListener);
        }
    }

    public StringRequest attemptLogout() {
        return new LogoutRequest(Request.Method.POST,
                loginUrl, successlistener, errorListener);
    }


}
