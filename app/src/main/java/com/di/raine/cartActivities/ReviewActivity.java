package com.di.raine.cartActivities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.di.raine.R;
import com.di.raine.adapters.ShopsAdapter;
import com.di.raine.branches.Branch;
import com.di.raine.cartHelper.ShoppingCartHelper;

import java.util.List;

/**
 * Created by di on 8/9/2016.
 */
public class ReviewActivity extends Activity {

    private List<Branch> mShopsList;
    private ShopsAdapter mShopsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review);

        mShopsList = ShoppingCartHelper.getShopsForReview();

        TextView reviewShopTextView = (TextView) findViewById(R.id.textViewForReviewShop);
        reviewShopTextView.setText(mShopsList.get( getIntent().getExtras().getInt(ShoppingCartHelper.SHOP_INDEX)).getName());






    }
}