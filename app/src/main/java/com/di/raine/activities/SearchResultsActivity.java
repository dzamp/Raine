package com.di.raine.activities;

import android.app.ListActivity;
import android.app.SearchManager;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.di.raine.R;
import com.di.raine.products.Product;
import com.di.raine.products.SearchObject;
import com.di.raine.services.NetworkService;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by jim on 31/8/2016.
 */

public class SearchResultsActivity extends AppCompatActivity {
    private final static String TAG = SearchResultsActivity.class.toString();
    private NetworkService networkService;
    private boolean mBound;
    private Menu menu;
    private String query;
    private ArrayList<Product> products = new ArrayList<>();
    private final static int[] ids = {1,2,3,4};
    private String productType = "";
    private ListView gridview;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            NetworkService.NetworkBinder binder = (NetworkService.NetworkBinder) service;
            networkService = binder.getService();
            mBound = true;
            handleIntent(getIntent());
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SearchResultsActivity.ImageAdapter adapter = new ImageAdapter(getApplicationContext(),setProductTypeAccordingToQuery());
        switch (item.getItemId()){
            case R.id.submenu_sort_by_model:
                sortByName(products);
                gridview.setAdapter(adapter);
                break;
            case R.id.submenu_sort_high_to_low:
                Log.d(TAG, "to be implemented");
                gridview.setAdapter(adapter);
                // FIXME: 2/9/2016
                break;
            case R.id.submenu_sort_low_to_high:
                Log.d(TAG, "to be implemented");
                gridview.setAdapter(adapter);
                // FIXME: 2/9/2016
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

    public void setListViewWithProducts(){
        gridview = (ListView) findViewById(R.id.listView);
        gridview.setAdapter(new SearchResultsActivity.ImageAdapter(this, setProductTypeAccordingToQuery()));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {
                Snackbar.make(v, "clicked  item " + position, Snackbar.LENGTH_SHORT).show();
                if (mBound) {
//                    sortByName(products);
                    SearchResultsActivity.ImageAdapter adapter = new ImageAdapter(getApplicationContext(),setProductTypeAccordingToQuery());
                    gridview.setAdapter(adapter);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.menu = menu;
        getMenuInflater().inflate(R.menu.search_result_menu, menu);
        return true;
    }

    private int setProductTypeAccordingToQuery() {
        if(productType.contains("Laptop"))
            return R.drawable.laptop;
        else if (productType.contains("Desktop"))
            return R.mipmap.pc;
        else if (productType.contains("Sound"))
            return R.drawable.sound;
        else if (productType.contains("HomeCinema"))
            return R.drawable.homecinema;
        else if (productType.toLowerCase().contains("television")||productType.toLowerCase().contains("tv"))
            return R.mipmap.monitors;
        else return 0;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        Intent intent = new Intent(this, NetworkService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Toast.makeText(getApplicationContext(), "Hello from onNewIntent", Toast.LENGTH_LONG).show();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this, "Searching by: " + query, Toast.LENGTH_SHORT).show();

        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            String uri = intent.getDataString();
            Toast.makeText(this, "Suggestion: " + uri, Toast.LENGTH_SHORT).show();
        }
        setIntent(intent);
        handleIntent(intent);
    }

    public void onListItemClick(ListView l,
                                View v, int position, long id) {
        // call detail activity for clicked entry
    }

    public void sortByName(ArrayList<Product> sort){
        Collections.sort(sort, new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });
    }

    private void handleIntent(Intent intent) {
//        handleIntent(getIntent());

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);

            networkService.searchProducts(query, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("documents");
                        if (jsonArray.length() != 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                SearchObject searchObject = new Gson().fromJson(jsonArray.getString(i), SearchObject.class);
                                productType = searchObject.getCategory();
                                products.add(searchObject.convertToProduct());
                            }
                            setListViewWithProducts();
                        } else {
                            Toast.makeText(getApplicationContext(), "No results found for \"query\" ", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                          .convertToProduct();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, error.toString());
                    Toast.makeText(getApplicationContext(), "error in search", Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    private void doSearch(String queryStr) {
        // get a Cursor, prepare the ListAdapter
        // and set it
    }

    public class ImageAdapter extends BaseAdapter {
        private static final int PADDING = 8;
        private static final int WIDTH = 200;
        private static final int HEIGHT = 200;
        private Context mContext;
        private int mThumbIds;

        // Store the list of image IDs
        public ImageAdapter(Context c, int id) {
            mContext = c;
            this.mThumbIds = id;
        }

        // Return the number of items in the Adapter
        @Override
        public int getCount() {
            return products.size();
        }

        // Return the data item at position
        @Override
        public Object getItem(int position) {
            return products.get(position);
        }

        // Will get called to provide the ID that
        // is passed to OnItemClickListener.onItemClick()
        @Override
        public long getItemId(int position) {
            return 0;
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
                textView.setTextColor(Color.BLACK);
                ImageView imageView = (ImageView) grid.findViewById(R.id.image_grid);
                textView.setText(products.get(position).getName());
//                Log.d("-----------------", productTexts.get(position) + " " + mThumbIds.get(position));
                imageView.setImageResource(mThumbIds);
            } else {
                grid = (View) convertView;
            }
            return grid;
        }
    }


}