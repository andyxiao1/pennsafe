package edu.upenn.cis350.cis350project.api;

import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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

    @POST("uploadPhoto")
    @FormUrlEncoded
    Single<DefaultResponse> sendPhoto(
            @Query("username") String username,
            @Field("image") String image
    );

    @POST("updateUser")
    Single<DefaultResponse> sendUpdatedData(
            @Query("username") String username,
            @Query("email") String email,
            @Query("phone") String phone,
            @Query("address") String address,
            @Query("nickname") String nickname,
            @Query("other") String other
    );

    @POST("user/{username}/gps")
    Single<DefaultResponse> sendGPS(
            @Path("username") String username,
            @Query("gps") boolean gps
    );
}
