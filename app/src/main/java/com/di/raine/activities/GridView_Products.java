package com.di.raine.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.di.raine.R;
import com.di.raine.products.CelloResponse;
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
import java.util.List;


public class GridView_Products extends AppCompatActivity {
    private NetworkService networkService;
    private boolean mBound;
    private Menu menu;
    private ArrayList<Integer> productIds = new ArrayList<Integer>(
            Arrays.asList(R.drawable.homecinema, R.drawable.sound, R.drawable.laptops, R.mipmap.pc,
                    R.mipmap.monitors));
    private ArrayList<String> productTexts = new ArrayList<>(
            Arrays.asList("Home Cinema", "Sound", "Laptops", "Desktops", "TV"));

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
        setContentView(R.layout.listview_products);
        ListView gridview = (ListView) findViewById(R.id.listView);
        gridview.setAdapter(new ImageAdapter(this, productIds));

        // Set an setOnItemClickListener on the GridView
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {
                Snackbar.make(v, "clicked  item " + position, Snackbar.LENGTH_SHORT).show();
                if (mBound) {
                    networkService.getCategory(position + 1,new JSONObject(), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Gson son = new Gson();
                           CelloResponse celloResponse = son.fromJson(response.toString(), CelloResponse.class);
                            System.out.println(celloResponse.toString());
                            if(position == 2){
                               ArrayList<Laptop> laptops = (ArrayList)celloResponse.getData();
                                Log.d("HASH",laptops.get(0).getDescription());
                            }
                        }

//                        @Override
//                        public void onResponse(String response) {
////                            JSONArray obj = null;
////                            ArrayList<Product> products = new ArrayList<>();
////                            try {
////                                obj = (JSONArray) new JSONObject(response).get("data");
////                               Product productObj =  getProductAccordingtoPosition(position);
////                                for (int i = 0; i < obj.length(); i++) {
////                                    Gson son = new Gson();
////                                    JSONObject json = obj.getJSONObject(i);
////                                    products.add(son.fromJson(json.toString(), productObj.getClass()));
////                                }
////                            } catch (JSONException e) {
////                                e.printStackTrace();
////                            }
////
//                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Unable to retrieve list of products", Toast.LENGTH_SHORT).show();
                            Log.d("ERROR", error.getMessage());
                        }
                    });
                }
            }
        });

    }

    private Product getProductAccordingtoPosition(int position){
        Product productClass = null;
        switch (position) {
            case 0:
                productClass = new HomeCinema();
                break;
            case 1:
                productClass = new Sound();
                break;
            case 2:
                productClass = new Laptop();
                break;
            case 3:
                productClass = new Desktop();
                break;
            case 4:
                productClass = new Television();
                break;
        }
        return productClass;
    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v,
//                                    ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.grip_product_menu, menu);
//    }

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

    public void requestCategories() {
        if (mBound) {
            networkService.requestCategories(new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.d("Sucess", response.toString());

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Error", error.toString());

                }
            });
        }
    }

    public class ImageAdapter extends BaseAdapter {
        private static final int PADDING = 8;
        private static final int WIDTH = 200;
        private static final int HEIGHT = 200;
        private Context mContext;
        private List<Integer> mThumbIds;

        // Store the list of image IDs
        public ImageAdapter(Context c, List<Integer> ids) {
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
            return mThumbIds.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            View grid;
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // if convertView's not recycled, initialize some attributes
            if (convertView == null) {
//                imageView = new ImageView(mContext);
//                imageView.setLayoutParams(new GridView.LayoutParams(WIDTH, HEIGHT));
//                imageView.setPadding(PADDING, PADDING, PADDING, PADDING);
//                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                grid = new View(mContext);
                grid = inflater.inflate(R.layout.image_and_text, null);
                TextView textView = (TextView) grid.findViewById(R.id.grid_image_text);
                ImageView imageView = (ImageView) grid.findViewById(R.id.image_grid);
                textView.setText(productTexts.get(position));
                Log.d("-----------------", productTexts.get(position) + " " + mThumbIds.get(position));

                imageView.setImageResource(mThumbIds.get(position));
            } else {
                grid = (View) convertView;
            }
            return grid;
        }
    }

}