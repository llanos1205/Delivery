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
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button x,y;
    public boolean registered=false;
    private PopupWindow mPopupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

                ///sql
                //
                ///
                registered=true;
                if(registered)
                {
                    x.setVisibility(View.VISIBLE);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"no existe",Toast.LENGTH_LONG);
                }
            }
        });
    }
}
