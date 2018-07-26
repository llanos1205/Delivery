package com.nodomain.clientside;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.SphericalUtil;
import com.nodomain.clientside.ClassesOp.BDAyuda;
import com.nodomain.clientside.ClassesOp.DestinyPoint;
import com.nodomain.clientside.ClassesOp.SucursalPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Sucursal extends AppCompatActivity {

    public MarkerOptions marcadorcentral;
    public Marker currentmarker;
    public GoogleMap mMap;
    boolean permissiongrantedDB=false;
    public List<DestinyPoint> localizaciones=new ArrayList<>();
    public LinkedList<String> arrayidpedido=new LinkedList<>();
    int tamanoid=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sucursal);
        getLocationPermission();
    }

    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                permissiongrantedDB = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, 1234);
            }

        } else {
            ActivityCompat.requestPermissions(this, permissions, 1234);
        }
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_DB);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                googleMap.setTrafficEnabled(true);
                googleMap.setIndoorEnabled(true);
                googleMap.setBuildingsEnabled(true);
                mMap.getUiSettings().setZoomControlsEnabled(true);

                Toast.makeText(Sucursal.this, "Map ready", Toast.LENGTH_LONG).show();


                if (permissiongrantedDB) {
                    getDeviceLoccation();
                    if (ActivityCompat.checkSelfPermission(Sucursal.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Sucursal.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    // mMap.setMyLocationEnabled(true);
                    mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "http://androiddelivery.000webhostapp.com/sendSucursal.php", null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            BDAyuda developerBD = new BDAyuda(getApplicationContext());


                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jsonObject = response.getJSONObject(i);

                                    DestinyPoint dp = new DestinyPoint("", Double.parseDouble(jsonObject.get("latitud").toString()), Double.parseDouble(jsonObject.get("longitud").toString()),"");
                                    localizaciones.add(dp);

                                    arrayidpedido.add(jsonObject.get("idsucursal").toString());
                                    tamanoid++;
                                }
                                try {
                                    for (int i = 0; i < localizaciones.size(); i++) {

                                        BDAyuda developerBD2 = new BDAyuda(getApplicationContext());
                                        developerBD2.agregarLocalizacion(localizaciones.get(i).nombredestino, String.valueOf(localizaciones.get(i).latitud), String.valueOf(localizaciones.get(i).longitud), localizaciones.get(i).pedido);

                                    }
                                } catch (Exception e) {


                                }
                                if(localizaciones.size()==0)
                                {
                                    Toast.makeText(getApplicationContext(),"No hay pedidos en cola",Toast.LENGTH_SHORT).show();
                                }


                                MarkerOptions markaux;
                                //El objetivo es que con una funcion se llene los puntos con la lista que me va a llegar
                                LatLng pointaux;
                                for (int i = 0; i < localizaciones.size(); i++) {
                                    pointaux = new LatLng(localizaciones.get(i).latitud, localizaciones.get(i).longitud);

                                    float[] r = new float[1];
                                    Location.distanceBetween(
                                            pointaux.latitude,
                                            pointaux.longitude,
                                            marcadorcentral.getPosition().latitude,
                                            marcadorcentral.getPosition().longitude,
                                            r);
                                    markaux = new MarkerOptions().position(pointaux)
                                            .title(localizaciones.get(i).nombredestino + " Pedido: " + localizaciones.get(i).pedido)
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                            .title(String.valueOf(Math.round(((r[0])*100.0)/100.0)/1000.0)+" Km");
                                    mMap.addMarker(markaux);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "No hay pedidos pendientes", Toast.LENGTH_SHORT).show();

                        }
                    });
                    requestQueue.add(jsonArrayRequest);
                }
            }
        });
    }
    private void getDeviceLoccation() {
        FusedLocationProviderClient mFused = LocationServices.getFusedLocationProviderClient(this);
        try {

            if (permissiongrantedDB) {
                final Task location = mFused.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Location current = (Location) task.getResult();
                            mooveCamera(new LatLng(current.getLatitude(), current.getLongitude()), 12f);
                            marcadorcentral= new MarkerOptions()
                                    .position(new LatLng(current.getLatitude(), current.getLongitude()))
                                    .title(String.valueOf("YOU")).draggable(true);
                            currentmarker=mMap.addMarker(marcadorcentral);



                        } else {
                            Toast.makeText(Sucursal.this, "Null location", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void mooveCamera(LatLng coordinates, float zoom) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, zoom));
    }
}
