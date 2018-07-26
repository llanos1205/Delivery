package com.nodomain.clientside.ClassesOp;
import com.google.android.gms.maps.model.LatLng;

public class RutaArco {

    private LatLng _origin;
    private LatLng _end;
    private double _distance;
    private String _time;

    public RutaArco()
    {
        _origin=null;
        _end=null;
        _distance=0;
        _time=null;
    }
    public RutaArco(LatLng A,LatLng B ,Double y,String x)
    {
        _origin=new LatLng(A.latitude,A.longitude);
        _end=new LatLng(B.latitude,B.longitude);
        _time=x;
        _distance=y;

    }
    public void set_origin(LatLng a)
    {
        _origin=a;

    }
    public void set_end(LatLng a)
    {
        _end=a;
    }
    public void  set_distance(double d)
    {
        _distance=d;


    }
    public void set_time(String v)
    {
        _time=v;
    }


    public LatLng get_origin() {
        return _origin;
    }

    public LatLng get_end() {
        return _end;
    }

    public double get_distance() {
        return _distance;
    }

    public String get_time() {
        return _time;
    }
}
