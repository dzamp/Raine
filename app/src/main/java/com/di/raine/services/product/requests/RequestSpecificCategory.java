package com.di.raine.services.product.requests;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jim on 1/9/2016.
 */

public class RequestSpecificCategory extends StringRequest {
    int id ;
    private static final String requestCategoryEndpoint = "http://cello.jamwide.com/webserv/api/v0/product/list?category=";
    private Map<String, String> mParams= new HashMap<>();

    public RequestSpecificCategory(int id, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super( "http://cello.jamwide.com/webserv/api/v0/product/list?category=" + id,  listener, errorListener);
        this.id = id;
        mParams.put("category",Integer.toString(id));
    }


    @Override
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
