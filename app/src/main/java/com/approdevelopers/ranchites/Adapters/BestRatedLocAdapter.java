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
import com.approdevelopers.ranchites.Models.BestRatedLocation;
import com.approdevelopers.ranchites.R;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
public class BestRatedLocAdapter extends ListAdapter<BestRatedLocation, BestRatedLocAdapter.BestRatedLocViewHolder> {

    int layoutId;

    public BestRatedLocAdapter(@NonNull DiffUtil.ItemCallback<BestRatedLocation> diffCallback, int layoutId) {
        super(diffCallback);
        this.layoutId = layoutId;
    }

    @NonNull
    @Override
    public BestRatedLocViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new BestRatedLocViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BestRatedLocViewHolder holder, int position) {
        BestRatedLocation loc = getItem(position);

        GlideApp.with(holder.itemView.getContext()).load(loc.getImageUrl())
                .thumbnail(GlideApp.with(holder.itemView.getContext()).load(loc.getImageUrl()).override(30))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .timeout(20000)
                .dontTransform().placeholder(R.drawable.loading_pic_anim)
                .error( GlideApp.with(holder.itemView.getContext()).load(loc.getImageUrl())
                        .thumbnail(GlideApp.with(holder.itemView.getContext()).load(loc.getImageUrl()).override(30))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .timeout(20000)
                        .dontTransform().placeholder(R.drawable.loading_pic_anim)
                        .into(holder.imgBestRatedLoc))
                 .into(holder.imgBestRatedLoc);




        holder.txtBestRatedLocTitle.setText(loc.getTitle());
        String ratingCompleteStr = "Ratings : "+ loc.getOverallRating()+ "/5";
        holder.txtBestRatedLocRatings.setText(ratingCompleteStr);

        holder.ratingBestRatedItem.setRating((float) loc.getOverallRating());
        if (layoutId == R.layout.best_rated_item) {
            holder.txtBestRatedLocCategory.setText(loc.getCategory());
        } else if (layoutId == R.layout.best_rated_detailed_item) {
            String categoryString = "Category :" + loc.getCategory();
            holder.txtBestRatedLocCategory.setText(categoryString);
        }
    }

    class BestRatedLocViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgBestRatedLoc;
        TextView txtBestRatedLocTitle;
        TextView txtBestRatedLocRatings;
        TextView txtBestRatedLocCategory;
        AppCompatRatingBar ratingBestRatedItem;
        Context context;

        BestRatedLocViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            if (layoutId == R.layout.best_rated_item) {
                imgBestRatedLoc = itemView.findViewById(R.id.img_best_rated);
                txtBestRatedLocTitle = itemView.findViewById(R.id.txt_best_rated_title);
                txtBestRatedLocRatings = itemView.findViewById(R.id.txt_best_rated_ratings);
                txtBestRatedLocCategory = itemView.findViewById(R.id.txt_best_rated_category);
                ratingBestRatedItem = itemView.findViewById(R.id.rating_best_rated_item);
            } else if (layoutId == R.layout.best_rated_detailed_item) {
                imgBestRatedLoc = itemView.findViewById(R.id.img_best_rated_det);
                txtBestRatedLocTitle = itemView.findViewById(R.id.txt_best_rated_det_title);
                txtBestRatedLocRatings = itemView.findViewById(R.id.txt_best_rated_det_ratings);
                txtBestRatedLocCategory = itemView.findViewById(R.id.txt_best_rated_det_category);
                ratingBestRatedItem = itemView.findViewById(R.id.rating_best_rated_det_item);
            }


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            BestRatedLocation location = getItem(getAdapterPosition());
            Intent intent = new Intent(context, LocationCompleteDetailsPage.class);
            intent.putExtra("documentId", location.getDocumentId());
            context.startActivity(intent);
        }
    }
}
