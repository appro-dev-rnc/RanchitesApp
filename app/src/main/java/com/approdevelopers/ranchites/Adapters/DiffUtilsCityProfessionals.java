package com.approdevelopers.ranchites.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.approdevelopers.ranchites.Models.CityProfessionalsModel;

import java.util.Objects;

public class DiffUtilsCityProfessionals extends DiffUtil.ItemCallback<CityProfessionalsModel> {
    @Override
    public boolean areItemsTheSame(@NonNull CityProfessionalsModel oldItem, @NonNull CityProfessionalsModel newItem) {
        return Objects.equals(oldItem.getuId(), newItem.getuId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull CityProfessionalsModel oldItem, @NonNull CityProfessionalsModel newItem) {
        return oldItem.toString().equals(newItem.toString());
    }
}
