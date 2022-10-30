package com.approdevelopers.ranchites.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.approdevelopers.ranchites.Models.ImagesModel;

import java.util.Objects;

public class DiffUtilsImagesClass extends DiffUtil.ItemCallback<ImagesModel> {

    @Override
    public boolean areItemsTheSame(@NonNull ImagesModel oldItem, @NonNull ImagesModel newItem) {
        return Objects.equals(oldItem.getDocumentId(), newItem.getDocumentId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull ImagesModel oldItem, @NonNull ImagesModel newItem) {
        return oldItem.toString().equals(newItem.toString());
    }
}
