package com.approdevelopers.ranchites.Adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.approdevelopers.ranchites.Activities.LocationCompleteDetailsPage;
import com.approdevelopers.ranchites.ApplicationFiles.GlideApp;
import com.approdevelopers.ranchites.Interfaces.NearMeRecyclerItemClickInterface;
import com.approdevelopers.ranchites.Models.SearchResultModel;
import com.approdevelopers.ranchites.R;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.card.MaterialCardView;

import java.util.Locale;


public class NearMeLocationsAdapter extends ListAdapter<SearchResultModel, NearMeLocationsAdapter.NearMeLocationsViewHolder> {

    private final NearMeRecyclerItemClickInterface nearMeRecyclerItemClickInterface;


    public NearMeLocationsAdapter(@NonNull DiffUtil.ItemCallback<SearchResultModel> diffCallback, NearMeRecyclerItemClickInterface nearMeRecyclerItemClickInterface) {
        super(diffCallback);
        this.nearMeRecyclerItemClickInterface = nearMeRecyclerItemClickInterface;
    }

    @NonNull
    @Override
    public NearMeLocationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.near_me_search_item, parent, false);

        return new NearMeLocationsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NearMeLocationsViewHolder holder, int position) {
        SearchResultModel searchModel = getItem(position);
        if (searchModel != null) {
            GlideApp.with(holder.itemView.getContext()).load(searchModel.getImageUrl())
                    .thumbnail(GlideApp.with(holder.itemView.getContext()).load(searchModel.getImageUrl()).override(30))
                    .placeholder(R.drawable.loading_pic_anim)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontTransform()
                    .timeout(20000)
                   .into(holder.imgNearMeSearchItem);
            holder.txtNearMeSearchTitle.setText(searchModel.getTitle());
            holder.txtNearMeSearchCategory.setText(searchModel.getCategory());
            holder.ratingNearMeSearchItem.setRating((float) searchModel.getOverallRating());

            double distance = searchModel.getDistanceFromUser();
            String distanceString = null;
            if (distance != 0) {
                distanceString = String.format(Locale.UK, "%.2f", distance / 1000) + "km away";
            }
            holder.txtNearMeItemDistanceFromUser.setText(distanceString);
        }
    }

    class NearMeLocationsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgNearMeSearchItem;
        TextView txtNearMeSearchTitle, txtNearMeSearchCategory, txtNearMeItemDistanceFromUser;
        AppCompatRatingBar ratingNearMeSearchItem;

        MaterialCardView imgNearMeItemDirections, imgNearMeItemFullScreen;

        public NearMeLocationsViewHolder(View itemView) {
            super(itemView);

            imgNearMeSearchItem = itemView.findViewById(R.id.img_near_me_search_item);
            txtNearMeSearchTitle = itemView.findViewById(R.id.txt_near_me_search_title);
            txtNearMeSearchCategory = itemView.findViewById(R.id.txt_near_me_search_category);
            ratingNearMeSearchItem = itemView.findViewById(R.id.rating_near_me_search_item);
            txtNearMeItemDistanceFromUser = itemView.findViewById(R.id.txt_near_me_item_distance_from_user);

            imgNearMeItemDirections = itemView.findViewById(R.id.img_near_me_item_direction_parent);
            imgNearMeItemFullScreen = itemView.findViewById(R.id.img_near_me_item_full_scr_parent);

            imgNearMeItemFullScreen.setOnClickListener(this);
            imgNearMeItemDirections.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            SearchResultModel model = getItem(getAdapterPosition());

            if (view.getId() == R.id.near_me_search_item_parent) {
                if (nearMeRecyclerItemClickInterface != null) {
                    nearMeRecyclerItemClickInterface.nearMeItemClicked(model.getDocumentId());
                }
            }

            if (view.getId() == R.id.img_near_me_item_direction_parent) {
                Toast.makeText(itemView.getContext(), "Directions" + model.getTitle(), Toast.LENGTH_SHORT).show();
                String geoUri = "google.navigation:q=" + model.getLocGeopoint().getLatitude() + "," + model.getLocGeopoint().getLongitude();

                Uri gmmIntentUri = Uri.parse(geoUri);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(itemView.getContext().getPackageManager()) != null) {
                    itemView.getContext().startActivity(mapIntent);

                } else {
                    Toast.makeText(itemView.getContext(), "Please download Google Maps to continue", Toast.LENGTH_SHORT).show();
                }
            }
            if (view.getId() == R.id.img_near_me_item_full_scr_parent) {
                if (model.getDocumentId() != null) {
                    Intent intent = new Intent(itemView.getContext(), LocationCompleteDetailsPage.class);
                    intent.putExtra("documentId", model.getDocumentId());
                    itemView.getContext().startActivity(intent);
                }

            }
        }
    }
}
