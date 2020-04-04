package edu.upenn.cis350.cis350project.api;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {

    @GET("validateLogin")
    Single<LoginResponse> validateLogin(
            @Query("username") String username,
            @Query("password") String password);

    // Uses a login response object because it has the same data fields
    @GET("signup")
    Single<LoginResponse> signup(
            @Query("username") String username,
            @Query("password") String password);

}
