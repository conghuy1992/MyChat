package com.conghuy.mychat.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.conghuy.mychat.ChattingActivity;
import com.conghuy.mychat.MainActivity;
import com.conghuy.mychat.MyApplication;
import com.conghuy.mychat.R;
import com.conghuy.mychat._class.Config;
import com.conghuy.mychat._class.Const;
import com.conghuy.mychat.dto.RoomDtoDetails;
import com.conghuy.mychat.fragments.FragmentChat;
import com.conghuy.mychat.util.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;


/**
 * Created by Ravi Tamada on 08/08/16.
 * www.androidhive.info
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
    private NotificationUtils notificationUtils;
    private NotificationManager mNotificationManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        Log.e(TAG, "From: " + remoteMessage.getFrom());//4103456745464

        if (remoteMessage == null)
            return;

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
//            Log.d(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }


    private void handleDataMessage(JSONObject json) {
//        Log.d(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");
            Log.d(TAG, "data:" + data.toString());
            if (data.toString().startsWith("{")) {
                RoomDtoDetails dto = new Gson().fromJson(data.toString(), RoomDtoDetails.class);
                String title = dto.name;
                String message = dto.last_msg;
                String imageUrl = dto.image_noti;
                int chat_room_id = dto.chat_room_id;


                if (MyApplication.ROOM_NO != chat_room_id) {
                    // show notify
                    Intent myIntent = new Intent(MyFirebaseMessagingService.this, ChattingActivity.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

                    myIntent.putExtra(Const.FCM_DATA_OPEN_CHAT, dto);
                    myIntent.putExtra(Const.TYPE_CHAT, 0);
                    sendNotification(message, title, imageUrl, myIntent, chat_room_id);
                } else {
                    // sendBroadcast to update current list chat with user
                    Intent intent = new Intent(Const.ACTION_RECEIVER_NOTIFICATION);
                    intent.putExtra(Const.FCM_DATA_NOTIFICATOON, dto);
                    sendBroadcast(intent);
                }

                // sendBroadcast to update last msg of list chat room
                Intent intentLastMsg = new Intent(Const.ACTION_RECEIVER_LAST_MSG);
                intentLastMsg.putExtra(Const.FCM_DATA_NOTIFICATOON, dto);
                sendBroadcast(intentLastMsg);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }


    private void sendNotification(String msg, final String title, String avatarUrl, Intent myIntent, final long roomNo) {
        final long[] vibrate = new long[]{500, 500, 0, 0, 0};
        final Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        final PendingIntent contentIntent = PendingIntent.getActivity(this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        final String msgTemp = msg;
        if (avatarUrl != null) {
            new DownloadImageTask(avatarUrl, new getBitmap() {
                @Override
                public void onSuccess(Bitmap result) {
                    if (result == null) {
                        result = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                    }
                    mBuilder
//                            .setNumber(unReadCount)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setLargeIcon(result)
                            .setContentTitle(title)
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(msgTemp))
                            .setContentText(msgTemp)
                            .setPriority(Notification.PRIORITY_MAX)
                            .setAutoCancel(true);

                    // Check notification setting and config notification
//                    if (isEnableSound)
                    mBuilder.setSound(soundUri);
//                    if (isEnableVibrate)
                    mBuilder.setVibrate(vibrate);
                    mBuilder.setContentIntent(contentIntent);
                    Notification notification = mBuilder.build();


                    notification.number = 100;
                    mNotificationManager.notify((int) roomNo, notification);
                    mNotificationManager.notify((int) roomNo, mBuilder.build());
                }
            }).execute();
        }
    }

    interface getBitmap {
        void onSuccess(Bitmap result);
    }

    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        String avatarUrl;
        getBitmap calback;

        public DownloadImageTask(String avatarUrl, getBitmap calback) {
            this.avatarUrl = avatarUrl;
            this.calback = calback;
            Log.d(TAG, "avatarUrl: " + avatarUrl);
        }

        Bitmap bitmapOrg = null;

        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(avatarUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                bitmapOrg = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
//                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmapOrg;
        }

        protected void onPostExecute(final Bitmap result) {
            calback.onSuccess(result);
        }
    }
}
