package edu.upenn.cis350.cis350project;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationStartReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent intent2 = new Intent(context, NotificationService.class);
            context.startForegroundService(intent2);
        }
    }
}
