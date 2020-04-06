package edu.upenn.cis350.cis350project.api;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {

    @GET("validateLogin")
    Single<DefaultResponse> validateLogin(
            @Query("username") String username,
            @Query("password") String password);

    @POST("signup")
    Single<DefaultResponse> signup(
            @Query("username") String username,
            @Query("password") String password);

    @POST("deleteAccount")
    Single<DefaultResponse> deleteAccount(
            @Query("username") String username
    );

}
