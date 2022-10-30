package com.approdevelopers.ranchites.Activities;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.approdevelopers.ranchites.Adapters.UserComDetTabAdapter;
import com.approdevelopers.ranchites.ApplicationFiles.GlideApp;
import com.approdevelopers.ranchites.Broadcasts.NetworkConnection;
import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.Utils.ConnectionUtils;
import com.approdevelopers.ranchites.ViewModels.UserComDetViewModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.ArrayList;


public class UserCompleteDetailPage extends AppCompatActivity implements View.OnClickListener, NetworkConnection.ReceiverListener {

    private static final int DOCUMENT_TYPE_USER = 2;

    String userId;
    UserComDetViewModel userViewModel;

    ImageView imgUserComDetProfPic;
    TextView txtUserComDetProfession,txtUserComDetPhone,txtUserComDetEmail;
    AppCompatRatingBar ratingUserComDetProfile;

    AppBarLayout userDetAppBarLayout;
    CollapsingToolbarLayout userDetCollapsingToolbarLayout;







    TabLayout userComDetTabLayout;
    ViewPager2 userComDetViewPager;

    NetworkConnection networkConnection;
    ConnectionUtils connectionUtils;
    CoordinatorLayout userDetPageParentView;
    String userProfileImageUrl;
    MaterialCardView materialUserDetPhoneParent,materialUserDetEmailParent;




    String[] tabNames = {"About","Media","Reviews and Ratings","Your Review"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_detail_page_modified);

        networkConnection = new NetworkConnection();

        Intent intent = getIntent();
        if (intent!=null){
            userId  = intent.getStringExtra("userId");
        }
        if (userId==null){
            Toast.makeText(this, "Failed to fetch user details", Toast.LENGTH_SHORT).show();
            finish();
        }

        Toolbar toolbar = findViewById(R.id.user_det_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //hooks
        imgUserComDetProfPic = findViewById(R.id.img_user_com_det_profile_pic);
        txtUserComDetProfession = findViewById(R.id.txt_user_com_det_profession);
        txtUserComDetPhone = findViewById(R.id.txt_user_com_det_phone);
        txtUserComDetEmail = findViewById(R.id.txt_user_com_det_email);
        ratingUserComDetProfile = findViewById(R.id.rating_usr_com_profile_rating);

        userDetPageParentView = findViewById(R.id.user_det_page_parent_view);
        materialUserDetPhoneParent = findViewById(R.id.material_user_det_phone_parent);
        materialUserDetEmailParent = findViewById(R.id.material_user_det_email_parent);





        userComDetTabLayout = findViewById(R.id.user_com_det_tab_layout);
        userComDetViewPager = findViewById(R.id.user_com_det_view_pager);

        userComDetTabLayout.addTab(userComDetTabLayout.newTab().setText("Media"));
        userComDetTabLayout.addTab(userComDetTabLayout.newTab().setText("Ratings and Reviews"));
        userComDetTabLayout.addTab(userComDetTabLayout.newTab().setText("Your Reviews"));
        userComDetTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        userDetAppBarLayout = findViewById(R.id.user_det_app_barlayout);
        userDetCollapsingToolbarLayout = findViewById(R.id.user_det_collapsing_toolbar_layout);

        UserComDetTabAdapter userComDetTabAdapter = new UserComDetTabAdapter(getSupportFragmentManager(),getLifecycle(),userId);
        userComDetViewPager.setAdapter(userComDetTabAdapter);
        userComDetViewPager.setOffscreenPageLimit(2);

        userViewModel = new ViewModelProvider(this).get(UserComDetViewModel.class);




        userViewModel.getUserCompleteDetLive(userId).observe(this, userCompleteDetModel -> {
            if (userCompleteDetModel!=null ){
                userProfileImageUrl = userCompleteDetModel.getUserProfileImageUrl();
                GlideApp.with(UserCompleteDetailPage.this).load(userCompleteDetModel.getUserProfileImageUrl())
                        .timeout(20000)
                        .thumbnail(GlideApp.with(this).load(userCompleteDetModel.getUserProfileImageUrl()).override(30))
                        .error(    GlideApp.with(UserCompleteDetailPage.this).load(userCompleteDetModel.getUserProfileImageUrl())
                                .timeout(20000)
                                .thumbnail(GlideApp.with(this).load(userCompleteDetModel.getUserProfileImageUrl()).override(30))
                                .into(imgUserComDetProfPic))
                        .into(imgUserComDetProfPic);
                userDetCollapsingToolbarLayout.setTitle(userCompleteDetModel.getUserName());

                txtUserComDetProfession.setText(userCompleteDetModel.getUserProfession());
                txtUserComDetPhone.setText(userCompleteDetModel.getUserPhone());
                txtUserComDetEmail.setText(userCompleteDetModel.getUserEmail());
                ratingUserComDetProfile.setRating((float) userCompleteDetModel.getUserRating());


            }else {
                userProfileImageUrl = null;
            }
        });




        new TabLayoutMediator(userComDetTabLayout,userComDetViewPager,(tab, position) -> tab.setText(tabNames[position])).attach();

        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        imgUserComDetProfPic.setOnClickListener(this);
        materialUserDetPhoneParent.setOnClickListener(this);
        materialUserDetEmailParent.setOnClickListener(this);

        userViewModel.addToRecentlyViewed(userId);
    }


    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.img_user_com_det_profile_pic){
            ArrayList<String> imageUrlList = new ArrayList<>();

            imageUrlList.add(userProfileImageUrl);

            Intent intent = new Intent(UserCompleteDetailPage.this, ImageFullScreen.class);
            intent.putStringArrayListExtra("imageUrls", imageUrlList);
            startActivity(intent);
        }

        if (view.getId() == R.id.material_user_det_phone_parent){
            String phoneUri = "tel:"+ userViewModel.getUserPhoneNumber();
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(phoneUri));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
        if (view.getId()==R.id.material_user_det_email_parent){
            String emailId = userViewModel.getUserEmailId();
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, emailId);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_com_det_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_user_det_report){
            Intent intent = new Intent(UserCompleteDetailPage.this,AddReport.class);
            intent.putExtra("documentId",userId);
            intent.putExtra("documentType",DOCUMENT_TYPE_USER);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        connectionUtils = new ConnectionUtils(UserCompleteDetailPage.this,networkConnection,userDetPageParentView);
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