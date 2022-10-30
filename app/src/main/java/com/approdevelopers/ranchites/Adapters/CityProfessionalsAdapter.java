package com.approdevelopers.ranchites.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.approdevelopers.ranchites.Activities.UserCompleteDetailPage;
import com.approdevelopers.ranchites.ApplicationFiles.GlideApp;
import com.approdevelopers.ranchites.Models.CityProfessionalsModel;
import com.approdevelopers.ranchites.R;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import de.hdodenhof.circleimageview.CircleImageView;

public class CityProfessionalsAdapter extends ListAdapter<CityProfessionalsModel, CityProfessionalsAdapter .CityProfessionalsViewHolder> {

    public CityProfessionalsAdapter(@NonNull DiffUtil.ItemCallback<CityProfessionalsModel> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public CityProfessionalsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_professional_item,parent,false);

        return new CityProfessionalsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CityProfessionalsViewHolder holder, int position) {

        CityProfessionalsModel model = getItem(position);
        GlideApp.with(holder.itemView.getContext()).load(model.getUserProfileImageUrl())
                .thumbnail(GlideApp.with(holder.itemView.getContext()).load(model.getUserProfileImageUrl()).override(40))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .timeout(20000)
                .dontTransform()
                .error( GlideApp.with(holder.itemView.getContext()).load(model.getUserProfileImageUrl())
                        .thumbnail(GlideApp.with(holder.itemView.getContext()).load(model.getUserProfileImageUrl()).override(40))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .timeout(20000)
                        .dontTransform().placeholder(R.drawable.loading_pic_anim)
                        .into(holder.imgCityProfessionalItem))
               .into(holder.imgCityProfessionalItem);

        holder.txtCityProfessionalItemTitle.setText(model.getUserName());
        holder.txtCityProfessionalItemProfession.setText(model.getUserProfession());
        holder.ratingCityProfessionalItem.setRating((float) model.getUserRating());
    }

    class CityProfessionalsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView imgCityProfessionalItem;
        TextView txtCityProfessionalItemTitle, txtCityProfessionalItemProfession;
        AppCompatRatingBar ratingCityProfessionalItem;
        public CityProfessionalsViewHolder(View itemView){
            super(itemView);
            imgCityProfessionalItem = itemView.findViewById(R.id.img_city_professional_item);
            txtCityProfessionalItemTitle = itemView.findViewById(R.id.txt_city_professional_item_title);
            txtCityProfessionalItemProfession = itemView.findViewById(R.id.txt_city_professional_item_profession);
            ratingCityProfessionalItem = itemView.findViewById(R.id.rating_city_professional_item);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            CityProfessionalsModel model  = getItem(getAdapterPosition());
            Intent intent = new Intent(itemView.getContext(), UserCompleteDetailPage.class);
            intent.putExtra("userId",model.getuId());
            itemView.getContext().startActivity(intent);
        }
    }
}
