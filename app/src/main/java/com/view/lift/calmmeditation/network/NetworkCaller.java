package com.view.lift.calmmeditation.network;



import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import com.view.lift.calmmeditation.dto.Feed;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkCaller  {

    private API api;

    public NetworkCaller(@NonNull Application application) {
        this.api = NetworkController.getNetworkInstance().create(API.class);
    }

    public LiveData<Feed> getRssFeed()
    {
        final MutableLiveData<Feed> data = new MutableLiveData<>();
        this.api.getAllVideos()
                .enqueue(new Callback<Feed>()
                {
                    @Override
                    public void onResponse(Call<Feed> call,     Response<Feed> response)
                    {
                        if (response.isSuccessful()) {
                            data.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<Feed> call, Throwable t) {
                        data.setValue(null);
                    }
                });
        return data;
    }

}
