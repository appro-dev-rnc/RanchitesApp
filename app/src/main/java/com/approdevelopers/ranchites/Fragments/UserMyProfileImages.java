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
import com.approdevelopers.ranchites.Adapters.ImagesModelAdapter;
import com.approdevelopers.ranchites.Interfaces.ImagesModelItemClickInterface;
import com.approdevelopers.ranchites.Models.ImagesModel;
import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.ViewModels.UserProfViewModel;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserMyProfileImages#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserMyProfileImages extends Fragment implements ImagesModelItemClickInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView profImagesRecyclerView;
    private ImagesModelAdapter myProfImagesAdapter;
    private UserProfViewModel viewModel;
    private TextView txtMyProfNoImageFoundError;
    private List<ImagesModel> imagesModelsList;

    public UserMyProfileImages() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserMyProfileImages.
     */
    // TODO: Rename and change types and number of parameters
    public static UserMyProfileImages newInstance(String param1, String param2) {
        UserMyProfileImages fragment = new UserMyProfileImages();
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

        imagesModelsList = new ArrayList<>();
        viewModel = new ViewModelProvider(requireActivity()).get(UserProfViewModel.class);
        myProfImagesAdapter = new ImagesModelAdapter(new DiffUtilsImagesClass(),this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_my_profile_images, container, false);

        profImagesRecyclerView = view.findViewById(R.id.recyclerProfileImages);
        txtMyProfNoImageFoundError =view.findViewById(R.id.txt_my_prof_no_images_found_error);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profImagesRecyclerView.setLayoutManager(new GridLayoutManager(requireActivity(),3));


        viewModel.getCurrentProfImages().observe(requireActivity(), imagesModels -> {
            imagesModelsList =imagesModels;
            if (imagesModels!=null&& !imagesModels.isEmpty()){
                myProfImagesAdapter.submitList(imagesModels);
                profImagesRecyclerView.setAdapter(myProfImagesAdapter);

                profImagesRecyclerView.setVisibility(View.VISIBLE);
                txtMyProfNoImageFoundError.setVisibility(View.GONE);

            }else {
                profImagesRecyclerView.setVisibility(View.GONE);
                txtMyProfNoImageFoundError.setVisibility(View.VISIBLE);
            }
        });



    }


    @Override
    public void getItemPosition(int position) {
        ArrayList<String> imageUrls =new ArrayList<>();
        ArrayList<String> imageStoragePaths = new ArrayList<>();
        ArrayList<String> imageDocumenIds = new ArrayList<>();
        for (ImagesModel model: imagesModelsList){
            imageUrls.add(model.getImageUrl());
            imageStoragePaths.add(model.getStoragePath());
            imageDocumenIds.add(model.getDocumentId());

        }
        Intent intent = new Intent(requireActivity(), ImageFullScreen.class);
        intent.putStringArrayListExtra("imageUrls", imageUrls);
        intent.putExtra("isAdmin",true);
        intent.putExtra("imagePosition",position);
        intent.putExtra("documentType",2);
        intent.putExtra("imageStoragePaths",imageStoragePaths);
        intent.putStringArrayListExtra("imageDocumentIds",imageDocumenIds);

        String userId = viewModel.getUserId();
        if (userId!=null){
            intent.putExtra("user_place_id",userId);
        }
        startActivity(intent);
    }
}