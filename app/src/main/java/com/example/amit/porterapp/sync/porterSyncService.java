package com.example.amit.porterapp.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by amit on 7/23/2015.
 */
public class porterSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static porterSyncAdapter sporterSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("SunshineSyncService", "onCreate - SunshineSyncService");
        synchronized (sSyncAdapterLock) {
            if (sporterSyncAdapter == null) {
                sporterSyncAdapter = new porterSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sporterSyncAdapter.getSyncAdapterBinder();
    }
}
