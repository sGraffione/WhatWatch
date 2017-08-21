package com.example.simone.whatwatch.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.example.simone.whatwatch.Classes.TransparentActivity;

import java.io.IOException;


public class InternetReceiver extends BroadcastReceiver {

    public boolean isConnected;
    @Override
    public void onReceive(Context context, Intent intent) {

        if (isConnected()) {
            Log.d("NetReceiver", "Internet is connected");
            isConnected = true;
        }else {
            Log.d("NetReceiver", "Internet is not connected");
            /*Intent startIntent = new Intent(context, TransparentActivity.class);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(startIntent);*/
            isConnected = false;
        }
    }

    public boolean isConnected() {

        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e)          { Log.e("ERROR", "IOException",e); }
        catch (InterruptedException e) { Log.e("ERROR", "InterruptedException",e); }

        return false;
    }

    public boolean getIsConnected(){
        return isConnected;
    }

}
