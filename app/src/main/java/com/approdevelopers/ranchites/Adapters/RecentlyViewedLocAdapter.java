package com.approdevelopers.ranchites.Adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.approdevelopers.ranchites.Activities.LocationCompleteDetailsPage;
import com.approdevelopers.ranchites.Activities.UserCompleteDetailPage;
import com.approdevelopers.ranchites.ApplicationFiles.GlideApp;
import com.approdevelopers.ranchites.Models.RecentlyViewedDetailedModel;
import com.approdevelopers.ranchites.R;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.card.MaterialCardView;


public class RecentlyViewedLocAdapter extends ListAdapter<RecentlyViewedDetailedModel,RecentlyViewedLocAdapter.RecentlyViewedLocViewHolder> {

    public RecentlyViewedLocAdapter(@NonNull DiffUtil.ItemCallback<RecentlyViewedDetailedModel> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public RecentlyViewedLocViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recently_viewed_item,parent,false);
        return new RecentlyViewedLocViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentlyViewedLocViewHolder holder, int position) {
        RecentlyViewedDetailedModel location = getItem(position);

        GlideApp.with(holder.itemView.getContext()).load(location.getImageUrl())
                .thumbnail(GlideApp.with(holder.itemView.getContext()).load(location.getImageUrl()).override(30))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .timeout(20000)
                .dontTransform()
                .error(GlideApp.with(holder.itemView.getContext()).load(location.getImageUrl())
                        .thumbnail(GlideApp.with(holder.itemView.getContext()).load(location.getImageUrl()).override(50,50))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .timeout(20000)
                        .dontTransform().placeholder(R.drawable.loading_pic_anim)
                        .into(holder.imgRecentlyViewedItem))
                 .into(holder.imgRecentlyViewedItem);



        holder.txtRecentlyViewedLocTitle.setText(location.getTitle());
    }

    class RecentlyViewedLocViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
     ImageView imgRecentlyViewedItem;
     TextView txtRecentlyViewedLocTitle;
     Context context;
     MaterialCardView recentdetParentView;
     RecentlyViewedLocViewHolder(View itemView){
         super(itemView);
         this.context = itemView.getContext();
         imgRecentlyViewedItem = itemView.findViewById(R.id.img_recently_viewed_item);
         txtRecentlyViewedLocTitle = itemView.findViewById(R.id.txt_recently_viewed_title);
         recentdetParentView = itemView.findViewById(R.id.recent_det_parent_view);

         itemView.setOnClickListener(this);

     }

        @Override
        public void onClick(View view) {
            RecentlyViewedDetailedModel location = getItem(getAdapterPosition());
            int documentType = location.getDocumentType();
            if (documentType==1){
                Intent recentIntent = new Intent(view.getContext(), LocationCompleteDetailsPage.class);
                recentIntent.putExtra("documentId",location.getDocumentId());
                view.getContext().startActivity(recentIntent);
            }
            if (documentType==2){
                Intent recentIntent = new Intent(view.getContext(), UserCompleteDetailPage.class);
                recentIntent.putExtra("userId",location.getDocumentId());
                recentIntent.putExtra("documentId",location.getDocumentId());
                view.getContext().startActivity(recentIntent);
            }

        }
    }
}
