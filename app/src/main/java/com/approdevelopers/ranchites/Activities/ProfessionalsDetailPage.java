package com.approdevelopers.ranchites.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.approdevelopers.ranchites.Adapters.CityProfessionalsAdapter;
import com.approdevelopers.ranchites.Adapters.DiffUtilsCityProfessionals;
import com.approdevelopers.ranchites.Broadcasts.NetworkConnection;
import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.Utils.ConnectionUtils;
import com.approdevelopers.ranchites.ViewModels.ProfessionalsDetViewModel;


public class ProfessionalsDetailPage extends AppCompatActivity implements NetworkConnection.ReceiverListener {

    RecyclerView recyclerProfessionalsDet;
    CityProfessionalsAdapter professionalsAdapter;
    ProfessionalsDetViewModel professionalsDetViewModel;

    NetworkConnection networkConnection;
    ConnectionUtils connectionUtils;
    CoordinatorLayout profDetParentView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professionals_detail_page);
        Toolbar toolbar = findViewById(R.id.professionals_det_page_toolbar);
        toolbar.setTitle("City Professionals");
        setSupportActionBar(toolbar);

        networkConnection = new NetworkConnection();

        recyclerProfessionalsDet = findViewById(R.id.recycler_city_professional_det);

        profDetParentView = findViewById(R.id.professional_det_parent_view);

        recyclerProfessionalsDet.setLayoutManager(new GridLayoutManager(this,2));

        professionalsDetViewModel = new ViewModelProvider(this).get(ProfessionalsDetViewModel.class);
        professionalsAdapter = new CityProfessionalsAdapter(new DiffUtilsCityProfessionals());

        professionalsDetViewModel.getCityProfessionalsLive().observe(this, cityProfessionalsModels -> {
            professionalsAdapter.submitList(cityProfessionalsModels);
            recyclerProfessionalsDet.setAdapter(professionalsAdapter);
        });

        toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    @Override
    protected void onResume() {
        super.onResume();
        connectionUtils = new ConnectionUtils(ProfessionalsDetailPage.this,networkConnection,profDetParentView);
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