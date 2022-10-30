package com.approdevelopers.ranchites.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;


import com.approdevelopers.ranchites.Adapters.DiffUtilsPlacesBasicClass;
import com.approdevelopers.ranchites.Adapters.SavedPlacesAdapter;
import com.approdevelopers.ranchites.Broadcasts.NetworkConnection;
import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.Utils.ConnectionUtils;
import com.approdevelopers.ranchites.ViewModels.SavedPlacesViewModel;



public class SavedPlacesActivity extends AppCompatActivity implements NetworkConnection.ReceiverListener {


    SavedPlacesViewModel savedPlacesViewModel;
    RecyclerView recyclerSavedLocations;
    SavedPlacesAdapter savedPlacesAdapter;

    NetworkConnection networkConnection;
    ConnectionUtils connectionUtils;
    CoordinatorLayout savedPlacesParentView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_location);

        networkConnection = new NetworkConnection();

        Toolbar toolbar = findViewById(R.id.tool_bar_saved_activity);
        toolbar.setTitle("Saved");
        setSupportActionBar(toolbar);



        //hooks
        recyclerSavedLocations = findViewById(R.id.recycler_saved_locations);
        savedPlacesParentView = findViewById(R.id.saved_places_parent_view);
        savedPlacesViewModel = new ViewModelProvider(this).get(SavedPlacesViewModel.class);

        recyclerSavedLocations.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        savedPlacesAdapter = new SavedPlacesAdapter(new DiffUtilsPlacesBasicClass());



        savedPlacesViewModel.getSavedPlacesIdLive().observe(this, strings -> {

            if (strings!=null && !strings.isEmpty()){

                savedPlacesViewModel.getSavedPlacesCompleteDetLive(strings).observe(SavedPlacesActivity.this, placesBasicModels -> {
                    savedPlacesAdapter.submitList(placesBasicModels);
                    recyclerSavedLocations.setAdapter(savedPlacesAdapter);
                    recyclerSavedLocations.setVisibility(View.VISIBLE);
                });
            }else {
                recyclerSavedLocations.setVisibility(View.GONE);
            }

        });





        toolbar.setNavigationOnClickListener(view -> onBackPressed());


    }

    @Override
    protected void onResume() {
        super.onResume();
        connectionUtils = new ConnectionUtils(SavedPlacesActivity.this,networkConnection,savedPlacesParentView);
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