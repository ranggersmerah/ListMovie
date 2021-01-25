package com.example.listmovie.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listmovie.R;
import com.example.listmovie.adapter.ReviewAdapter;
import com.example.listmovie.adapter.TrailerAdapter;
import com.example.listmovie.dbase.MovieContract;
import com.example.listmovie.model.Movie;
import com.example.listmovie.model.Reviews;
import com.example.listmovie.model.Trailers;
import com.example.listmovie.utils.NetworkUtils;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailFragment extends Fragment implements
        TrailerAdapter.OnItemClickListener, TrailersTask.Listener, ReviewsTask.Listener, ReviewAdapter.OnItemClickListener{

    public static final String LOG_TAG = MovieDetailFragment.class.getSimpleName();

    public static final String ARG_MOVIE = "ARG_MOVIE";
    public static final String EXTRA_TRAILERS = "EXTRA_TRAILERS";
    public static final String EXTRA_REVIEWS = "EXTRA_REVIEWS";

    private Movie mMovie;
    private TrailerAdapter mTrailerListAdapter;
    private ReviewAdapter mReviewAdapter;
    private ShareActionProvider mShareActionProvider;
    private LiveData<List<Movie>> movies;
    public static List<Movie> updated_list;   //This is updated favorite movie list

    @BindView(R.id.trailer_list)
    RecyclerView mRecyclerViewTrailers;
    @BindView(R.id.review_list)
    RecyclerView mRecyclerViewReviews;

    @BindView(R.id.movie_title)
    TextView mMovieTitleView;
    @BindView(R.id.movie_overview)
    TextView mMovieOverviewView;
    @BindView(R.id.movie_release_date)
    TextView mMovieReleaseDateView;
    @BindView(R.id.movie_user_rating)
    TextView mMovieRatingView;
    @BindView(R.id.movie_poster)
    ImageView mMoviePosterView;

    @BindView(R.id.button_watch_trailer)
    Button mButtonWatchTrailer;


    public MovieDetailFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_MOVIE)) {
            mMovie = getArguments().getParcelable(ARG_MOVIE);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout)
                activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null && activity instanceof DetailMovie) {
            appBarLayout.setTitle(mMovie.getOriginalTitle());
        }

        ImageView movieBackdrop = ((ImageView) activity.findViewById(R.id.movie_backdrop));
        if (movieBackdrop != null) {
            Picasso.get()
                    .load(mMovie.getBackdropPath())
                    .config(Bitmap.Config.RGB_565)
                    .into(movieBackdrop);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_details, container, false);
        ButterKnife.bind(this, rootView);

        mMovieTitleView.setText(mMovie.getOriginalTitle());
        mMovieOverviewView.setText(mMovie.getOverview());
        mMovieReleaseDateView.setText(mMovie.getReleaseDate());
        Picasso.get()
                .load(mMovie.getPosterPath())
                .config(Bitmap.Config.RGB_565)
                .into(mMoviePosterView);

        load_trailers(savedInstanceState);
        load_reviews(savedInstanceState);
        Log.d(LOG_TAG, MovieContract.MovieEntry.CONTENT_URI.toString());
        /*IF savedInstanceState == null*/

        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_TRAILERS)) {
            List<Trailers> trailers = savedInstanceState.getParcelableArrayList(EXTRA_TRAILERS);
            mTrailerListAdapter.add(trailers);
            mButtonWatchTrailer.setEnabled(true);
        } else {
            getTrailers();
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_REVIEWS)) {
            List<Reviews> reviews = savedInstanceState.getParcelableArrayList(EXTRA_REVIEWS);
            mReviewAdapter.add(reviews);
        } else {
            getReviews();
        }
        Log.d(LOG_TAG, "Current selected movie id is: " + String.valueOf(mMovie.getId()));

        return rootView;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Trailers> trailers = mTrailerListAdapter.getTrailers();
        if (trailers != null && !trailers.isEmpty()) {
            outState.putParcelableArrayList(EXTRA_TRAILERS, trailers);
        }

        ArrayList<Reviews> reviews = mReviewAdapter.getReviews();
        if (reviews != null && !reviews.isEmpty()) {
            outState.putParcelableArrayList(EXTRA_REVIEWS, reviews);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void load_reviews(Bundle savedInstanceState) {
        // List of reviews (Vertically Arranged)
        mReviewAdapter = new ReviewAdapter(new ArrayList<Reviews>(), this);
        mRecyclerViewReviews.setAdapter(mReviewAdapter);

        // Request for the reviews if only savedInstanceState == null
        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_REVIEWS)) {
            List<Reviews> reviews = savedInstanceState.getParcelableArrayList(EXTRA_REVIEWS);
            mReviewAdapter.add(reviews);
        } else {
            getReviews();
        }
    }

    private void load_trailers(Bundle savedInstanceState) {
        //List of Trailers (Horizontal Layout)
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewTrailers.setLayoutManager(layoutManager);
        mTrailerListAdapter = new TrailerAdapter(new ArrayList<Trailers>(), this);
        mRecyclerViewTrailers.setAdapter(mTrailerListAdapter);
        mRecyclerViewTrailers.setNestedScrollingEnabled(false);

        //  Request for the trailers if only savedInstanceState == null
        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_TRAILERS)) {
            List<Trailers> trailers = savedInstanceState.getParcelableArrayList(EXTRA_TRAILERS);
            mTrailerListAdapter.add(trailers);
            mButtonWatchTrailer.setEnabled(true);
        } else {
            getTrailers();
        }
    }

    private void getTrailers() {
        if (NetworkUtils.networkStatus(getContext())) {
            TrailersTask task = new TrailersTask((TrailersTask.Listener) MovieDetailFragment.this);
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,  mMovie.getId());
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setTitle(getString(R.string.title_network_alert));
            dialog.setMessage(getString(R.string.message_network_alert));
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    private void getReviews() {
        ReviewsTask task = new ReviewsTask((ReviewsTask.Listener) MovieDetailFragment.this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mMovie.getId());
    }

    /*Implemented method from TrailerTask Class*/
    @Override
    public void onLoadFinished(List<Trailers> trailers) {
        mTrailerListAdapter.add(trailers);
        mButtonWatchTrailer.setEnabled(!trailers.isEmpty());
        if (mTrailerListAdapter.getItemCount() > 0) {
            Trailers trailer = mTrailerListAdapter.getTrailers().get(0);
            if (trailer != null) {
                refresh_share_action_provider(trailer);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_detail_fragment, menu);
        MenuItem shareTrailerMenuItem = menu.findItem(R.id.share_trailer);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareTrailerMenuItem);
    }


    /*User is able to share the trailer url with others*/
    private void refresh_share_action_provider(Trailers trailers) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mMovie.getOriginalTitle());
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, trailers.getName() + ": "
                + trailers.getTrailerUrl());
        mShareActionProvider.setShareIntent(sharingIntent);
    }

    /*Implemented method from ReviewsTask Class*/
    @Override
    public void on_reviews_loaded(List<Reviews> reviews) {
        mReviewAdapter.add(reviews);
    }

    /*Overidden Methods from Tariler Task and ReviewTask Classes*/
    @Override
    public void watch_trailer(Trailers trailers, int position) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailers.getTrailerUrl())));
    }

    @Override
    public void read_reviews(Reviews review, int position) {
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse(review.getmUrl())));
    }

}
