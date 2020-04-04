package edu.upenn.cis350.cis350project.api;

import android.util.Log;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIHandler {

    private final boolean runOnEmulator = false;
    private final String apiEndpoint = "http://192.168.1.31:3000";

    private Retrofit getRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiEndpoint)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }

    public void validateLogin(String username, String password, final APIResponseWrapper responseWrapper) {
        APIService apiService = getRetrofit().create(APIService.class);
        Single<LoginResponse> result = apiService.validateLogin(username, password);
        result.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<LoginResponse>() {

                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onSuccess(LoginResponse response) {
                        responseWrapper.onResponse(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        responseWrapper.onResponse(null);
                    }

                });
    }

    public void signup(String username, String password, final APIResponseWrapper responseWrapper) {
        APIService apiService = getRetrofit().create(APIService.class);
        Single<LoginResponse> result = apiService.signup(username, password);
        result.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<LoginResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onSuccess(LoginResponse response) {
                        responseWrapper.onResponse(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        responseWrapper.onResponse(null);
                    }
                });
    }

}
