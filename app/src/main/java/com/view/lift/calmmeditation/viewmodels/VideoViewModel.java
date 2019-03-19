package com.view.lift.calmmeditation.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.view.lift.calmmeditation.dto.Feed;
import com.view.lift.calmmeditation.repository.AppRepository;

public class VideoViewModel extends AndroidViewModel {

    AppRepository appRepository;
    LiveData<Feed> allVideos;

    public VideoViewModel(@NonNull Application application){
        super(application);

        appRepository = new AppRepository(application);
    }

    public LiveData<Feed> getAllVideos() {

        if(allVideos == null){
            allVideos = appRepository.getAllVideos();
        }
        return allVideos;
    }

}
