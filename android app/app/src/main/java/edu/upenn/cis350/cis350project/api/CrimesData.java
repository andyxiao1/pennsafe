package edu.upenn.cis350.cis350project.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CrimesData {

    @Expose
    @SerializedName("crimes")
    private CrimeData[] crimes;

    public CrimeData[] getCrimes() {
         return crimes;
    }

}
