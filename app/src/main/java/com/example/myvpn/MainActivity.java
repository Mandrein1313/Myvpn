package com.example.myvpn;

import android.app.Activity;
import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

private static final int VPN_REQUEST_CODE = 100;

private TextView txtStatus;
private Button btnConnect;
private Button btnDisconnect;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    txtStatus = findViewById(R.id.txtStatus);
    btnConnect = findViewById(R.id.btnConnect);
    btnDisconnect = findViewById(R.id.btnDisconnect);

    txtStatus.setText("Disconnected");

    btnConnect.setOnClickListener(v -> connectVpn());

    btnDisconnect.setOnClickListener(v -> disconnectVpn());
}

private void connectVpn() {

    Intent intent = VpnService.prepare(this);

    if (intent != null) {
        startActivityForResult(intent, VPN_REQUEST_CODE);
    } else {
        onActivityResult(
                VPN_REQUEST_CODE,
                RESULT_OK,
                null
        );
    }
}

private void disconnectVpn() {

    stopService(
            new Intent(
                    this,
                    MyVpnService.class
            )
    );

    txtStatus.setText("Disconnected");
}

@Override
protected void onActivityResult(
        int requestCode,
        int resultCode,
        Intent data
) {

    super.onActivityResult(
            requestCode,
            resultCode,
            data
    );

    if (requestCode == VPN_REQUEST_CODE
            && resultCode == RESULT_OK) {

        startService(
                new Intent(
                        this,
                        MyVpnService.class
                )
        );

        txtStatus.setText("Connected");
    }
}

}