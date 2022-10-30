package com.approdevelopers.ranchites.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.approdevelopers.ranchites.Fragments.UserMyProfileAbout;
import com.approdevelopers.ranchites.Fragments.UserMyProfileImages;
import com.approdevelopers.ranchites.Fragments.UserMyProfileMyPlaces;
import com.approdevelopers.ranchites.Fragments.UserMyProfileMyReviews;

public class UserMyProfileTabAdapter extends FragmentStateAdapter {

    int NUM_OF_TABS = 4;

    public UserMyProfileTabAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new UserMyProfileAbout();
            case 1:
                return new UserMyProfileImages();
            case 2:
                return new UserMyProfileMyPlaces();
            case 3:
                return new UserMyProfileMyReviews();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return NUM_OF_TABS;
    }
}
