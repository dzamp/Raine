package com.di.raine.products;

import android.support.annotation.Nullable;

/**
 * Created by jim on 1/9/2016.
 */

public class SearchObject {
    private String id;
    private String doc_id;
    private String doc_type;
    private String name;
    private String description;
    private String category;
    private double score;

    @Nullable
    public Product convertToProduct(){
        if(category.contains("Laptop"))
            return new Laptop(doc_id,name,description);
        else if (category.contains("Desktop"))
            return new Desktop(doc_id,name,description);
        else if (category.contains("Sound"))
            return new Sound(doc_id,name,description);
        else if (category.contains("HomeCinema"))
            return new HomeCinema(doc_id,name,description);
        else if (category.toLowerCase().contains("television") || category.toLowerCase().contains("tv")  )
            return new Television(doc_id,name,description);
        else return null;
    }

    public SearchObject() {
    }

    public SearchObject(String id, String doc_id, String doc_type, String name, String description, String category, double score) {
        this.id = id;
        this.doc_id = doc_id;
        this.doc_type = doc_type;
        this.name = name;
        this.description = description;
        this.category = category;
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }

    public String getDoc_type() {
        return doc_type;
    }

    public void setDoc_type(String doc_type) {
        this.doc_type = doc_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}