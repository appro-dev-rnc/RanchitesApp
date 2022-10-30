package com.approdevelopers.ranchites.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.approdevelopers.ranchites.Models.UserReviewModel;

import java.util.Objects;

public class DiffUtilsUserReviewClass extends DiffUtil.ItemCallback<UserReviewModel> {
    @Override
    public boolean areItemsTheSame(@NonNull UserReviewModel oldItem, @NonNull UserReviewModel newItem) {
        return Objects.equals(oldItem.getUserId(), newItem.getUserId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull UserReviewModel oldItem, @NonNull UserReviewModel newItem) {
        return oldItem.toString().equals(newItem.toString());
    }
}
