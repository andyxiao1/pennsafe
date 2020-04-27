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
import edu.upenn.cis350.cis350project.api.CrimeData;
import edu.upenn.cis350.cis350project.api.CrimesDataAPIResponse;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<Marker> crimeMarkers = new ArrayList<>();
    CrimeData[] crimes;

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

        // Add Bluelight phone markers
        LatLng one = new LatLng(39.950755, -75.198928);
        MarkerOptions oneTemp = new MarkerOptions().position(one).icon(BitmapDescriptorFactory.
                defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        oneTemp = oneTemp.position(one).title("Bluelight");
        mMap.addMarker(oneTemp);

        LatLng two = new LatLng(39.961587, -75.208887);
        MarkerOptions twoTemp = new MarkerOptions().position(two).icon(BitmapDescriptorFactory.
                defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        twoTemp = twoTemp.position(two).title("Bluelight");
        mMap.addMarker(twoTemp);

        LatLng three = new LatLng(39.958889, -75.204404);
        MarkerOptions threeTemp = new MarkerOptions().position(three).icon(BitmapDescriptorFactory.
                defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        threeTemp = threeTemp.position(three).title("Bluelight");
        mMap.addMarker(threeTemp);

        LatLng four = new LatLng(39.953653, -75.185253);
        MarkerOptions fourTemp = new MarkerOptions().position(four).icon(BitmapDescriptorFactory.
                defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        fourTemp = fourTemp.position(four).title("Bluelight");
        mMap.addMarker(fourTemp);

        LatLng five = new LatLng(39.962250, -75.198484);
        MarkerOptions fiveTemp = new MarkerOptions().position(five).icon(BitmapDescriptorFactory.
                defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        fiveTemp = fiveTemp.position(five).title("Bluelight");
        mMap.addMarker(fiveTemp);

        LatLng six = new LatLng(39.958884, -75.202572);
        MarkerOptions sixTemp = new MarkerOptions().position(six).icon(BitmapDescriptorFactory.
                defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        sixTemp = sixTemp.position(six).title("Bluelight");
        mMap.addMarker(sixTemp);

        LatLng seven = new LatLng(39.958055, -75.192775);
        MarkerOptions sevenTemp = new MarkerOptions().position(seven).icon(BitmapDescriptorFactory.
                defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        sevenTemp = sevenTemp.position(seven).title("Bluelight");
        mMap.addMarker(sevenTemp);

        LatLng eight = new LatLng(39.957206, -75.199978);
        MarkerOptions eightTemp = new MarkerOptions().position(eight).icon(BitmapDescriptorFactory.
                defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        eightTemp = eightTemp.position(eight).title("Bluelight");
        mMap.addMarker(eightTemp);

        LatLng nine = new LatLng(39.952036,-75.190375 );
        MarkerOptions nineTemp = new MarkerOptions().position(nine).icon(BitmapDescriptorFactory.
                defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        nineTemp = nineTemp.position(nine).title("Bluelight");
        mMap.addMarker(nineTemp);

        LatLng ten = new LatLng(39.954165,-75.199898 );
        MarkerOptions tenTemp = new MarkerOptions().position(ten).icon(BitmapDescriptorFactory.
                defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        tenTemp = tenTemp.position(ten).title("Bluelight");
        mMap.addMarker(tenTemp);

        // Add Penn Police Location
        LatLng police = new LatLng(39.956626, -75.203648);
        MarkerOptions policeTemp = new MarkerOptions().position(police).icon(BitmapDescriptorFactory.
                defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
        policeTemp = policeTemp.position(police).title("Penn Police");
        mMap.addMarker(policeTemp);

        // Add crimes to the map
        APIHandler apiHandler = new APIHandler();
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
