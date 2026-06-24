package com.example.myvpn;

import android.content.Intent;
import android.net.VpnService;
import android.os.ParcelFileDescriptor;

import java.io.IOException;

public class MyVpnService extends VpnService {

    private ParcelFileDescriptor vpnInterface;

    @Override
    public int onStartCommand(
            Intent intent,
            int flags,
            int startId) {

        Builder builder = new Builder();

        builder.setSession("MyVPN");

        builder.addAddress(
                "10.0.0.2",
                24
        );

        builder.addDnsServer(
                "8.8.8.8"
        );

        builder.addRoute(
                "0.0.0.0",
                0
        );

        vpnInterface = builder.establish();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (vpnInterface != null) {
            try {
                vpnInterface.close();
            } catch (IOException ignored) {
            }
        }
    }
}