package com.approdevelopers.ranchites.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.approdevelopers.ranchites.Models.BestRatedLocation;

import java.util.Objects;

public class DiffUtilsBestRatedClass extends DiffUtil.ItemCallback<BestRatedLocation> {
    @Override
    public boolean areItemsTheSame(@NonNull BestRatedLocation oldItem, @NonNull BestRatedLocation newItem) {
        return Objects.equals(oldItem.getDocumentId(),newItem.getDocumentId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull BestRatedLocation oldItem, @NonNull BestRatedLocation newItem) {
        return oldItem.toString().equals(newItem.toString());
    }
}
