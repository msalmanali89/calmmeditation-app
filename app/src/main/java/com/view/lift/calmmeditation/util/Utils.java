package com.view.lift.calmmeditation.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.view.lift.calmmeditation.dto.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Utils {
    static  String TAG = "Utils";

    public static void log(String msg){
        Log.d(TAG,msg);
    }

    public static boolean  isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }

    public static MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }

    public static ProgressBar createProgressDialog(Context context, ViewGroup layout){

        ProgressBar progressBar = new ProgressBar(context.getApplicationContext(),null,android.R.attr.progressBarStyleInverse);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(200,200);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(progressBar,params);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.GONE);

        return progressBar;
    }

    public static boolean isConnectedNetwork(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

        if (activeNetworkInfo != null) { // connected to the internet
            Toast.makeText(context, activeNetworkInfo.getTypeName(), Toast.LENGTH_SHORT).show();

            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return true;
            } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                return true;
            }
        }
        return false;
    }


    public static ArrayList<Object> convertData(List<Item> items){

        HashMap<String,ArrayList<Item>> categoricalItems = new HashMap<>();

        for(Item item : items) {

            int position = item.getCategoryList().size()-1;
            String cat =  item.getCategoryList().get(position);

            if(categoricalItems.containsKey(cat)) {
              ArrayList<Item> itemArrayList =   categoricalItems.get(cat);
                itemArrayList.add(item);
                categoricalItems.put(cat,itemArrayList);
            }else{
                ArrayList<Item> itemArrayList = new ArrayList<>();
                itemArrayList.add(item);
                categoricalItems.put(cat,itemArrayList);

            }

        }



        return  new ArrayList<>(categoricalItems.values());
    }
}
