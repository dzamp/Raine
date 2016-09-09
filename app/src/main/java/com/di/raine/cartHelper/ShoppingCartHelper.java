package com.di.raine.cartHelper;

/**
 * Created by di on 5/9/2016.
 */
import java.util.List;
import java.util.Vector;

import android.content.res.Resources;

import com.di.raine.branches.Branch;
import com.di.raine.branches.Locality;
import com.di.raine.branches.Point;
import com.di.raine.R;
import com.di.raine.products.CartProduct;

public class ShoppingCartHelper {

    public static final String PRODUCT_INDEX = "PRODUCT_INDEX";

    private static List<CartProduct> catalog;
    private static List<CartProduct> cart;
    private static List<Branch> shops;

    public static List<CartProduct> getCatalog(Resources res) {
        if (catalog == null) {
            catalog = new Vector<CartProduct>();
            catalog.add(new CartProduct("laptop", res
                    .getDrawable( R.drawable.camera , null),
                    "A very good Laptop", 29.99,new Branch("katasthma 1", "111" ,new Locality("Athens","12345","kapou 13",new Point(123,345)))));
            catalog.add(new CartProduct("Camera", res
                    .getDrawable(R.drawable.camera, null),
                    "What a nice camera", 24.99,new Branch("katasthma 1", "111" ,new Locality("Athens","12345","kapou 13",new Point(123,345)))));
            catalog.add(new CartProduct("something", res
                    .getDrawable(R.drawable.camera, null),
                    "Someting some", 14.99,new Branch("katasthma 1", "111" ,new Locality("Athens","12345","kapou 13",new Point(123,345)))));
        }

        return catalog;
    }

    public static List<CartProduct> getCart() {
        if (cart == null) {
            cart = new Vector<CartProduct>();
        }

        return cart;
    }

    public static List<Branch> getShopsForReview() {
        if (shops == null) {
            shops = new Vector<Branch>();
        }
        return shops;
    }
}
