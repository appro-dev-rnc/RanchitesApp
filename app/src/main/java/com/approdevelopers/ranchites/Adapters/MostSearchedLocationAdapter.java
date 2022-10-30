package com.approdevelopers.ranchites.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.approdevelopers.ranchites.Activities.LocationCompleteDetailsPage;
import com.approdevelopers.ranchites.ApplicationFiles.GlideApp;
import com.approdevelopers.ranchites.Models.MostSearchedLocation;
import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.Repository.FirebaseUserRepository;
import com.approdevelopers.ranchites.Repository.FirestoreRepository;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseUser;

public class MostSearchedLocationAdapter extends ListAdapter<MostSearchedLocation,MostSearchedLocationAdapter.MostSearchedViewHolder> {

    LifecycleOwner lifecycleOwner;
    public MostSearchedLocationAdapter(@NonNull DiffUtil.ItemCallback<MostSearchedLocation> diffCallback,LifecycleOwner lifecycleOwner) {
        super(diffCallback);
        this.lifecycleOwner = lifecycleOwner;
    }

    @NonNull
    @Override
    public MostSearchedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.most_searched_item,parent,false);
        return new MostSearchedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MostSearchedViewHolder holder, int position) {

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.fade_in);
        MostSearchedLocation location = getItem(position);
        GlideApp.with(holder.itemView.getContext()).load(location.getImageUrl())
                .thumbnail(GlideApp.with(holder.itemView.getContext()).load(location.getImageUrl()).override(30))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontTransform()
                .timeout(20000)
                .error( GlideApp.with(holder.itemView.getContext()).load(location.getImageUrl())
                        .thumbnail(GlideApp.with(holder.itemView.getContext()).load(location.getImageUrl()).override(30))

                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontTransform()
                        .timeout(20000)
                        .placeholder(R.drawable.loading_pic_anim)
                        .into(holder.imgMostSearchedItem))
                .into(holder.imgMostSearchedItem);


        holder.txtMostSearchedTitle.setText(location.getTitle());
        holder.txtMostSearchedCategory.setText(location.getCategory());
        holder.ratingBarMostSearched.setRating((float) location.getOverallRating());
        FirebaseUser user = FirebaseUserRepository.getInstance().getCurrentUser();
        String currentUserId = null;
        if (user!=null){
            currentUserId= user.getUid();
        }


        LiveData<Boolean> saved = FirestoreRepository.getInstance().checkLocationSaved(location.getDocumentId(),currentUserId);
        saved.observe(lifecycleOwner, resultBool -> holder.btnMostSearchedSavedToggle.setChecked(resultBool));


        holder.btnMostSearchedSavedToggle.setOnClickListener(view -> {
            String userId = null;
            if (user!=null){
                userId = user.getUid();
            }
            if (holder.btnMostSearchedSavedToggle.isChecked()){

                FirestoreRepository.getInstance().addToSavedLocation(location.getDocumentId(),userId);

            }else {
                FirestoreRepository.getInstance().removeFromSavedLocation(location.getDocumentId(),userId);


            }
        });

        holder.itemView.startAnimation(animation);
    }

     class MostSearchedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtMostSearchedTitle;
        TextView txtMostSearchedCategory;
        AppCompatRatingBar ratingBarMostSearched;
        ImageView imgMostSearchedItem;
        AppCompatToggleButton btnMostSearchedSavedToggle;

        public MostSearchedViewHolder(View itemView){
            super(itemView);
            txtMostSearchedTitle = itemView.findViewById(R.id.txt_most_searched_title);
            txtMostSearchedCategory = itemView.findViewById(R.id.txt_most_searched_category);
            ratingBarMostSearched = itemView.findViewById(R.id.rating_most_searched_item);
            imgMostSearchedItem = itemView.findViewById(R.id.img_most_searched);
            btnMostSearchedSavedToggle = itemView.findViewById(R.id.btn_most_searched_saved_loc);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            MostSearchedLocation location = getItem(getAdapterPosition());
            Intent mostIntent = new Intent(view.getContext(), LocationCompleteDetailsPage.class);
            mostIntent.putExtra("documentId",location.getDocumentId());
            view.getContext().startActivity(mostIntent);
        }
    }
}
