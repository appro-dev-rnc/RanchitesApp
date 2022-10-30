package com.approdevelopers.ranchites.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.approdevelopers.ranchites.Activities.MyLocationFullDetailPage;
import com.approdevelopers.ranchites.Adapters.DiffUtilsMyLocationClass;
import com.approdevelopers.ranchites.Adapters.MyLocationsAdapter;
import com.approdevelopers.ranchites.Interfaces.RecyclerViewItemClickInterface;
import com.approdevelopers.ranchites.Models.LocationModel;
import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.ViewModels.UserProfViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserMyProfileMyPlaces#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserMyProfileMyPlaces extends Fragment implements RecyclerViewItemClickInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView myLocRecyclerView;

    private MyLocationsAdapter myLocationsAdapter;

    private UserProfViewModel viewModel;
    private List<LocationModel> myLocations ;
    private TextView txtMyProfNoPlacesFoundError;

    public UserMyProfileMyPlaces() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserMyProfileMyPlaces.
     */
    // TODO: Rename and change types and number of parameters
    public static UserMyProfileMyPlaces newInstance(String param1, String param2) {
        UserMyProfileMyPlaces fragment = new UserMyProfileMyPlaces();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        viewModel = new ViewModelProvider(requireActivity()).get(UserProfViewModel.class);
        myLocations = new ArrayList<>();
        //init adapters
        myLocationsAdapter = new MyLocationsAdapter(new DiffUtilsMyLocationClass(),this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_my_profile_my_places, container, false);

        txtMyProfNoPlacesFoundError = view.findViewById(R.id.txt_my_prof_no_places_found_error);
        myLocRecyclerView = view.findViewById(R.id.recyclerMyLocations);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        myLocRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false));

        //observer for my location items
        viewModel.getCurrentUserLocationLiveData().observe(requireActivity(), locationModels -> {
            if (locationModels!= null){
                myLocations = locationModels;
                myLocationsAdapter.submitList(locationModels);
                myLocRecyclerView.setAdapter(myLocationsAdapter);

                myLocRecyclerView.setVisibility(View.VISIBLE);
                txtMyProfNoPlacesFoundError.setVisibility(View.GONE);
            }else {
                myLocRecyclerView.setVisibility(View.GONE);
                txtMyProfNoPlacesFoundError.setVisibility(View.VISIBLE);
            }
        });




    }

    @Override
    public void itemClick(int position) {
        Intent intent = new Intent(requireActivity(), MyLocationFullDetailPage.class);
        String locationDocId = null;
        if (myLocations!=null){
            locationDocId = myLocations.get(position).getDocumentId();
        }
        intent.putExtra("locationDocId",locationDocId);
        startActivity(intent);
    }
}