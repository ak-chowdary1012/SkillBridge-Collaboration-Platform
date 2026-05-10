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
import com.skillbridge.app.model.User;
import com.skillbridge.app.repository.AppRepository;

import java.util.List;

public class ConnectionsViewModel extends AndroidViewModel {

    private final AppRepository repository;
    private final SQLiteDatabaseHelper dbHelper;
    private final MutableLiveData<List<User>> usersLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> statusMessage = new MutableLiveData<>();

    public ConnectionsViewModel(@NonNull Application application) {
        super(application);
        repository = AppRepository.getInstance(application);
        dbHelper = SQLiteDatabaseHelper.getInstance(application);
        loadUsers();
    }

    public LiveData<List<User>> getUsersLiveData() {
        return usersLiveData;
    }

    public LiveData<String> getStatusMessage() {
        return statusMessage;
    }

    public void loadUsers() {
        List<User> allUsers = repository.getAllUsers();
        // Sync connection state from DB
        for (User user : allUsers) {
            user.setConnected(dbHelper.isConnected(user.getName()));
        }
        usersLiveData.setValue(allUsers);
    }

    public void toggleConnection(User user) {
        boolean newState = !user.isConnected();
        user.setConnected(newState);
        
        if (newState) {
            dbHelper.addConnection(user.getName());
            statusMessage.setValue("Connected with " + user.getName());
        } else {
            dbHelper.removeConnection(user.getName());
            statusMessage.setValue("Connection removed");
        }
        loadUsers();
    }
}