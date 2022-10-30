package com.approdevelopers.ranchites.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.ViewModels.AddNewLocViewModel;
import com.approdevelopers.ranchites.ViewModels.AddNewLocViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddEditLocFragmentDescDet#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEditLocFragmentDescDet extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FloatingActionButton btnNextFragDescDet,btnPreviousFragDescDet;
    ViewPager2 addEditLocViewPager;
    private String locationDocId;
    AddNewLocViewModel viewModel;
    TextInputLayout edtAddLocDesc,textInputAutoCompleteCat;
    AppCompatAutoCompleteTextView autoTextAddLocCategory;

    ArrayAdapter<String> categoriesAdapter;

    public AddEditLocFragmentDescDet() {
        // Required empty public constructor
    }
 public AddEditLocFragmentDescDet(String locationDocId) {
        // Required empty public constructor
     this.locationDocId = locationDocId;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddEditLocFragmentDescDet.
     */
    // TODO: Rename and change types and number of parameters
    public static AddEditLocFragmentDescDet newInstance(String param1, String param2) {
        AddEditLocFragmentDescDet fragment = new AddEditLocFragmentDescDet();
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
        addEditLocViewPager = requireActivity().findViewById(R.id.add_edit_view_pager);
        viewModel = new ViewModelProvider(requireActivity(),new AddNewLocViewModelFactory(locationDocId)).get(AddNewLocViewModel.class);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_edit_loc_desc_det, container, false);
        btnNextFragDescDet = view.findViewById(R.id.btn_next_frag_desc_det);
        btnPreviousFragDescDet = view.findViewById(R.id.btn_previous_frag_desc_det);
        edtAddLocDesc = view.findViewById(R.id.textLayoutAddDesc);
        textInputAutoCompleteCat = view.findViewById(R.id.textLayoutAddLocCategory);
        autoTextAddLocCategory = view.findViewById(R.id.autocompleteTxtViewAddCategory);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getEditLocationModel().observe(requireActivity(), locationModel -> {
            if (locationModel!= null){
                Objects.requireNonNull(edtAddLocDesc.getEditText()).setText(locationModel.getDesc());
                autoTextAddLocCategory.setText(locationModel.getCategory());
                viewModel.setCategory(locationModel.getCategory());
                viewModel.setDesc(locationModel.getDesc());
            }else {
                Objects.requireNonNull(edtAddLocDesc.getEditText()).setText("");
                autoTextAddLocCategory.setText("");
                viewModel.setCategory(null);
                viewModel.setDesc(null);
            }
        });


        viewModel.getCategoriesNameList().observe(requireActivity(), strings -> {
            if (strings!=null){
                categoriesAdapter = new ArrayAdapter<>(requireActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, strings);
                autoTextAddLocCategory.setAdapter(categoriesAdapter);
            }
        });


        Objects.requireNonNull(edtAddLocDesc.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //nothing to do here
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edtAddLocDesc.setErrorEnabled(false);
                viewModel.setDesc(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        autoTextAddLocCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textInputAutoCompleteCat.setErrorEnabled(false);
                viewModel.setCategory(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        autoTextAddLocCategory.setOnItemClickListener((adapterView, view1, i, l) -> {
            viewModel.setCategory(adapterView.getItemAtPosition(i).toString());
            Toast.makeText(requireActivity(), viewModel.getCategory(), Toast.LENGTH_SHORT).show();

        });
        btnPreviousFragDescDet.setOnClickListener(this);
        btnNextFragDescDet.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.btn_next_frag_desc_det){

            if (validateDescData()){
                addEditLocViewPager.setCurrentItem(2);
            }

        }
        if (view.getId()==R.id.btn_previous_frag_desc_det){
            addEditLocViewPager.setCurrentItem(0);
        }
    }



    private boolean validateDescData() {


        if (!viewModel.validateCategory()) {
            textInputAutoCompleteCat.setError("Choose a category");
            return false;
        }
        if (!viewModel.validateDesc()) {
            edtAddLocDesc.setError("Description cannot be empty");
            return false;
        }

        return true;
    }
}