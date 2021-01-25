package com.example.listmovie.adapter;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listmovie.R;
import com.example.listmovie.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.listmovie.data.Api.UriImage;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> implements Filterable {

    private final List<Movie> OriList;
    private List<Movie> FilterList;

    private int lastPosition = -1;
    private final SparseBooleanArray selectedItem;
    private OnItemClickListener monItemClickListener;
    Context ctx;
    private final ItemFilter mFilter = new ItemFilter();

    public interface  OnItemClickListener{
        void onItemClick(View view, Movie obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.monItemClickListener = mItemClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView NameMovie;
        ImageView imgThumbnail;

        ViewHolder(View itemView) {
            super(itemView);
            NameMovie = itemView.findViewById(R.id.movie_title);
            imgThumbnail = itemView.findViewById(R.id.movie_thumbnail);

        }
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list_movie, parent, false);
        return new ViewHolder(view);
    }

    public MovieListAdapter(Context context, List<Movie> items) {
        ctx = context;
        OriList = items;
        FilterList = items;
        selectedItem = new SparseBooleanArray();
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Movie movie = FilterList.get(position);

        holder.NameMovie.setText(movie.getOriginalTitle());

        setAnimation(holder.itemView, position);
        Log.i("Mobile",movie.getPosterPath());

        Picasso.get().load(movie.getPosterPath())
                .placeholder(R.drawable.image_placeholder)
                .into(holder.imgThumbnail);

        holder.imgThumbnail.setOnClickListener(v -> {
            if (monItemClickListener !=null){
                Animation fadein = new AlphaAnimation(0,1);
                fadein.setDuration(50);
                holder.imgThumbnail.startAnimation(fadein);
                monItemClickListener.onItemClick(v, movie,position);
            }
        });
        holder.imgThumbnail.setActivated(selectedItem.get(position,false));

    }


    // Animation appear with you scroll down only
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.INFINITE, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(new Random().nextInt(1001));//to make duration random number between [0,501)
            viewToAnimate.startAnimation(anim);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return FilterList.size();
    }


    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint){
            String query = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();
            final List<Movie> list = OriList;
            final List<Movie> result_list = new ArrayList<>(list.size());

            for (int i =0; i < list.size(); i++){
                String str_title = list.get(i).getOriginalTitle();
                if (str_title.toLowerCase().contains(query)){
                    result_list.add(list.get(i));
                }
            }

            results.values = result_list;
            results.count = result_list.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            FilterList = (List<Movie>) results.values;
            notifyDataSetChanged();
        }

    }

}
