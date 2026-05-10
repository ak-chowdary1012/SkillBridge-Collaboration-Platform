/*
 * SkillBridge Android Application
 * Copyright (c) 2026
 * Developed by:
 * Nekkanti Venkata Avinash Krishna
 * Kommana Dheeraj
 *
 * Unauthorized academic re-submission or commercial reuse is prohibited.
 */
package com.skillbridge.app.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class DataSyncService extends Service {

    public static final String ACTION_SYNC_COMPLETE = "com.skillbridge.app.SYNC_COMPLETE";
    public static final String ACTION_SYNC_PROGRESS = "com.skillbridge.app.SYNC_PROGRESS";
    public static final String EXTRA_PROGRESS = "progress";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(() -> {
            for (int i = 0; i <= 100; i += 20) {
                try {
                    Thread.sleep(1000);
                    Intent progressIntent = new Intent(ACTION_SYNC_PROGRESS);
                    progressIntent.setPackage(getPackageName());
                    progressIntent.putExtra(EXTRA_PROGRESS, i);
                    sendBroadcast(progressIntent);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            Intent completeIntent = new Intent(ACTION_SYNC_COMPLETE);
            completeIntent.setPackage(getPackageName());
            sendBroadcast(completeIntent);
            stopSelf();
        }).start();
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}