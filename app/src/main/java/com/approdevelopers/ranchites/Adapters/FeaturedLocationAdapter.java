package com.approdevelopers.ranchites.Adapters;

import android.content.Context;
import android.content.Intent;
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

import com.approdevelopers.ranchites.Activities.LocationCompleteDetailsPage;
import com.approdevelopers.ranchites.ApplicationFiles.GlideApp;
import com.approdevelopers.ranchites.Models.FeaturedLocation;
import com.approdevelopers.ranchites.R;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class FeaturedLocationAdapter extends ListAdapter<FeaturedLocation, FeaturedLocationAdapter.FeaturedLocationViewHolder> {

    public FeaturedLocationAdapter(@NonNull DiffUtil.ItemCallback<FeaturedLocation> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public FeaturedLocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.featured_location_item,parent,false);
        return new FeaturedLocationViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull FeaturedLocationViewHolder holder, int position) {
         FeaturedLocation featuredLocation = getItem(position);
        GlideApp.with(holder.itemView.getContext()).load(featuredLocation.getImageUrl())
                .thumbnail(GlideApp.with(holder.itemView.getContext()).load(featuredLocation.getImageUrl()).override(40))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .timeout(20000)
                .dontTransform().placeholder(R.drawable.loading_pic_anim)
                .error( GlideApp.with(holder.itemView.getContext()).load(featuredLocation.getImageUrl())
                        .thumbnail(GlideApp.with(holder.itemView.getContext()).load(featuredLocation.getImageUrl()).override(40))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .timeout(20000)
                        .dontTransform().placeholder(R.drawable.loading_pic_anim)
                        .into(holder.imgFeatured))
                .into(holder.imgFeatured);


        holder.txtFeaturedTitle.setText(featuredLocation.getTitle());
        holder.txtFeaturedCategory.setText(featuredLocation.getCategory());
        holder.featuredRatingBar.setRating((float) featuredLocation.getOverallRating());

    }

    class FeaturedLocationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtFeaturedTitle;
        TextView txtFeaturedCategory;
        AppCompatRatingBar featuredRatingBar;
        ImageView imgFeatured;
        Context context;

        public FeaturedLocationViewHolder(View itemView) {
            super(itemView);
            this.context = itemView.getContext();
            txtFeaturedTitle = itemView.findViewById(R.id.txt_featured_loc_title);
            txtFeaturedCategory = itemView.findViewById(R.id.txt_featured_loc_category);
            featuredRatingBar = itemView.findViewById(R.id.featured_loc_rating_bar);
            imgFeatured = itemView.findViewById(R.id.img_featured_loc);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            FeaturedLocation location = getItem(getAdapterPosition());
            Intent featIntent = new Intent(view.getContext(), LocationCompleteDetailsPage.class);
            featIntent.putExtra("documentId",location.getDocumentId());


            view.getContext().startActivity(featIntent);
        }
    }


}

