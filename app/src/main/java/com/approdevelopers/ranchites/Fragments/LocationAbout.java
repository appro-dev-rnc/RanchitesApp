package com.approdevelopers.ranchites.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.ViewModels.LocationDetailPageViewModel;
import com.google.android.material.card.MaterialCardView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LocationAbout#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationAbout extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView txtLocDetDesc;
    LocationDetailPageViewModel viewModel;

    String locationId;
    private TextView txtLocDetNoAboutFoundError;
    private MaterialCardView locDetAboutParent;

    public LocationAbout() {
        // Required empty public constructor
    }public LocationAbout(String locationId) {
        // Required empty public constructor
        this.locationId = locationId;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocationAbout.
     */
    // TODO: Rename and change types and number of parameters
    public static LocationAbout newInstance(String param1, String param2) {
        LocationAbout fragment = new LocationAbout();
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
        viewModel= new ViewModelProvider(requireActivity()).get(LocationDetailPageViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location_about, container, false);

        txtLocDetNoAboutFoundError = view.findViewById(R.id.txt_loc_det_no_about_found_error);
        locDetAboutParent = view.findViewById(R.id.loc_det_about_parent);
        txtLocDetDesc = view.findViewById(R.id.txt_loc_detail_desc);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        viewModel.getCurrentLocationDetailLive(locationId).observe(requireActivity(), locationModel -> {
            if (locationModel!=null){
                if (locationModel.getDesc()!=null){
                    txtLocDetDesc.setText(locationModel.getDesc());

                    txtLocDetNoAboutFoundError.setVisibility(View.GONE);
                    locDetAboutParent.setVisibility(View.VISIBLE);
                }else {
                    txtLocDetNoAboutFoundError.setVisibility(View.VISIBLE);
                    locDetAboutParent.setVisibility(View.GONE);
                }
            }
        });

    }
}