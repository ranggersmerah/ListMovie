package com.example.listmovie.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ListGenre implements Serializable {

    @SerializedName("id")
    @Expose
    private final String id;

    @SerializedName("name")
    @Expose
    private final String name;

    public ListGenre(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
