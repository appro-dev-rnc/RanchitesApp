package com.approdevelopers.ranchites.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.approdevelopers.ranchites.Models.RecentlyViewedDetailedModel;

import java.util.Objects;

public class DiffUtilsRecentlyViewedClass extends DiffUtil.ItemCallback<RecentlyViewedDetailedModel> {
    @Override
    public boolean areItemsTheSame(@NonNull RecentlyViewedDetailedModel oldItem, @NonNull RecentlyViewedDetailedModel newItem) {
        return Objects.equals(oldItem.getDocumentId(),newItem.getDocumentId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull RecentlyViewedDetailedModel oldItem, @NonNull RecentlyViewedDetailedModel newItem) {
        return oldItem.toString().equals(newItem.toString());
    }
}
