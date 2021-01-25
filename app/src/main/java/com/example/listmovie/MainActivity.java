package com.example.listmovie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listmovie.activity.ListMovie;
import com.example.listmovie.adapter.GenreListAdapter;
import com.example.listmovie.data.Api;
import com.example.listmovie.data.NetworkClient;
import com.example.listmovie.model.Genre;
import com.example.listmovie.model.ListGenre;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.listmovie.utils.Utils.ErrorMessage;
import static com.example.listmovie.utils.Utils.getToken;

public class MainActivity extends AppCompatActivity {

    View lytNotFound,lytSomethingWrong;
    TextView tvNotFound,tvSomethingWrong;
    RecyclerView recyclerView;
    ProgressBar pgLoading;
    public List<ListGenre> listG = new ArrayList<>();
    GenreListAdapter mGenre;
    Retrofit retrofit;
    Api mappingApis;
//    EditText edSearch;
//    boolean isSearch = true;
//    ImageView Search;
//    TextView Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrofit = NetworkClient.getRetrofitClient();
        mappingApis = retrofit.create(Api.class);

        InitView();

        getListGenre();

    }

    private void InitView(){

        lytNotFound = findViewById(R.id.lyt_not_found);
        tvNotFound = findViewById(R.id.tv_no_data);

        lytSomethingWrong = findViewById(R.id.lyt_something_wrong);
        tvSomethingWrong = findViewById(R.id.tv_something_wrong);

        recyclerView = findViewById(R.id.rc_list_genre);

        pgLoading = findViewById(R.id.pg_list_genre);
    }

    private void Clear(){
        listG.clear();
        if (mGenre!=null){
            mGenre.notifyDataSetChanged();
        }
    }

    private void getListGenre(){
        pgLoading.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        lytNotFound.setVisibility(View.GONE);
        lytSomethingWrong.setVisibility(View.GONE);
        Clear();
        Call<Genre> call = mappingApis.getGenre(getToken(), Api.ApiKey,"en-US");
        call.enqueue(new Callback<Genre>() {
            @Override
            public void onResponse(@NonNull Call<Genre> call, @NonNull Response<Genre> response) {
                if (response.isSuccessful()){
                    assert response.body() != null;
                    listG = response.body().getGenres();
                    if (listG!=null){
                        if (listG.size()!=0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            lytNotFound.setVisibility(View.GONE);
                            final LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                            recyclerView.setLayoutManager(layoutManager);

                            mGenre = new GenreListAdapter(MainActivity.this, listG);
                            mGenre.setOnItemClickListener((view, obj, position) -> {
                                Intent i = new Intent();
                                i.setClass(MainActivity.this, ListMovie.class);
                                i.putExtra("IdGenre",obj.getId());
                                startActivity(i);
                            });
                            recyclerView.setAdapter(mGenre);
                            if (mGenre!=null){
                                mGenre.notifyDataSetChanged();
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
            public void onFailure(@NonNull Call<Genre> call, @NonNull Throwable t) {
                recyclerView.setVisibility(View.GONE);
                lytNotFound.setVisibility(View.GONE);
                lytSomethingWrong.setVisibility(View.VISIBLE);
                tvSomethingWrong.setText(t.getMessage());
                pgLoading.setVisibility(View.GONE);
            }
        });

    }


}