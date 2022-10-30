package com.approdevelopers.ranchites.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.approdevelopers.ranchites.Activities.ImageFullScreen;
import com.approdevelopers.ranchites.ApplicationFiles.GlideApp;
import com.approdevelopers.ranchites.R;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

public class CityDetImagesAdapter extends RecyclerView.Adapter<CityDetImagesAdapter.CityDetViewHolder> {

    List<String> imagesUrl;


    public CityDetImagesAdapter(List<String> imagesUrl) {
        this.imagesUrl = imagesUrl;
    }

    @NonNull
    @Override
    public CityDetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_det_page_images_item,parent,false);

        return new CityDetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CityDetViewHolder holder, int position) {


        GlideApp.with(holder.itemView.getContext()).load(imagesUrl.get(holder.getAdapterPosition()))
                .timeout(20000)
                .thumbnail(GlideApp.with(holder.itemView.getContext()).load(imagesUrl.get(holder.getAdapterPosition())).override(30))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontTransform().placeholder(R.drawable.loading_pic_anim)
                .error(GlideApp.with(holder.itemView.getContext()).load(imagesUrl.get(holder.getAdapterPosition())).timeout(20000)
                        .thumbnail(GlideApp.with(holder.itemView.getContext()).load(imagesUrl.get(holder.getAdapterPosition())).override(30))

                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontTransform().placeholder(R.drawable.loading_pic_anim).error(R.drawable.ic_image_not_found).into(holder.imgCityDetImageItem))
                .into(holder.imgCityDetImageItem);
    }

    @Override
    public int getItemCount() {
        return imagesUrl.size();
    }

     class CityDetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgCityDetImageItem;
        CityDetViewHolder(View itemView){
            super(itemView);
            imgCityDetImageItem = itemView.findViewById(R.id.img_city_upd_det_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {


            ArrayList<String> imageUrls = new ArrayList<>(imagesUrl);
            Intent intent = new Intent(itemView.getContext(), ImageFullScreen.class);
            intent.putStringArrayListExtra("imageUrls", imageUrls);
            intent.putExtra("isAdmin",false);
            intent.putExtra("imagePosition",getAdapterPosition());
            itemView.getContext().startActivity(intent);
        }
    }
}
