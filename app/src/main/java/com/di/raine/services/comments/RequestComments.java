package com.di.raine.services.comments;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.di.raine.services.Endpoint;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jim on 11/9/2016.
 */

public class RequestComments extends StringRequest{
    private final static String requestComments = Endpoint.endpoint + "/webserv/api/v0/comment/forBranch?branch=";
    private Map<String, String> mParams = new HashMap<>();
    public RequestComments(String branchId,   Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(requestComments+branchId, listener, errorListener);

    }
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
