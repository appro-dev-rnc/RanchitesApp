package com.approdevelopers.ranchites.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.approdevelopers.ranchites.Fragments.Media;
import com.approdevelopers.ranchites.Fragments.ReviewsAndRatings;
import com.approdevelopers.ranchites.Fragments.UserDetAbout;
import com.approdevelopers.ranchites.Fragments.UserYourReviews;

public class UserComDetTabAdapter extends FragmentStateAdapter {

    int NUM_OF_TABS = 4;
    String userId;

    public UserComDetTabAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle,String userId) {
        super(fragmentManager, lifecycle);
        this.userId = userId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new UserDetAbout(userId);
            case 1:
                return new Media(userId);
            case 2:
                return new ReviewsAndRatings(userId);
            case 3:
                return new UserYourReviews(userId);

            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return NUM_OF_TABS;
    }


}
