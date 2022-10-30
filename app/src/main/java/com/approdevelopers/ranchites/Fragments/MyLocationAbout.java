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
import com.approdevelopers.ranchites.ViewModels.MyLocFullDetailViewModel;
import com.google.android.material.card.MaterialCardView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyLocationAbout#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyLocationAbout extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView txtMyLocDetailDesc;
    private String locationDocId;
    private MyLocFullDetailViewModel viewModel;

    private MaterialCardView myLocAboutParent;
    private TextView txtMyLocAboutError;

    public MyLocationAbout() {
        // Required empty public constructor
    }public MyLocationAbout(String locationDocId) {
        // Required empty public constructor
        this.locationDocId =locationDocId;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyLocationAbout.
     */
    // TODO: Rename and change types and number of parameters
    public static MyLocationAbout newInstance(String param1, String param2) {
        MyLocationAbout fragment = new MyLocationAbout();
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

        if (locationDocId!=null)
            viewModel = new ViewModelProvider(requireActivity())
                    .get(MyLocFullDetailViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_location_about, container, false);
        txtMyLocDetailDesc = view.findViewById(R.id.txtMyLocDetailDesc);
        txtMyLocAboutError = view.findViewById(R.id.txt_my_loc_no_about_found_error);
        myLocAboutParent = view.findViewById(R.id.txt_my_loc_about_parent);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getCurrentLocationDetailLiveData(locationDocId).observe(requireActivity(), locationModel -> {
            if (locationModel!=null&& !locationModel.toString().isEmpty()){
                txtMyLocDetailDesc.setText(locationModel.getDesc());
                txtMyLocAboutError.setVisibility(View.GONE);
                myLocAboutParent.setVisibility(View.VISIBLE);
            }else {
                myLocAboutParent.setVisibility(View.GONE);
                txtMyLocAboutError.setVisibility(View.VISIBLE);
            }
        });
    }
}