package com.nodomain.clientside;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
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
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.nodomain.clientside.ClassesOp.SucursalPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DataBaseActivity extends AppCompatActivity {

    boolean permissiongrantedDB=false;
    public GoogleMap mMap;
    public MarkerOptions marcadorcentral;
    private Marker currentmarker;
    final String url="http://androiddelivery.000webhostapp.com/receive.php";
    private TextView Nom;
    private Button btnagregar;
    private EditText ednit;
    private ImageButton Bayudaprecios;
    private BDAyuda developerBD;
    private Spinner slugares,smarcag,scantidadpecho,scantidadpierna,scantidadgaseosa;
    private String Pedidos;
    private PopupWindow mPopupWindow;
    int pechugacuarto=25;
    int piernacuarto=20;
    int soda500=8;
    int soda2lt=15;
    int idfinal=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base);
        getLocationPermission();

        Bayudaprecios=findViewById(R.id.Bayuda);
        Nom=findViewById(R.id.Tname);
        smarcag=findViewById(R.id.smarcagaseosa);
        scantidadpecho=findViewById(R.id.scantpechuga);
        scantidadpierna=findViewById(R.id.scantpierna);
        scantidadgaseosa=findViewById(R.id.scantidadgaseosa);
        ednit=findViewById(R.id.ednit);


        slugares=findViewById(R.id.spinner);
        UpdateSpinners(slugares);




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


                Toast.makeText(DataBaseActivity.this, "Map ready", Toast.LENGTH_LONG).show();


                if (permissiongrantedDB) {
                    getDeviceLoccation();
                    if (ActivityCompat.checkSelfPermission(DataBaseActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DataBaseActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    // mMap.setMyLocationEnabled(true);
                    mMap.getUiSettings().setMyLocationButtonEnabled(true);
                    mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                        @Override
                        public void onMarkerDragStart(Marker marker) {

                            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                        }

                        @Override
                        public void onMarkerDrag(Marker marker) {




                        }

                        @Override
                        public void onMarkerDragEnd(Marker marker) {

                            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        }
                    });



                    mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                        @Override
                        public void onMapLongClick(LatLng latLng) {

                            currentmarker.setPosition(latLng);



                        }
                    });
                    btnagregar=findViewById(R.id.Bsave_DB);
                    btnagregar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            switch(view.getId())
                            {
                                case R.id.Bsave_DB:
                                {





                                        Pedidos=obtenerPedidos(scantidadpecho.getSelectedItem().toString(),scantidadpierna.getSelectedItem().toString(),smarcag.getSelectedItem().toString(),scantidadgaseosa.getSelectedItem().toString());
                                        final int  Precio=obtenerPrecio(scantidadpecho.getSelectedItem().toString(),scantidadpierna.getSelectedItem().toString(),smarcag.getSelectedItem().toString(),scantidadgaseosa.getSelectedItem().toString());
                                        final LinkedList<SucursalPoint> list=new LinkedList<>();
                                        final int[] tamano = {0};
                                    final int[] idtemp = {0};
                                       //Todo esto va a servir para obtener todas las sucursales con su latitud y longitud para despues comparar cual esta mas cerca
                                      final RequestQueue queuetemp=Volley.newRequestQueue(getApplicationContext());
                                      JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, "http://androiddelivery.000webhostapp.com/sendSucursal.php", null, new Response.Listener<JSONArray>() {
                                          double finaldist=0.0;
                                          int op=0;

                                         @Override
                                          public void onResponse(JSONArray response) {
                                              for (int i = 0; i < response.length(); i++) {
                                                  JSONObject jsonObject = null;
                                                  try {
                                                      jsonObject = response.getJSONObject(i);
                                                  } catch (JSONException e) {
                                                      e.printStackTrace();
                                                  }
                                                  SucursalPoint sp= null;
                                                  try {
                                                      sp = new SucursalPoint(Double.parseDouble(jsonObject.get("latitud").toString()),Double.parseDouble(jsonObject.get("longitud").toString()),jsonObject.getInt("idsucursal"));
                                                  } catch (JSONException e) {

                                                  }
                                                  list.add(sp);

                                                  LatLng latLng=new LatLng(list.getFirst().lat,list.getFirst().lng);
                                                  LatLng latLng1=new LatLng(currentmarker.getPosition().latitude,currentmarker.getPosition().longitude);
                                                  double temp=SphericalUtil.computeDistanceBetween(latLng,latLng1);
                                                  if(op==0)
                                                  {
                                                      idfinal=list.getFirst().id;
                                                      finaldist=temp;
                                                      idtemp[0] =idfinal;
                                                      op=1;
                                                  }
                                                  else
                                                  {
                                                      if(temp<finaldist)
                                                      {
                                                          idfinal=list.getFirst().id;
                                                          finaldist=temp;
                                                          idtemp[0] =idfinal;
                                                      }
                                                  }
                                                  list.removeFirst();

                                              }
                                             final RequestQueue queue=Volley.newRequestQueue(getApplicationContext());
                                             SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                             Date date = new Date();
                                             DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
                                             String fecha = dateFormat.format(date);






                                             Calendar calendario = Calendar.getInstance();
                                             int hora =calendario.get(Calendar.HOUR_OF_DAY);
                                             int minutos = calendario.get(Calendar.MINUTE);
                                             int segundos = calendario.get(Calendar.SECOND);

                                             Map<String, String> postParam= new HashMap<String, String>();
                                             postParam.put("fecha", fecha);
                                             postParam.put("hora", String.valueOf(hora)+":"+String.valueOf(minutos)+":"+String.valueOf(segundos));
                                             postParam.put("lat", String.valueOf(currentmarker.getPosition().latitude));
                                             postParam.put("longitud", String.valueOf(currentmarker.getPosition().longitude));
                                             postParam.put("nit", ednit.getText().toString());
                                             postParam.put("nombrecliente", Nom.getText().toString());
                                             postParam.put("productos", Pedidos);
                                             postParam.put("montototal", String.valueOf(Precio)+" Bs");
                                             //Convertir el id final en un int en tu php close
                                             postParam.put("idsucursal",String.valueOf(idfinal));
                                             postParam.put("basesdatos","id6044948_pedido");


                                             JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                                                     url, new JSONObject(postParam),
                                                     new Response.Listener<JSONObject>() {

                                                         @Override
                                                         public void onResponse(JSONObject response) {

                                                             Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_SHORT).show();
                                                             queue.stop();
                                                         }
                                                     }, new Response.ErrorListener() {

                                                 @Override
                                                 public void onErrorResponse(VolleyError error) {

                                                     queue.stop();

                                                 }
                                             }) {

                                                 /**
                                                  * Passing some request headers
                                                  * */
                                                 @Override
                                                 public Map<String, String> getHeaders() throws AuthFailureError {
                                                     HashMap<String, String> headers = new HashMap<String, String>();
                                                     headers.put("Content-Type", "application/json; charset=utf-8");
                                                     return headers;
                                                 }



                                             };


                                             // Adding request to request queue
                                             queue.add(jsonObjReq);



                                          }

                                      }, new Response.ErrorListener() {
                                          @Override
                                          public void onErrorResponse(VolleyError error) {
                                              Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                                          }
                                      });

                                      queuetemp.add(jsonArrayRequest);
                                      //Esto calcula el mayor



                                              //developerBD.agregarLocalizacion(Nom.getText().toString(), String.valueOf(currentmarker.getPosition().latitude), String.valueOf(currentmarker.getPosition().longitude),Pedidos);

                                        Toast.makeText(DataBaseActivity.this, "Insertado", Toast.LENGTH_LONG).show();
                                        UpdateSpinners(slugares);
                                        //developerBD.close();

                                    break;

                                }


                            }



                        }
                    });

                    Bayudaprecios.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            LayoutInflater inflater = (LayoutInflater) DataBaseActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);

                            // Inflate the custom layout/view
                            View customView = inflater.inflate(R.layout.customprecios,null);
                            mPopupWindow = new PopupWindow(
                                    customView,//findViewById(R.id.rl).getWidth()-20,findViewById(R.id.rl).getHeight()


                                    LayoutParams.WRAP_CONTENT,
                                    LayoutParams.WRAP_CONTENT
                            );

                            // Set an elevation value for popup window
                            // Call requires API level 21
                            if(Build.VERSION.SDK_INT>=21){
                                mPopupWindow.setElevation(5.0f);
                            }

                            // Get a reference for the custom view close button
                            ImageButton closeButton = (ImageButton) customView.findViewById(R.id.ib_close);

                            // Set a click listener for the popup window close button
                            closeButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Dismiss the popup window
                                    mPopupWindow.dismiss();
                                }
                            });
                            mPopupWindow.showAtLocation(findViewById(R.id.rl), Gravity.CENTER,0,0);

                        }
                    });




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
                            mooveCamera(new LatLng(current.getLatitude(), current.getLongitude()), 15f);
                            marcadorcentral= new MarkerOptions()
                                    .position(new LatLng(current.getLatitude(), current.getLongitude()))
                                    .title(String.valueOf("Position")).draggable(true);
                            currentmarker=mMap.addMarker(marcadorcentral);



                        } else {
                            Toast.makeText(DataBaseActivity.this, "Null location", Toast.LENGTH_LONG).show();
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
    private void UpdateSpinners(Spinner slugares)
    {
        BDAyuda bd=new BDAyuda(getApplicationContext());
        List<DestinyPoint> localizaciones=new ArrayList<>();
        localizaciones=bd.listarLocalizaciones();

        String counter[]=new String[localizaciones.size()];
        for(int i=0;i<localizaciones.size();i++)
        {
            DestinyPoint aux=localizaciones.get(i);



            counter[i]= String.valueOf(aux.nombredestino);
        }
        ArrayAdapter<CharSequence> adapterx = new ArrayAdapter<CharSequence>(DataBaseActivity.this,android.R.layout.simple_dropdown_item_1line,counter);
        slugares.setAdapter(adapterx);


    }
    private String obtenerPedidos(String cantpecho,String cantpierna,String marcasoda, String cantSoda)
    {
        if(cantpecho.equals("0") && cantpierna.equals("0") && marcasoda.equals("Ninguno"))
        {
            return "No tiene pedido";
        }
        if(cantpecho.equals("0") && cantpierna.equals("0") && !marcasoda.equals("Ninguno"))
        {
            if(cantSoda.equals("0"))
            {
                return "Sin Pedido";
            }
            else
            {
                return marcasoda+","+"Cant:"+cantSoda;
            }
        }
        if(cantpecho.equals("0") && !cantpierna.equals("0") && marcasoda.equals("Ninguno"))
        {
            return "Piernas:"+cantpierna;
        }

        if(cantpecho.equals("0") && !cantpierna.equals("0") && !marcasoda.equals("Ninguno"))
        {
            if(cantSoda.equals("0"))
            {
                return "Piernas:"+cantpierna;

            }
            else
            {
                return "Piernas:"+cantpierna+","+marcasoda+",Cant:"+cantSoda;

            }

        }



        if(!cantpecho.equals("0") && cantpierna.equals("0") && marcasoda.equals("Ninguno"))
        {
            return "Pechugas:"+cantpecho;
        }

        if(!cantpecho.equals("0") && cantpierna.equals("0") && !marcasoda.equals("Ninguno"))
        {
            if(cantSoda.equals("0"))
            {
                return "Pechugas:"+cantpecho;

            }
            else
            {
                return "Pechugas:"+cantpecho+","+marcasoda+",Cant:"+cantSoda;

            }

        }
        if(!cantpecho.equals("0") && !cantpierna.equals("0") && marcasoda.equals("Ninguno"))
        {
            return "Pechugas:"+cantpecho+","+"Piernas:"+cantpierna;
        }
        if(!cantpecho.equals("0") && !cantpierna.equals("0") && !marcasoda.equals("Ninguno"))
        {
            if(cantSoda.equals("0"))
            {
                return "Pechugas:" + cantpecho + "," + "Piernas" + cantpierna;

            }
            else {
                return "Pechugas:" + cantpecho + "," + "Piernas" + cantpierna + "," + marcasoda + ",Cant:" + cantSoda;
            }
        }







        return "No hay pedido";
    }
    private int obtenerPrecio(String cantpecho,String cantpierna,String marcasoda, String cantSoda)
    {
        int pecho= Integer.parseInt(cantpecho);
        int cantPiena= Integer.parseInt(cantpierna);
        int cantsoda=Integer.parseInt(cantSoda);

        if(marcasoda.contains("500ml"))
        {
            return pecho*pechugacuarto+cantPiena*piernacuarto+cantsoda*soda500;


        }
        if(marcasoda.contains("2lts"))
        {
            return pecho*pechugacuarto+cantPiena*piernacuarto+cantsoda*soda2lt;

        }
        return pecho*pechugacuarto+cantPiena*piernacuarto;







    }



}
