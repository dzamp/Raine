package com.di.raine.cartActivities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.di.raine.R;
import com.di.raine.adapters.ReviewAdapter;
import com.di.raine.branches.Comment;
import com.di.raine.cartHelper.ShoppingCartHelper;
import com.di.raine.services.NetworkService;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jim on 11/9/2016.
 */

public class ListiViewComments extends AppCompatActivity {
    List<Comment> comments;
    boolean mBound = false;
    private NetworkService networkService;
    private ReviewAdapter rReviewAdapter;
    private String shopId;
    private ServiceConnection mConnection = new ServiceConnection()

    {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            NetworkService.NetworkBinder binder = (NetworkService.NetworkBinder) service;
            networkService = binder.getService();
            mBound = true;

            networkService.requestComments(shopId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    JSONObject JsonResponse = null;
                    try {
                        JsonResponse = new JSONObject(response);
                        JSONArray jsonComments = JsonResponse.getJSONArray("data");
                        comments = new ArrayList<Comment>();
                        for (int i = 0; i < jsonComments.length(); i++) {
                            JSONObject jsonComment = jsonComments.getJSONObject(i);
                            Comment comm = new Gson().fromJson(jsonComment.toString(), Comment.class);
                            comm.setBranchId(jsonComment.getJSONObject("branch").getInt("id"));
                            comments.add(comm);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                   /* Set adapter and click listeners*/
                    final ListView listViewReviews = (ListView) findViewById(R.id.ListViewReviewsItems);
                    rReviewAdapter = new ReviewAdapter(comments, getLayoutInflater());
                    listViewReviews.setAdapter( rReviewAdapter);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("ERROR", error.getMessage());
                }
            });

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_commetns);
        shopId = getIntent().getStringExtra(ShoppingCartHelper.PRODUCT_INDEX);

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, NetworkService.class);
        startService(intent);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

    }

}
