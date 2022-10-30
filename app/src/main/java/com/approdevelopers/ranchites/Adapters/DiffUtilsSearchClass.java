package com.approdevelopers.ranchites.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.approdevelopers.ranchites.Models.SearchResultModel;

import java.util.Objects;

public class DiffUtilsSearchClass extends DiffUtil.ItemCallback<SearchResultModel> {
    @Override
    public boolean areItemsTheSame(@NonNull SearchResultModel oldItem, @NonNull SearchResultModel newItem) {
        return Objects.equals(oldItem.getDocumentId(), newItem.getDocumentId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull SearchResultModel oldItem, @NonNull SearchResultModel newItem) {
        return oldItem.toString().equals(newItem.toString());
    }
}
