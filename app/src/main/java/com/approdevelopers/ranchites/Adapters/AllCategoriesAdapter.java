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

public class AllCategoriesAdapter extends ListAdapter<CategoriesModel,AllCategoriesAdapter.AllCategoriesViewHolder> {

    private int layoutId;


    public AllCategoriesAdapter(@NonNull DiffUtil.ItemCallback<CategoriesModel> diffCallback, int layoutId) {
        super(diffCallback);
        this.layoutId= layoutId;

    }


    @NonNull
    @Override
    public AllCategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutId,parent,false);

        return new AllCategoriesViewHolder(itemView,layoutId);
    }

    @Override
    public void onBindViewHolder(@NonNull AllCategoriesViewHolder holder, int position) {

        CategoriesModel category = getItem(position);

        GlideApp.with(holder.itemView.getContext()).load(category.getCategoryImageUrl())
                .thumbnail(GlideApp.with(holder.itemView.getContext()).load(category.getCategoryImageUrl()).override(30))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .timeout(20000)
                .dontTransform().placeholder(R.drawable.loading_pic_anim)
                .error(    GlideApp.with(holder.itemView.getContext()).load(category.getCategoryImageUrl())
                        .thumbnail(GlideApp.with(holder.itemView.getContext()).load(category.getCategoryImageUrl()).override(30))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .timeout(20000)
                        .dontTransform().placeholder(R.drawable.loading_pic_anim)
                        .into(holder.imgAllCategoryItem))
               .into(holder.imgAllCategoryItem);
        holder.txtAllCategoryTitle.setText(category.getCategoryName());

    }

     class AllCategoriesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imgAllCategoryItem;
        TextView txtAllCategoryTitle;
        public AllCategoriesViewHolder(View itemView, int layoutId){
            super(itemView);
            if (layoutId==R.layout.all_categories_item){
                imgAllCategoryItem = itemView.findViewById(R.id.img_all_categories_item);
                txtAllCategoryTitle = itemView.findViewById(R.id.txt_all_categories_item_title);
            }else if (layoutId==R.layout.near_me_categories_item){
                imgAllCategoryItem = itemView.findViewById(R.id.img_near_me_categories_item);
                txtAllCategoryTitle = itemView.findViewById(R.id.txt_near_me_categories_item_title);
            }

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            CategoriesModel model = getItem(getAdapterPosition());
            if (model!=null){
                Intent intent = new Intent(itemView.getContext(), SearchActivity.class);
                intent.putExtra("searchKeyword",model.getCategoryName());
                itemView.getContext().startActivity(intent);
            }


        }
    }
}
