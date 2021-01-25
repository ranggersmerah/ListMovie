package com.example.listmovie.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Genre implements Serializable {

    @SerializedName("genres")
    @Expose
    private List<ListGenre> genres = new ArrayList<>();

    public Genre(List<ListGenre> genres) {
        this.genres = genres;
    }

    public List<ListGenre> getGenres() {
        return genres;
    }
}
