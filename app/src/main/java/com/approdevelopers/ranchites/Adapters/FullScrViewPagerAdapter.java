package com.approdevelopers.ranchites.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.approdevelopers.ranchites.ApplicationFiles.GlideApp;
import com.approdevelopers.ranchites.R;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jsibbold.zoomage.ZoomageView;

import java.util.ArrayList;

public class FullScrViewPagerAdapter extends RecyclerView.Adapter<FullScrViewPagerAdapter.FullScreenViewHolder> {

    ArrayList<String> imageUrlList;
    Context context;
    LayoutInflater layoutInflater ;

    public FullScrViewPagerAdapter(Context context,ArrayList<String> imageUrlList){
        this.context = context;
        this.imageUrlList = imageUrlList;

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public FullScreenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.full_scr_image_item,parent,false);
        return new FullScreenViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FullScreenViewHolder holder, int position) {
        String url = imageUrlList.get(position);
        if (url!=null && !url.equals("")){
            GlideApp.with(holder.itemView.getContext()).load(url).thumbnail(GlideApp.with(holder.itemView.getContext()
            ).load(url).override(30))
                    .timeout(20000)
                    .placeholder(R.drawable.loading_pic_anim)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontTransform().into(holder.imgFullScreenItem);
        }else {
            GlideApp.with(holder.itemView.getContext()).load(R.drawable.ic_image_not_found).into(holder.imgFullScreenItem);
        }

    }

    @Override
    public int getItemCount() {
        return imageUrlList.size();
    }


    static class FullScreenViewHolder extends RecyclerView.ViewHolder{
        ZoomageView imgFullScreenItem;
        public FullScreenViewHolder(View itemView){
            super(itemView);
            imgFullScreenItem = itemView.findViewById(R.id.img_full_scr_item);
        }
    }
}
