package com.example.gpskeychain;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapActivity extends FragmentActivity implements View.OnClickListener, OnMapReadyCallback, AdapterView.OnItemSelectedListener{
    TextView hello;
    ImageView person;
    Button logout,stop;
    Spinner spinner;
    //Vibrator vibrator;

    GoogleMap map;

    //MarkerOptions markerOptions;
    //Polyline currentPolyline;

    Location current_loc;
    FusedLocationProviderClient client;
    private static final int REQ_CODE = 1;
    boolean clicked=false;

    ArrayList<String> keyList=new ArrayList<>();

   // SQL db=new SQL(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        FirebaseApp.initializeApp(this);

        hello = findViewById(R.id.hello);
        person = findViewById(R.id.person);
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(this);
        stop=findViewById(R.id.stop);
        stop.setOnClickListener(this);

       // settingName();
        //settingAdapter();
        spinnerInitialize();

        Animation animation= AnimationUtils.loadAnimation(this,R.anim.bounce);
        stop.startAnimation(animation);


//        SharedPreferences sp=getSharedPreferences("Key",MODE_PRIVATE);
//        String username= sp.getString("username",null);

//        userLoc= new MarkerOptions().position(new LatLng(current_loc.getLatitude(),current_loc.getLongitude())).title("User location");
//        keyLoc= new MarkerOptions().position(new LatLng(db.fetchLat(username),db.fetchLong(username))).title("Key location");
//
//        String url= getUrl(userLoc.getPosition(),keyLoc.getPosition(),"driving");
//        new FetchURL(MapActivity.this).execute(url,"driving");
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    /*private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        String str_origin="origin" + origin.latitude + "," + origin.longitude;
        String str_dest="destination" + dest.latitude + "," + dest.longitude;
        String mode= "mode" + directionMode;
        String parameters= str_origin + "&" + str_dest + "&" + mode;
        String output="json";
        String url="https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }*/

    // public void settingAdapter() {

    //        SharedPreferences sp=getSharedPreferences("Key",MODE_PRIVATE);

    //        String username=sp.getString("username",null);

    //

    //        int id=db.fetchId(username);

    //        int count=db.fetchCountKey(id);

    //

    //    }

    private void settingName() {
        SharedPreferences sp = getSharedPreferences("key", MODE_PRIVATE);
        String client_name = sp.getString("fname", null);
        String final_name = hello.getText().toString() + client_name;
        hello.setText(final_name);
    }

    public void spinnerInitialize() {
        for (int i=1;i<=5;i++){
            String temp="Key "+i;
            keyList.add(temp);
        }
        spinner=findViewById(R.id.key_select);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, keyList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    /* private void getCurrentLocation() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new MapActivity();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 10, locationListener);
    } */

    public void logout() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient client = GoogleSignIn.getClient(this, gso);
        client.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent i = new Intent(MapActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

        client= LocationServices.getFusedLocationProviderClient(this);
        fetchLoc();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        if (v == logout) {
            logout();
            Toast.makeText(this, "Logout Successful", Toast.LENGTH_LONG).show();
        }
        else if(v==stop){
            clicked=true;
            vibrate(current_loc.getLatitude(),current_loc.getLongitude(),clicked);
        }
    }

    private void fetchLoc() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_CODE);
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    current_loc = location;
                    Toast.makeText(getApplicationContext(), current_loc.getLatitude() + "" + current_loc.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(MapActivity.this);
                }
            }
        });
    }

    void vibrate(double lat, double lng, Boolean clicked) {
        final long[] pattern = {1000, 2000};

        MediaPlayer player=MediaPlayer.create(this,R.raw.buzzersound);

        if ((distance(current_loc.getLatitude(), current_loc.getLongitude(), lat, lng) < 0.01) && !clicked) {
            player.start();
            stop.setVisibility(View.VISIBLE);
        }
        if (clicked) {
            player.pause();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // LatLng latLng = new LatLng(current_loc.getLatitude(), current_loc.getLongitude());

        //MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I am here!");

        //googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

        //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));

        //googleMap.addMarker(markerOptions);

        map= googleMap;

        FirebaseDatabase db=FirebaseDatabase.getInstance();
        DatabaseReference reff=db.getReference("GPS");
        ValueEventListener listener=reff.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double lat= Double.parseDouble((String) snapshot.child("Latitude").getValue());
                double lng= Double.parseDouble((String) snapshot.child("Longitude").getValue());

                LatLng location=new LatLng(lat,lng);
                map.addMarker(new MarkerOptions().position(location).title("Key Location"));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(location,10));

                vibrate(lat,lng,clicked);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


   /* public void onTaskDone(Object... values) {
        if(currentPolyline!=null)
            currentPolyline.remove();
        currentPolyline=map.addPolyline((PolylineOptions) values[0]);
    }*/
}