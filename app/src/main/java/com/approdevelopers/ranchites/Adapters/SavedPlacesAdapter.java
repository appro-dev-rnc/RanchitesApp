package com.approdevelopers.ranchites.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.approdevelopers.ranchites.Activities.LocationCompleteDetailsPage;
import com.approdevelopers.ranchites.ApplicationFiles.GlideApp;
import com.approdevelopers.ranchites.Models.PlacesBasicModel;
import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.Repository.FirestoreRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SavedPlacesAdapter extends ListAdapter<PlacesBasicModel,SavedPlacesAdapter.SavedPlacesViewHolder> {

    public SavedPlacesAdapter(@NonNull DiffUtil.ItemCallback<PlacesBasicModel> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public SavedPlacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_places_item,parent,false);

        return new SavedPlacesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedPlacesViewHolder holder, int position) {
        PlacesBasicModel placesBasicModel= getItem(position);
        if (placesBasicModel.getImageUrl()!=null){
            GlideApp.with(holder.itemView.getContext()).load(placesBasicModel.getImageUrl())
                    .timeout(20000)
                    .thumbnail(GlideApp.with(holder.itemView.getContext()).load(placesBasicModel.getImageUrl()).override(30))
                    .placeholder(R.drawable.loading_pic_anim)
                    .error( GlideApp.with(holder.itemView.getContext()).load(placesBasicModel.getImageUrl())
                            .timeout(20000)
                            .thumbnail(GlideApp.with(holder.itemView.getContext()).load(placesBasicModel.getImageUrl()).override(30))
                            .placeholder(R.drawable.loading_pic_anim)
                            .into(holder.imgSavedLocBanner))
                   .into(holder.imgSavedLocBanner);
        }
        holder.txtSavedLocTitle.setText(placesBasicModel.getTitle());
        holder.txtSavedLocCategory.setText(placesBasicModel.getCategory());
        holder.ratingSavedLocItem.setRating((float) placesBasicModel.getOverallRating());

        holder.btnSavedItemsOptions.setOnClickListener(view -> {
            //creating a popup menu
            PopupMenu popup = new PopupMenu(holder.itemView.getContext(), holder.btnSavedItemsOptions);
            //inflating menu from xml resource
            popup.inflate(R.menu.saved_item_menu);
            //adding click listener
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.nav_saved_loc_item_remove) {//handle menu1 click
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (currentUser != null) {
                        FirestoreRepository.getInstance().removeFromSavedLocation(placesBasicModel.getDocumentId(), currentUser.getUid());
                    }
                    return true;
                }
                return false;
            });
            //displaying the popup
            popup.show();
        });


    }

     class SavedPlacesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgSavedLocBanner;
        TextView txtSavedLocTitle,txtSavedLocCategory;
        AppCompatRatingBar ratingSavedLocItem;
        TextView btnSavedItemsOptions;
        public SavedPlacesViewHolder(View itemView){
            super(itemView);

            imgSavedLocBanner = itemView.findViewById(R.id.img_saved_loc_banner);
            txtSavedLocTitle = itemView.findViewById(R.id.txt_saved_loc_title);
            txtSavedLocCategory = itemView.findViewById(R.id.txt_saved_loc_category);
            ratingSavedLocItem = itemView.findViewById(R.id.rating_saved_loc);
            btnSavedItemsOptions = itemView.findViewById(R.id.txt_btn_saved_item_options);

            itemView.setOnClickListener(this);
            btnSavedItemsOptions.setClickable(true);
        }

        @Override
        public void onClick(View view) {

            if (view==itemView) {
                PlacesBasicModel model =getItem(getAdapterPosition());
                Intent detailPageIntent = new Intent(itemView.getContext(), LocationCompleteDetailsPage.class);
                detailPageIntent.putExtra("documentId",model.getDocumentId() );
                itemView.getContext().startActivity(detailPageIntent);
            }
        }
    }
}
