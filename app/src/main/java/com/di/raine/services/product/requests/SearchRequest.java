package com.di.raine.services.product.requests;

import android.support.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.di.raine.products.Desktop;
import com.di.raine.products.HomeCinema;
import com.di.raine.products.Laptop;
import com.di.raine.products.Product;
import com.di.raine.products.Sound;
import com.di.raine.products.Television;
import com.di.raine.services.Endpoint;

/**
 * Created by jim on 1/9/2016.
 */

public class SearchRequest extends StringRequest {

    private final static String searchEndpoint = Endpoint.endpoint + "/webserv/api/v0/product/search?query=";

    public SearchRequest( String query , Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, searchEndpoint+query, listener, errorListener);
    }
    

}
