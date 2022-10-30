package com.approdevelopers.ranchites.Activities;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.approdevelopers.ranchites.Adapters.AllCategoriesAdapter;
import com.approdevelopers.ranchites.Adapters.DiffUtilsAllCategoriesClass;
import com.approdevelopers.ranchites.Broadcasts.NetworkConnection;
import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.Utils.ConnectionUtils;
import com.approdevelopers.ranchites.ViewModels.AllCategoriesViewModel;


public class AllCategories extends AppCompatActivity implements NetworkConnection.ReceiverListener{

    RecyclerView recyclerAllCategories;

    AllCategoriesViewModel allCategoriesViewModel;
    AllCategoriesAdapter allCategoriesAdapter;
    NetworkConnection networkConnection;
    ConnectionUtils connectionUtils;
    CoordinatorLayout allCatParentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_categories);
        Toolbar toolbar = findViewById(R.id.tool_bar_all_category_activity);
        toolbar.setTitle("All Categories");
        setSupportActionBar(toolbar);

        networkConnection = new NetworkConnection();

        //hooks
        recyclerAllCategories = findViewById(R.id.recycler_all_categories);
        allCatParentView = findViewById(R.id.all_categories_parent_view);

        recyclerAllCategories.setHasFixedSize(true);

        recyclerAllCategories.setLayoutManager(new GridLayoutManager(this, 2));
        //init view model
        allCategoriesViewModel = new ViewModelProvider(this).get(AllCategoriesViewModel.class);
        allCategoriesAdapter = new AllCategoriesAdapter(new DiffUtilsAllCategoriesClass(), R.layout.all_categories_item);



        allCategoriesViewModel.getAllCategoriesLive(getApplicationContext()).observe(this, categoriesModels -> {

            if (categoriesModels != null && !categoriesModels.isEmpty()) {

                    allCategoriesAdapter.submitList(categoriesModels);


                recyclerAllCategories.setAdapter(allCategoriesAdapter);

            }
        });

        toolbar.setNavigationOnClickListener(view -> onBackPressed());

    }

    @Override
    protected void onResume() {
        super.onResume();
        connectionUtils = new ConnectionUtils(AllCategories.this, networkConnection, allCatParentView);
        NetworkConnection.Listener = this;
        connectionUtils.checkConnection();
    }

    @Override
    protected void onPause() {
        super.onPause();
        connectionUtils.destroySnackBar();
        this.unregisterReceiver(networkConnection);}

    @Override
    public void onNetworkChange(boolean isConnected) {
        connectionUtils.showSnackBar(isConnected);
    }



}