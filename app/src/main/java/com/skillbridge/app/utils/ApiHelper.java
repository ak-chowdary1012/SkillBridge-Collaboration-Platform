/*
 * SkillBridge Android Application
 * Copyright (c) 2026
 * Developed by:
 * Nekkanti Venkata Avinash Krishna
 * Kommana Dheeraj
 *
 * Unauthorized academic re-submission or commercial reuse is prohibited.
 */
package com.skillbridge.app.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class ApiHelper {
    private static ApiHelper instance;
    private RequestQueue requestQueue;

    public interface ApiCallback {
        void onSuccess(String name, String company);
        void onError(String message);
    }

    private ApiHelper(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized ApiHelper getInstance(Context context) {
        if (instance == null) {
            instance = new ApiHelper(context);
        }
        return instance;
    }

    public void fetchTrendingDeveloper(ApiCallback callback) {
        // Hardcoded as requested to show Infopredator with Cyber Security title
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            callback.onSuccess("Infopredator", "Cyber Security");
        }, 500); // Small delay to simulate network for the progress bar
    }
}