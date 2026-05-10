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

public class Notification {
    private int id;
    private String message;
    private String type;
    private String timeAgo;
    private boolean isRead;

    public Notification(int id, String message, String type, String timeAgo, boolean isRead) {
        this.id = id;
        this.message = message;
        this.type = type;
        this.timeAgo = timeAgo;
        this.isRead = isRead;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getTimeAgo() { return timeAgo; }
    public void setTimeAgo(String timeAgo) { this.timeAgo = timeAgo; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}