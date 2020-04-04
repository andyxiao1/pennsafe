import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModel {

    @Expose
    @SerializedName("id")
    private String username;

    @Expose
    @SerializedName("password")
    private String password;

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public void setUsername(String s) { username = s; }
    public void setPassword(String s) { password = s; }

}
