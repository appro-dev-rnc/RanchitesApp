package com.approdevelopers.ranchites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.approdevelopers.ranchites.Activities.AboutPage;
import com.approdevelopers.ranchites.Activities.AllCategories;
import com.approdevelopers.ranchites.Activities.ContactUsPage;
import com.approdevelopers.ranchites.Activities.SavedPlacesActivity;
import com.approdevelopers.ranchites.Activities.SearchActivity;
import com.approdevelopers.ranchites.ApplicationFiles.GlideApp;
import com.approdevelopers.ranchites.Broadcasts.NetworkConnection;
import com.approdevelopers.ranchites.Fragments.HomeMainFragment;
import com.approdevelopers.ranchites.Fragments.MyPlacesMainFragment;
import com.approdevelopers.ranchites.Repository.FirestoreRepository;
import com.approdevelopers.ranchites.Utils.ConnectionUtils;
import com.approdevelopers.ranchites.ViewModels.MainViewModel;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, NetworkConnection.ReceiverListener, NavigationBarView.OnItemSelectedListener{




    //MainView Model instance
    MainViewModel mainViewModel;

    private CircleImageView imgNavHeaderUserProf;

    DrawerLayout homeDrawerLayout;
    NavigationView homeNavView;
    View navViewHeader;
    CoordinatorLayout homeDashboardParentLayout;
    ConstraintLayout cons_lay_home;
    RelativeLayout ranchitesLoadingParent;
    FrameLayout mainFrameLayout;

    ConnectionUtils connectionUtils;

    //constants
    private static final float END_SCALE = 0.7f;

    NetworkConnection networkConnection;


    private TextView txtNavHeaderName;
    private TextView txtNavHeaderEmail;

    HomeMainFragment homeMainFragment;
    MyPlacesMainFragment myPlacesMainFragment;
    FragmentManager fragmentManager;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getWindow().setStatusBarColor(getColor(R.color.home_page_bg));

        networkConnection = new NetworkConnection();

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = null;
        if (pInfo != null) {
            version = pInfo.versionName;
        }
        if (version!=null){
            Task<DocumentSnapshot> versionCheck = FirestoreRepository.getInstance().getVersionDetails();
            String finalVersion = version;
            versionCheck.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        DocumentSnapshot snapshot = task.getResult();
                        if (snapshot!=null && snapshot.exists()){
                            String liveVersion = snapshot.getString("version");
                            boolean forceUpdate = snapshot.getBoolean("forceUpdate");
                            if (liveVersion!=null){
                                if (!liveVersion.equals(finalVersion)){
                                    if (forceUpdate){
                                        showForcedUpdateDialog();
                                    }else {
                                        showBasicUpdateDialog();
                                    }
                                }
                            }
                        }
                    }
                }
            });
        }

         homeMainFragment = new HomeMainFragment();
         myPlacesMainFragment= new MyPlacesMainFragment();
         fragmentManager = getSupportFragmentManager();


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1001);
        }

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        MobileAds.initialize(this, initializationStatus -> {});
        ranchitesLoadingParent = findViewById(R.id.ranchites_loading_parent);
        BottomNavigationView bottomMainNavView = findViewById(R.id.mainBottomNavView);


        if (savedInstanceState==null){
            fragmentManager.beginTransaction()
                    .add(R.id.main_frame_layout_container,homeMainFragment,HomeMainFragment.class.getSimpleName())
                    .add(R.id.main_frame_layout_container,myPlacesMainFragment,MyPlacesMainFragment.class.getSimpleName()).commit();
        }

        bottomMainNavView.setOnItemSelectedListener(this);
        bottomMainNavView.setSelectedItemId(R.id.mainBottomNavHomeitem);


        cons_lay_home = findViewById(R.id.constraintLayoutHome);
        homeDashboardParentLayout = findViewById(R.id.home_dashboard_parent_cons_layout);


        homeDrawerLayout = findViewById(R.id.home_drawer_layout);
        homeNavView = findViewById(R.id.home_nav_view);



        navViewHeader = homeNavView.getHeaderView(0);

        imgNavHeaderUserProf = navViewHeader.findViewById(R.id.img_nav_header_prof_icon);



        txtNavHeaderName = navViewHeader.findViewById(R.id.txt_nav_header_user_name);
        txtNavHeaderEmail = navViewHeader.findViewById(R.id.txt_nav_header_user_email);
        mainFrameLayout = navViewHeader.findViewById(R.id.main_frame_layout_container);

        //Setting recycler views properties


        //init main view model
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);


        //adding listeners

        animateNavigationDrawer();
        homeNavView.setNavigationItemSelectedListener(this);

        imgNavHeaderUserProf.setOnClickListener(this);




        mainViewModel.getCurrentUserProfData().observe(this, userBasicInfo -> {
            if (userBasicInfo != null) {
                GlideApp.with(MainActivity.this).load(userBasicInfo.getUserProfileImageUrl())
                        .timeout(20000)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontTransform()
                        .thumbnail(GlideApp.with(this).load(userBasicInfo.getUserProfileImageUrl()).override(30))
                        .error(  GlideApp.with(MainActivity.this).load(userBasicInfo.getUserProfileImageUrl())
                                .timeout(20000)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .dontTransform()
                                .thumbnail(GlideApp.with(this).load(userBasicInfo.getUserProfileImageUrl()).override(30))
                                .placeholder(R.drawable.ic_person)
                                .into(imgNavHeaderUserProf))
                        .placeholder(R.drawable.ic_person)
                        .into(imgNavHeaderUserProf);
                txtNavHeaderName.setText(userBasicInfo.getUserName());
                txtNavHeaderEmail.setText(userBasicInfo.getUserEmail());

            }
        });




        toolbar.setNavigationOnClickListener(view -> {
            if (!homeDrawerLayout.isDrawerVisible(GravityCompat.START)) {
                homeDrawerLayout.openDrawer(GravityCompat.START);
            }
        });


    }

    private void showBasicUpdateDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Update Available")
                .setMessage("Update the app for better functionality")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("Update Now", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("Later", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void showForcedUpdateDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Update Available")
                .setMessage("Please update the app to continue using:")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("Update Now", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    @Override
    public void onBackPressed() {
        if (homeDrawerLayout.isDrawerVisible(GravityCompat.START)) {
            homeDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (homeDrawerLayout.isOpen()) {

            homeDrawerLayout.close();
        }




        connectionUtils = new ConnectionUtils(MainActivity.this, networkConnection, homeDashboardParentLayout);
        NetworkConnection.Listener = this;
        connectionUtils.checkConnection();




    }

    @Override
    protected void onPause() {
        super.onPause();
        connectionUtils.destroySnackBar();



        this.unregisterReceiver(networkConnection);
    }




    //ui onclick listeners
    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.img_nav_header_prof_icon) {
            Intent intent = new Intent(MainActivity.this, UserProfile.class);
            startActivity(intent);
        }


    }

    private void animateNavigationDrawer() {

        //Add any color or remove it to use the default one!
        //To make it transparent use Color.Transparent in side setScrimColor();
        //drawerLayout.setScrimColor(Color.TRANSPARENT);
        homeDrawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                homeDashboardParentLayout.setScaleX(offsetScale);
                homeDashboardParentLayout.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = homeDashboardParentLayout.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                homeDashboardParentLayout.setTranslationX(xTranslation);
            }
        });

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_all_categories) {
            Intent allCategoriesIntent = new Intent(MainActivity.this, AllCategories.class);
            startActivity(allCategoriesIntent);

        }
        if (item.getItemId() == R.id.nav_saved_locations) {
            Intent savedPlacesIntent = new Intent(MainActivity.this, SavedPlacesActivity.class);
            startActivity(savedPlacesIntent);
        }

        if (item.getItemId() == R.id.nav_about) {
            Intent intent = new Intent(MainActivity.this, AboutPage.class);
            startActivity(intent);

        }
        if (item.getItemId() == R.id.nav_privacy_policy) {


            Intent intent = new Intent(MainActivity.this, ContactUsPage.class);
            startActivity(intent);


        }

        if (item.getItemId() == R.id.mainBottomNavHomeitem){

           fragmentManager.beginTransaction().hide(myPlacesMainFragment).show(homeMainFragment).commit();
        }
        if (item.getItemId()==R.id.mainBottomNavMyPlaceItem){

            fragmentManager.beginTransaction().hide(homeMainFragment).show(myPlacesMainFragment).commit();
        }


        return true;
    }



    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_homepage_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_main_item_profile) {
            Intent intent = new Intent(MainActivity.this, UserProfile.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.menu_main_item_search) {
            Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(searchIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.menu_main_item_profile);
        if (item != null) {
            mainViewModel.getCurrentUserProfData().observe(this, userBasicInfo -> {
                if (userBasicInfo != null) {
                    txtNavHeaderName.setText(userBasicInfo.getUserName());
                    txtNavHeaderEmail.setText(userBasicInfo.getUserEmail());

                    GlideApp.with(MainActivity.this).asBitmap().load(userBasicInfo.getUserProfileImageUrl()).timeout(10000)
                            .thumbnail(GlideApp.with(this).asBitmap().load(userBasicInfo.getUserProfileImageUrl()).override(30))
                            .placeholder(R.drawable.loading_pic_anim)
                            .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {

                            Bitmap bitmap = getCircledBitmap(resource);
                            Drawable d = new BitmapDrawable(getResources(), bitmap);
                            item.setIcon(d);
                            return false;
                        }
                    }).error(R.drawable.ic_person).into(imgNavHeaderUserProf);
                }
            });


        }
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public void onNetworkChange(boolean isConnected) {
        // display snack bar
        connectionUtils.showSnackBar(isConnected);
        if (mainViewModel!=null){
            mainViewModel.getNetworkAvalable().postValue(isConnected);
        }
    }


    public static Bitmap getCircledBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(bitmap.getWidth()/ 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }




}