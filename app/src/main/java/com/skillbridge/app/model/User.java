/*
 * SkillBridge Android Application
 * Copyright (c) 2026
 * Developed by:
 * Nekkanti Venkata Avinash Krishna
 * Kommana Dheeraj
 *
 * Unauthorized academic re-submission or commercial reuse is prohibited.
 */
package com.skillbridge.app.model;

import androidx.annotation.NonNull;

public class User {
    private final int id;
    private final String name;
    private final String email;
    private final String bio;
    private final String skills;
    private final String profileImageUrl;
    private final int projectsCount;
    private final int connectionsCount;
    private boolean isConnected;

    public User(int id, String name, String email, String bio, String skills, String profileImageUrl, int projectsCount, int connectionsCount, boolean isConnected) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.bio = bio;
        this.skills = skills;
        this.profileImageUrl = profileImageUrl;
        this.projectsCount = projectsCount;
        this.connectionsCount = connectionsCount;
        this.isConnected = isConnected;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getBio() { return bio; }
    public String getSkills() { return skills; }
    public String getProfileImageUrl() { return profileImageUrl; }
    public int getProjectsCount() { return projectsCount; }
    public int getConnectionsCount() { return connectionsCount; }
    public boolean isConnected() { return isConnected; }
    public void setConnected(boolean connected) { isConnected = connected; }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}