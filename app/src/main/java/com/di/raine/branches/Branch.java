package com.di.raine.branches;

/**
 * Created by jim on 5/9/2016.
 */

public class Branch {
    private String name;
    private String id;
    private Locality locality;

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


}
