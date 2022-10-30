package com.approdevelopers.ranchites.Activities;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.approdevelopers.ranchites.Adapters.AddEditFragAdapter;
import com.approdevelopers.ranchites.Broadcasts.NetworkConnection;
import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.Utils.ConnectionUtils;
import com.approdevelopers.ranchites.ViewModels.AddNewLocViewModel;
import com.approdevelopers.ranchites.ViewModels.AddNewLocViewModelFactory;
import com.google.android.material.textfield.TextInputEditText;

public class AddNewLocationActivity extends AppCompatActivity implements NetworkConnection.ReceiverListener {


    private static final int CAMERA_REQUEST_CODE = 1010;
    private static final int STORAGE_REQUEST_CODE = 1015;
    private static final int LOCATION_REQUEST_CODE = 1000;





    AddEditFragAdapter addEditFragAdapter;
    

    // View Model variable
    AddNewLocViewModel newLocViewModel;

    NetworkConnection networkConnection;
    ConnectionUtils connectionUtils;
    //Button btnUpload,btnUpdateLocData;
    ViewPager2 addEditLocViewPager;
    RelativeLayout addLocParentView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_my_loc_page_modified);


        Toolbar toolbar = findViewById(R.id.add_edit_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        networkConnection = new NetworkConnection();





        //hooks
        addEditLocViewPager = findViewById(R.id.add_edit_view_pager);
        addLocParentView = findViewById(R.id.add_loc_parent);


        Intent intent = getIntent();
        boolean editLocation = intent.getBooleanExtra("editLocation", false);
        boolean addLocation = intent.getBooleanExtra("addLocation", false);
        String locationDocId = intent.getStringExtra("locationDocId");


        newLocViewModel = new ViewModelProvider(this,new AddNewLocViewModelFactory(locationDocId)).get(AddNewLocViewModel.class);

         addEditFragAdapter = new AddEditFragAdapter(getSupportFragmentManager(),getLifecycle(), locationDocId);
        addEditLocViewPager  =  findViewById(R.id.add_edit_view_pager);
        addEditLocViewPager.setAdapter(addEditFragAdapter);
        addEditLocViewPager.setOffscreenPageLimit(3);
        addEditLocViewPager.setUserInputEnabled(false);

        if (editLocation){
            if (locationDocId !=null){
                newLocViewModel.setDocumentId(locationDocId);
                toolbar.setTitle("Edit place/business");


            }
        }
        if (addLocation){
            if (locationDocId ==null){
                toolbar.setTitle("Add place/business");
            }

        }

        toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof TextInputEditText || v instanceof AppCompatAutoCompleteTextView) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment  =addEditFragAdapter.createFragment(2);
        fragment.onActivityResult(requestCode,resultCode,data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==LOCATION_REQUEST_CODE){
            Fragment fragment = addEditFragAdapter.createFragment(2);
            fragment.onRequestPermissionsResult(requestCode,permissions,grantResults);
        }
        if (requestCode==CAMERA_REQUEST_CODE || requestCode==STORAGE_REQUEST_CODE){
            Fragment fragment = addEditFragAdapter.createFragment(0);
            fragment.onRequestPermissionsResult(requestCode,permissions,grantResults);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        connectionUtils = new ConnectionUtils(AddNewLocationActivity.this,networkConnection,addLocParentView);
        NetworkConnection.Listener = this;
        connectionUtils.checkConnection();
    }

    @Override
    protected void onPause() {
        super.onPause();
        connectionUtils.destroySnackBar();

        this.unregisterReceiver(networkConnection);
    }

    @Override
    public void onNetworkChange(boolean isConnected) {
        connectionUtils.showSnackBar(isConnected);
    }



}