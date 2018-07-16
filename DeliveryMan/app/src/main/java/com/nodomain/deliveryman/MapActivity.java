package com.nodomain.deliveryman;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.PolyUtil;
import com.nodomain.deliveryman.ClassesOp.BDAyuda;
import com.nodomain.deliveryman.ClassesOp.DestinyPoint;
import com.nodomain.deliveryman.ClassesOp.GeoTask;
import com.nodomain.deliveryman.ClassesOp.Prim;
import com.nodomain.deliveryman.ClassesOp.RutaArco;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class MapActivity extends AppCompatActivity implements GeoTask.Geo {

    public boolean permissiongranted = false;
    public GoogleMap mMap;
    public FusedLocationProviderClient mFused;
    static final float DEFAULT_ZOOM = 15f;
    public int count;
    public List<LatLng> Points;
    public MarkerOptions start;
    public Marker start1;
    private final String url_jsonarray="http://androiddelivery.000webhostapp.com/send.php";
    TextView Tdist;
    public  List<DestinyPoint> localizaciones=new ArrayList<>();
    public RutaArco array2[] = new RutaArco[100];
    int tamanoid=0;
    TextView Ttimes;
    public RutaArco rutas[];
    LatLng _origin;
    TextView ultimotextview;
    LatLng _end;
    double _distance;
    String _time;
    int contador = 0;
    List<RutaArco> aux = new ArrayList<>();
    Prim prim;
    public LinkedList<String> arrayidpedido=new LinkedList<>();
    List<Double> distancaistemp = new ArrayList<>();
    List<Double> tempotemp = new ArrayList<>();
    MarkerOptions centralpermanent;
    Button BEntrega, Bcalculo, Bruta, BmarcadoresDB;
    ImageButton buttoneraser;
    String temporaloptimaltime = "";
    Double temporaloptimaldistance = 0.0;
    Prim p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getLocationPermission();
        count = 0;
        Points = new ArrayList<>();
        Bruta = findViewById(R.id.Bruta);
        Bcalculo = findViewById(R.id.btncalcular);
        BEntrega=findViewById(R.id.btnentregalista);
        BmarcadoresDB = findViewById(R.id.BmostrarExistencias);
    }

    public void drawRoute(LatLng A, LatLng B) {
        StringBuilder sb;
        Object[] dataTransfer = new Object[4];
        sb = new StringBuilder();
        sb.append("https://maps.googleapis.com/maps/api/directions/json?");
        sb.append("origin=" + A.latitude + "," + A.longitude);
        sb.append("&destination=").append(B.latitude).append(",").append(B.longitude);
        sb.append("&key=" + "AIzaSyB5dTdiyjZijSPPD6za3O7qugaidYNZPTo");
        GetDirectionsData getDirectionsData = new GetDirectionsData(getApplicationContext());
        dataTransfer[0] = mMap;
        dataTransfer[1] = sb.toString();
        dataTransfer[2] = new LatLng(A.latitude, A.longitude);
        dataTransfer[3] = new LatLng(B.latitude, B.longitude);
        getDirectionsData.execute(dataTransfer);
    }


    private void mooveCamera(LatLng coordinates, float zoom) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, zoom));
    }

    private void getDeviceLoccation() {
        mFused = LocationServices.getFusedLocationProviderClient(this);
        try {

            if (permissiongranted) {
                final Task location = mFused.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Location current = (Location) task.getResult();
                            mooveCamera(new LatLng(current.getLatitude(), current.getLongitude()), DEFAULT_ZOOM);
                            MarkerOptions auxcentral = new MarkerOptions().position(new LatLng(current.getLatitude(), current.getLongitude())).
                                    title(String.valueOf(count)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("Central");
                            Points.add(new LatLng(current.getLatitude(), current.getLongitude()));
                            centralpermanent=auxcentral;
                            mMap.addMarker(auxcentral);

                        } else {
                            Toast.makeText(MapActivity.this, "Null location", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();

        }
    }

    private void  initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                Toast.makeText(MapActivity.this, "Map ready", Toast.LENGTH_LONG).show();
                if (permissiongranted) {
                    getDeviceLoccation();
                    if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mMap.setMyLocationEnabled(true);
                    mMap.getUiSettings().setMyLocationButtonEnabled(true);
                    //para crear marcadores en largo presionamiento
                    // updateDBdotsonMAP();

                    //para ver detalles del marcador
                    Bruta.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onClick(View view) {
                            contador = 0;
                            RutaArco u;
                            rutas = new RutaArco[100];
                            if(Points.size()<=1)
                            {
                                Toast.makeText(getApplicationContext(),"No hay ruta por mostrar",Toast.LENGTH_LONG).show();
                                return;
                            }
                            //actualiza la distancaisen orden(creo)
                            for (int i = 0; i < aux.size(); i++) {
                                aux.get(i).set_distance(distancaistemp.get(i));
                                aux.get(i).set_time(tempotemp.get(i).toString());
                            }
                            ///

                            while (contador < aux.size()) {
                                u = new RutaArco(aux.get(contador).get_origin(), aux.get(contador).get_end(), aux.get(contador).get_distance(), aux.get(contador).get_time());
                                //aqui es el error
                                rutas[contador] = u;
                                contador++;
                            }

                            int vertices = Points.size();
                            int tamano = contador;
                            Prim prim = new Prim(rutas, vertices, contador);
                            array2 = new RutaArco[100];
                            array2 = prim.indicesaArco();

                            double finaldistanceaux = 0;
                            double finaltimeaux = 0;




                            try {
                                for (int i = 0; i < array2.length; i++) {

                                    drawRoute(array2[i].get_origin(), array2[i].get_end());
                                    finaldistanceaux += array2[i].get_distance();
                                    finaltimeaux += Double.parseDouble(array2[i].get_time());
                                }
                            } catch (Exception e) {
                                // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                            ultimotextview = findViewById(R.id.Ttotalview);

                            ultimotextview.setText("Total: " + Math.floor(finaldistanceaux) + " Km," + Math.floor(finaltimeaux) + " Mins");
                        }
                    });
                    Bcalculo.setOnClickListener(new View.OnClickListener() {
                                                    @RequiresApi(api = Build.VERSION_CODES.N)
                                                    @Override
                                                    public void onClick(View view) {
                                                        aux = new ArrayList<>();
                                                        //Toast.makeText(this,"ForceBrute=not done yet",Toast.LENGTH_LONG).show();
                                                        for (int i = 0; i < Points.size(); i++) {
                                                            for (int j = 0; j < Points.size(); j++) {

                                                                if (i != j) {
                                                                    String starter = String.valueOf(Points.get(i).latitude) + "," + String.valueOf(Points.get(i).longitude);
                                                                    String ender = String.valueOf(Points.get(j).latitude) + "," + String.valueOf(Points.get(j).longitude);
                                                                    String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + starter + "&destinations=" + ender + "&mode=driving&language=fr-FR&avoid=tolls&key=AIzaSyBfZc21Ujrrn7hXOXTjIgiS5pOcWPihK9s";
                                                                    new GeoTask(MapActivity.this).execute(url);
                                                                    //AQUI SIEMPRE ESTA LLENANDO CON CEROS LA DISTANCIA
                                                                    aux.add(new RutaArco(Points.get(i), Points.get(j), temporaloptimaldistance, temporaloptimaltime));
                                                                    temporaloptimaltime = "";
                                                                    temporaloptimaldistance = 0.0;
                                                                }

                                                            }
                                                        }
                                                        //deberia reestructurar los puntos

                                                    }
                                                }
                    );
                    BEntrega.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final AlertDialog.Builder builderes = new AlertDialog.Builder(MapActivity.this);
                            builderes.setMessage("Si presiona ok, los pedidos viejos se borraran y se marcaran como entregados:")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {


                                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            try {
                                                BDAyuda bd = new BDAyuda(getApplicationContext());


                                                try {
                                                    bd.Borrar_Tabla();
                                                } catch (Exception e) {
                                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                                }






                                                final RequestQueue queue=Volley.newRequestQueue(getApplicationContext());

                                                Map<String, String> postParam= new HashMap<String, String>();
                                                if(arrayidpedido.size()==1)
                                                {
                                                    postParam.put("idpedido1",arrayidpedido.get(0));
                                                    postParam.put("idpedido2","0");
                                                    postParam.put("idpedido3","0");
                                                    postParam.put("idpedido4","0");
                                                    postParam.put("idpedido5","0");

                                                }
                                                if(arrayidpedido.size()==2)
                                                {
                                                    postParam.put("idpedido1",arrayidpedido.get(0));
                                                    postParam.put("idpedido2",arrayidpedido.get(1));
                                                    postParam.put("idpedido3","0");
                                                    postParam.put("idpedido4","0");
                                                    postParam.put("idpedido5","0");

                                                }

                                                if(arrayidpedido.size()==3)
                                                {
                                                    postParam.put("idpedido1",arrayidpedido.get(0));
                                                    postParam.put("idpedido2",arrayidpedido.get(1));
                                                    postParam.put("idpedido3",arrayidpedido.get(2));
                                                    postParam.put("idpedido4","0");
                                                    postParam.put("idpedido5","0");

                                                }

                                                if(arrayidpedido.size()==4)
                                                {
                                                    postParam.put("idpedido1",arrayidpedido.get(0));
                                                    postParam.put("idpedido2",arrayidpedido.get(1));
                                                    postParam.put("idpedido3",arrayidpedido.get(2));
                                                    postParam.put("idpedido4",arrayidpedido.get(3));
                                                    postParam.put("idpedido5","0");

                                                }

                                                if(arrayidpedido.size()==5)
                                                {
                                                    postParam.put("idpedido1",arrayidpedido.get(0));
                                                    postParam.put("idpedido2",arrayidpedido.get(1));
                                                    postParam.put("idpedido3",arrayidpedido.get(2));
                                                    postParam.put("idpedido4",arrayidpedido.get(3));
                                                    postParam.put("idpedido5",arrayidpedido.get(4));

                                                }





                                                arrayidpedido = new LinkedList<>();


                                                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,"http://androiddelivery.000webhostapp.com/entregados.php"
                                                        , new JSONObject(postParam),
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


                                                Toast.makeText(MapActivity.this, "Limpiado", Toast.LENGTH_LONG).show();


                                            }
                                            catch(Exception e)
                                            {
                                                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    } ).setNegativeButton("Cancelar", null);
                            AlertDialog alert = builderes.create();
                            alert.show();
                        }

                    });
                    BmarcadoresDB.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Esta parte tiene que recibir un objeto json. el listar
                            //Localizaciones no tiene que estar
                            //BDAyuda bd = new BDAyuda(getApplicationContext());
                            localizaciones=new ArrayList<>();
                            BDAyuda aux=new BDAyuda(getApplicationContext());
                            SQLiteDatabase db=aux.getReadableDatabase();
                            if(checkEmpty(db,"PEDIDOS")) {


                                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "http://androiddelivery.000webhostapp.com/send.php", null, new Response.Listener<JSONArray>() {
                                    @Override
                                    public void onResponse(JSONArray response) {
                                        BDAyuda developerBD = new BDAyuda(getApplicationContext());


                                        try {
                                            for (int i = 0; i < response.length(); i++) {
                                                JSONObject jsonObject = response.getJSONObject(i);

                                                DestinyPoint dp = new DestinyPoint(jsonObject.get("nombrecliente").toString(), Double.parseDouble(jsonObject.get("lat").toString()), Double.parseDouble(jsonObject.get("longitud").toString()), jsonObject.get("productos").toString());
                                                localizaciones.add(dp);

                                                arrayidpedido.add(jsonObject.get("idpedido").toString());
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
                                                Points.add(pointaux);
                                                markaux = new MarkerOptions().position(pointaux).title(localizaciones.get(i).nombredestino + " Pedido: " + localizaciones.get(i).pedido);
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
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Tienes pedidos pendientes no puedes solicitar mas",Toast.LENGTH_SHORT).show();
                                List<DestinyPoint> aux2=new ArrayList<>();
                                BDAyuda aux3=new BDAyuda(getApplicationContext());
                                aux2=aux3.listarLocalizaciones();
                                MarkerOptions markaux;
                                //El objetivo es que con una funcion se llene los puntos con la lista que me va a llegar
                                LatLng pointaux;

                                for (int i = 0; i < aux2.size(); i++) {
                                    pointaux = new LatLng(aux2.get(i).latitud, aux2.get(i).longitud);
                                    Points.add(pointaux);
                                    markaux = new MarkerOptions().position(pointaux).title(aux2.get(i).nombredestino + " Pedido: " + aux2.get(i).pedido);
                                    mMap.addMarker(markaux);
                                }
                            }
                        }
                    });

                    int m;
                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            if (marker.isInfoWindowShown()) {
                                marker.hideInfoWindow();
                                return true;
                            }
                            marker.showInfoWindow();
                            return true;
                        }
                    });
                    buttoneraser = findViewById(R.id.BlimpiarMapa);
                    buttoneraser.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mMap.clear();
                            ultimotextview = findViewById(R.id.Ttotalview);
                            ultimotextview.setText("");
                            Points=new ArrayList<>();
                            Points.add(centralpermanent.getPosition());
                            //la central no se pierde
                            mMap.addMarker(centralpermanent);
                        }
                    });
                }
            }
        });
    }
    private String getnombredemarcador(LatLng x)
    {
        String aux="";
        LatLng perm;
        for (int i=0;i<localizaciones.size();i++)
        {perm= new LatLng(localizaciones.get(i).latitud,localizaciones.get(i).longitud);
            if(perm.latitude==x.latitude&&perm.longitude==x.longitude)
            {
                aux+=localizaciones.get(i).nombredestino+" Pedido"+localizaciones.get(i).pedido;
            }
        }
        return aux;
    }
    private void generateRoute(LatLng A, LatLng B) {
        String url = getRequestedUrl(A, B);
    }
    private String getRequestedUrl(LatLng a, LatLng b) {
        String str_org = "origin=" + a.latitude + "," + a.longitude;
        String str_dest = "destination=" + b.latitude + "," + b.longitude;
        String sensor = "sensor=false";
        String mode = "mode=driving";
        String paramm = str_org + "&" + str_dest + "&" + sensor + "&" + mode;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + paramm;
        return url;
    }

    private String requestedDirection(String uri) throws IOException {
        String response = "";
        InputStream input = null;
        HttpURLConnection conecction = null;
        try {
            URL url = new URL(uri);
            conecction = (HttpURLConnection) url.openConnection();
            conecction.connect();
            input = conecction.getInputStream();
            InputStreamReader reader = new InputStreamReader(input);
            BufferedReader Breader = new BufferedReader(reader);
            StringBuffer strbuffer = new StringBuffer();
            String line = "";
            while ((line = Breader.readLine()) != null) {
                strbuffer.append(line);
            }
            response = strbuffer.toString();
            Breader.close();
            input.close();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (input != null) {
                input.close();
            }
            conecction.disconnect();
        }
        return response;
    }

    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                permissiongranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, 1234);
            }

        } else {
            ActivityCompat.requestPermissions(this, permissions, 1234);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissiongranted = false;
        switch (requestCode) {
            case 1234: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            permissiongranted = false;
                            return;
                        }
                    }
                    permissiongranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }
    public boolean checkEmpty(SQLiteDatabase db, String tabla){
        return DatabaseUtils.queryNumEntries(db, tabla) == 0;
    }

    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    public class GetDirectionsData extends AsyncTask<Object, String, String> {
        GoogleMap mMap;
        String url;
        LatLng startLatLng, endLatLng;

        HttpURLConnection httpURLConnection = null;
        String data = "";
        InputStream inputStream = null;
        Context c;

        GetDirectionsData(Context c) {
            this.c = c;
        }

        @Override
        protected String doInBackground(Object... params) {
            mMap = (GoogleMap) params[0];
            url = (String) params[1];
            startLatLng = (LatLng) params[2];
            endLatLng = (LatLng) params[3];

            try {
                URL myurl = new URL(url);
                httpURLConnection = (HttpURLConnection) myurl.openConnection();
                httpURLConnection.connect();

                inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer sb = new StringBuffer();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                data = sb.toString();
                bufferedReader.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return data;//sdfdsf

        }

        @Override
        protected void onPostExecute(String s) {

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("routes").getJSONObject(0)
                        .getJSONArray("legs").getJSONObject(0).getJSONArray("steps");

                int count = jsonArray.length();
                String[] polyline_array = new String[count];

                JSONObject jsonobject2;

                for (int i = 0; i < count; i++) {
                    jsonobject2 = jsonArray.getJSONObject(i);

                    String polygone = jsonobject2.getJSONObject("polyline").getString("points");

                    polyline_array[i] = polygone;
                }

                int count2 = polyline_array.length;

                for (int i = 0; i < count2; i++) {

                    PolylineOptions options2 = new PolylineOptions();
                    //colores

                    options2.color(Color.BLACK);
                    options2.width(5);
                    options2.addAll(PolyUtil.decode(polyline_array[i]));

                    mMap.addPolyline(options2);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void setDouble(String result) {
        String res[] = result.split(",");
        Double min = Double.parseDouble(res[0]) / 60;
        Double dist = Double.parseDouble(res[1]) / 1000;
        distancaistemp.add(dist);
        tempotemp.add(min);

    }
}
