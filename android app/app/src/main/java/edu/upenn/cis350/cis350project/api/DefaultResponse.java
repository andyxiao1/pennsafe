package edu.upenn.cis350.cis350project.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DefaultResponse extends APIResponse{
    @Expose
    @SerializedName("successful")
    private Boolean successful;

    @Expose
    @SerializedName("message")
    private String message;

    public boolean getSuccessful() { return successful; }
    public String getMessage() { return message; }
    public void setSuccessful(boolean b) { successful = b; }
    public void setMessage(String s) { message = s; }
}
