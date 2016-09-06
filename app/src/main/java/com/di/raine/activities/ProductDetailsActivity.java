package com.di.raine.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.di.raine.R;
import com.di.raine.branches.Branch;
import com.di.raine.branches.Locality;
import com.di.raine.branches.Point;
import com.di.raine.products.Desktop;
import com.di.raine.products.HomeCinema;
import com.di.raine.products.Laptop;
import com.di.raine.products.Product;
import com.di.raine.products.Sound;
import com.di.raine.products.Television;
import com.di.raine.services.NetworkService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailsActivity extends AppCompatActivity {
    private final static String TAG = ProductDetailsActivity.class.toString();
    private TextView descriptionInfo = null;
    private ArrayList<Branch> branches = new ArrayList<>();
    private NetworkService networkService;
    private boolean mBound = false;
    private Product product = null;
    private ListView gridview = null;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            NetworkService.NetworkBinder binder = (NetworkService.NetworkBinder) service;
            networkService = binder.getService();
            mBound = true;
            networkService.requestProductBranches(product.getId(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    JSONObject JsonBranch = null;
                    Branch branch = null;
                    try {
                        JSONObject JsonResponse = new JSONObject(response);
                        JsonBranch = JsonResponse.getJSONArray("data").getJSONObject(0).getJSONObject("branch");
                        branch = new Gson().fromJson(JsonBranch.toString(), Branch.class);
                        branches.add(branch);
                        branches.add(branch);
                        branches.add(new Branch("yolo", "5", new Locality("athens", "1221", "Stournari 12", new Point(31.3123, 43.3233))));
                        branches.add(new Branch("dadwa", "5", new Locality("athadwdawens", "1221", "Stournari 12", new Point(31.3123, 43.3233))));
                        gridview = (ListView) findViewById(R.id.listBranch_alt);
                        gridview.setAdapter(new ProductDetailsActivity.BranchesAdapter(getApplicationContext(), branches));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "Error in fetching branches of product");
                }
            });

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details_alt);
        Intent intent = getIntent();
        String object = intent.getStringExtra("product");
        String productType = intent.getStringExtra("productType");
        if (productType.contains("Laptop"))
            product = new Gson().fromJson(object, Laptop.class);
        else if (productType.contains("Desktop"))
            product = new Gson().fromJson(object, Desktop.class);
        else if (productType.contains("Sound"))
            product = new Gson().fromJson(object, Sound.class);
        else if (productType.contains("HomeCinema"))
            product = new Gson().fromJson(object, HomeCinema.class);
        else if (productType.contains("Television"))
            product = new Gson().fromJson(object, Television.class);

        TextView textView = (TextView) findViewById(R.id.product_label);
        textView.setText(product.getName());

        Intent in = new Intent(this, NetworkService.class);
        bindService(in, mConnection, Context.BIND_AUTO_CREATE);

        TextView descriptionText = (TextView) findViewById(R.id.product_description_text);
        descriptionText.setText(product.dataInfo().get("description"));
        ImageView image = (ImageView) findViewById(R.id.productDetailImage);
        System.out.println("Image id " + (intent.getIntExtra("productImage", 0)));
        image.setImageResource((intent.getIntExtra("productImage", 0)));
        descriptionInfo = (TextView) findViewById(R.id.product_description_text);
        descriptionInfo.setVisibility(View.GONE);
    }


    public void show_contents(View v) {
        descriptionInfo.setVisibility(descriptionInfo.isShown() ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, NetworkService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            networkService.sendLogoutRequest();
            unbindService(mConnection);
            mBound = false;
        }
    }





    private class BranchesAdapter extends ArrayAdapter<Branch> {
        private static final int PADDING = 8;
        private static final int WIDTH = 200;
        private static final int HEIGHT = 200;
        private Context mContext;
        private List<Branch> branches;

        public BranchesAdapter(Context mContext, List<Branch> mThumbIds) {
            super(mContext, R.layout.branch_list_item, mThumbIds);
            this.mContext = mContext;
            this.branches = mThumbIds;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View grid;
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // if convertView's not recycled, initialize some attributes
            if (convertView == null) {
                grid = new View(mContext);
                grid = inflater.inflate(R.layout.branch_list_item, null);
                TextView textView = (TextView) grid.findViewById(R.id.branch_text);
                Log.d(TAG, String.valueOf(position));
                textView.setText(branches.get(position).getName() + ", " + branches.get(position).getLocality().getAddress() + ", "
                        + branches.get(position).getLocality().getCity());
                textView.setTextColor(Color.BLACK);
            } else {
                grid = (View) convertView;
            }
            return grid;
        }
    }


}
