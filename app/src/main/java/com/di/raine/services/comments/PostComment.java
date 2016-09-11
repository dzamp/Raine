package com.di.raine.services.comments;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.di.raine.activities.LoginActivity;
import com.di.raine.services.Endpoint;
import com.google.android.gms.appdatasearch.GetRecentContextCall;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jim on 11/9/2016.
 */

public class PostComment extends StringRequest {
    private final static String endpoint = Endpoint.endpoint + "/webserv/api/v0/comment/add";
    private String comment="Nice store!";
    private int rating = 0;
    private String  branchId;

    private Map<String, String> params = new HashMap<String, String>();
//    private Map<String, String> headerParams = new HashMap<String, String>();

    public PostComment( String branchId, String comment, int rating, Response.Listener<String> listener, Response.ErrorListener errorListener) throws UnsupportedEncodingException {
        super(Method.POST,endpoint, listener, errorListener);
        this.comment = comment;
        this.rating = rating;
        this.branchId = branchId;

    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> pars = new HashMap<String, String>();
        pars.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        pars.put("Accept", "application/xhtml+xml");
        pars.put("Accept", "application/json");
        LoginActivity.get().addSessionCookie(pars);
        return pars;
    }
//
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        params.put("branch", this.branchId);
        params.put("text", this.comment);
        params.put("rating",String.valueOf(this.rating));
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
