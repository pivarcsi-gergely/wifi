package hu.petrik.wifi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.Formatter;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView BottomNavView;
    private TextView TVInfo;
    private WifiManager wifiManager;
    private WifiInfo wifiInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        BottomNavView.setOnClickListener(v -> {
            switch (v.getItemId()) {
                case R.id.wifi_on:
                    //Android 10-től (API 29) az alkalmazád nem tudja ki/bekapcsolni a wifit
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                        TVInfo.setText("Nincs jogosultság átváltásra");
                        //Le panel
                        Intent panelIntent = new Intent(Settings.Panel.ACTION_WIFI);
                        startActivityForResult(panelIntent, 0);
                    }
                    else {
                        wifiManager.setWifiEnabled(true);
                        TVInfo.setText("Wifi bekapcsolva");
                    }
                    break;
                case R.id.wifi_off:
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                        TVInfo.setText("Nincs jogosultság átváltásra");
                        //Le panel
                        Intent panelIntent = new Intent(Settings.Panel.ACTION_WIFI);
                        startActivityForResult(panelIntent, 0);
                    }
                    else {
                        wifiManager.setWifiEnabled(false);
                        TVInfo.setText("Wifi kikapcsolva");
                    }
                    break;
                case R.id.wifi_info:
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    if (networkInfo.isConnected()) {
                        int ip = wifiInfo.getIpAddress();
                        //Formatter: android.text.format.formatter
                        String ipConverted = Formatter.formatIpAddress(ip);
                        TVInfo.setText("IP cím: " + ipConverted);
                    }
                    else {
                        TVInfo.setText("Nem csatlakoztál internethez, így nem tudom megmutatni az IP-címedet");
                    }
                    break;
            }
        });
    }

    public void init() {
        BottomNavView = findViewById(R.id.BottomNavView);
        TVInfo = findViewById(R.id.TVInfo);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED || wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
                TVInfo.setText("Wifi bekapcsolva");
            }
            else if(wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED || wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLING) {
                TVInfo.setText("Wifi kikapcsolva");
            }
        }
    }
}