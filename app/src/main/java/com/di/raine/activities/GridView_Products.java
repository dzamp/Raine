package com.di.raine.activities;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
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


public class GridView_Products extends AppCompatActivity /*implements SearchView.OnQueryTextListener*/ {
    private NetworkService networkService;
    private boolean mBound;
    private Menu menu;
    private int count = 0;
    SearchManager searchManager =null;
    private SearchView searchView;
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
//        getActionBar().setDisplayHomeAsUpEnabled(true);
//        getActionBar().setHomeButtonEnabled(true);
        // Set an setOnItemClickListener on the GridView
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {
                Snackbar.make(v, "clicked  item " + position, Snackbar.LENGTH_SHORT).show();
                if (mBound) {
                    networkService.getCategory(position + 1, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JSONArray obj = null;
                            ArrayList<Product> products = new ArrayList<>();
                            try {
                                obj = (JSONArray) new JSONObject(response).get("data");
                                String pType= "";
                               Product productObj =  getProductAccordingtoPosition(position, pType);
                                //start new Intent for new Activity
                                Intent i = new Intent(getApplicationContext(), ProductsActivity.class);
                                i.putExtra("products", obj.toString());
                                i.putExtra("productType",  productObj.getClass().toString());
                                startActivity(i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
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



    private Product getProductAccordingtoPosition(int position, String pType){
        pType = "";
        Product productClass = null;
        switch (position) {
            case 0:
                productClass = new HomeCinema();
                pType = "HomeCinema";
                break;
            case 1:
                productClass = new Sound();
                pType = "Sound";
                break;
            case 2:
                productClass = new Laptop();
                pType = "Laptop";
                break;
            case 3:
                productClass = new Desktop();
                pType = "Desktop";
                break;
            case 4:
                productClass = new Television();
                pType = "Television";
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Toast.makeText(getApplicationContext(), "Search functionality", Toast.LENGTH_LONG).show();
                onSearchRequested();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.menu = menu;
        getMenuInflater().inflate(R.menu.grip_product_menu, menu);
        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchActionBarItem = menu.findItem(R.id.search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchActionBarItem);
//        searchView = (EnglishVerbSearchView) MenuItemCompat.getActionView(searchActionBarItem);

        ComponentName cn = new ComponentName(this, SearchResultsActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(cn));
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
//         Associate searchable configuration with the SearchView
//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = (SearchView) menu.findItem(R.id.search)
//                .getActionView();
//        searchView.setSearchableInfo(searchManager
//                .getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
//        searchView.setOnQueryTextListener(this);
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

//    @Override
//    public boolean onQueryTextSubmit(String query) {
//        searchView.clearFocus();
//        searchView.setIconified(true);
////                Bundle appData = new Bundle();
////        appData.putString("hello", "world");
////        startSearch(null, false, appData, false);
////        return true;
////        onSearchRequested();
//        Log.d("Count", ""+count++);
//        searchManager.startSearch(query,false,new ComponentName(this, SearchResultsActivity.class),null,false);
//        return true;
//    }
//
////    @Override
////    public boolean onSearchRequested() {
////        Bundle appData = new Bundle();
////        appData.putString("hello", "world");
////        startSearch(null, false, appData, false);
////        return true;
////    }
//
//    @Override
//    public boolean onQueryTextChange(String newText) {
//        return false;
//    }

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



    //                        @Override
//                        public void onResponse(JSONObject response) {
//                            Gson son = new Gson();
//                            CelloResponse<Laptop> celloResponse = new CelloResponse<Laptop>();
//                            CelloResponse<Laptop> celloRespons = son.fromJson(response.toString(), celloResponse.getClass());
//                            System.out.println(celloResponse.toString());
//                            CelloResponse<Laptop> lpts =  new CelloResponse<Laptop>();
//                            ArrayList<Laptop> lList = new ArrayList<Laptop>();
//                            lList.add(new Laptop("1","laptop", "good laptop"));
//                            lpts.setData(lList);
////                            if(position == 2){
////                              ArrayList<Laptop> laptops = (ArrayList<Laptop>) celloRespons.getData();
////                                System.out.println(laptops.get(0).getName());
//////                                Log.d("HASH",laptops.get(0).getName());
////                            }
//                        }

}