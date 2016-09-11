package com.di.raine.cartActivities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.di.raine.R;
import com.di.raine.adapters.ShopsAdapter;
import com.di.raine.branches.Branch;
import com.di.raine.cartHelper.ShoppingCartHelper;

import java.util.List;

/**
 * Created by di on 6/9/2016.
 */
public class CheckoutActivity extends Activity {
    private List<Branch> mShopsList;
    private ShopsAdapter mShopsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkoutshops);

        mShopsList = ShoppingCartHelper.getShopsForReview();

        for (int i = 0; i < mShopsList.size(); i++) {
            mShopsList.get(i).selected = false;
        }

        final ListView listViewShop = (ListView) findViewById(R.id.ShopListView);
        mShopsAdapter = new ShopsAdapter(mShopsList, getLayoutInflater(), true);
        listViewShop.setAdapter(mShopsAdapter);


        listViewShop.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Branch selectedShop = mShopsList.get(position);
                //CHANGES 11/9
                if (selectedShop.selected == true){
                    selectedShop.selected = false;}
                else {
                    for (int i = 0; i < mShopsList.size(); i++) {
                        mShopsList.get(i).selected = false;
                    }
                    selectedShop.selected = true;

                }
                mShopsAdapter.notifyDataSetInvalidated();


            }
        });
        Button reviewButton = (Button) findViewById(R.id.ButtonReview);
        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getSelectedBool()){
                    Intent reviewShopIntent = new Intent(getBaseContext(),ReviewActivity.class);
                    reviewShopIntent.putExtra(ShoppingCartHelper.SHOP_INDEX, getSelected());
                    //reviewShopIntent.putExtra(ShoppingCartHelper.SHOP_INDEX, -1);
                    startActivity(reviewShopIntent);
                }

            }
        });
    }

    public boolean getSelectedBool(){
        for (int i = 0; i < mShopsList.size(); i++) {
            if(mShopsList.get(i).selected){
                return true;
            }
        }
        return false;
    }

    public int getSelected(){
        for (int i = 0; i < mShopsList.size(); i++) {
            if(mShopsList.get(i).selected){
                return i;
            }
        }
        return -1;
    }
}
