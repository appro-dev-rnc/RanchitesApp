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
import com.approdevelopers.ranchites.ViewModels.UserComDetViewModel;
import com.google.android.material.card.MaterialCardView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserDetAbout#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserDetAbout extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private UserComDetViewModel viewModel;
    private String userId;
    TextView txtUserComDetUserAbout;
    private MaterialCardView userComDetAboutParent;
    private TextView txtUserComDetNoAboutFoundError;


    public UserDetAbout() {
        // Required empty public constructor
    }public UserDetAbout(String userId) {
        // Required empty public constructor
        this.userId = userId;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserDetAbout.
     */
    // TODO: Rename and change types and number of parameters
    public static UserDetAbout newInstance(String param1, String param2) {
        UserDetAbout fragment = new UserDetAbout();
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

        viewModel = new ViewModelProvider(requireActivity()).get(UserComDetViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_det_about, container, false);
        txtUserComDetUserAbout = view.findViewById(R.id.txt_com_det_user_about);
        txtUserComDetNoAboutFoundError = view.findViewById(R.id.txt_user_det_no_about_found_error);
        userComDetAboutParent = view.findViewById(R.id.user_det_about_parent);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getUserCompleteDetLive(userId).observe(requireActivity(), userCompleteDetModel -> {
            if (userCompleteDetModel!=null ){
                if (userCompleteDetModel.getUserAbout()!=null){
                    txtUserComDetUserAbout.setText(userCompleteDetModel.getUserAbout());


                    userComDetAboutParent.setVisibility(View.VISIBLE);
                    txtUserComDetNoAboutFoundError.setVisibility(View.GONE);
                }else {
                    userComDetAboutParent.setVisibility(View.GONE);
                    txtUserComDetNoAboutFoundError.setVisibility(View.VISIBLE);
                }

            }
        });
    }
}