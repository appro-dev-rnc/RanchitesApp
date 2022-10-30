package com.approdevelopers.ranchites.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.approdevelopers.ranchites.Models.LocationModel;

import java.util.Objects;

public class DiffUtilsMyLocationClass extends DiffUtil.ItemCallback<LocationModel> {
    @Override
    public boolean areItemsTheSame(@NonNull LocationModel oldItem, @NonNull LocationModel newItem) {
        return  Objects.equals(oldItem.getDocumentId(), newItem.getDocumentId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull LocationModel oldItem, @NonNull LocationModel newItem) {
        return oldItem.toString().equals(newItem.toString());
    }
}
