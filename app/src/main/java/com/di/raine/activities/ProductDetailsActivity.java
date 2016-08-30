package com.di.raine.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.di.raine.R;
import com.di.raine.products.Desktop;
import com.di.raine.products.HomeCinema;
import com.di.raine.products.Laptop;
import com.di.raine.products.Product;
import com.di.raine.products.Sound;
import com.di.raine.products.Television;
import com.google.gson.Gson;

public class ProductDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Intent intent = getIntent();
        String object  = intent.getStringExtra("product");
        String productType = intent.getStringExtra("productType");
        Product product = null;
        if(productType.contains("Laptop"))
            product = new Gson().fromJson(object, Laptop.class);
        else if (productType.contains("Desktop"))
            product = new Gson().fromJson(object, Desktop.class);
        else if (productType.contains("Sound"))
            product = new Gson().fromJson(object, Sound.class);
        else if (productType.contains("HomeCinema"))
            product = new Gson().fromJson(object, HomeCinema.class);
        else if (productType.contains("Television"))
            product = new Gson().fromJson(object, Television.class);

        TextView textView = (TextView) findViewById(R.id.ImageName);
        textView.setText(product.getName());
        TextView descriptionText = (TextView) findViewById(R.id.descriptionText);
        descriptionText.setText(product.dataInfo().get("description"));
        ImageView image = (ImageView) findViewById(R.id.productDetailImage);
        System.out.println("Image id " +(intent.getIntExtra("productImage",0)));
        image.setImageResource((intent.getIntExtra("productImage",0)));


    }
}
