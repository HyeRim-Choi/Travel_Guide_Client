package fcm;


import android.app.NotificationChannel;

import android.app.NotificationManager;

import android.app.PendingIntent;

import android.content.Context;
import android.content.Intent;


import android.os.Build;


import android.util.Log;


import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.chr.travel.FindIdActivity;
import com.chr.travel.MainActivity;
import com.chr.travel.NotificationActivity;
import com.chr.travel.R;
import com.google.firebase.messaging.FirebaseMessagingService;

import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;


public class FireBaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onNewToken(String token) {
        Log.d("FCM Log", "Refreshed token: " + token);
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // 포그라운드 부분에서 작동
        //Log.i("test", remoteMessage.getNotification().getBody());


        if (remoteMessage.getData().size() > 0) {

            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            //Log.i("test1", remoteMessage.getNotification().getBody());


            if (false) {
                handleNow();
            }


        }

        if (remoteMessage.getData() != null) {

            //Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            //Log.i("test2", remoteMessage.getNotification().getBody());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.i("test2", remoteMessage.getData().get("title"));
                sendNotification(remoteMessage.getData());
            }

        }


    }

    private void handleNow() {

        Log.d(TAG, "Short lived task is done.");

    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    private void sendNotification(Map<String, String> messageBody) {

        Intent intent = new Intent(this, NotificationActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,

                PendingIntent.FLAG_UPDATE_CURRENT);


        String channelId = "channelId";


        NotificationCompat.Builder notificationBuilder =

                new NotificationCompat.Builder(this, channelId)

                        .setSmallIcon(R.mipmap.ic_launcher)

                        .setContentTitle(messageBody.get("title"))

                        .setContentText(messageBody.get("content"))

                        .setAutoCancel(true)

                        .setContentIntent(pendingIntent);



        NotificationManager notificationManager =

                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String channelName = "channelName";

            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);

            notificationManager.createNotificationChannel(channel);

        }

        notificationManager.notify(0, notificationBuilder.build());

    }

}

