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

import edu.upenn.cis350.cis350project.api.APIHandler;

public class PhoneActivity extends AppCompatActivity {
    LocationManager locationManager ;
    String user;
    boolean gpsStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        user = getIntent().getStringExtra("username");
        gpsStatus = getIntent().getBooleanExtra("gps", gpsStatus);

        Switch s = (Switch) findViewById(R.id.switch1);
        s.setChecked(gpsStatus);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    gpsStatus = true;
                } else {
                    gpsStatus = false;
                }

                APIHandler apiHandler = new APIHandler();
                apiHandler.postGPSStatus(user, gpsStatus);
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
        startActivity(callIntent);
    }
}
