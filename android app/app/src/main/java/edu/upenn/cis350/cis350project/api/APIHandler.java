package edu.upenn.cis350.cis350project.api;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIHandler {

    private final String apiEndpoint =  "http://f1a75d2c.ngrok.io";

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
        Single<DefaultResponse> result = apiService.validateLogin(username, password);
        result.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<DefaultResponse>() {

                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onSuccess(DefaultResponse response) {
                        responseWrapper.onResponse(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        responseWrapper.onResponse(null);
                        e.printStackTrace();
                    }

                });
    }

    public void logLogin(String username) {
        logLogin(username, null, null);
    }

    public void logLogin(String username, Double latitude, Double longitude) {
        APIService apiService = getRetrofit().create(APIService.class);
        Single<DefaultResponse> result = apiService.logLogin(username, latitude, longitude);
        result.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<DefaultResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {}
                    @Override
                    public void onSuccess(DefaultResponse defaultResponse) {}
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    public void signup(String username, String password, final APIResponseWrapper responseWrapper) {
        APIService apiService = getRetrofit().create(APIService.class);
        Single<DefaultResponse> result = apiService.signup(username, password);
        result.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<DefaultResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onSuccess(DefaultResponse response) {
                        responseWrapper.onResponse(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        responseWrapper.onResponse(null);
                    }
                });
    }

    public void deleteAccount(String username, final APIResponseWrapper responseWrapper) {
        APIService apiService = getRetrofit().create(APIService.class);
        Single<DefaultResponse> result = apiService.deleteAccount(username);
        result.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<DefaultResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onSuccess(DefaultResponse defaultResponse) {
                        responseWrapper.onResponse(defaultResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        responseWrapper.onResponse(null);
                    }
                });
    }

    public void getUserData(String username, final APIResponseWrapper responseWrapper) {
        APIService apiService = getRetrofit().create(APIService.class);
        Single<UserDataAPIResponse> result = apiService.getUser(username);
        result.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<UserDataAPIResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onSuccess(UserDataAPIResponse userDataAPIResponse) {
                        responseWrapper.onResponse(userDataAPIResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        responseWrapper.onResponse(null);
                    }
                });
    }

}
