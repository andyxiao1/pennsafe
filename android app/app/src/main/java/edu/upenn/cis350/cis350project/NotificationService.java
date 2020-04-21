package edu.upenn.cis350.cis350project;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class NotificationService extends Service {

    private Socket socket;
    private static int notificationID = 0;
    private static String channelID = "safety-app-notification-channel";

    public NotificationService() {
        try {
            socket = IO.socket("http://10.0.2.2:3000");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (socket != null) {
            socket.connect();
            socket.on("safety-app-notification", onSocketMessage);
            registerNotificationChannel();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        socket.disconnect();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void sendNotification(String title, String message) {
        Context context = getApplicationContext();

        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NotificationChannel.DEFAULT_CHANNEL_ID)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setChannelId(channelID)
                .setVibrate(new long[]{1000, 1000});

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationID++, builder.build());
    }

    private void registerNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, "Safety App Notifications", NotificationManager.IMPORTANCE_HIGH);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    private Emitter.Listener onSocketMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            JSONObject data = (JSONObject) args[0];
            try {
                String title = data.getString("title");
                String message = data.getString("message");
                sendNotification(title, message);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };
}
