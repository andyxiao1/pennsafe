package edu.upenn.cis350.cis350project.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserData {

    @Expose
    @SerializedName("username")
    private String username;

    @Expose
    @SerializedName("password")
    private String password;

    @Expose
    @SerializedName("banned")
    private Boolean banned;

    @Expose
    @SerializedName("lastLoggedIn")
    private Long lastLoggedIn;

    @Expose
    @SerializedName("latitude")
    private Double latitude;

    @Expose
    @SerializedName("longitude")
    private Double longitude;

    @Expose
    @SerializedName("email")
    private String email;

    @Expose
    @SerializedName("telephone")
    private String telephone;

    @Expose
    @SerializedName("address")
    private String address;

    @Expose
    @SerializedName("image")
    private String image;

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Boolean getBanned() { return banned; }
    public Long getLastLoggedIn() { return lastLoggedIn; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public String getEmail() { return email; }
    public String getTelephone() { return telephone; }
    public String getAddress() { return address; }
    public String getImage() { return image; }

}
