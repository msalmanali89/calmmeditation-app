package com.view.lift.calmmeditation;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.Espresso.*;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import com.view.lift.calmmeditation.dto.Feed;
import com.view.lift.calmmeditation.fragments.VideoListFragment;
import com.view.lift.calmmeditation.repository.AppRepository;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.logging.Handler;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {


    public  VideoListFragment videoListFragment;

    @Rule
    public  ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.view.lift.calmmeditation", appContext.getPackageName());
    }

    @Test
    public void testOnClickVideo(){

        videoListFragment = VideoListFragment.getInstance(rule.getActivity());
        //rule.getActivity().addFragment(videoListFragment);
        videoListFragment.playVideo("http://clips.vorwaerts-gmbh.de/VfE_html5.mp4",true,0);


        rule.getActivity().getMainLooper().prepare();
        android.os.Handler handler = new android.os.Handler(rule.getActivity().getMainLooper());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                assertTrue(true);
            }
        },2500000);
    }

   // @Test
    public void callToServer(){

        videoListFragment = VideoListFragment.getInstance(rule.getActivity());

        AppRepository  appRepository = new AppRepository(rule.getActivity().getApplication());

        LiveData<Feed> feedLiveData = appRepository.getAllVideos();

        Feed feed = feedLiveData.getValue();

        if(feed == null) {
            assertTrue(false);
        }else {
            assertTrue(true);
        }


    }



}
