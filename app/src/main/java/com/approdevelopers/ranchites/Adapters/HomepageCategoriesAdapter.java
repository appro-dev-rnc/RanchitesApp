package com.approdevelopers.ranchites.Adapters;


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

import com.approdevelopers.ranchites.Activities.SearchActivity;
import com.approdevelopers.ranchites.ApplicationFiles.GlideApp;
import com.approdevelopers.ranchites.Models.CategoriesModel;
import com.approdevelopers.ranchites.R;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


public class HomepageCategoriesAdapter extends ListAdapter<CategoriesModel,HomepageCategoriesAdapter.HomepageCategoriesViewHolder> {



    public HomepageCategoriesAdapter(@NonNull DiffUtil.ItemCallback<CategoriesModel> diffCallback) {
        super(diffCallback);

    }

    @NonNull
    @Override
    public HomepageCategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_home_page_item,parent,false);

        return new HomepageCategoriesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomepageCategoriesViewHolder holder, int position) {
        CategoriesModel modelCat = getItem(position);
        holder.txtHomePageCatItem.setText(modelCat.getCategoryName());
        GlideApp.with(holder.itemView.getContext()).load(modelCat.getCategoryImageUrl())
                .thumbnail(GlideApp.with(holder.itemView.getContext()).load(modelCat.getCategoryImageUrl()).override(30))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontTransform()
                .timeout(20000)
                .error( GlideApp.with(holder.itemView.getContext()).load(modelCat.getCategoryImageUrl())
                        .thumbnail(GlideApp.with(holder.itemView.getContext()).load(modelCat.getCategoryImageUrl()).override(30))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontTransform()
                        .timeout(20000)

                        .into(holder.imgHomePageCatItem))
                .into(holder.imgHomePageCatItem);

    }


     class HomepageCategoriesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtHomePageCatItem;
        ImageView imgHomePageCatItem;
        public HomepageCategoriesViewHolder(View itemView){
            super(itemView);

            txtHomePageCatItem = itemView.findViewById(R.id.txt_home_page_cat_item_title);
            imgHomePageCatItem = itemView.findViewById(R.id.img_home_page_cat_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            CategoriesModel model = getItem(getAdapterPosition());
            Intent intent = new Intent(itemView.getContext(), SearchActivity.class);
            intent.putExtra("searchKeyword",model.getCategoryName());
            itemView.getContext().startActivity(intent);
        }
    }
}
