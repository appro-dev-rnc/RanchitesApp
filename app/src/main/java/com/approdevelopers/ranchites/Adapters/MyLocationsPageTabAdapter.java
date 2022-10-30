package com.approdevelopers.ranchites.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.approdevelopers.ranchites.Fragments.MyLocationAbout;
import com.approdevelopers.ranchites.Fragments.MyLocationImages;
import com.approdevelopers.ranchites.Fragments.MyLocationMapView;
import com.approdevelopers.ranchites.Fragments.MyLocationReviewAndRatings;

public class MyLocationsPageTabAdapter extends FragmentStateAdapter {

    int NUM_OF_TABS = 4;
    String locationDocId;

    public MyLocationsPageTabAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle,String locationDocId) {
        super(fragmentManager, lifecycle);
        this.locationDocId = locationDocId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new MyLocationAbout(locationDocId);
            case 1:
                return new MyLocationImages(locationDocId);
            case 2:
                return new MyLocationMapView(locationDocId);
            case 3:
                return new MyLocationReviewAndRatings(locationDocId);
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return NUM_OF_TABS;
    }
}
