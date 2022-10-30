package com.approdevelopers.ranchites.Utils;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import com.approdevelopers.ranchites.Broadcasts.NetworkConnection;
import com.google.android.material.snackbar.Snackbar;

public class ConnectionUtils {

    private Context context;
    private NetworkConnection networkConnection;
    private Snackbar snackbar;
    private View parentView;

    public ConnectionUtils(Context context,NetworkConnection networkConnection,View parentView){
        this.context  = context;
        this.networkConnection = networkConnection;
        this.parentView = parentView;

    }

    public void checkConnection() {

        // initialize intent filter
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

        // add action
        intentFilter.addAction("android.new.conn.CONNECTIVITY_CHANGE");

        // register receiver
        context.registerReceiver(networkConnection, intentFilter);


        // Initialize connectivity manager
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Initialize network info
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        // get connection status
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        // display snack bar
        showSnackBar(isConnected);
    }

    public void showSnackBar(boolean isConnected) {

        // initialize color and message
        String message;
        int textColor,bgColor;

        // check condition
        if (isConnected) {

            // when internet is connected
            // set message
            message = "Back Online";

            // set text color
            textColor = Color.BLACK;
            bgColor = Color.GREEN;

            if (snackbar!=null && snackbar.isShown()){
                snackbar.dismiss();
                // initialize snack bar
                snackbar= Snackbar.make( parentView,message, Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(bgColor);
                snackbar.setTextColor(textColor);


                snackbar.show();
            }

        } else {

            // when internet
            // is disconnected
            // set message
            message = "No Internet Connection";

            // set text color
            bgColor = Color.RED;
            textColor = Color.WHITE;
            snackbar= Snackbar.make( parentView,message, Snackbar.LENGTH_INDEFINITE);
            snackbar.setBackgroundTint(bgColor);
            snackbar.setTextColor(textColor);
            snackbar.show();
        }


    }

    public void destroySnackBar(){
        if (snackbar!=null){
            snackbar.dismiss();
        }
    }
}
