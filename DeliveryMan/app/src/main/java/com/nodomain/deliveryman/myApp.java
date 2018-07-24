package com.nodomain.deliveryman;

import android.app.Application;
import android.os.Bundle;

import java.util.ArrayList;

public class myApp extends Application{

    private int i=0;
    public int inc()
    {
        return ++i;
    }
}
