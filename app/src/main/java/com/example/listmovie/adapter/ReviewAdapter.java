package com.example.listmovie.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.listmovie.R;
import com.example.listmovie.model.Reviews;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>{

    @SuppressWarnings("unused")
    private final static String LOG_TAG = ReviewAdapter.class.getSimpleName();

    private final ArrayList<Reviews> mReviews;
    private final OnItemClickListener onItemClickListener;

    public ReviewAdapter(ArrayList<Reviews> reviews, OnItemClickListener listener) {
        mReviews = reviews;
        onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void read_reviews(Reviews review, int position);
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reviews_list_container, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Reviews review = mReviews.get(position);

        holder.mReview = review;
        holder.mContentView.setText(review.getmContent());
        holder.mAuthorView.setText(review.getmAuthor());

        holder.mView.setOnClickListener(v -> onItemClickListener.read_reviews(review, holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        @BindView(R.id.review_content)
        TextView mContentView;
        @BindView(R.id.review_author)
        TextView mAuthorView;
        public Reviews mReview;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mView = view;
        }
    }

    public void add(List<Reviews> reviews) {
        mReviews.clear();
        mReviews.addAll(reviews);
        notifyDataSetChanged();
    }

    public ArrayList<Reviews> getReviews() {
        return mReviews;
    }
}
