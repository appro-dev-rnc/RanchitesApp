package com.approdevelopers.ranchites.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.approdevelopers.ranchites.Models.CategoriesModel;

import java.util.Objects;

public class DiffUtilsAllCategoriesClass extends DiffUtil.ItemCallback<CategoriesModel> {

    @Override
    public boolean areItemsTheSame(@NonNull CategoriesModel oldItem, @NonNull CategoriesModel newItem) {
        return Objects.equals(oldItem.getDocumentId(),newItem.getDocumentId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull CategoriesModel oldItem, @NonNull CategoriesModel newItem) {
        return oldItem.toString().equals(newItem.toString());
    }
}
