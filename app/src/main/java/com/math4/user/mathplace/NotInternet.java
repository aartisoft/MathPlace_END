package com.math4.user.mathplace;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.math4.user.mathplace.R;


public class NotInternet extends AppCompatActivity {

    private TextView mNetworkStatusField;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_internet);
        mNetworkStatusField = findViewById(R.id.tvNetworkStatus);
    }

    private void updateNetworkStatus() {
        if (isNetworkConnected()) {
            mNetworkStatusField.setTextColor(Color.GREEN);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mNetworkStatusField.setText(getConnectionTransportType());
            } else {
                mNetworkStatusField.setText(getConnectionTypeName());
            }
        } else {
            mNetworkStatusField.setTextColor(Color.RED);
            mNetworkStatusField.setText(R.string.no_connection);
        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private String getConnectionTransportType() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                // If current connection is VPN, check if it's over WIFI or Cellular
                for (Network network: cm.getAllNetworks()) {
                    NetworkCapabilities otherNetworkCap = cm.getNetworkCapabilities(network);
                    if (otherNetworkCap.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return getString(R.string.vpn) + ": " + getString(R.string.wifi);
                    } else if (otherNetworkCap.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return getString(R.string.vpn) + ": " + getString(R.string.cellurar);
                    }
                }
                return getString(R.string.vpn);
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return getString(R.string.wifi);
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return getString(R.string.cellurar);
            }
            return getString(R.string.other_connection);
        } else {
            return getString(R.string.no_connection);
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    private String getConnectionTypeName() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork.getTypeName();
    }


    private void registeReceiverForCallbacks() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private void registerDefaultNetworkCallback() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        cm.registerDefaultNetworkCallback(networkCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            registerDefaultNetworkCallback();
        } else {
            registeReceiverForCallbacks();
        }
        updateNetworkStatus();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            cm.unregisterNetworkCallback(networkCallback);
        } else {
            unregisterReceiver(networkChangeReceiver);
        }
    }

    private BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateNetworkStatus();
        }
    };

    private ConnectivityManager.NetworkCallback networkCallback =
            new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    updateNetworkStatus();
                    super.onAvailable(network);
                }

                @Override
                public void onLost(Network network) {
                    updateNetworkStatus();
                    super.onLost(network);
                }

                @Override
                public void onUnavailable() {
                    updateNetworkStatus();
                    super.onUnavailable();
                }
            };
}