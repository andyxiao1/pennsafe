package edu.upenn.cis350.cis350project.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CrimeData {

    @Expose
    @SerializedName("date")
    private String date;

    @Expose
    @SerializedName("time")
    private String time;

    @Expose
    @SerializedName("description")
    private String description;

    @Expose
    @SerializedName("latitude")
    private double latitude;

    @Expose
    @SerializedName("longitude")
    private double longitude;

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

}
