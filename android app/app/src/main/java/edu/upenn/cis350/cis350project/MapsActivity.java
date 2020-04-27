package edu.upenn.cis350.cis350project;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.ArrayList;

import edu.upenn.cis350.cis350project.api.APIHandler;
import edu.upenn.cis350.cis350project.api.APIResponse;
import edu.upenn.cis350.cis350project.api.APIResponseWrapper;
import edu.upenn.cis350.cis350project.api.BluelightData;
import edu.upenn.cis350.cis350project.api.BluelightDataAPIResponse;
import edu.upenn.cis350.cis350project.api.CrimeData;
import edu.upenn.cis350.cis350project.api.CrimesDataAPIResponse;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<Marker> crimeMarkers = new ArrayList<>();
    CrimeData[] crimes;
    BluelightData[] bluelights;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Spinner spinner = (Spinner) findViewById(R.id.crimes_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.crimes_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                // An item was selected. You can retrieve the selected item using
                String setting = parent.getItemAtPosition(pos).toString();
                onPause();
                if (setting.equals("All")) {
                    onResume();
                } else {
                    for (int i = 0; i < crimes.length; i++) {
                        CrimeData crime = crimes[i];
                        if (crime.getDescription().equals(setting)) {
//                            ((TextView) findViewById(R.id.test_text)).setText(String.valueOf(crimes.length));
                            String date = crime.getDate();
                            String time = crime.getTime();
                            String description = crime.getDescription();
                            double latitude = crime.getLatitude();
                            double longitude = crime.getLongitude();

                            LatLng coord = new LatLng(latitude, longitude);
                            crimeMarkers.add(mMap.addMarker(new MarkerOptions().position(coord).title(description + " " +
                                    date + " " + time)));
                        }
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }});

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Penn and move the camera
        LatLng penn = new LatLng(39.952304, -75.193321);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(penn));
        LatLng ne = new LatLng(39.961742, -75.178446);
        LatLng sw = new LatLng(39.945244, -75.217756);
        LatLngBounds bounds = new LatLngBounds(sw, ne);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 1080, 1920, 0));

        // Add Penn Police Location
        LatLng police = new LatLng(39.956626, -75.203648);
        MarkerOptions policeTemp = new MarkerOptions().position(police).icon(BitmapDescriptorFactory.
                defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
        policeTemp = policeTemp.position(police).title("Penn Police");
        mMap.addMarker(policeTemp);

        // Add Bluelight phone markers
        APIHandler apiHandler = new APIHandler();
        apiHandler.getBlueLights(new APIResponseWrapper() {
            @Override
            public void onResponse(APIResponse response) {
                BluelightDataAPIResponse dataResponse = (BluelightDataAPIResponse) response;
                if (dataResponse != null && dataResponse.getBluelightsData() != null) {
                    bluelights = dataResponse.getBluelightsData();
                    for (BluelightData bluelight : bluelights) {
                        LatLng loc = new LatLng(bluelight.getLatitude(), bluelight.getLongitude());
                        MarkerOptions marker = new MarkerOptions().position(loc).icon(BitmapDescriptorFactory.
                                defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                        marker = marker.position(loc).title("Bluelight");
                        mMap.addMarker(marker);
                    }
                }
            }
        });

        // Add crimes to the map
        apiHandler = new APIHandler();
        apiHandler.getCrimesData(new APIResponseWrapper() {
            @Override
            public void onResponse(APIResponse response) {
                CrimesDataAPIResponse dataResponse = (CrimesDataAPIResponse) response;
                if (dataResponse != null && dataResponse.getCrimesData() != null) {
                    crimes = dataResponse.getCrimesData();
                    for (int i = 0; i < crimes.length; i++) {
                        CrimeData curr = crimes[i];
                        String date = curr.getDate();
                        String time = curr.getTime();
                        String description = curr.getDescription();
                        double latitude = curr.getLatitude();
                        double longitude = curr.getLongitude();

                        LatLng coord = new LatLng(latitude, longitude);
                        crimeMarkers.add(mMap.addMarker(new MarkerOptions().position(coord).title(description + " " +
                                date + " " + time)));
                    }


                }
            }
        });
    }

    @Override
    public void onPause() {
        for (int i = 0; i < crimeMarkers.size(); i++) {
            crimeMarkers.get(i).remove();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        // Add crimes to the map
        APIHandler apiHandler = new APIHandler();
        apiHandler.getCrimesData(new APIResponseWrapper() {
            @Override
            public void onResponse(APIResponse response) {
                CrimesDataAPIResponse dataResponse = (CrimesDataAPIResponse) response;
                if (dataResponse != null && dataResponse.getCrimesData() != null) {
                    CrimeData[] crimes = dataResponse.getCrimesData();
                    for (int i = 0; i < crimes.length; i++) {
                        CrimeData curr = crimes[i];
                        String date = curr.getDate();
                        String time = curr.getTime();
                        String description = curr.getDescription();
                        double latitude = curr.getLatitude();
                        double longitude = curr.getLongitude();

                        LatLng coord = new LatLng(latitude, longitude);
                        crimeMarkers.add(mMap.addMarker(new MarkerOptions().position(coord).title(description + " " +
                                date + " " + time)));
                    }


                }
            }
        });
        super.onResume();
    }
}
