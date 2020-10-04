package com.example.covidmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment {

    private static GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest mLocationRequest;
    final int ZOOM=15;

    private OnMapReadyCallback callback = new OnMapReadyCallback()  {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;

            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
            //Get location permissions right off the bat
            getPermissions(Manifest.permission.ACCESS_FINE_LOCATION);
            //Check for permissions
            if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    !mFusedLocationClient.getLastLocation().isSuccessful()) {
                getLastLocation();



            } else {
                //If no permissions, focus on Melbourne
                LatLng melbourne = new LatLng(-37, 144);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(melbourne));
            }


            if(mMap != null){
                mMap = mMap;
                mMap.getUiSettings().setMapToolbarEnabled(true);
            }
//            genMarkers();


//            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
//                @Override
//                public void onMapLoaded() {

//Add markers here


//                }


        }

        @SuppressLint("MissingPermission")
        private void getLastLocation() {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        LatLng loc = new LatLng(location.getLatitude(),
                                location.getLongitude());
                        //focus map to current location
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, ZOOM)); // zoom

                        //Show current location
                        mMap.setMyLocationEnabled(true);

                    }
                }
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

    }

//    private void genMarkers()
//    {
//
//
//                Log.d("TEST", "made to genMarkers");
//                DatabaseHelper db = new DatabaseHelper(App.getmContext());
//                double lat;
//                double lng;
//                int size = db.getLocationSize();
//                LatLng ll;
//                Log.d("TEST23", "checkId0");
//                boolean y = db.checkId(1000);
//                Log.d("TEST23", "checkId01");
//
////        List<PCLocation> PCLList = new ArrayList<PCLocation>();
//                List<PCLocation> PCLList2 = db.getAllPostcodeLocations();
//                List<Postcode> allPCs = db.getAllPostcodes();
//                Log.d("TEST23", "allPCs size: " + allPCs.size());
//                Log.d("TEST23", "PCLList2 size: " + PCLList2.size());
//                Log.d("TEST23", "checkId01");
//                Log.d("TEST23", "PC " + allPCs.get(0).getPostcode());
//                Log.d("TEST23", "PCL " + PCLList2.get(0).getPc());
//        Log.d("TEST23", "checkId01");
////        LatLng testll = new LatLng(PCLList2.get(0).getLat(), PCLList2.get(0).getLat());
////        Marker marker = this.mMap.addMarker(new MarkerOptions().position(testll));
//        LatLng testll2 = new LatLng(-37.811090,144.958430);
//        Marker marker = this.mMap.addMarker(new MarkerOptions().position(testll2).title("yeet"));
//        Log.d("TEST23", "checkId022");
//                mMap.clear();
////                for (int i = 0; i < allPCs.size(); ++i) {
////
////
////                    lat = PCLList2.get(i).getLat();
////                    lng = PCLList2.get(i).getLng();
////                    ll = new LatLng(lat, lng);
////
////                    Marker marker = this.mMap.addMarker(new MarkerOptions()
////                            .position(ll)
////                            .title(Integer.toString(PCLList2.get(i).getPc()) + "\nActive: " + allPCs.get(i).getActive() + "\nTotal: " + allPCs.get(i).getCases()));
////                    Log.d("TEST", "made to genMarkers2");
////                }
//
//
//
//
//    }

    public static GoogleMap getMap()
    {
        return mMap;
    }

    private void getPermissions(String permission) {
        if(ActivityCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Permissions", "Permission \"" + permission + "\" is not granted, requesting");
            ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, 333);
        } else {
            Log.d("Permissions", "Permission \"" + permission + "\" is granted");
        }
    }

    public void focusLatLng(LatLng loc) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, ZOOM));
    }

    @SuppressLint("MissingPermission")
    public void focusCurrent() {
        getPermissions(Manifest.permission.ACCESS_FINE_LOCATION);
        mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    LatLng loc = new LatLng(location.getLatitude(),
                            location.getLongitude());
                    //focus map to current location
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, ZOOM)); // zoom

                    mMap.setMyLocationEnabled(true);
                }
            }
        });
    }
}