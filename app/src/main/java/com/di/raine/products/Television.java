package com.di.raine.products;

/**
 * Created by jim on 27/8/2016.
 */

public class Television extends Product {
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

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {

    }
}
