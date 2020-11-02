package com.example.mapsicesi;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mapsicesi.comunicacionBD.HuecoThread;
import com.example.mapsicesi.comunicacionBD.UserThread;
import com.example.mapsicesi.modelo.Hueco;
import com.example.mapsicesi.modelo.User;
import com.example.mapsicesi.util.Constants;
import com.example.mapsicesi.util.HTTPSWebUtilDomi;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, View.OnClickListener, MyDialogFragment.Listener {

    private GoogleMap mMap;
    private User us;

    private UserThread userThread;
    private HuecoThread huecoThread;

    private LocationManager manager;
    private Marker me;
    private ArrayList<Marker>usuarios;
    private ArrayList<Marker>huecos;

    private TextView infoTV;
    private Button addHuecoBtn;

    private double lat,lon;
    private String dir;

    private LatLng myPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        String username = getIntent().getExtras().getString("username");
        us = new User(UUID.randomUUID().toString(), username);

        Gson gson = new Gson();
        String json = gson.toJson(us);
        HTTPSWebUtilDomi https = new HTTPSWebUtilDomi();
        new Thread(
                () ->{
                    String response = https.PUTrequest(Constants.BASEURL+"users/"+us.getName()+".json", json);
                }
        ).start();

        usuarios = new ArrayList<Marker>();
        huecos = new ArrayList<Marker>();

        infoTV = findViewById(R.id.infoTV);
        addHuecoBtn = findViewById(R.id.addHuecoBtn);

        addHuecoBtn.setOnClickListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        manager = (LocationManager) getSystemService(LOCATION_SERVICE);

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
        setInicialPosicion();


        userThread = new UserThread(this);
        huecoThread = new HuecoThread(this);
        userThread.start();
        huecoThread.start();
    }

    @SuppressLint("MissingPermission")
    public void setInicialPosicion(){
        Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location!=null){
            updateMylocacion(location);
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        updateMylocacion(location);
    }

    public void updateMylocacion(Location location){
        myPos = new LatLng(location.getLatitude(),location.getLongitude());
        if(me==null){
            me = mMap.addMarker(new MarkerOptions().position(myPos).title("yo"));
            us.setLongitud(myPos.longitude);
            us.setLatitud(myPos.latitude);

            infoTV.setText("Hueco a "+huecoMasCercano(location)+" metros");

        }else{
            me.setPosition(myPos);
            us.setLongitud(myPos.longitude);
            us.setLatitud(myPos.latitude);
            infoTV.setText("Hueco a "+huecoMasCercano(location)+" metros");
        }

        Gson gson = new Gson();
        String json = gson.toJson(us);
        HTTPSWebUtilDomi https = new HTTPSWebUtilDomi();
        new Thread(
                () -> {
                    String response = https.PUTrequest(Constants.BASEURL + "users/" + us.getName() + ".json", json);
                }
        ).start();



        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPos,18));
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addHuecoBtn:

                lat = myPos.latitude;
                lon = myPos.longitude;

                MyDialogFragment myDialogFragment = new MyDialogFragment();
                myDialogFragment.setListener(this);
                myDialogFragment.setCoordenadaTV(lat+" , "+lon);
                obtenerDirección();
                myDialogFragment.setDireccionTV(dir);
                myDialogFragment.show(getSupportFragmentManager(), "MyFragment");
                break;
        }
    }

    public void obtenerDirección (){
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> direccion = geocoder.getFromLocation(lat, lon,1);
            dir = "";
            Address addr = direccion.get(0);
            for (int i = 0; i<=addr.getMaxAddressLineIndex();i++){
                dir += addr.getAddressLine(i);
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public void buttonOK(){
        Hueco newHueco = new Hueco(UUID.randomUUID().toString(), us.getName(), lat, lon, dir, false);

        Gson gson = new Gson();
        String json = gson.toJson(newHueco);
        HTTPSWebUtilDomi http = new HTTPSWebUtilDomi();
        new Thread(
                ()-> {
                    String response = http.POSTrequest(Constants.BASEURL+"huecos/"+us.getName()+".json", json);
                }
        ).start();

        mMap.addMarker(new MarkerOptions().position(new LatLng(newHueco.getLatitud(), newHueco.getLongitud()))).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.hueco));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void actualizarUsuarios(HashMap<String, User> users){
        usuarios = new ArrayList<>();
        if(users!=null){
            users.forEach(
                    (key,value)->{
                        runOnUiThread(
                                ()->{
                                    if(!value.getId().equals(us.getId())){
                                        Marker m = mMap.addMarker(new MarkerOptions().position(new LatLng(value.getLatitud(),value.getLongitud())));
                                        m.setTitle(value.getName());
                                        m.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.usuario));
                                        usuarios.add(m);
                                    }
                                }
                        );
                    }
            );
        }
    }

    public double huecoMasCercano(Location loc){
        double distanciaI = 0;
        for (int i =0; i<huecos.size();i++){
            if(huecos.get(i)!=null){
                LatLng poscHueco = huecos.get(i).getPosition();

                Location locationHueco = new Location("locHueco");
                locationHueco.setLatitude(poscHueco.latitude);
                locationHueco.setLongitude(poscHueco.longitude);

                double distanciaAnalizar = loc.distanceTo(locationHueco);
                if(i==0){
                    distanciaI = loc.distanceTo(locationHueco);
                }
                if(distanciaAnalizar<distanciaI){
                    distanciaI = distanciaAnalizar;
                }
            }

        }
        distanciaI = distanciaI*1000;
        return distanciaI;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void actualizarHuecos(HashMap<String, Hueco> HashHuecos){
        huecos = new ArrayList<>();
        if(HashHuecos!=null){
            HashHuecos.forEach(
                    (key,value)->{
                        this.runOnUiThread(
                                ()->{
                                    Log.e("valores", value.getLatitud()+", "+value.getLongitud());
                                    Marker m = mMap.addMarker(new MarkerOptions().position(new LatLng(value.getLatitud(),value.getLongitud())));
                                    m.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.hueco));
                                    huecos.add(m);
                                }
                        );
                    }
            );
        }


    }
}