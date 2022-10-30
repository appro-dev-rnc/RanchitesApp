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
import android.widget.RelativeLayout;
import com.approdevelopers.ranchites.Activities.AddNewLocationActivity;
import com.approdevelopers.ranchites.Activities.AddPlaceBusinessGuide;
import com.approdevelopers.ranchites.Activities.MyLocationFullDetailPage;
import com.approdevelopers.ranchites.Adapters.DiffUtilsMyLocationClass;
import com.approdevelopers.ranchites.Adapters.MyLocationsAdapter;
import com.approdevelopers.ranchites.Interfaces.RecyclerViewItemClickInterface;
import com.approdevelopers.ranchites.Models.LocationModel;
import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.ViewModels.MainViewModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyPlacesMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyPlacesMainFragment extends Fragment implements RecyclerViewItemClickInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MainViewModel viewModel;
    private MyLocationsAdapter myLocationsAdapter;
    private List<LocationModel> myLocations ;
    private RecyclerView myLocRecyclerView;
    private FloatingActionButton btnMainMyPlaceAddGuide;
    private RelativeLayout mainMyPlaceErrorParent;
    private ExtendedFloatingActionButton btnFloatAddNewPlace;




    public MyPlacesMainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyPlacesMainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyPlacesMainFragment newInstance(String param1, String param2) {
        MyPlacesMainFragment fragment = new MyPlacesMainFragment();
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
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_places_main, container, false);
        myLocRecyclerView = view.findViewById(R.id.recycler_main_page_my_place);
        btnMainMyPlaceAddGuide = view.findViewById(R.id.fab_main_my_place_add_guide);
        mainMyPlaceErrorParent = view.findViewById(R.id.main_my_place_error_relative_parent);
        btnFloatAddNewPlace  = view.findViewById(R.id.floatBtnAddNewPlace);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myLocationsAdapter = new MyLocationsAdapter(new DiffUtilsMyLocationClass(),this);

        myLocRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false));

        //observer for my location items
        viewModel.getCurrentUserLocationLiveData().observe(requireActivity(), locationModels -> {

            if (locationModels!= null && !locationModels.isEmpty()){
                myLocations = locationModels;
                myLocationsAdapter.submitList(locationModels);
                myLocRecyclerView.setAdapter(myLocationsAdapter);

                myLocRecyclerView.setVisibility(View.VISIBLE);
                mainMyPlaceErrorParent.setVisibility(View.GONE);
            }else {
                myLocRecyclerView.setVisibility(View.GONE);
                mainMyPlaceErrorParent.setVisibility(View.VISIBLE);
            }


        });




        myLocRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy >0) {
                    // Scroll Down
                   btnFloatAddNewPlace.shrink();
                }
                else if (dy <0) {
                    // Scroll Up
                  btnFloatAddNewPlace.extend();
                }
            }
        });

        btnFloatAddNewPlace.setOnClickListener(view1 -> {
            Intent intent = new Intent(requireActivity(), AddNewLocationActivity.class);
            intent.putExtra("editLocation",false);
            intent.putExtra("addLocation",true);
            intent.putExtra("locationDocId", (String) null);
            startActivity(intent);
        });

        btnMainMyPlaceAddGuide.setOnClickListener(view12 -> {
            Intent intent = new Intent(requireActivity(), AddPlaceBusinessGuide.class);
            startActivity(intent);
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