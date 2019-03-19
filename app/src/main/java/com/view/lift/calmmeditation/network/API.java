package com.view.lift.calmmeditation.network;
import com.view.lift.calmmeditation.dto.Feed;

import retrofit2.Call;
import retrofit2.http.GET;

public interface API {

    @GET("feed_firetv.xml")
    Call<Feed> getAllVideos(
          //  @Query("api_key") String api_key,
            //@Query("page") int page
    );

}
