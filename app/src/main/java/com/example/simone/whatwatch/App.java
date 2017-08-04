package com.example.simone.whatwatch;

import android.app.Application;
import android.content.Context;

/**
 * Created by Simone on 04/08/2017.
 */

public class App extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }
}
