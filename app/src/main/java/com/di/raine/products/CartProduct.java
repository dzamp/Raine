package com.di.raine.products;

import com.di.raine.branches.Branch;

/**
 * Created by jim on 7/9/2016.
 */

public class CartProduct  {
    private Product productInCart;
    private float price;
    private Branch branch;

    public CartProduct() {
    }

    public CartProduct(Product productInCart, float price, Branch branch) {
        this.productInCart = productInCart;
        this.price = price;
        this.branch = branch;
    }

    public Product getProductInCart() {
        return productInCart;
    }

    public void setProductInCart(Product productInCart) {
        this.productInCart = productInCart;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }
}
