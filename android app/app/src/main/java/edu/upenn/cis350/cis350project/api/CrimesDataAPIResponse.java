package edu.upenn.cis350.cis350project.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CrimesDataAPIResponse extends APIResponse {

    @Expose
    @SerializedName("successful")
    private Boolean successful;

    @Expose
    @SerializedName("crimes")
    private CrimeData[] crimes;

    public boolean getSuccessful() { return successful; }
    public CrimeData[] getCrimesData() { return crimes; }

}
