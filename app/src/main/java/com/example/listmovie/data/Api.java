package com.example.listmovie.data;

import com.example.listmovie.model.Genre;
import com.example.listmovie.model.PojoMovie;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Query;

public interface Api {

    String ApiKey = "f2ab6e3cbb511b188815519cd6f53ecc";
    String Token = "9590eba9e64b3f360da387d0aeed9b750c325273";
    String UriImage = "https://image.tmdb.org/t/p/w500";

    @GET("genre/movie/list")
    Call<Genre> getGenre(@HeaderMap Map<String, String> Token,
                         @Query("api_key") String ApiKey,
                         @Query("language") String Language);

    @GET("discover/movie")
    Call<PojoMovie> getMovie(@HeaderMap Map<String, String> Token,
                             @Query("api_key") String ApiKey,
                             @Query("language") String Language,
                             @Query("page") String Page,
                             @Query("with_genres") int GenreID);

}
