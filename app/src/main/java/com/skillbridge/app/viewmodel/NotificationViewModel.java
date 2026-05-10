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

import com.skillbridge.app.model.Notification;
import com.skillbridge.app.repository.AppRepository;

import java.util.List;

public class NotificationViewModel extends AndroidViewModel {

    private final AppRepository repository;
    private final MutableLiveData<List<Notification>> notificationsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> unreadCount = new MutableLiveData<>();

    public NotificationViewModel(@NonNull Application application) {
        super(application);
        repository = AppRepository.getInstance(application);
        loadNotifications();
    }

    public LiveData<List<Notification>> getNotificationsLiveData() {
        return notificationsLiveData;
    }

    public LiveData<Integer> getUnreadCount() {
        return unreadCount;
    }

    public void loadNotifications() {
        notificationsLiveData.setValue(repository.getAllNotifications());
        unreadCount.setValue(repository.getUnreadCount());
    }

    public void markAllRead() {
        repository.markAllRead();
        loadNotifications();
    }
}