package com.approdevelopers.ranchites.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.approdevelopers.ranchites.Models.ReviewModel;

import java.util.Objects;

public class DiffUtilsReviewsClass extends DiffUtil.ItemCallback<ReviewModel> {
    @Override
    public boolean areItemsTheSame(@NonNull ReviewModel oldItem, @NonNull ReviewModel newItem) {
        return Objects.equals(oldItem.getUserId(), newItem.getUserId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull ReviewModel oldItem, @NonNull ReviewModel newItem) {
        return oldItem.toString().equals(newItem.toString());
    }
}
