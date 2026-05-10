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
import com.skillbridge.app.model.User;
import com.skillbridge.app.repository.AppRepository;

import java.util.ArrayList;
import java.util.List;

public class SearchViewModel extends AndroidViewModel {

    private final AppRepository repository;
    private final SQLiteDatabaseHelper dbHelper;
    private final MutableLiveData<List<User>> usersLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Project>> projectsLiveData = new MutableLiveData<>();

    public SearchViewModel(@NonNull Application application) {
        super(application);
        repository = AppRepository.getInstance(application);
        dbHelper = SQLiteDatabaseHelper.getInstance(application);
        loadAll();
    }

    private void loadAll() {
        syncUsers(repository.getAllUsers());
        
        List<Project> combined = new ArrayList<>(dbHelper.getAllPostedProjects());
        combined.addAll(repository.getAllProjects());
        projectsLiveData.setValue(combined);
    }

    private void syncUsers(List<User> users) {
        if (users != null) {
            for (User user : users) {
                user.setConnected(dbHelper.isConnected(user.getName()));
            }
        }
        usersLiveData.setValue(users);
    }

    public LiveData<List<User>> getUsersLiveData() {
        return usersLiveData;
    }

    public LiveData<List<Project>> getProjectsLiveData() {
        return projectsLiveData;
    }

    public void search(String query) {
        if (query == null || query.isEmpty()) {
            loadAll();
        } else {
            syncUsers(repository.searchUsers(query));
            
            // Search local projects
            List<Project> localResults = new ArrayList<>();
            for (Project p : dbHelper.getAllPostedProjects()) {
                if (p.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    p.getSkillsRequired().toLowerCase().contains(query.toLowerCase())) {
                    localResults.add(p);
                }
            }
            
            // Search repository projects and combine
            List<Project> combinedResults = new ArrayList<>(localResults);
            combinedResults.addAll(repository.searchProjects(query));
            
            projectsLiveData.setValue(combinedResults);
        }
    }

    public void toggleConnection(User user) {
        boolean newState = !user.isConnected();
        user.setConnected(newState);
        
        if (newState) {
            dbHelper.addConnection(user.getName());
        } else {
            dbHelper.removeConnection(user.getName());
        }
        // Refresh the list to reflect changes
        usersLiveData.setValue(usersLiveData.getValue());
    }
}