/*
 * SkillBridge Android Application
 * Copyright (c) 2026
 * Developed by:
 * Nekkanti Venkata Avinash Krishna
 * Kommana Dheeraj
 *
 * Unauthorized academic re-submission or commercial reuse is prohibited.
 */
package com.skillbridge.app.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.skillbridge.app.database.SQLiteDatabaseHelper;

public class MainViewModel extends AndroidViewModel {
    private final MutableLiveData<Integer> projectCount = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> connectionCount = new MutableLiveData<>(0);
    private final MutableLiveData<Boolean> refreshTrigger = new MutableLiveData<>(false);
    private final SQLiteDatabaseHelper dbHelper;

    public MainViewModel(@NonNull Application application) {
        super(application);
        dbHelper = SQLiteDatabaseHelper.getInstance(application);
        updateCounts();
    }

    public LiveData<Integer> getProjectCount() {
        return projectCount;
    }

    public LiveData<Integer> getConnectionCount() {
        return connectionCount;
    }

    public LiveData<Boolean> getRefreshTrigger() {
        return refreshTrigger;
    }

    public void updateCounts() {
        new Thread(() -> {
            projectCount.postValue(dbHelper.getProjectCount());
            connectionCount.postValue(dbHelper.getConnectionCount());
        }).start();
    }

    public void incrementConnectionCount() {
        updateCounts();
    }

    public void triggerRefresh() {
        updateCounts();
        refreshTrigger.setValue(true);
        refreshTrigger.setValue(false);
    }
}