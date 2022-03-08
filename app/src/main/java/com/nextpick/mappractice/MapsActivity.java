package com.nextpick.mappractice;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.nextpick.mappractice.databinding.ActivityMapsBinding;

import java.security.Permission;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {
//    AIzaSyAbNNeGmSw7YMQvIxWW3S5EP9kr3hLrTfg
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    LocationManager locationManager;
    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        AIzaSyAbNNeGmSw7YMQvIxWW3S5EP9kr3hLrTfg

//        String DirectionClien = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lon + "&sensor=false&key=" + sp.getString("googlemapkey", "");
        setContentView(R.layout.activity_maps);
        locationManager =  (LocationManager) getSystemService(LOCATION_SERVICE);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location==null){
                    LocationRequest locationRequest =  LocationRequest.create();
                    locationRequest.setInterval(10000);
                    locationRequest.setInterval(3000);
                    fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,null);
                }
            }
        });


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
    Marker marker;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
      marker = mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney").icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin_small)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,14));
        try {
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                if(getPackageManager().checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,getPackageName())!= PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},3323);
                }else{
                    getCurrentLocation();
                }
            }else{
                getCurrentLocation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }
    FusedLocationProviderClient fusedLocationProviderClient ;
private void getCurrentLocation(){
    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    if(location!=null){
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        marker.setPosition(latLng);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));
    }
}
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        }
    }

//    public void requestAuthorize( String stringToSend,final ServiceRespone serviceRespone) {
//        String response = "";
//        RequestQueue queue = Volley.newRequestQueue(context);
////        https://cabtreasureappapi.co.uk/CabTreasureWebApi/Home/VerifyClientAppAccountWeb?accountCode=dev2123&deviceInfo=Android&deviceId=what&hashKey=dev2123Androidwhat4321orue
//        String url = "https://cabtreasureappapi.co.uk/CabTreasureWebApi/Home/VerifyClientAppAccountWeb?"+stringToSend;
//        url=url.replace(" ","%20");
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            String prefix ="\\\\";
//                            if(response.startsWith("\"")){
//                                response =response.substring(1,response.length()-1);
//                                response = response.replaceAll(prefix, "");
//                            }
//                            serviceRespone.getServiceResponse(response);
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            serviceRespone.getServiceResponse("pdaclienterror:Error in parsing details,Please try again later");
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                serviceRespone.getServiceResponse("pdaclienterror:"+error.getLocalizedMessage());
//            }
//        });
//        stringRequest.setTag(FojAcceptTag);
//// Add the request to the RequestQueue.
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                8000,
//                0,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        queue.add(stringRequest);
//
//    }
}