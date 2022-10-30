package com.approdevelopers.ranchites.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.approdevelopers.ranchites.LoadingScreenFragments.OnBoardingFragOne;
import com.approdevelopers.ranchites.LoadingScreenFragments.OnBoardingFragmentThree;
import com.approdevelopers.ranchites.LoadingScreenFragments.OnBoardingFragmentTwo;

public class OnboardingPagesAdapter extends FragmentStateAdapter {

    private int num_of_pages;
    public OnboardingPagesAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle,int num_of_pages) {
        super(fragmentManager, lifecycle);
        this.num_of_pages = num_of_pages;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new OnBoardingFragOne();
            case 1:
                return new OnBoardingFragmentTwo();
            case 2:
                return new OnBoardingFragmentThree();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return num_of_pages;
    }
}
