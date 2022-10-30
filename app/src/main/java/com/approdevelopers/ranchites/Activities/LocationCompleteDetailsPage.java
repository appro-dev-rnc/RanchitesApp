package com.approdevelopers.ranchites.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.approdevelopers.ranchites.Adapters.LocationComDetTabAdapter;
import com.approdevelopers.ranchites.ApplicationFiles.GlideApp;
import com.approdevelopers.ranchites.Broadcasts.NetworkConnection;
import com.approdevelopers.ranchites.Models.LocationModel;
import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.Utils.ConnectionUtils;
import com.approdevelopers.ranchites.ViewModels.LocationDetailPageViewModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class LocationCompleteDetailsPage extends AppCompatActivity implements View.OnClickListener, NetworkConnection.ReceiverListener {

    private static final int DOCUMENT_TYPE_LOCATION = 1;
    String documentId;
    LocationDetailPageViewModel locationDetailPageViewModel;


    TextView txtLocDetCategory,txtLocDetAddress,txtLocDetRatingNumbered;
    AppCompatRatingBar ratingLocDecItem;
    ImageView imgLocCompleteDetails;

    TabLayout locationComDetTabLayout;
    ViewPager2 locationComDetViewPager;

    NetworkConnection networkConnection;
    ConnectionUtils connectionUtils;
    CoordinatorLayout locationComDetParentView;



    String[] tabNames = {"Media","About","Map","Owner","Reviews and Ratings","Your Reviews"};

    CollapsingToolbarLayout collapsingToolbarLayout;
    AppBarLayout locDetAppbarLayout;

    Menu menu;
    String locDetBannerImageUrl;

    private static boolean isLocationSaved =false;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_det_page_modified);

        networkConnection = new NetworkConnection();




        Intent intent = getIntent();
        if (intent!=null){
            documentId =intent.getStringExtra("documentId");
        }
        if (documentId==null){
            Toast.makeText(this, "Cannot fetch location details ...", Toast.LENGTH_SHORT).show();
            finish();
        }

        Toolbar toolbar = findViewById(R.id.loc_det_toolbar);
        setSupportActionBar(toolbar);

         collapsingToolbarLayout = findViewById(R.id.loc_det_collapsing_toolbar_layout);
         locDetAppbarLayout = findViewById(R.id.loc_det_app_barlayout);


        //hooks

        txtLocDetAddress = findViewById(R.id.txt_loc_detail_address);
        txtLocDetCategory = findViewById(R.id.txt_loc_detail_category);
        ratingLocDecItem =findViewById(R.id.rating_loc_detail_item);
        imgLocCompleteDetails = findViewById(R.id.img_loc_complete_det_banner);
        locationComDetParentView = findViewById(R.id.location_com_det_parent_view);
        locationComDetParentView = findViewById(R.id.location_com_det_parent_view);

        txtLocDetRatingNumbered = findViewById(R.id.loc_det_rating_numbered);
        locationComDetTabLayout = findViewById(R.id.location_com_det_tab_layout);
        locationComDetViewPager = findViewById(R.id.loc_com_det_view_pager);



        locationComDetTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        LocationComDetTabAdapter locationComDetTabAdapter = new LocationComDetTabAdapter(getSupportFragmentManager(),getLifecycle(),documentId);
        locationComDetViewPager.setAdapter(locationComDetTabAdapter);
        locationComDetViewPager.setOffscreenPageLimit(5);

        locationDetailPageViewModel = new ViewModelProvider(this).get(LocationDetailPageViewModel.class);









        locationDetailPageViewModel.updatePlacesSearchCount(documentId);
        locationDetailPageViewModel.getCurrentLocationDetailLive(documentId).observe(this, locationModel -> {
            if (locationModel!=null){
                locDetBannerImageUrl = locationModel.getImageUrl();
                populateUiElements(locationModel);

            }else {
                locDetBannerImageUrl = null;
            }
        });







        new TabLayoutMediator(locationComDetTabLayout,locationComDetViewPager,(tab, position) -> tab.setText(tabNames[position])).attach();



        locDetAppbarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {

            if (Math.abs(verticalOffset)  >= locDetAppbarLayout.getTotalScrollRange()) { // collapse

                Objects.requireNonNull(toolbar.getOverflowIcon()).setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
                Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(Color.BLACK,PorterDuff.Mode.SRC_ATOP);


            } else if (verticalOffset == 0) { // fully expand
                Objects.requireNonNull(toolbar.getOverflowIcon()).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(Color.WHITE,PorterDuff.Mode.SRC_ATOP);

            }
        });
        

        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        imgLocCompleteDetails.setOnClickListener(this);

        locationDetailPageViewModel.addToRecentlyViewed(documentId);


    }




    private void populateUiElements(LocationModel locationModel) {
        if (locationModel!=null){

            GlideApp.with(this).load(locationModel.getImageUrl())
                    .timeout(20000)
                    .thumbnail(GlideApp.with(this).load(locationModel.getImageUrl()).override(30))
                    .error(
                            GlideApp.with(this).load(locationModel.getImageUrl())
                                    .timeout(20000)
                                    .thumbnail(GlideApp.with(this).load(locationModel.getImageUrl()).override(30))
                                    .into(imgLocCompleteDetails))
                    .into(imgLocCompleteDetails);
            collapsingToolbarLayout.setTitle(locationModel.getTitle());

            txtLocDetAddress.setText(locationModel.getAddress());
            txtLocDetCategory.setText(locationModel.getCategory());
            ratingLocDecItem.setRating((float) locationModel.getOverallRating());
            String rating = String.format(Locale.UK,"%1.1f out of 5",locationModel.getOverallRating());
            txtLocDetRatingNumbered.setText(rating);




        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.loc_comp_det_menu,menu);
        this.menu = menu;

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(@NonNull Menu menu) {
      locationDetailPageViewModel.getLocationSavedBool(documentId).observe(this, boolResult -> {

          isLocationSaved = boolResult;
          if (boolResult){
              menu.findItem(R.id.menu_loc_det_save).setTitle("Remove from saved");
          }else {
              menu.findItem(R.id.menu_loc_det_save).setTitle("Add to saved");
          }

      });


        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_loc_det_report){
            Intent intent = new Intent(LocationCompleteDetailsPage.this,AddReport.class);
            intent.putExtra("documentId",documentId);
            intent.putExtra("documentType",DOCUMENT_TYPE_LOCATION);
            startActivity(intent);
        }
        if (item.getItemId()==R.id.menu_loc_det_save){
            if (isLocationSaved){
                locationDetailPageViewModel.removeLocationFromSaved(documentId);
                Toast.makeText(this, "Remove from saved collection", Toast.LENGTH_SHORT).show();

            }else {
                locationDetailPageViewModel.addLocationToSaved(documentId);
                Toast.makeText(this, "Added to saved collection", Toast.LENGTH_SHORT).show();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        connectionUtils = new ConnectionUtils(LocationCompleteDetailsPage.this,networkConnection,locationComDetParentView);
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
    public void onClick(View view) {
        if (view.getId()==R.id.img_loc_complete_det_banner){
            ArrayList<String> imageUrlList = new ArrayList<>();

            imageUrlList.add(locDetBannerImageUrl);

            Intent intent = new Intent(LocationCompleteDetailsPage.this, ImageFullScreen.class);
            intent.putStringArrayListExtra("imageUrls", imageUrlList);
            startActivity(intent);
        }
    }


    @Override
    public void onNetworkChange(boolean isConnected) {
        connectionUtils.showSnackBar(isConnected);
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof TextInputEditText ) {
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


}