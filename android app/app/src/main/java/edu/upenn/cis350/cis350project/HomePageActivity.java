package edu.upenn.cis350.cis350project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import edu.upenn.cis350.cis350project.api.APIHandler;
import edu.upenn.cis350.cis350project.api.APIResponse;
import edu.upenn.cis350.cis350project.api.APIResponseWrapper;
import edu.upenn.cis350.cis350project.api.DefaultResponse;
import edu.upenn.cis350.cis350project.api.UserDataAPIResponse;

public class HomePageActivity extends AppCompatActivity {

    private final int LOCATION_REQUEST_CODE = 1;
    private final int REQUEST_GPS_CODE = 2;

    private String user;
    private boolean isBanned;
    private boolean gpsEnabled;
    private boolean locationEnabled;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Intent intent = getIntent();
        user = intent.getStringExtra(LoginActivity.USERNAME_TAG);

        if (user == null) {
            intent = new Intent(this, LoginActivity.class);
            intent.putExtra(LoginActivity.STATUS, LoginActivity.ERROR);
            startActivity(intent);
        } else {
            ((TextView) findViewById(R.id.user_text)).setText(user);
        }

        onLogin();

    }

    /*
    When user logs in, send time of login and location to server.
     */
    private void onLogin() {
        if (!hasLocationPermissions()) {
            requestLocationPermissions();
        } else {
            locationEnabled = true;
        }

        final APIHandler apiHandler = new APIHandler();
        apiHandler.logLogin(user);

        apiHandler.getUserData(user, new APIResponseWrapper() {
            @Override
            public void onResponse(APIResponse response) {
                UserDataAPIResponse dataResponse = (UserDataAPIResponse) response;
                if (dataResponse != null && dataResponse.getUserData() != null) {
                    Boolean bannedData = dataResponse.getUserData().getBanned();
                    isBanned = bannedData == null ? false : bannedData;
                    Boolean gpsData = dataResponse.getUserData().getGPS();
                    gpsEnabled = gpsData == null ? true : gpsData;
                    TextView bannedText = findViewById(R.id.banned_text);
                    String text = isBanned ? "Banned" : "Active";
                    int color = isBanned ? Color.RED : Color.GREEN;
                    bannedText.setText(text);
                    bannedText.setTextColor(color);
                }
            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null && gpsEnabled) {
                            apiHandler.logLogin(user, location.getLatitude(), location.getLongitude());
                        } else {
                            apiHandler.logLogin(user);
                        }
                    }
                });
    }

    public void onLogoutClick(View view) {
        SharedPreferences prefs = getSharedPreferences(LoginActivity.PREFS_LOGIN_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(LoginActivity.PREFS_LOGGED_IN, false);
        editor.remove(LoginActivity.PREFS_LOGGED_IN_USER);
        editor.commit();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void onDeleteClick(View view) {
        final String username = ((TextView) findViewById(R.id.user_text)).getText().toString();
        APIHandler a = new APIHandler();
        a.deleteAccount(username, new APIResponseWrapper() {
            @Override
            public void onResponse(APIResponse response) {
                DefaultResponse deleteResponse = (DefaultResponse) response;
                if (deleteResponse != null && deleteResponse.getSuccessful()) {
                    onLogoutClick(null);
                }
            }
        });
    }

    public void onPhoneClick(View view) {
        Intent intent = new Intent(this, PhoneActivity.class);
        intent.putExtra("gps", gpsEnabled);
        intent.putExtra("username", user);
        startActivityForResult(intent, REQUEST_GPS_CODE);
    }

    public void onAccountClick(View view) {
        Intent intent = new Intent(this, AccountActivity.class);
        intent.putExtra("username", user);
        startActivity(intent);
    }

    public void onFlashlightClick(View view) {
        Intent intent = new Intent(this, FlashlightActivity.class);
        startActivity(intent);
    }

    public boolean hasLocationPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestLocationPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int code, String[] permissions, int[] results) {
        switch (code) {
            case LOCATION_REQUEST_CODE:
                if (results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED) {
                    locationEnabled = true;
                    onLogin();
                } else {
                    requestLocationPermissions();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (REQUEST_GPS_CODE) : {
                final APIHandler apiHandler = new APIHandler();
                apiHandler.getUserData(user, new APIResponseWrapper() {
                    @Override
                    public void onResponse(APIResponse response) {
                        UserDataAPIResponse dataResponse = (UserDataAPIResponse) response;
                        if (dataResponse != null && dataResponse.getUserData() != null) {
                            Boolean gpsData = dataResponse.getUserData().getGPS();
                            gpsEnabled = gpsData == null ? true : gpsData;
                        }
                    }
                });
            }
        }
    }

}
