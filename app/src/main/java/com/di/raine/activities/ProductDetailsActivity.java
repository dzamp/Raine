package com.di.raine.activities;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.di.raine.R;
import com.di.raine.branches.Branch;
import com.di.raine.branches.Comment;
import com.di.raine.branches.Locality;
import com.di.raine.branches.Point;
import com.di.raine.cartHelper.ShoppingCartHelper;
import com.di.raine.products.CartProduct;
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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProductDetailsActivity extends AppCompatActivity {
    private final static String TAG = ProductDetailsActivity.class.toString();
    private TextView descriptionInfo = null;
    private ArrayList<Pair<Branch, String>> branches = new ArrayList<>(); //Pair of branches and their prices on this product
    private NetworkService networkService;
    private boolean mBound = false;
    private Menu menu;
    private Product product = null;
    private LocationManager mLocationManager;
    private Location currentLocation;
    private ListView gridview;
    private int productImage;

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            currentLocation = location;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(TAG, "OnStatusChanged");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG, "onProviderEnabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "onProviderDisabled");
        }
    };
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
                        // FIXME: 11/9/2016

                        JSONArray jsonBranches  = JsonResponse.getJSONArray("data");
                        for (int i=0;i<jsonBranches.length();i++) {
                            JsonBranch = jsonBranches.getJSONObject(i).getJSONObject("branch");
                            branch = new Gson().fromJson(JsonBranch.toString(), Branch.class);
                            String price = JsonResponse.getJSONArray("data").getJSONObject(i).getString("price");
                            branches.add(new Pair(branch, price));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    gridview.setAdapter(new ProductDetailsActivity.BranchesAdapter(getApplicationContext(), branches));
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "Error in fetching branches of product");
                }
            });



            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                               /* Uri gmmIntentUri = Uri.parse("google.streetview:cbll=" + Double.toString(branches.get(position).first.getLocality().getPoint().getLatitude())
                                        + "," + Double.toString(branches.get(position).first.getLocality().getPoint().getLongitude()));
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps");*/

                                CartProduct cartProduct = new CartProduct(product.getName(), ContextCompat.getDrawable(getApplicationContext(),
                                        productImage),product.getDescription(), Double.valueOf(branches.get(position).second), branches.get(position).first);
                                List<CartProduct> cartProducts = ShoppingCartHelper.getCatalog(getApplicationContext());
                                cartProducts.add(cartProduct);
                                Intent productDetailsIntent = new Intent(getBaseContext(), com.di.raine.cartActivities.ProductDetailsActivity.class);
                                productDetailsIntent.putExtra(ShoppingCartHelper.PRODUCT_INDEX, 0);
                                startActivity(productDetailsIntent);

                }
            });

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    private void requestImmediateLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();
//        criteria.setAccuracy(Criteria.NO_REQUIREMENT);
        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Get Current Location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        currentLocation = mLocationManager.getLastKnownLocation(provider);

        //latitude of location
        double myLatitude = currentLocation.getLatitude();

        //longitude og location
        double myLongitude = currentLocation.getLongitude();
    }

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
        gridview = (ListView) findViewById(R.id.listBranch_alt);
        Intent in = new Intent(this, NetworkService.class);
        bindService(in, mConnection, Context.BIND_AUTO_CREATE);

        TextView descriptionText = (TextView) findViewById(R.id.product_description_text);
        descriptionText.setText(product.dataInfo().get("description"));
        ImageView image = (ImageView) findViewById(R.id.productDetailImage);
        productImage = intent.getIntExtra("productImage",0);
        System.out.println("Image id " + (intent.getIntExtra("productImage", 0)));
        image.setImageResource((intent.getIntExtra("productImage", 0)));
        descriptionInfo = (TextView) findViewById(R.id.product_description_text);
        descriptionInfo.setVisibility(View.GONE);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100,
                10, mLocationListener);

    }


    public void show_contents(View v) {
        descriptionInfo.setVisibility(descriptionInfo.isShown() ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, NetworkService.class);
//        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.menu = menu;
        getMenuInflater().inflate(R.menu.branch_reorder_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ProductDetailsActivity.BranchesAdapter adapter = new ProductDetailsActivity.BranchesAdapter(getApplicationContext(), branches);
        switch (item.getItemId()) {
            case R.id.submenu_sort_high_to_low:
                Toast.makeText(getApplicationContext(), "hello", Toast.LENGTH_SHORT).show();
                sortBranchByPriceHighLow(branches);
                gridview.setAdapter(adapter);
                break;
            case R.id.submenu_sort_low_to_high:
                sortBranchByPriceLowHigh(branches);
                gridview.setAdapter(adapter);
                break;
            case R.id.submenu_sort_by_proximity:
                sortBranchByProximity(branches);
                gridview.setAdapter(adapter);
                // FIXME: 2/9/2016
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
//        return super.onContextItemSelected(item);
        return true;
    }

    public void sortBranchByPriceLowHigh(ArrayList<Pair<Branch, String>> list) {
        Collections.sort(list, new Comparator<Pair<Branch, String>>() {
            @Override
            public int compare(Pair<Branch, String> o1, Pair<Branch, String> o2) {
                float num1 = Float.parseFloat(o1.second);
                float num2 = Float.parseFloat(o2.second);
                return (int) (num1 - num2);
            }
        });
    }

    public void sortBranchByPriceHighLow(ArrayList<Pair<Branch, String>> list) {
        Collections.sort(list, new Comparator<Pair<Branch, String>>() {
            @Override
            public int compare(Pair<Branch, String> o1, Pair<Branch, String> o2) {
                float num1 = Float.parseFloat(o1.second);
                float num2 = Float.parseFloat(o2.second);
                return (int) (num2 - num1);
            }
        });
    }

    public void sortBranchByProximity(ArrayList<Pair<Branch, String>> list) {
        requestImmediateLocation();
        Collections.sort(list, new Comparator<Pair<Branch, String>>() {
            @Override
            public int compare(Pair<Branch, String> o1, Pair<Branch, String> o2) {
                double currentLat = currentLocation.getLatitude();
                double currentLong = currentLocation.getLongitude();
                double distrance1 = Math.abs((currentLat + currentLong)
                        - (o1.first.getLocality().getPoint().getLatitude() + o1.first.getLocality().getPoint().getLongitude()));
                double distrance2 = Math.abs((currentLat + currentLong)
                        - (o2.first.getLocality().getPoint().getLatitude() + o2.first.getLocality().getPoint().getLongitude()));

                //The absolute distance of the current position minus the first point needs to be smaller
                // than the absolute distance of the current position minus the second point
                if (Math.abs((currentLat + currentLong)
                        - (o1.first.getLocality().getPoint().getLatitude() + o1.first.getLocality().getPoint().getLongitude()))
                        < Math.abs((currentLat + currentLong)
                        - (o2.first.getLocality().getPoint().getLatitude() + o2.first.getLocality().getPoint().getLongitude()))) {
                    return -1;
                } else return 1;

            }
        });
    }


    private class BranchesAdapter extends ArrayAdapter<Pair<Branch, String>> {
        private static final int PADDING = 8;
        private static final int WIDTH = 200;
        private static final int HEIGHT = 200;
        private Context mContext;
        private List<Pair<Branch, String>> branches;

        public BranchesAdapter(Context mContext, List<Pair<Branch, String>> mThumbIds) {
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
                textView.setText(branches.get(position).first.getName() + ", " + branches.get(position).first.getLocality().getAddress() + ", "
                        + branches.get(position).first.getLocality().getCity());
                textView.setTextColor(Color.BLACK);
                TextView priceView = (TextView) grid.findViewById(R.id.branch_price);
                priceView.setText(branches.get(position).second + " €");
                priceView.setTextColor(Color.BLACK);
            } else {
                grid = (View) convertView;
            }
            return grid;
        }
    }


}
