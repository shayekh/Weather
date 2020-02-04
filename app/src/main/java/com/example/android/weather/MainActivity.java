package com.example.android.weather;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.android.weather.weather.WeatherResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import android.location.LocationListener;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private FusedLocationProviderClient client;
    private static int REQUEST_CODE_FOR_LOCATION = 1;
    private LocationRequest request;
    private LocationCallback callback;
    private Api_service service;

    double lat=23.750883;
    double lon=90.393404;
    private TextView textView;
    private ProgressDialog progressDialog;
   // private Geocoder geocoder;
    //public String locationName;
    private String unit = "metric";
    public  double latn;
    public  double longitude,latitude;

    @android.annotation.SuppressLint("MissingPermission")@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=(TextView)findViewById(R.id.textTv);
           android.location.LocationManager locationManager = (android.location.LocationManager) getSystemService(android.content.Context.LOCATION_SERVICE);
//        client = LocationServices.getFusedLocationProviderClient(this);
//        request = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(30000).setFastestInterval(10000);
//
//        callback = new LocationCallback() {
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                super.onLocationResult(locationResult);
//                Location loc = new Location(locationResult.getLastLocation());
//
////                lat = loc.getLatitude();
////                lon = loc.getLongitude();
//                for (Location location : locationResult.getLocations()) {
//                    latn = location.getLatitude();
//                    longitude = location.getLongitude();
//
//
////                    try {
////                        List<Address> addresses = geocoder.getFromLocation(latn, longitude, 1);
////                        locationName = addresses.get(0).getFeatureName() + " , " + addresses.get(0).getLocality() + " ," + addresses.get(0).getPostalCode();
////
////                    } catch (IOException e) {
////                        e.printStackTrace();
////                    }
//
//                }
//
////                LatLng sydney = new LatLng(lat, lon);
//////                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,16));
//////                mMap.addMarker(new MarkerOptions().position(sydney).title(locationName));
//////                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//            }
//        };
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_FOR_LOCATION);
//            return;
//        }
//        client.requestLocationUpdates(request, callback, null);
//

        //mMap = googleMap;

        // Add a marker in Sydney and move the camera

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Please wait");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);



     // Get user location
        locationManager = (android.location.LocationManager) getSystemService(android.content.Context.LOCATION_SERVICE);

		// Keep track of user location.
		// Use callback/listener since requesting immediately may return null location.
		// IMPORTANT: TO GET GPS TO WORK, MAKE SURE THE LOCATION SERVICES ON YOUR PHONE ARE ON.
		// FOR ME, THIS WAS LOCATED IN SETTINGS > LOCATION.
        locationManager.requestLocationUpdates(android.location.LocationManager.NETWORK_PROVIDER, 1000, 10, new Listener());
        // Have another for GPS provider just in case.
        locationManager.requestLocationUpdates(android.location.LocationManager.GPS_PROVIDER, 1000, 10, new Listener());
        // Try to request the location immediately
        Location location = locationManager.getLastKnownLocation(android.location.LocationManager.NETWORK_PROVIDER);
        if (location == null){
        	location = locationManager.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER);
        }
        if (location != null){
        	handleLatLng(location.getLatitude(), location.getLongitude());
        }
        android.widget.Toast.makeText(getApplicationContext(),
        	"Trying to obtain GPS coordinates. Make sure you have location services on.",
        	android.widget.Toast.LENGTH_SHORT).show();


        String url = String.format("weather?lat=%f&lon=%f&units=%s&appid=%s",latitude,longitude,unit,getResources().getString(R.string.weather_key));

        service = Api_Response.getUser().create(Api_service.class);
        Call<WeatherResponse> weatherResponseCall = service.getAllUser(url);
        weatherResponseCall.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call <WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.code() == 200) {
                    WeatherResponse userResponse = response.body();
                    textView.setText(userResponse.getMain().getTempMin().toString()+ " C"+ "\n"+userResponse.getMain().getHumidity().toString()+" H");
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call <WeatherResponse> call, Throwable t) {

            }
        });


        }



    /**
     * Handle lat lng.
     */
    private void handleLatLng(double latitude, double longitude){
        android.util.Log.v("TAG", "(" + latitude + "," + longitude + ")");
    }


    /**
     * Listener for changing gps coords.
     */

    private class Listener implements LocationListener {
	    public void onLocationChanged(Location location) {
	         longitude = location.getLongitude();
	         latitude = location.getLatitude();
	        handleLatLng(latitude, longitude);
	    }

	    public void onProviderDisabled(String provider){}
	    public void onProviderEnabled(String provider){}
	    public void onStatusChanged(String provider, int status, Bundle extras){}
    }
    }










