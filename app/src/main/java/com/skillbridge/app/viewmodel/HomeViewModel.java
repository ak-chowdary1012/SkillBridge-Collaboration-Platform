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
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.skillbridge.app.model.Project;
import com.skillbridge.app.repository.AppRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private final AppRepository repository;
    private final MutableLiveData<List<Project>> projectsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public HomeViewModel(@NonNull Application application) {
        super(application);
        repository = AppRepository.getInstance(application);
        loadProjects();
    }

    public LiveData<List<Project>> getProjectsLiveData() {
        return projectsLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void loadProjects() {
        isLoading.setValue(true);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            projectsLiveData.setValue(repository.getAllProjects());
            isLoading.setValue(false);
        }, 300);
    }

    public void refreshFeed() {
        loadProjects();
    }

    public int getUnreadNotificationCount() {
        return repository.getUnreadCount();
    }
}