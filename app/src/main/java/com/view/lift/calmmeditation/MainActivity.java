package com.view.lift.calmmeditation;

import android.content.pm.ApplicationInfo;
import android.security.NetworkSecurityPolicy;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.view.lift.calmmeditation.fragments.VideoListFragment;

public class MainActivity extends AppCompatActivity {

    VideoListFragment videoListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       //NetworkSecurityPolicy.getInstance().setCleartextTrafficPermitted((data.appInfo.flags & ApplicationInfo.FLAG_USES_CLEARTEXT_TRAFFIC) != 0);

        setContentView(R.layout.activity_main);
        videoListFragment = VideoListFragment.getInstance(this);
        addFragment(videoListFragment);

    }


    public void addFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction  fragmentTransaction =  fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.add(R.id.fragmentContainer, fragment, videoListFragment.getClass().toString());
        fragmentTransaction.commit();

    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
