package com.di.raine.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.di.raine.R;
import com.di.raine.products.Desktop;
import com.di.raine.products.HomeCinema;
import com.di.raine.products.Laptop;
import com.di.raine.products.Product;
import com.di.raine.products.Sound;
import com.di.raine.products.Television;
import com.di.raine.services.NetworkService;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ProductsActivity extends AppCompatActivity {

    private static final String TAG = "ProductsActivity.class";
    private NetworkService networkService;
    private boolean mBound;
    private Menu menu;
    private ArrayList<Pair<Product, Integer>> products;



    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            NetworkService.NetworkBinder binder = (NetworkService.NetworkBinder) service;
            networkService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        Intent intent = getIntent();
        products = populateProductList(intent);
        ListView gridview = (ListView) findViewById(R.id.listView);
        gridview.setAdapter(new ImageAdapter(this, products));

    }

    private ArrayList<Pair<Product, Integer>> populateProductList(Intent intent) {
        String productType = intent.getStringExtra("productType");
        products =new ArrayList<>();

        String productString = intent.getStringExtra("products");
        JSONArray prod = null;
        try {
            prod = new JSONArray(productString);
            for (int i = 0; i < prod.length(); i++) {
                Gson son = new Gson();
                JSONObject json = prod.getJSONObject(i);
                if (productType.contains("Laptop")) {
                    products.add(new Pair(son.fromJson(json.toString(), Laptop.class), R.drawable.laptop));
                } else if (productType.contains("Desktop")) {
                    products.add(new Pair(son.fromJson(json.toString(), Laptop.class), R.mipmap.pc));
                } else if (productType.contains("Sound")) {
                    products.add(new Pair(son.fromJson(json.toString(), Laptop.class), R.drawable.sound));
                } else if (productType.contains("HomeCinema")) {
                    products.add(new Pair(son.fromJson(json.toString(), Laptop.class), R.drawable.homecinema));
                } else if (productType.contains("Television")) {
                    products.add(new Pair(son.fromJson(json.toString(), Laptop.class), R.mipmap.monitors));
                }

            }

        } catch (JSONException e) {
            Log.d(TAG, "Error unparsing JSON");
            e.printStackTrace();
        }
        return products;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.menu = menu;
        getMenuInflater().inflate(R.menu.grip_product_menu, menu);
        return true;
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
        // Unbind from the service
        if (mBound) {
            networkService.sendLogoutRequest();
            unbindService(mConnection);
            mBound = false;
        }
    }


    public class ImageAdapter extends BaseAdapter {
        private static final int PADDING = 8;
        private static final int WIDTH = 200;
        private static final int HEIGHT = 200;
        private Context mContext;
        private ArrayList<Pair<Product,Integer>> mThumbIds;

        // Store the list of image IDs
        public ImageAdapter(Context c,  ArrayList<Pair<Product,Integer>> ids) {
            mContext = c;
            this.mThumbIds = ids;
        }

        // Return the number of items in the Adapter
        @Override
        public int getCount() {
            return mThumbIds.size();
        }

        // Return the data item at position
        @Override
        public Object getItem(int position) {
            return mThumbIds.get(position);
        }

        // Will get called to provide the ID that
        // is passed to OnItemClickListener.onItemClick()
        @Override
        public long getItemId(int position) {
            return mThumbIds.get(position).second;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            View grid;
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // if convertView's not recycled, initialize some attributes
            if (convertView == null) {
                grid = new View(mContext);
                grid = inflater.inflate(R.layout.image_and_text, null);
                TextView textView = (TextView) grid.findViewById(R.id.grid_image_text);
                ImageView imageView = (ImageView) grid.findViewById(R.id.image_grid);
                Log.d(TAG, String.valueOf(position));
                textView.setText(products.get(position).first.getName());
//                Log.d("-----------------", productTexts.get(position) + " " + mThumbIds.get(position));

                imageView.setImageResource(mThumbIds.get(position).second);
            } else {
                grid = (View) convertView;
            }
            return grid;
        }
    }


}
