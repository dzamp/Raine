package com.di.raine.services.product.requests;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.di.raine.services.Endpoint;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jim on 1/9/2016.
 */

public final class RequestCategories extends JsonArrayRequest {


    private final static String requestCategoriesEndpoint =  Endpoint.endpoint + "/webserv/api/v0/category/list";

    public RequestCategories( Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(requestCategoriesEndpoint, listener, errorListener);
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> pars = new HashMap<String, String>();
        pars.put("Accept", "application/xhtml+xml");
        pars.put("Accept", "application/json");
        return pars;
    }




}
