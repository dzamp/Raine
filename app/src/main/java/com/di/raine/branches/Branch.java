package com.di.raine.branches;

/**
 * Created by jim on 5/9/2016.
 */

public class Branch {
    public String name;
    public String id;
    public  Locality locality;
    public boolean selected;
    public boolean reviewed;


    public Branch() {
    }

    public Branch(String name, String id, Locality locality) {
        this.name = name;
        this.id = id;
        this.locality = locality;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Locality getLocality() {
        return locality;
    }

    public void setLocality(Locality locality) {
        this.locality = locality;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


}
