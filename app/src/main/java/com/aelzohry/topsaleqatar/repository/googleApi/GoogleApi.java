package com.aelzohry.topsaleqatar.repository.googleApi;


import com.aelzohry.topsaleqatar.repository.googleApi.model.AutoCompleteResponse;
import com.aelzohry.topsaleqatar.repository.googleApi.model.GetPlaceDetailsResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleApi {

    @GET("place/autocomplete/json")
    Observable<AutoCompleteResponse> getAddressAutoComplete(@Query("key") String key, @Query("input") String input, @Query("components") String components);

    @GET("place/details/json")
    Observable<GetPlaceDetailsResponse> getPlaceById(@Query("key") String key, @Query("placeid") String placeId, @Query("fields") String fields);

}
