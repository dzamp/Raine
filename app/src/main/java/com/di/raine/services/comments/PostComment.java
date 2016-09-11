package com.di.raine.services.comments;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.di.raine.services.Endpoint;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
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
    static {
        System.out.println( endpoint + "?branch="+1 +"&text="+"hello guys"+"&rating="+5);
    }
    private Map<String, String> params = new HashMap<String, String>();
//    private Map<String, String> headerParams = new HashMap<String, String>();

    public PostComment(URI endpoint, String branchId, String comment, int rating, Response.Listener<String> listener, Response.ErrorListener errorListener) throws UnsupportedEncodingException {
        super(URLEncoder.encode(endpoint.toString(),"UTF-8"), listener, errorListener);
        this.comment = comment;
        this.rating = rating;
        this.branchId = branchId;

    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> pars = new HashMap<String, String>();
        pars.put("Content-Type", "application/json; charset=utf-8");
        pars.put("Accept", "application/json");
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
}
