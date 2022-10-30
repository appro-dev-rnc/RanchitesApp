package com.approdevelopers.ranchites.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.approdevelopers.ranchites.Models.PlacesBasicModel;

import java.util.Objects;

public class DiffUtilsPlacesBasicClass extends DiffUtil.ItemCallback<PlacesBasicModel> {
    @Override
    public boolean areItemsTheSame(@NonNull PlacesBasicModel oldItem, @NonNull PlacesBasicModel newItem) {
        return Objects.equals(oldItem.getDocumentId(), newItem.getDocumentId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull PlacesBasicModel oldItem, @NonNull PlacesBasicModel newItem) {
        return oldItem.toString().equals(newItem.toString());
    }
}
