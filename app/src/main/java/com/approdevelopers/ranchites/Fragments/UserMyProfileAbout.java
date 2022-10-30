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
import com.approdevelopers.ranchites.ViewModels.UserProfViewModel;
import com.google.android.material.card.MaterialCardView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserMyProfileAbout#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserMyProfileAbout extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private UserProfViewModel viewModel;
    private TextView txtUserMyProfileAbout;

    private MaterialCardView myProfAboutParent;
    private TextView txtMyProfAboutError;

    public UserMyProfileAbout() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserMyProfileAbout.
     */
    // TODO: Rename and change types and number of parameters
    public static UserMyProfileAbout newInstance(String param1, String param2) {
        UserMyProfileAbout fragment = new UserMyProfileAbout();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_my_profile_about, container, false);
        txtUserMyProfileAbout = view.findViewById(R.id.txt_profile_det_user_about);
        txtMyProfAboutError = view.findViewById(R.id.txt_my_prof_no_about_found_error);
        myProfAboutParent = view.findViewById(R.id.material_my_prof_about_parent);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getCurrentUserCompleteDataLive().observe(requireActivity(), userCompleteDetModel -> {
            if (userCompleteDetModel!=null){
                if (userCompleteDetModel.getUserAbout()!=null){
                    txtUserMyProfileAbout.setText(userCompleteDetModel.getUserAbout());
                    txtMyProfAboutError.setVisibility(View.GONE);
                    myProfAboutParent.setVisibility(View.VISIBLE);
                }else {
                    txtMyProfAboutError.setVisibility(View.VISIBLE);
                    myProfAboutParent.setVisibility(View.GONE);
                }

            }
        });
    }
}