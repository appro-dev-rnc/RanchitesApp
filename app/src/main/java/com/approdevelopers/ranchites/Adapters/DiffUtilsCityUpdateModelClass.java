package com.approdevelopers.ranchites.Adapters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.approdevelopers.ranchites.Models.CityUpdatesModel;

import java.util.Objects;

public class DiffUtilsCityUpdateModelClass extends DiffUtil.ItemCallback<CityUpdatesModel> {
    @Override
    public boolean areItemsTheSame(@NonNull CityUpdatesModel oldItem, @NonNull CityUpdatesModel newItem) {
        return Objects.equals(oldItem.getDocumentId(),newItem.getDocumentId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull CityUpdatesModel oldItem, @NonNull CityUpdatesModel newItem) {
        Log.i("content", "areContentsTheSame: ");
        return oldItem.toString().equals(newItem.toString());
    }
}
