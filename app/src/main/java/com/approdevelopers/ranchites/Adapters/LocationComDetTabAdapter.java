package com.approdevelopers.ranchites.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.approdevelopers.ranchites.Fragments.LocationAbout;
import com.approdevelopers.ranchites.Fragments.LocationMap;
import com.approdevelopers.ranchites.Fragments.LocationMedia;
import com.approdevelopers.ranchites.Fragments.LocationOwnerFrag;
import com.approdevelopers.ranchites.Fragments.LocationReviewsAndRatingsFrag;
import com.approdevelopers.ranchites.Fragments.LocationYourReview;

public class LocationComDetTabAdapter extends FragmentStateAdapter {

    int NUM_OF_TABS = 6;
    String locationId;

    public LocationComDetTabAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle,String locationId) {
        super(fragmentManager, lifecycle);
        this.locationId = locationId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new LocationMedia(locationId);
            case 1:
                return new LocationAbout(locationId);
            case 2:
               return new LocationMap(locationId);
            case 3:
                return new LocationOwnerFrag(locationId);
            case 4:
                return new LocationReviewsAndRatingsFrag(locationId);
            case 5:
                return new LocationYourReview(locationId);

            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return NUM_OF_TABS;
    }
}
