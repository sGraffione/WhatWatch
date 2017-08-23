package com.example.simone.whatwatch.Firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.simone.whatwatch.ChatGroup.ChatGroup;
import com.example.simone.whatwatch.FragmentClasses.WatchedListFragment;
import com.example.simone.whatwatch.MainActivity;
import com.example.simone.whatwatch.R;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.RemoteMessage;


public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    String TAG = "NotificationService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        String from = remoteMessage.getData().get("from_user");
        if(!from.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            Log.d(TAG, "Invio notifica");
            sendNotification(remoteMessage);
        }else{
            Log.d(TAG, "Blocco notifica");
        }
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "CurrentUser: " + FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    private void sendNotification(RemoteMessage remoteMessage) {

        String messageBody = remoteMessage.getNotification().getBody();
        String title = remoteMessage.getNotification().getTitle();
        String click_action = remoteMessage.getNotification().getClickAction();
        String identifier = remoteMessage.getData().get("identifier");
        String titleOfElement = remoteMessage.getData().get("titleOfElement");

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setSmallIcon(R.drawable.notification);

        Intent intent = new Intent(click_action);
        intent.putExtra("identifier", identifier);
        //intent.putExtra("title", titleOfElement);
        Log.d(TAG, "titleOfElement: " + titleOfElement);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}
