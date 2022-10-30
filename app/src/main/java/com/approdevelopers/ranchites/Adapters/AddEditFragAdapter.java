package com.approdevelopers.ranchites.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.approdevelopers.ranchites.Fragments.AddEditLocFragmentBasicDet;
import com.approdevelopers.ranchites.Fragments.AddEditLocFragmentDescDet;
import com.approdevelopers.ranchites.Fragments.AddEditLocFragmentMapDet;
import com.approdevelopers.ranchites.Fragments.AddEditLocFragmentTerms;

public class AddEditFragAdapter extends FragmentStateAdapter {
    private static final int NO_OF_PAGES=4;
    private final String locationDocId;

    private AddEditLocFragmentBasicDet basicDet;
    private AddEditLocFragmentDescDet descDet;
    private AddEditLocFragmentMapDet mapDet;
    private AddEditLocFragmentTerms termsDet;
    public AddEditFragAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle,String locationDocId) {
        super(fragmentManager, lifecycle);
        this.locationDocId = locationDocId;

        basicDet = new AddEditLocFragmentBasicDet(locationDocId);
        descDet = new AddEditLocFragmentDescDet(locationDocId);
        mapDet = new AddEditLocFragmentMapDet(locationDocId);
        termsDet = new AddEditLocFragmentTerms(locationDocId);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return basicDet;
            case 1:
                return descDet ;
            case 2:
                return mapDet ;
            case 3:
                return termsDet;
            default:
                return null;
        }

    }

    @Override
    public int getItemCount() {
        return NO_OF_PAGES;
    }



}
