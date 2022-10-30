package com.approdevelopers.ranchites.Utils;

import com.approdevelopers.ranchites.Models.SearchResultModel;
import com.google.android.gms.maps.model.LatLng;

import java.util.Comparator;

public class SortLocationsWithDistance implements Comparator<SearchResultModel> {

    @Override
    public int compare(SearchResultModel o1, SearchResultModel o2) {

        double distance1 = o1.getDistanceFromUser();
        double distance2 = o2.getDistanceFromUser();


        if (distance1>distance2){
            return 1;
        }else if (distance1<distance2){
            return -1;
        }else {
            return 0;
        }

    }
}
