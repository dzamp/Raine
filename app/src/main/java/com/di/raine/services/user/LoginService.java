package com.di.raine.services.user;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class LoginService {

    private static final String url = "https://httpbin.org/get";
    private static final String loginUrl = "http://cello.jamwide.com/webserv/api/login";
    private String username;
    private String password;
    private SuccessListener successListener = new SuccessListener();
    private ErrorListener errorListener = new ErrorListener();

    public String getResponse() { return successListener.returnResponse(); }

    public VolleyError getError() { return errorListener.getErrorMessage(); }

    public StringRequest attemptLogin(String username, String password) {
        this.username = username;
        this.password = password;
        return new LoginRequest(Request.Method.POST,
                loginUrl, successListener, errorListener);
    }

    public static class SuccessListener implements Response.Listener<String> {
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

    public static class ErrorListener implements Response.ErrorListener {
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


    private class LoginRequest extends StringRequest {

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
    }









//
//    public static void sendGetRequest(Context appContext) {
//
//        RequestQueue queue = Volley.newRequestQueue(appContext);
//
//
//// Request a string response from the provided URL.
//        JsonObjectRequest ObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        // Display the first 500 characters of the response string.
//                        //System.out.println("Response is: " + response.substring(0, 500));
//                        Log.d("Debug", response.toString());
//                        try {
////                            JSONObject obj = response.getJSONObject("origin");
//                            String str = (String) response.get("origin");
//                            System.out.println(str);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                System.out.println("That didn't work!");
//            }
//        }) {
//
//            /**
//             * Passing some request headers
//             * */
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/json");
//                return headers;
//            }
//
//        };
//        queue.add(ObjRequest);
//    }

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
