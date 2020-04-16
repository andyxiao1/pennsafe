package edu.upenn.cis350.cis350project.api;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {

    @GET("validateLogin")
    Single<DefaultResponse> validateLogin(
            @Query("username") String username,
            @Query("password") String password);

    @POST("login")
    Single<DefaultResponse> logLogin(
            @Query("username") String username,
            @Query("latitude") Double latitude,
            @Query("longitude") Double longitude

    );

    @POST("signup")
    Single<DefaultResponse> signup(
            @Query("username") String username,
            @Query("password") String password);

    @POST("deleteAccount")
    Single<DefaultResponse> deleteAccount(
            @Query("username") String username
    );

    @GET("user/{username}")
    Single<UserDataAPIResponse> getUser(
            @Path("username") String username
    );

    @POST("user/uploadPhoto")
    Single<DefaultResponse> sendPhoto();

}
