package com.example.listmovie.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listmovie.R;
import com.example.listmovie.model.ListGenre;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenreListAdapter extends RecyclerView.Adapter<GenreListAdapter.ViewHolder> implements Filterable {

    private final List<ListGenre> OriList;
    private List<ListGenre> FilterList;

    private int lastPosition = -1;
    private final SparseBooleanArray selectedItem;
    private OnItemClickListener monItemClickListener;
    Context ctx;
    private final ItemFilter mFilter = new ItemFilter();

    public interface  OnItemClickListener{
        void onItemClick(View view, ListGenre obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.monItemClickListener = mItemClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView NameGenre;
        LinearLayout lyt_parent;

        ViewHolder(View itemView) {
            super(itemView);
            NameGenre = itemView.findViewById(R.id.name_genre);
            lyt_parent = itemView.findViewById(R.id.lyt_genre);

        }
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list_genre, parent, false);
        return new ViewHolder(view);
    }

    public GenreListAdapter(Context context, List<ListGenre> items) {
        ctx = context;
        OriList = items;
        FilterList = items;
        selectedItem = new SparseBooleanArray();
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ListGenre listGenre = FilterList.get(position);

        holder.NameGenre.setText(listGenre.getName());

        holder.lyt_parent.setOnClickListener(view -> {
            if (monItemClickListener !=null){
                Animation fadein = new AlphaAnimation(0,1);
                fadein.setDuration(50);
                holder.lyt_parent.startAnimation(fadein);
                monItemClickListener.onItemClick(view, listGenre,position);
            }
        });
        holder.lyt_parent.setActivated(selectedItem.get(position,false));

        setAnimation(holder.itemView, position);
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
            final List<ListGenre> list = OriList;
            final List<ListGenre> result_list = new ArrayList<>(list.size());

            for (int i =0; i < list.size(); i++){
                String str_title = list.get(i).getName();
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
            FilterList = (List<ListGenre>) results.values;
            notifyDataSetChanged();
        }

    }

}
