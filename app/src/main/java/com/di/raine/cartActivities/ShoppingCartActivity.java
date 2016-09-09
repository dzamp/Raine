package com.di.raine.cartActivities;

/**
 * Created by di on 5/9/2016.
 */

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import android.widget.AdapterView.OnItemClickListener;

import com.di.raine.R;
import com.di.raine.adapters.ProductAdapter;
import com.di.raine.branches.Branch;
import com.di.raine.cartHelper.ShoppingCartHelper;
import com.di.raine.products.CartProduct;

public class ShoppingCartActivity extends Activity {

    private List<CartProduct> mCartList;
    private List<Branch> mShopsForReview;
    private ProductAdapter mProductAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoppingcart);

        mCartList = ShoppingCartHelper.getCart();
        mShopsForReview = ShoppingCartHelper.getShopsForReview();

        for (int i = 0; i < mCartList.size(); i++) {
            mCartList.get(i).selected = false;
        }

        final ListView listViewCatalog = (ListView) findViewById(R.id.ListViewCatalog);
        mProductAdapter = new ProductAdapter(mCartList, getLayoutInflater(), true);
        listViewCatalog.setAdapter(mProductAdapter);

        listViewCatalog.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                CartProduct selectedProduct = mCartList.get(position);
                if (selectedProduct.selected == true)
                    selectedProduct.selected = false;
                else
                    selectedProduct.selected = true;

                mProductAdapter.notifyDataSetInvalidated();

            }
        });

        Button removeButton = (Button) findViewById(R.id.ButtonRemoveFromCart);
        removeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = mCartList.size() - 1; i >= 0; i--) {

                    if (mCartList.get(i).selected) {
                        mCartList.remove(i);
                    }
                }
                double subTotal = 0;
                for (CartProduct p : mCartList) {

                    subTotal += p.price;
                }

                TextView productPriceTextView = (TextView) findViewById(R.id.TextViewSubtotal);
                productPriceTextView.setText("Subtotal: €" + subTotal);
                mProductAdapter.notifyDataSetChanged();
            }
        });

        Button checkoutButton = (Button) findViewById(R.id.ButtonCheckout);
        checkoutButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = mCartList.size() - 1; i >= 0; i--) {
                    if (!mShopsForReview.contains(mCartList.get(i).getStore())) {
                        mShopsForReview.add(mCartList.get(i).getStore());
                    }
                    mCartList.remove(i);
                    Intent viewShopListForReview = new Intent(getBaseContext(), CheckoutActivity.class);
                    startActivity(viewShopListForReview);
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Refresh the data
        if (mProductAdapter != null) {
            mProductAdapter.notifyDataSetChanged();
        }

        double subTotal = 0;
        for (CartProduct p : mCartList) {

            subTotal += p.price;
        }

        TextView productPriceTextView = (TextView) findViewById(R.id.TextViewSubtotal);
        productPriceTextView.setText("Subtotal: €" + subTotal);
    }

}
