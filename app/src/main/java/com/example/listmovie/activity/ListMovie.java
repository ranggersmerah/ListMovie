package com.example.listmovie.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.listmovie.R;
import com.example.listmovie.adapter.MovieListAdapter;
import com.example.listmovie.data.Api;
import com.example.listmovie.data.NetworkClient;
import com.example.listmovie.model.Movie;
import com.example.listmovie.model.PojoMovie;
import com.example.listmovie.model.PojoListMovie;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.listmovie.utils.Utils.ErrorMessage;
import static com.example.listmovie.utils.Utils.getToken;

public class ListMovie extends AppCompatActivity {

    View lytNotFound,lytSomethingWrong;
    TextView tvNotFound,tvSomethingWrong;
    RecyclerView recyclerView;
    ProgressBar pgLoading;
    public List<Movie> listM = new ArrayList<>();
    MovieListAdapter mMovie;
    Retrofit retrofit;
    Api mappingApis;
    int IdGenre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_movie);

        Bundle b = getIntent().getExtras();
        if (b!=null){
            IdGenre = Integer.parseInt(b.getString("IdGenre"));
        }

        retrofit = NetworkClient.getRetrofitClient();
        mappingApis = retrofit.create(Api.class);

        InitVIew();

    }

    private void InitVIew(){

        lytNotFound = findViewById(R.id.lyt_not_found);
        tvNotFound = findViewById(R.id.tv_no_data);

        lytSomethingWrong = findViewById(R.id.lyt_something_wrong);
        tvSomethingWrong = findViewById(R.id.tv_something_wrong);

        recyclerView = findViewById(R.id.rc_list_movie);

        pgLoading = findViewById(R.id.pg_list_movie);

        getListGenreMovie();

    }

    private void Clear(){
        listM.clear();
        if (mMovie!=null){
            mMovie.notifyDataSetChanged();
        }
    }

    private void getListGenreMovie(){
        pgLoading.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        lytNotFound.setVisibility(View.GONE);
        lytSomethingWrong.setVisibility(View.GONE);
        Clear();
        Call<PojoMovie> call = mappingApis.getMovie(getToken(), Api.ApiKey,"en-US","1",IdGenre);
        call.enqueue(new Callback<PojoMovie>() {
            @Override
            public void onResponse(@NonNull Call<PojoMovie> call, @NonNull Response<PojoMovie> response) {
                if (response.isSuccessful()){
                    assert response.body() != null;
                    listM = response.body().getResults();
                    Log.i("Mobile", listM.get(1).getOriginalTitle());
                    if (listM!=null){
                        if (listM.size()!=0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            lytNotFound.setVisibility(View.GONE);
                            final GridLayoutManager layoutManager = new GridLayoutManager(ListMovie.this,
                                    getResources()
                                    .getInteger(R.integer.number_of_grid_columns));
                            recyclerView.setLayoutManager(layoutManager);
                            mMovie = new MovieListAdapter(ListMovie.this,listM);
                            mMovie.setOnItemClickListener((view, obj, position) -> {
                                Intent intent = new Intent();
                                intent.setClass(ListMovie.this, DetailMovie.class);
                                intent.putExtra(MovieDetailFragment.ARG_MOVIE, obj);
                                startActivity(intent);
                            });
                            recyclerView.setAdapter(mMovie);
                            if (mMovie!=null){
                                mMovie.notifyDataSetChanged();
                            }
                        }else {
                            recyclerView.setVisibility(View.GONE);
                            lytNotFound.setVisibility(View.VISIBLE);
                            tvNotFound.setText(R.string.no_data_to_show);
                        }
                    }else {
                        recyclerView.setVisibility(View.GONE);
                        lytNotFound.setVisibility(View.VISIBLE);
                        tvNotFound.setText(R.string.no_data_to_show);
                    }
                    lytSomethingWrong.setVisibility(View.GONE);
                }else {
                    recyclerView.setVisibility(View.GONE);
                    lytNotFound.setVisibility(View.GONE);
                    lytSomethingWrong.setVisibility(View.VISIBLE);
                    tvSomethingWrong.setText(ErrorMessage(response.raw().code(),"GET"));
                }
                pgLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<PojoMovie> call, @NonNull Throwable t) {
                recyclerView.setVisibility(View.GONE);
                lytNotFound.setVisibility(View.GONE);
                lytSomethingWrong.setVisibility(View.VISIBLE);
                tvSomethingWrong.setText(t.getMessage());
                pgLoading.setVisibility(View.GONE);
            }
        });

    }

}