package com.di.raine.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.di.raine.R;
import com.di.raine.products.Laptop;
import com.di.raine.products.Product;
import com.di.raine.services.NetworkService;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ProductsActivity extends AppCompatActivity {

    private static final String TAG = "ProductsActivity.class";
    private NetworkService networkService;
    private boolean mBound;
    private Menu menu;
    private ArrayList<Pair<Product, Integer>> products;
    private ListView gridview;
    private ArrayList<String> productPrices;
    private ArrayList<Pair<Product,String>> product_price_pair;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            NetworkService.NetworkBinder binder = (NetworkService.NetworkBinder) service;
            networkService = binder.getService();
            mBound = true;
            final Intent intent = getIntent();
            products = populateProductList(intent);
            gridview = (ListView) findViewById(R.id.listView);
            gridview.setAdapter(new ImageAdapter(getApplicationContext(), products));
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {
                    Snackbar.make(v, "clicked  item " + position, Snackbar.LENGTH_SHORT).show();
                    if (mBound) {
                        Intent i = new Intent(getApplicationContext(), ProductDetailsActivity.class);
                        String jsonObject = new Gson().toJson(product_price_pair.get(position).first);
                        i.putExtra("product", jsonObject);
                        i.putExtra("productType", product_price_pair.get(position).first.getClass().toString());
                        i.putExtra("productImage", Integer.valueOf(products.get(position).second));
                        startActivity(i);
                    }
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
        setContentView(R.layout.activity_products);

    }

    private ArrayList<Pair<Product, Integer>> populateProductList(Intent intent) {
        String productType = intent.getStringExtra("productType");
        products = new ArrayList<>();
        productPrices = new ArrayList<>();
        product_price_pair =  new ArrayList<>();

        String productString = intent.getStringExtra("products");
        JSONArray prod = null;
        try {
            prod = new JSONArray(productString);
            for (int i = 0; i < prod.length(); i++) {
                Gson son = new Gson();
                JSONObject json = prod.getJSONObject(i);
                if (productType.contains("Laptop")) {
                    products.add(new Pair(son.fromJson(json.get("product").toString(), Laptop.class), R.drawable.laptop));
                } else if (productType.contains("Desktop")) {
                    products.add(new Pair(son.fromJson(json.get("product").toString(), Laptop.class), R.mipmap.pc));
                } else if (productType.contains("Sound")) {
                    products.add(new Pair(son.fromJson(json.get("product").toString(), Laptop.class), R.drawable.sound));
                } else if (productType.contains("HomeCinema")) {
                    products.add(new Pair(son.fromJson(json.get("product").toString(), Laptop.class), R.drawable.homecinema));
                } else if (productType.contains("Television")) {
                    products.add(new Pair(son.fromJson(json.get("product").toString(), Laptop.class), R.mipmap.monitors));
                }
                productPrices.add(json.getString("price"));
                product_price_pair.add(new Pair<Product, String>(products.get(i).first,productPrices.get(i)));
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
        getMenuInflater().inflate(R.menu.search_result_menu, menu);
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ProductsActivity.ImageAdapter adapter = new ProductsActivity.ImageAdapter(getApplicationContext(), products);
        switch (item.getItemId()) {
            case R.id.submenu_sort_by_model:
                sortByName(product_price_pair);
                gridview.setAdapter(adapter);
                break;
            case R.id.submenu_sort_high_to_low:
                sortByPriceHighLow(product_price_pair);
                Log.d(TAG, "to be implemented");
                gridview.setAdapter(adapter);
                break;
            case R.id.submenu_sort_low_to_high:
                sortByPriceLowHigh(product_price_pair);
                Log.d(TAG, "to be implemented");
                gridview.setAdapter(adapter);
                break;
            case R.id.submenu_sort_by_proximity:
                Log.d(TAG, "to be implemented");
                gridview.setAdapter(adapter);
                // FIXME: 2/9/2016
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
//        return super.onContextItemSelected(item);
        return true;
    }

    public void sortByName(ArrayList<Pair<Product, String>> sort) {
        Collections.sort(sort, new Comparator<Pair<Product, String>>() {
            @Override
            public int compare(Pair<Product, String> o1, Pair<Product, String> o2) {
                return o1.first.getName().compareToIgnoreCase(o2.first.getName());
            }
        });
    }

    public void sortByPriceLowHigh(ArrayList<Pair<Product, String>> sort) {
        Collections.sort(sort, new Comparator<Pair<Product, String>>() {
            @Override
            public int compare(Pair<Product, String> o1, Pair<Product, String> o2) {
                int num1 = Integer.parseInt(o1.second);
                int num2 = Integer.parseInt(o2.second);
                return num1 - num2 ;
            }
        });
    }


    public void sortByPriceHighLow(ArrayList<Pair<Product, String>> sort) {
        Collections.sort(sort, new Comparator<Pair<Product, String>>() {
            @Override
            public int compare(Pair<Product, String> o1, Pair<Product, String> o2) {
                int num1 = Integer.parseInt(o1.second);
                int num2 = Integer.parseInt(o2.second);
                return num2 - num1 ;
            }
        });
    }

    public class ImageAdapter extends BaseAdapter {
        private static final int PADDING = 8;
        private static final int WIDTH = 200;
        private static final int HEIGHT = 200;
        private Context mContext;
        private ArrayList<Pair<Product, Integer>> mThumbIds;

        // Store the list of image IDs
        public ImageAdapter(Context c, ArrayList<Pair<Product, Integer>> ids) {
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
                textView.setText(product_price_pair.get(position).first.getName() + "\nPrice : "
                        + product_price_pair.get(position).second  + "€");
                textView.setTextColor(Color.BLACK);
                imageView.setImageResource(mThumbIds.get(position).second);
            } else {
                grid = (View) convertView;
            }
            return grid;
        }
    }


}
