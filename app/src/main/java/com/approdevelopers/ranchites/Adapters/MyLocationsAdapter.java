package com.approdevelopers.ranchites.Adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.approdevelopers.ranchites.ApplicationFiles.GlideApp;
import com.approdevelopers.ranchites.Interfaces.RecyclerViewItemClickInterface;
import com.approdevelopers.ranchites.Models.LocationModel;
import com.approdevelopers.ranchites.R;

public class MyLocationsAdapter extends ListAdapter<LocationModel, MyLocationsAdapter.MyLocationViewHolder> {

    private final RecyclerViewItemClickInterface recyclerViewItemClickInterface;
    private static final String ENABLED_STR = "Enabled";
    private static final String DISABLED_STR = "Disabled";

    public MyLocationsAdapter(@NonNull DiffUtil.ItemCallback<LocationModel> diffCallback, RecyclerViewItemClickInterface recyclerViewItemClickInterface) {
        super(diffCallback);
        this.recyclerViewItemClickInterface = recyclerViewItemClickInterface;
    }

    @NonNull
    @Override
    public MyLocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_loc_recycler_item, parent, false);
        return new MyLocationViewHolder(itemView, recyclerViewItemClickInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyLocationViewHolder holder, int position) {
        LocationModel location = getItem(position);
        GlideApp.with(holder.itemView.getContext()).load(location.getImageUrl())
                .timeout(20000)
                .thumbnail(GlideApp.with(holder.itemView.getContext()).load(location.getImageUrl()).override(
                        30
                )).placeholder(R.drawable.loading_pic_anim)
                .error(GlideApp.with(holder.itemView.getContext()).load(location.getImageUrl())
                        .timeout(20000)
                        .thumbnail(GlideApp.with(holder.itemView.getContext()).load(location.getImageUrl()).override(
                                30
                        )).placeholder(R.drawable.loading_pic_anim)
                        .into(holder.imgMyLocBanner))
                .into(holder.imgMyLocBanner);
        holder.txtMyLocTitle.setText(location.getTitle());
        holder.txtMyLocSearchCount.setText(String.valueOf(location.getSearchCount()));
        holder.txtMyLocCategory.setText(location.getCategory());
        holder.ratingMyLoc.setRating((float) location.getOverallRating());

        if (location.getLocationShareEnabled()) {
            holder.txtMyLocLocationShareEnabled.setText(ENABLED_STR);
        } else {
            holder.txtMyLocLocationShareEnabled.setText(DISABLED_STR);
        }

    }

    static class MyLocationViewHolder extends RecyclerView.ViewHolder {

        TextView txtMyLocTitle, txtMyLocCategory, txtMyLocSearchCount, txtMyLocLocationShareEnabled;
        AppCompatRatingBar ratingMyLoc;
        ImageView imgMyLocBanner;

        MyLocationViewHolder(View itemView, RecyclerViewItemClickInterface recyclerViewItemClickInterface) {
            super(itemView);
            txtMyLocTitle = itemView.findViewById(R.id.txtMyLocTitle);
            txtMyLocCategory = itemView.findViewById(R.id.txtMyLocCategory);
            txtMyLocSearchCount = itemView.findViewById(R.id.txtMyLocSearchCount);
            ratingMyLoc = itemView.findViewById(R.id.ratingMyLoc);
            imgMyLocBanner = itemView.findViewById(R.id.imgMyLocBanner);
            txtMyLocLocationShareEnabled = itemView.findViewById(R.id.txtMyLocLocSearchEnabled);

            itemView.setOnClickListener(view -> {
                if (recyclerViewItemClickInterface != null) {
                    int adapterPosition = getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        recyclerViewItemClickInterface.itemClick(adapterPosition);
                    }
                }
            });
        }
    }
}
