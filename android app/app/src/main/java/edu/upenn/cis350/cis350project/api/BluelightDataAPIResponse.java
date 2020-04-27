package edu.upenn.cis350.cis350project.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BluelightDataAPIResponse extends  APIResponse {
    @Expose
    @SerializedName("successful")
    private Boolean successful;

    @Expose
    @SerializedName("bluelights")
    private BluelightData[] bluelights;

    public boolean getSuccessful() { return successful; }
    public BluelightData[] getBluelightsData() { return bluelights; }
}