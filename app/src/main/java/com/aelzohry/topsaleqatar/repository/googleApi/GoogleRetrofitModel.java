package com.aelzohry.topsaleqatar.repository.googleApi;


import static com.aelzohry.topsaleqatar.helper.Constants.BASE_GOOGLE_API_URL;

import com.aelzohry.topsaleqatar.repository.googleApi.model.AutoCompleteResponse;
import com.aelzohry.topsaleqatar.repository.googleApi.model.GetPlaceDetailsResponse;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoogleRetrofitModel {

    // main variables
    private GoogleApi api;
    private Scheduler subscribeOn;
    private Scheduler observeOn;

    public GoogleRetrofitModel() {
        Interceptor interceptorToHeaderData;
        interceptorToHeaderData = chain -> {
            Request.Builder builder = chain.request().newBuilder();
            return chain.proceed(builder.build());
        };

        init(interceptorToHeaderData);
    }


    private void init(Interceptor interceptorToHeaderData) {

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptorToHeaderData)
                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .build();

        this.api = new Retrofit.Builder()
                .baseUrl(BASE_GOOGLE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient)
                .build()
                .create(GoogleApi.class);

        subscribeOn = Schedulers.io();
        observeOn = AndroidSchedulers.mainThread();
    }

    public Observable<AutoCompleteResponse> getAddressAutoComplete(String key, String input) {
        return api.getAddressAutoComplete(key, input,"country:qa")
                .subscribeOn(subscribeOn)
                .observeOn(observeOn);
    }

    public Observable<GetPlaceDetailsResponse> getPlaceById(String key, String placeId, String fields) {
        return api.getPlaceById(key, placeId,fields)
                .subscribeOn(subscribeOn)
                .observeOn(observeOn);
    }


}
