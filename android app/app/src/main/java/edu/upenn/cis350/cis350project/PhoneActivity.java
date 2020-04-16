package edu.upenn.cis350.cis350project;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.location.LocationManager;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PhoneActivity extends AppCompatActivity {
    LocationManager locationManager ;
    boolean GpsStatus ;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        context = getApplicationContext();
        getGPS();

        Switch s = (Switch) findViewById(R.id.switch1);
        s.setChecked(GpsStatus);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent1);
                } else {
                    Log.d("GPS ON", "Turn GPS Off");
                }
            }
        });
    }

    public void onButtonClick(View view) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        switch (view.getId()) {
            case R.id.security:
                callIntent.setData(Uri.parse("tel:0377778888"));
                break;
            case R.id.caps:
                callIntent.setData(Uri.parse("tel:0377778889"));
                break;
            case R.id.walking:
                callIntent.setData(Uri.parse("tel:0377778880"));
                break;
        }

        if (ActivityCompat.checkSelfPermission(PhoneActivity.this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(PhoneActivity.this, new String[]{Manifest.permission.CALL_PHONE},1);
            return;
        }
        Log.d("Here", "Here");
        startActivity(callIntent);
    }

    public void getGPS() {
        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(GpsStatus == true) {
            Log.d("GPS", "GPS ON");
        }
    }
}
