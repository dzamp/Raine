package com.di.raine.products;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by jim on 27/8/2016.
 */

public interface  Product {
//    private String name;
//    private String id;
////    private String description;
//
//    public Product() {
//    }
//
//    public Product(String name, String id /*, String description*/) {
//        this.name = name;
//        this.id = id;
////        this.description = description;
//    }

    public String getName();

    public void setName(String name);/* {
        this.name = name;
    }*/

    public String getId();/* {
        return id;
    }*/

    public void setId(String id);/* {
        this.id = id;
    }*/
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
    public String[] displayInfo();

}
