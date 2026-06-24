package com.example.myvpn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends Activity {

    private static final int VPN_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button connect = findViewById(R.id.btnConnect);

        connect.setOnClickListener(v -> {
            Intent intent = android.net.VpnService.prepare(this);

            if (intent != null) {
                startActivityForResult(intent, VPN_REQUEST_CODE);
            } else {
                onActivityResult(VPN_REQUEST_CODE, RESULT_OK, null);
            }
        });
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VPN_REQUEST_CODE &&
                resultCode == RESULT_OK) {

            startService(
                    new Intent(this, MyVpnService.class)
            );
        }
    }
}