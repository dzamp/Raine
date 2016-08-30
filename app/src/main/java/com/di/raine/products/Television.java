package com.di.raine.products;

import java.util.HashMap;

/**
 * Created by jim on 27/8/2016.
 */

public class Television implements Product {
    private String id;
    private String name;
    private String description;

    public Television() {
    }

    public Television(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public HashMap<String, String> dataInfo() {
        HashMap<String,String> info = new HashMap<>();
        info.put("id",id);
        info.put("name",name);
        info.put("description",description);
        return info;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {

    }

}
