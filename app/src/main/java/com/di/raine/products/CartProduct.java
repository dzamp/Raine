package com.di.raine.products;

import android.graphics.drawable.Drawable;

import com.di.raine.branches.Branch;

public class CartProduct {

    public String title;
    public Drawable productImage;
    public String description;
    public double price;
    public boolean selected;
    public Branch store;

    public CartProduct(String title, Drawable productImage, String description,
                       double price, Branch store) {
        this.title = title;
        this.productImage = productImage;
        this.description = description;
        this.price = price;
        this.store = store;
    }

    public Branch getStore() {
        return store;
    }

    public void setStore(Branch store) {
        this.store = store;
    }
}