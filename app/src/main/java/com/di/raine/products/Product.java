package com.di.raine.products;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by jim on 27/8/2016.
 */

public interface  Product {



    public String getName();
    public void setName(String name);
    public String getId();

    public void setId(String id);



    public String getDescription() ;

    public HashMap<String,String> dataInfo();

}
