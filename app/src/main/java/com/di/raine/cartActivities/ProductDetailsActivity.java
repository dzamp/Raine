package com.di.raine.cartActivities;

/**
 * Created by di on 5/9/2016.
 */
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.di.raine.R;
import com.di.raine.adapters.ProductAdapter;
import com.di.raine.adapters.ReviewAdapter;
import com.di.raine.branches.Comment;
import com.di.raine.cartHelper.NonScrollListView;
import com.di.raine.cartHelper.ShoppingCartHelper;
import com.di.raine.products.CartProduct;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.di.raine.services.NetworkService;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ProductDetailsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private CartProduct selectedProduct;
    boolean mBound = false;
    private NetworkService networkService;
    private ReviewAdapter rReviewAdapter;
    private String shopId;
    List<Comment> comments;
    private ServiceConnection mConnection = new ServiceConnection()

    {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            NetworkService.NetworkBinder binder = (NetworkService.NetworkBinder) service;
            networkService = binder.getService();
            mBound = true;

            networkService.requestComments(shopId/*branch stringid*/, new Response.Listener<String>() {
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
                    NonScrollListView listViewReviews = (NonScrollListView) findViewById(R.id.ListViewReviewsItems);
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
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.productdetails);

        List<CartProduct> catalog = ShoppingCartHelper.getCatalog(getApplicationContext());
        final List<CartProduct> cart = ShoppingCartHelper.getCart();

        int productIndex = getIntent().getExtras().getInt(ShoppingCartHelper.PRODUCT_INDEX);
        selectedProduct = catalog.get(productIndex);
        shopId = selectedProduct.getStore().getId();
        // Set the proper image and text
        ImageView productImageView = (ImageView) findViewById(R.id.ImageViewProduct);
        productImageView.setImageDrawable(selectedProduct.productImage);
        TextView productTitleTextView = (TextView) findViewById(R.id.TextViewProductTitle);
        productTitleTextView.setText(selectedProduct.title);
        TextView productDetailsTextView = (TextView) findViewById(R.id.TextViewProductDetails);
        productDetailsTextView.setText(selectedProduct.description);

        TextView productPriceTextView = (TextView) findViewById(R.id.TextViewProductPrice);
        productPriceTextView.setText("€" + selectedProduct.price);

        TextView productShopTextView = (TextView) findViewById(R.id.TextViewProductShop);
        productShopTextView.setText(selectedProduct.getStore().getName());


        Button getDirections = (Button) findViewById(R.id.ButtonGetDirections);
        getDirections.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + 37.99000 + "," + 23.73000 + "&daddr=" + selectedProduct.getStore().getLocality().getPoint().getLatitude() + "," + selectedProduct.getStore().getLocality().getPoint().getLongitude()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        });

        Button streetView = (Button) findViewById(R.id.ButtonStreetView);
        streetView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Uri gmmIntentUri = Uri.parse("google.streetview:cbll=" + Double.toString(selectedProduct.getStore().getLocality().getPoint().getLatitude())
                        + "," + Double.toString(selectedProduct.getStore().getLocality().getPoint().getLongitude()));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });


        Button addToCartButton = (Button) findViewById(R.id.ButtonAddToCart);
        addToCartButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                cart.add(selectedProduct);
                finish();
            }
        });


        if (cart.contains(selectedProduct)) {
            addToCartButton.setEnabled(false);
            addToCartButton.setText("Item in Cart");
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        LatLng shopLatLong = new LatLng(
                selectedProduct.getStore().getLocality().getPoint().getLatitude(), selectedProduct.getStore().getLocality().getPoint().getLongitude());
        mMap.addMarker(new MarkerOptions().position(shopLatLong).title("Shop"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(shopLatLong));

        CameraPosition cameraPosition = new CameraPosition.Builder().target(shopLatLong).zoom(17.0f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.moveCamera(cameraUpdate);
    }

    @Override
    protected void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        // Bind to LocalService
        Intent intent = new Intent(this, NetworkService.class);
        startService(intent);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("ProductDetails Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}