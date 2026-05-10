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
import com.skillbridge.app.model.Project;

import java.util.List;

public class PostViewModel extends AndroidViewModel {

    private final SQLiteDatabaseHelper dbHelper;
    private final MutableLiveData<List<Project>> myProjectsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> statusMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public PostViewModel(@NonNull Application application) {
        super(application);
        dbHelper = SQLiteDatabaseHelper.getInstance(application);
        loadMyProjects();
    }

    public LiveData<List<Project>> getMyProjectsLiveData() {
        return myProjectsLiveData;
    }

    public LiveData<String> getStatusMessage() {
        return statusMessage;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void loadMyProjects() {
        isLoading.postValue(true);
        new Thread(() -> {
            List<Project> list = dbHelper.getAllPostedProjects();
            myProjectsLiveData.postValue(list);
            isLoading.postValue(false);
        }).start();
    }

    public void postProject(String title, String desc, String skills, String location, int teamSize) {
        isLoading.postValue(true);
        new Thread(() -> {
            long id = dbHelper.insertProject(title, desc, skills, location, teamSize);
            if (id != -1) {
                statusMessage.postValue("Project posted successfully!");
                // Instead of calling loadMyProjects which starts another thread, 
                // we load directly here since we are already on a background thread.
                List<Project> list = dbHelper.getAllPostedProjects();
                myProjectsLiveData.postValue(list);
            } else {
                statusMessage.postValue("Failed to post project.");
            }
            isLoading.postValue(false);
        }).start();
    }

    public void deleteProject(int id) {
        new Thread(() -> {
            int rows = dbHelper.deleteProject(id);
            if (rows > 0) {
                statusMessage.postValue("Project deleted.");
                loadMyProjects();
            }
        }).start();
    }
}