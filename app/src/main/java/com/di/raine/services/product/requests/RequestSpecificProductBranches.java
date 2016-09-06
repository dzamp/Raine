package com.di.raine.services.product.requests;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jim on 5/9/2016.
 */

public class RequestSpecificProductBranches extends StringRequest {
    private static final String requestBranchOfProductEndpoint = "http://cello.jamwide.com/webserv/api/v0/product/find?product=";
    String id;
    private Map<String, String> mParams = new HashMap<>();

    public RequestSpecificProductBranches(String id, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(requestBranchOfProductEndpoint + id, listener, errorListener);
        this.id = id;
        mParams.put("product", id);
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
