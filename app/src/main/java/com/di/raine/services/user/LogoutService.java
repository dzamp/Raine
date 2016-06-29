package com.di.raine.services.user;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


public class LogoutService {

    private static final String loginUrl = "http://cello.jamwide.com/webserv/api/logout";
    private SuccessListener successlistener = new SuccessListener();
    private ErrorListener errorListener = new ErrorListener();
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

    public  class SuccessListener implements Response.Listener<String> {
        private String response;

        public String returnResponse() {
            return this.response;
        }

        @Override
        public void onResponse(String response) {
            //TODO better handling
            this.response = response;
            System.out.println(response);
        }
    }

    public  class ErrorListener implements Response.ErrorListener {
        private VolleyError error;

        public VolleyError getErrorMessage() {
            return error;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            this.error = error;
            //TODO better handling
            System.out.println("attemptLogin didn't work");
            System.out.println(error.networkResponse.headers);
        }
    }
}
