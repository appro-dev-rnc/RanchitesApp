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
import com.approdevelopers.ranchites.Interfaces.PlaceReviewReportInterface;
import com.approdevelopers.ranchites.Models.ReviewModel;
import com.approdevelopers.ranchites.Models.UserBasicInfo;
import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.Repository.FirestoreRepository;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReviewsAdapter extends ListAdapter<ReviewModel,ReviewsAdapter.ReviewsViewHolder> {

    private final LifecycleOwner lifecycleOwner;
    private final PlaceReviewReportInterface placeReviewReportInterface;
    public ReviewsAdapter(@NonNull DiffUtil.ItemCallback<ReviewModel> diffCallback, LifecycleOwner lifecycleOwner, PlaceReviewReportInterface placeReviewReportInterface) {
        super(diffCallback);
        this.lifecycleOwner = lifecycleOwner;
        this.placeReviewReportInterface = placeReviewReportInterface;

    }

    @NonNull
    @Override
    public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_item,parent,false);
        return new ReviewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsViewHolder holder, int position) {

        ReviewModel model = getItem(position);
        holder.ratingReviewItem.setRating((float) model.getRating());
        holder.txtReviewStringReviewItem.setText(model.getReview());



        LiveData<UserBasicInfo> userInfo = FirestoreRepository.getInstance().getUserInfo(model.getUserId());
        userInfo.observe(lifecycleOwner, userBasicInfo -> {
            if (userBasicInfo!=null){
                if (userBasicInfo.getUserProfileImageUrl()!=null && !userBasicInfo.getUserProfileImageUrl().equals("")){
                    GlideApp.with(holder.itemView.getContext()).load(userBasicInfo.getUserProfileImageUrl())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .timeout(20000)
                            .error( GlideApp.with(holder.itemView.getContext()).load(userBasicInfo.getUserProfileImageUrl())
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .timeout(20000)
                                    .dontTransform().thumbnail(GlideApp.with(holder.itemView.getContext()).load(userBasicInfo.getUserProfileImageUrl()).override(30)).error(R.drawable.ic_person).into(holder.imgReviewItemProf))
                            .dontTransform().thumbnail(GlideApp.with(holder.itemView.getContext()).load(userBasicInfo.getUserProfileImageUrl()).override(30)).into(holder.imgReviewItemProf);
                }else {
                    holder.imgReviewItemProf.setImageResource(R.drawable.ic_person);
                }

                holder.txtUserNameReviewItem.setText(userBasicInfo.getUserName());
            }
        });


        Date newDate = new Date(model.getUploadTimestamp().getSeconds()*1000);
        SimpleDateFormat format = new SimpleDateFormat("E, dd MMMM yyyy HH:mm:ss ",Locale.ENGLISH);

        holder.txtReviewTimestamp.setText(format.format(newDate));


    }

     class ReviewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imgReviewItemProf;
        TextView txtUserNameReviewItem;
        TextView txtReviewStringReviewItem;
        TextView txtReviewTimestamp;
        AppCompatRatingBar ratingReviewItem;
        TextView txtReviewReport;
       public ReviewsViewHolder(View itemView){
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
            ReviewModel model = getItem(getAdapterPosition());
            if (placeReviewReportInterface!=null){
                placeReviewReportInterface.getPlaceReviewDetails(model);
            }
            String reported = "Reported";
            txtReviewReport.setText(reported);
            txtReviewReport.setClickable(false);
        }
    }
}
