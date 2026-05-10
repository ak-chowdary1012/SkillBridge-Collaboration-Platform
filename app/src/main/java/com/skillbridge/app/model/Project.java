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

import java.io.Serializable;

public class Project implements Serializable {
    private final int id;
    private final String title;
    private final String description;
    private final String skillsRequired;
    private final String postedBy;
    private final String location;
    private final String timeAgo;
    private final double latitude;
    private final double longitude;
    private final int teamSize;
    private boolean isApplied;

    public Project(int id, String title, String description, String skillsRequired, String postedBy, String location, String timeAgo, double latitude, double longitude, int teamSize, boolean isApplied) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.skillsRequired = skillsRequired;
        this.postedBy = postedBy;
        this.location = location;
        this.timeAgo = timeAgo;
        this.latitude = latitude;
        this.longitude = longitude;
        this.teamSize = teamSize;
        this.isApplied = isApplied;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getSkillsRequired() { return skillsRequired; }
    public String getPostedBy() { return postedBy; }
    public String getLocation() { return location; }
    public String getTimeAgo() { return timeAgo; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public int getTeamSize() { return teamSize; }
    public boolean isApplied() { return isApplied; }
    public void setApplied(boolean applied) { isApplied = applied; }
}