package com.approdevelopers.ranchites.Adapters;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.approdevelopers.ranchites.Activities.CityUpdatesDetailPage;
import com.approdevelopers.ranchites.ApplicationFiles.GlideApp;
import com.approdevelopers.ranchites.Models.CityUpdatesModel;
import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.Repository.FirestoreRepository;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class CityUpdatesAdapter extends ListAdapter<CityUpdatesModel, CityUpdatesAdapter.CityUpdateViewHolder> {

    String currentUserId;

    public CityUpdatesAdapter(@NonNull DiffUtil.ItemCallback<CityUpdatesModel> diffCallback, String currentUserId ) {
        super(diffCallback);
        this.currentUserId = currentUserId;

    }

    @NonNull
    @Override
    public CityUpdateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_updates_item,parent,false);

        return new CityUpdateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityUpdateViewHolder holder, int position) {
        CityUpdatesModel updatesModel = getItem(position);

        GlideApp.with(holder.itemView.getContext()).load(updatesModel.getImageUrl())
                .placeholder(R.drawable.loading_pic_anim)
                .timeout(20000)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontTransform()
                .thumbnail(GlideApp.with(holder.itemView.getContext()).load(updatesModel.getImageUrl()).override(50))
                .error( GlideApp.with(holder.itemView.getContext()).load(updatesModel.getImageUrl())
                        .thumbnail(GlideApp.with(holder.itemView.getContext()).load(updatesModel.getImageUrl()).override(60,60))
                        .placeholder(R.drawable.loading_pic_anim)
                        .timeout(20000)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontTransform()
                        .into(holder.imgCityUpdate))
                 .into(holder.imgCityUpdate);



        holder.txtTitle.setText(updatesModel.getTitleText());
        holder.txtDesc.setText(updatesModel.getDescText());


        LiveData<Boolean> likedByUser = FirestoreRepository.getInstance().checkUserLike(updatesModel.getDocumentId(),currentUserId);
        likedByUser.observe((LifecycleOwner) holder.itemView.getContext(), aBoolean -> holder.toggleLikeBtn.setChecked(aBoolean));

        LiveData<Integer> likesCount = FirestoreRepository.getInstance().getCityUpdateLikesCount(updatesModel.getDocumentId());
        likesCount.observe((LifecycleOwner) holder.itemView.getContext(), integer -> holder.txtCityUpdLikeCount.setText(String.valueOf(integer)));


    }

    class CityUpdateViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgCityUpdate;
        TextView txtTitle;
        TextView txtDesc;
        ToggleButton toggleLikeBtn;
        TextView txtCityUpdLikeCount;

        public CityUpdateViewHolder(View itemView){
            super(itemView);

            //hooks
            imgCityUpdate = itemView.findViewById(R.id.img_city_update);
            txtTitle = itemView.findViewById(R.id.txt_city_update_title);
            txtDesc = itemView.findViewById(R.id.txt_city_update_desc);
            toggleLikeBtn  = itemView.findViewById(R.id.toggle_city_upd_like_btn);
            txtCityUpdLikeCount = itemView.findViewById(R.id.txt_city_like_count);

            toggleLikeBtn.setOnCheckedChangeListener((compoundButton, b) -> {
                String cityUpdateId = getItem(getAdapterPosition()).getDocumentId();

                if (b){

                        FirestoreRepository.getInstance().likeCityUpdate(cityUpdateId,currentUserId);


                }else {

                        FirestoreRepository.getInstance().unLikeCityUpdate(cityUpdateId,currentUserId);


                }
            });

            imgCityUpdate.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            CityUpdatesModel model = getItem(getAdapterPosition());
            Intent intent  = new Intent(itemView.getContext(), CityUpdatesDetailPage.class);
            intent.putExtra("cityUpdateId",model.getDocumentId());
            itemView.getContext().startActivity(intent);
        }

    }
}
