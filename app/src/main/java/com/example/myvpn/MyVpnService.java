package com.example.myvpn;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.VpnService;
import android.os.Build;
import android.os.ParcelFileDescriptor;

import androidx.core.app.NotificationCompat;

import java.io.IOException;

public class MyVpnService extends VpnService {

    private static final String CHANNEL_ID = "vpn_channel";
    private static final int NOTIFICATION_ID = 1;

    private ParcelFileDescriptor vpnInterface;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        createNotificationChannel();

        Notification notification =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle("MyVPN")
                        .setContentText("VPN Connected")
                        .setSmallIcon(android.R.drawable.stat_sys_download_done)
                        .setOngoing(true)
                        .build();

        startForeground(NOTIFICATION_ID, notification);

        try {

            Builder builder = new Builder();

            builder.setSession("MyVPN")
                    .addAddress("10.0.0.2", 24)
                    .addDnsServer("8.8.8.8")
                    .addDnsServer("1.1.1.1")
                    .addRoute("0.0.0.0", 0);

            vpnInterface = builder.establish();

        } catch (Exception e) {
            stopSelf();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        try {

            if (vpnInterface != null) {
                vpnInterface.close();
                vpnInterface = null;
            }

        } catch (IOException ignored) {
        }

        stopForeground(true);
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel =
                    new NotificationChannel(
                            CHANNEL_ID,
                            "VPN Service",
                            NotificationManager.IMPORTANCE_LOW
                    );

            channel.setDescription("VPN Connection");

            NotificationManager manager =
                    getSystemService(NotificationManager.class);

            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
}