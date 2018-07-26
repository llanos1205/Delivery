package com.nodomain.deliveryman;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button x,y;
    public boolean registered=false;
    private PopupWindow mPopupWindow;
    EditText user,password,idsucursal,idrepartidor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user=findViewById(R.id.edtusuario);
        password=findViewById(R.id.edtcontraseña);
        idsucursal=findViewById(R.id.edtidsucursal);
        idrepartidor=findViewById(R.id.edtidrepartidor);
        x=findViewById(R.id.BmapaAcceso);
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


        startActivity(new Intent(MainActivity.this, MapActivity.class));
            }


        });
        y=findViewById(R.id.Bpassword);
        y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Aqui o que vamos a hacer es obtener todos los datos que tengamos en los edit text para despues enviar por c¿servicio androi dvoley a la verificacion

                RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());

                HashMap<String,String> params = new HashMap<>();
                params.put("idrepartidor",String.valueOf(idrepartidor.getText()));
                params.put("idsucursal",String.valueOf(idsucursal.getText()));
                params.put("nombre",user.getText().toString());
                params.put("password",password.getText().toString());

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,"http://androiddelivery.000webhostapp.com/hayRepartidor.php"
                        , new JSONObject(params),
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    if((int)response.get("resultado")==1)
                                    {
                                        Intent intent=new Intent(MainActivity.this,MapActivity.class);
                                        intent.putExtra("idsucursal",String.valueOf(idsucursal.getText()));
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"Error de logueo verifique sus datos",Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                       Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                       error.printStackTrace();

                    }
                    int m;
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

                requestQueue.add(jsonObjReq);


            }
        });
    }
}
