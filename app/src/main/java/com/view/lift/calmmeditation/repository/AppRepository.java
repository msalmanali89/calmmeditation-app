package com.view.lift.calmmeditation.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import com.view.lift.calmmeditation.dto.Feed;
import com.view.lift.calmmeditation.network.NetworkCaller;


public class AppRepository {

    LiveData<Feed> ressFeeds;
    Application application;

    public AppRepository(@NonNull Application application){
        this.application = application;
    }

    public LiveData<Feed> getAllVideos() {
        NetworkCaller  networkCaller =  new NetworkCaller(this.application);
        ressFeeds = networkCaller.getRssFeed();
        return ressFeeds;
    }
}
