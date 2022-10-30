package com.approdevelopers.ranchites.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.approdevelopers.ranchites.ApplicationFiles.GlideApp;
import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.ViewModels.LocationDetailPageViewModel;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LocationOwnerFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationOwnerFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    LocationDetailPageViewModel viewModel;
    String locationId;
    ImageView imgLocDetOwnerProfPic;
    TextView txtLocDetOwnerName,txtLocDetOwnerEmail,txtLocDetOwnerPhone;

    public LocationOwnerFrag() {
        // Required empty public constructor
    }
    public LocationOwnerFrag(String locationId) {
        // Required empty public constructor
        this.locationId = locationId;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocationOwnerFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static LocationOwnerFrag newInstance(String param1, String param2) {
        LocationOwnerFrag fragment = new LocationOwnerFrag();
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
        }        viewModel= new ViewModelProvider(requireActivity()).get(LocationDetailPageViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location_owner, container, false);
        imgLocDetOwnerProfPic = view.findViewById(R.id.img_loc_det_owner_prof);
        txtLocDetOwnerName = view.findViewById(R.id.txt_loc_det_owner_name);
        txtLocDetOwnerEmail = view.findViewById(R.id.txt_loc_det_owner_email);
        txtLocDetOwnerPhone = view.findViewById(R.id.txt_loc_det_owner_phone);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        viewModel.getCurrentLocationDetailLive(locationId).observe(requireActivity(),
                locationModel -> {
                    if (locationModel!=null){
                        String userId = locationModel.getOwnerId();
                        viewModel.getGetOwnerDetails(userId).observe(requireActivity(), userBasicInfo -> {
                            if (userBasicInfo!=null){
                                GlideApp.with(requireActivity()).load(userBasicInfo.getUserProfileImageUrl())
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .dontTransform()
                                        .thumbnail(GlideApp.with(requireActivity()).load(userBasicInfo.getUserProfileImageUrl()).override(30))
                                        .error(R.drawable.ic_person)
                                        .into(imgLocDetOwnerProfPic);
                                txtLocDetOwnerName.setText(userBasicInfo.getUserName());
                                txtLocDetOwnerEmail.setText(userBasicInfo.getUserEmail());
                                txtLocDetOwnerPhone.setText(userBasicInfo.getUserPhone());

                            }
                        });
                    }
                });

    }
}