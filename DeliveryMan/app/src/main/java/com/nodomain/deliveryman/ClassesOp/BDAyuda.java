package com.nodomain.deliveryman.ClassesOp;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Editable;


import java.util.ArrayList;
import java.util.List;

public class BDAyuda extends SQLiteOpenHelper {
    //Sirve para manipular lka base de datos desde sus tablas
    private static final String NombreBD="DatosLocalizaciones";
    private static final int Version=1;
    private static final String Tabla_Localizaciones="CREATE TABLE PEDIDOS(NOMBRE TEXT NOT NULL, LATITUD TEXT NOT NULL, LONGITUD TEXT NOT NULL,PEDIDO TEXT NOT NULL)";


    public BDAyuda(Context context) {
        super(context, NombreBD, null, Version);
    }
    //Se ejecuta automaticamente
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Tabla_Localizaciones);


    }

    //Este metodo se reealizara unicamente cuando sea necesaria una modificacion en la estructura de datos
    //O conversion de datos

    //Hacemos lo siguiente porque estamos haciendo en una sola tabla
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+Tabla_Localizaciones);
        db.execSQL(Tabla_Localizaciones);


    }
    public void agregarLocalizacion(String nombre, String latitud, String longitud,String pedido) throws SQLException
    {
        SQLiteDatabase bd=getWritableDatabase();
        if(bd!=null)
        {

            bd.execSQL("INSERT INTO PEDIDOS VALUES('"+nombre+"','"+latitud+"','"+longitud+"','"+pedido+"')");
            bd.close();
        }

    }
    public void Borrar_Tabla()
    {
        SQLiteDatabase bd=getWritableDatabase();
        if(bd!=null)
        {
            bd.execSQL("DELETE FROM PEDIDOS");
            bd.close();
        }
    }
    //Para que te devulva la lista simplemente tienes que instanciar un objeto BDAyuda y ejecutar este metodo
    public List<DestinyPoint> listarLocalizaciones()
    {
        SQLiteDatabase bd=getReadableDatabase();
        Cursor cursor=bd.rawQuery("SELECT * FROM PEDIDOS",null);
        List<DestinyPoint> localizaciones=new ArrayList<>();
        if(cursor.moveToFirst())
        {
            do {
                DestinyPoint dp=new DestinyPoint();
                dp.nombredestino=cursor.getString(0);

                dp.latitud=Double.parseDouble(cursor.getString(1));
                dp.longitud=Double.parseDouble(cursor.getString(2));
                dp.pedido=cursor.getString(3);
                localizaciones.add(dp);



            }while(cursor.moveToNext());
        }

        return localizaciones;

    }





}

