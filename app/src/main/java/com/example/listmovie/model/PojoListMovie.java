package com.example.listmovie.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PojoListMovie {

    @SerializedName("id")
    @Expose
    private final String id;

    @SerializedName("vote_average")
    @Expose
    private final String voteAverage;

    @SerializedName("vote_count")
    @Expose
    private final String voteCount;

    @SerializedName("original_title")
    @Expose
    private final String originalTitle;

    @SerializedName("title")
    @Expose
    private final String title;

    @SerializedName("popularity")
    @Expose
    private final double popularity;

    @SerializedName("backdrop_path")
    @Expose
    private final String backdropPath;

    @SerializedName("overview")
    @Expose
    private final String overview;

    @SerializedName("release_date")
    @Expose
    private final String releaseDate;

    @SerializedName("poster_path")
    @Expose
    private final String posterPath;

    public PojoListMovie(String id, String voteAverage, String voteCount, String originalTitle, String title,
                         double popularity, String backdropPath, String overview, String releaseDate,
                         String posterPath) {
        this.id = id;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.originalTitle = originalTitle;
        this.title = title;
        this.popularity = popularity;
        this.backdropPath = backdropPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
    }

    public String getId() {
        return id;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getTitle() {
        return title;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }
}
