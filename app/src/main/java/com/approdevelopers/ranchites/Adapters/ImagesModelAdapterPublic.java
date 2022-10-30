package com.approdevelopers.ranchites.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.approdevelopers.ranchites.ApplicationFiles.GlideApp;
import com.approdevelopers.ranchites.Interfaces.ImagesModelItemClickInterface;
import com.approdevelopers.ranchites.Models.ImagesModel;
import com.approdevelopers.ranchites.R;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class ImagesModelAdapterPublic extends ListAdapter<ImagesModel, ImagesModelAdapterPublic.ProfImagesViewHolderPublic> {

    ImagesModelItemClickInterface imagesModelItemClickInterface;

    public ImagesModelAdapterPublic(@NonNull DiffUtil.ItemCallback<ImagesModel> diffCallback, ImagesModelItemClickInterface imagesModelItemClickInterface) {
        super(diffCallback);
        this.imagesModelItemClickInterface = imagesModelItemClickInterface;
    }

    @NonNull
    @Override
    public ProfImagesViewHolderPublic onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.user_profile_media_item;
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutId,parent,false);
        int imageId = R.id.img_user_media_images_item;
        return new ProfImagesViewHolderPublic(itemView, imageId);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesModelAdapterPublic.ProfImagesViewHolderPublic holder, int position) {
        ImagesModel imagesModel =getItem(position);

        if (imagesModel.getImageUrl()!=null){

            loadImageFromUrl(holder.itemView.getContext(),imagesModel.getImageUrl(),holder);


        }


    }

    private void loadImageFromUrl(Context context, String imageUrl, ImagesModelAdapterPublic.ProfImagesViewHolderPublic holder) {


        GlideApp.with(holder.itemView.getContext()).load(imageUrl)
                .timeout(20000)
                .thumbnail(GlideApp.with(context).load(imageUrl).override(30))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .dontTransform()
                .error(GlideApp.with(holder.itemView.getContext()).load(imageUrl).timeout(20000)
                        .thumbnail(GlideApp.with(context).load(imageUrl).override(30)).diskCacheStrategy(DiskCacheStrategy.NONE).dontTransform().placeholder(R.drawable.loading_pic_anim).error(R.drawable.ic_image_not_found).into(holder.profItemImage))
                .into(holder.profItemImage);

    }


    class ProfImagesViewHolderPublic extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView profItemImage;
        public ProfImagesViewHolderPublic(View itemView,int imageId){
            super(itemView);
            profItemImage = itemView.findViewById(imageId);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (imagesModelItemClickInterface!=null){
                imagesModelItemClickInterface.getItemPosition(getAdapterPosition());
            }

        }
    }
}