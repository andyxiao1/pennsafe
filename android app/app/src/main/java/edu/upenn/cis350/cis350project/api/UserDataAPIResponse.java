package edu.upenn.cis350.cis350project.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDataAPIResponse extends APIResponse {

    @Expose
    @SerializedName("successful")
    private Boolean successful;

    @Expose
    @SerializedName("user")
    private UserData user;

    public boolean getSuccessful() { return successful; }
    public UserData getUserData() { return user; }

}
