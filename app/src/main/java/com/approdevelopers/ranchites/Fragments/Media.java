package com.approdevelopers.ranchites.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.approdevelopers.ranchites.Activities.ImageFullScreen;
import com.approdevelopers.ranchites.Adapters.DiffUtilsImagesClass;
import com.approdevelopers.ranchites.Adapters.ImagesModelAdapterPublic;
import com.approdevelopers.ranchites.Interfaces.ImagesModelItemClickInterface;
import com.approdevelopers.ranchites.Models.ImagesModel;
import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.ViewModels.UserComDetViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Media#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Media extends Fragment implements ImagesModelItemClickInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private UserComDetViewModel viewModel;
    private String userId;

    RecyclerView recyclerUserComDetMedia;
    ImagesModelAdapterPublic userMediaAdapter;
    private TextView txtUserDetNoImagesFoundError;
    private List<ImagesModel> imagesModelsList;

    public Media() {
        // Required empty public constructor

    }
    public Media(String userId){
        this.userId = userId;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Media.
     */
    // TODO: Rename and change types and number of parameters
    public static Media newInstance(String param1, String param2) {
        Media fragment = new Media();
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
        imagesModelsList= new ArrayList<>();

        viewModel = new ViewModelProvider(requireActivity()).get(UserComDetViewModel.class);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_media, container, false);
        recyclerUserComDetMedia = view.findViewById(R.id.recycler_user_com_det_media);
        txtUserDetNoImagesFoundError = view.findViewById(R.id.txt_user_det_no_images_found_error);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerUserComDetMedia.setLayoutManager(new GridLayoutManager(requireActivity(),3));
        recyclerUserComDetMedia.setNestedScrollingEnabled(false);
        userMediaAdapter = new ImagesModelAdapterPublic(new DiffUtilsImagesClass(),this);
        viewModel.getUserMediaImagesLive(userId).observe(requireActivity(), imagesModels -> {
            imagesModelsList = imagesModels;
            if (imagesModels!=null && !imagesModels.isEmpty()){
                userMediaAdapter.submitList(imagesModels);
                recyclerUserComDetMedia.setAdapter(userMediaAdapter);

                txtUserDetNoImagesFoundError.setVisibility(View.GONE);
                recyclerUserComDetMedia.setVisibility(View.VISIBLE);
            }else {
                txtUserDetNoImagesFoundError.setVisibility(View.VISIBLE);
                recyclerUserComDetMedia.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void getItemPosition(int position) {
        ArrayList<String> imageUrls =new ArrayList<>();
        for (ImagesModel model: imagesModelsList){
            imageUrls.add(model.getImageUrl());
        }
        Intent intent = new Intent(requireActivity(), ImageFullScreen.class);
        intent.putStringArrayListExtra("imageUrls", imageUrls);
        intent.putExtra("isAdmin",false);
        intent.putExtra("imagePosition",position);

        startActivity(intent);

    }
}