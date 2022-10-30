package com.approdevelopers.ranchites.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.approdevelopers.ranchites.Models.FeaturedLocation;

import java.util.Objects;

public class DiffUtilsFeaturedLocationClass extends DiffUtil.ItemCallback<FeaturedLocation> {


    @Override
    public boolean areItemsTheSame(@NonNull FeaturedLocation oldItem, @NonNull FeaturedLocation newItem) {
        return Objects.equals(oldItem.getDocumentId(), newItem.getDocumentId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull FeaturedLocation oldItem, @NonNull FeaturedLocation newItem) {

        return oldItem.toString().equals(newItem.toString());
    }
}
