package com.di.raine.cartActivities;

/**
 * Created by di on 5/9/2016.
 */
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.di.raine.R;
import com.di.raine.cartHelper.ShoppingCartHelper;
import com.di.raine.products.CartProduct;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class ProductDetailsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private CartProduct selectedProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.productdetails);

        List<CartProduct> catalog = ShoppingCartHelper.getCatalog(getApplicationContext());
        final List<CartProduct> cart = ShoppingCartHelper.getCart();

        int productIndex = getIntent().getExtras().getInt(ShoppingCartHelper.PRODUCT_INDEX);
        selectedProduct = catalog.get(productIndex);

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

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr="+37.99000+","+23.73000+"&daddr="+ selectedProduct.getStore().getLocality().getPoint().getLatitude()+","+selectedProduct.getStore().getLocality().getPoint().getLongitude()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_LAUNCHER );
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


        if(cart.contains(selectedProduct)) {
            addToCartButton.setEnabled(false);
            addToCartButton.setText("Item in Cart");
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
}