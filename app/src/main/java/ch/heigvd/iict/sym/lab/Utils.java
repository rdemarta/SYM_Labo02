package ch.heigvd.iict.sym.lab;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;


public class Utils {

    public static ArrayList<String> valuesWaitingList = new ArrayList<>();

    /**
     * Check if the device is connected to internet
     * @param context The Context
     * @return True if there is an internet connection, false otherwise
     */
    public static boolean isConnectedToInternet(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

}
