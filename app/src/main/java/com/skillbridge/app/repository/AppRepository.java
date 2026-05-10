/*
 * SkillBridge Android Application
 * Copyright (c) 2026
 * Developed by:
 * Nekkanti Venkata Avinash Krishna
 * Kommana Dheeraj
 *
 * Unauthorized academic re-submission or commercial reuse is prohibited.
 */
package com.skillbridge.app.repository;

import android.content.Context;

import com.skillbridge.app.data.DummyData;
import com.skillbridge.app.model.Notification;
import com.skillbridge.app.model.Project;
import com.skillbridge.app.model.User;

import java.util.ArrayList;
import java.util.List;

public class AppRepository {
    private static AppRepository instance;
    private final List<Project> projects;
    private final List<User> users;
    private final List<Notification> notifications;

    private AppRepository(Context context) {
        projects = new ArrayList<>(DummyData.getProjects());
        users = new ArrayList<>(DummyData.getUsers());
        notifications = new ArrayList<>(DummyData.getNotifications());
    }

    public static synchronized AppRepository getInstance(Context context) {
        if (instance == null) {
            instance = new AppRepository(context);
        }
        return instance;
    }

    public List<Project> getAllProjects() {
        return projects;
    }

    public List<Project> searchProjects(String query) {
        List<Project> filtered = new ArrayList<>();
        for (Project p : projects) {
            if (p.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                p.getSkillsRequired().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(p);
            }
        }
        return filtered;
    }

    public void addProject(Project project) {
        projects.add(0, project);
    }

    public List<User> getAllUsers() {
        return users;
    }

    public List<User> searchUsers(String query) {
        List<User> filtered = new ArrayList<>();
        for (User u : users) {
            if (u.getName().toLowerCase().contains(query.toLowerCase()) ||
                u.getSkills().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(u);
            }
        }
        return filtered;
    }

    public List<Notification> getAllNotifications() {
        return notifications;
    }

    public int getUnreadCount() {
        int count = 0;
        for (Notification n : notifications) {
            if (!n.isRead()) count++;
        }
        return count;
    }

    public void markAllRead() {
        for (Notification n : notifications) {
            n.setRead(true);
        }
    }

    public User getLoggedInUser() {
        return DummyData.getLoggedInUser();
    }
}