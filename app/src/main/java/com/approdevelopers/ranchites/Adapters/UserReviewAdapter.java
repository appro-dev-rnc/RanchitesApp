package com.approdevelopers.ranchites.Adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.approdevelopers.ranchites.ApplicationFiles.GlideApp;
import com.approdevelopers.ranchites.Interfaces.UserReviewReportInterface;
import com.approdevelopers.ranchites.Models.UserBasicInfo;
import com.approdevelopers.ranchites.Models.UserReviewModel;
import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.Repository.FirestoreRepository;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UserReviewAdapter extends ListAdapter<UserReviewModel,UserReviewAdapter.UserReviewViewHolder> {

    private LifecycleOwner lifecycleOwner;
    private UserReviewReportInterface userReviewReportInterface;
    public UserReviewAdapter(@NonNull DiffUtil.ItemCallback<UserReviewModel> diffCallback,LifecycleOwner lifecycleOwner,UserReviewReportInterface userReviewReportInterface) {
        super(diffCallback);
        this.lifecycleOwner = lifecycleOwner;
        this.userReviewReportInterface = userReviewReportInterface;
    }

    @NonNull
    @Override
    public UserReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_item,parent,false);
        return new UserReviewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserReviewViewHolder holder, int position) {

        UserReviewModel model = getItem(position);
        holder.ratingReviewItem.setRating((float) model.getRating());
        holder.txtReviewStringReviewItem.setText(model.getReview());

        LiveData<UserBasicInfo> userInfo = FirestoreRepository.getInstance().getUserInfo(model.getUserId());
        userInfo.observe(lifecycleOwner, userBasicInfo -> {
            if (userBasicInfo!=null){
                GlideApp.with(holder.itemView.getContext()).load(userBasicInfo.getUserProfileImageUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontTransform()
                        .timeout(20000)
                        .error( GlideApp.with(holder.itemView.getContext()).load(userBasicInfo.getUserProfileImageUrl())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .dontTransform()
                                .timeout(20000)
                                .error(R.drawable.ic_person)
                                .into(holder.imgReviewItemProf))
                        .into(holder.imgReviewItemProf);
                holder.txtUserNameReviewItem.setText(userBasicInfo.getUserName());
            }
        });

        Date newDate = new Date(model.getReviewTimestamp().getSeconds()*1000);
        SimpleDateFormat format = new SimpleDateFormat("E, dd MMMM yyyy HH:mm:ss ", Locale.ENGLISH);

        holder.txtReviewTimestamp.setText(format.format(newDate));

    }

     class UserReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imgReviewItemProf;
        TextView txtUserNameReviewItem;
        TextView txtReviewStringReviewItem;
        AppCompatRatingBar ratingReviewItem;
        TextView txtReviewTimestamp;
        TextView txtReviewReport;
        public UserReviewViewHolder(View itemView){
            super(itemView);

            imgReviewItemProf = itemView.findViewById(R.id.img_review_item_prof);
            txtReviewStringReviewItem = itemView.findViewById(R.id.txt_review_string_review_item);
            txtUserNameReviewItem = itemView.findViewById(R.id.txt_user_name_review_item);
            ratingReviewItem = itemView.findViewById(R.id.rating_review_item);
            txtReviewTimestamp = itemView.findViewById(R.id.txt_review_timestamp);
            txtReviewReport = itemView.findViewById(R.id.txt_review_report);

            txtReviewReport.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            UserReviewModel model = getItem(getAdapterPosition());
            if (userReviewReportInterface!=null){
                userReviewReportInterface.getReviewData(model);
            }
            String reported = "Reported";
            txtReviewReport.setText(reported);
            txtReviewReport.setClickable(false);
        }
    }
}
