package com.example.listmovie.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PojoMovie implements Serializable {

    @SerializedName("page")
    @Expose
    private final int page;

    @SerializedName("results")
    @Expose
    private List<Movie> results = new ArrayList<>();

    public PojoMovie(int page, List<Movie> results) {
        this.page = page;
        this.results = results;
    }


    public int getPage() {
        return page;
    }

    public List<Movie> getResults() {
        return results;
    }

}
