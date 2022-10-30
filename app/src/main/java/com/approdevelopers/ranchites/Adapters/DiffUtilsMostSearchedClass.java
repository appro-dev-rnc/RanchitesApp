package com.approdevelopers.ranchites.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.approdevelopers.ranchites.Models.MostSearchedLocation;

import java.util.Objects;

public class DiffUtilsMostSearchedClass extends DiffUtil.ItemCallback<MostSearchedLocation> {


    @Override
    public boolean areItemsTheSame(@NonNull MostSearchedLocation oldItem, @NonNull MostSearchedLocation newItem) {
        return Objects.equals(oldItem.getDocumentId(), newItem.getDocumentId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull MostSearchedLocation oldItem, @NonNull MostSearchedLocation newItem) {
        return oldItem.toString().equals(newItem.toString());
    }
}
