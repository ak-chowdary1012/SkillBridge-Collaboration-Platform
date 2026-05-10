/*
 * SkillBridge Android Application
 * Copyright (c) 2026
 * Developed by:
 * Nekkanti Venkata Avinash Krishna
 * Kommana Dheeraj
 *
 * Unauthorized academic re-submission or commercial reuse is prohibited.
 */
package com.skillbridge.app.data;

import com.skillbridge.app.model.Notification;
import com.skillbridge.app.model.Project;
import com.skillbridge.app.model.User;

import java.util.ArrayList;
import java.util.List;

public class DummyData {

    public static List<User> getUsers() {
        List<User> users = new ArrayList<>();
        // Creators added to the top
        users.add(new User(101, "Avinash Krishna Nekkanti", "akchowdary21@gmail.com", "Cyber Security undergraduate at Amrita Vishwa Vidyapeetham with a strong interest in building secure, reliable, and scalable systems. Proficient in Python, C, and Java, with hands-on experience in academic research and practical security concepts.", "Cyber Security, Project Management, Java", "android.resource://com.skillbridge.app/drawable/avinash_krishna", 5, 500, false));
        users.add(new User(102, "K. Dheeraj", "dheeraj@example.com", "UI/UX Designer and Frontend Specialist of SkillBridge.", "Figma, Flutter, Kotlin, Design", "android.resource://com.skillbridge.app/drawable/dheeraj", 3, 95, false));

        users.add(new User(1, "Arjun Sharma", "arjun@example.com", "Building Android apps that change campus life. Open to collabs.", "Java, Android, Firebase", "https://i.pravatar.cc/150?img=1", 2, 45, false));
        users.add(new User(2, "Priya Nair", "priya@example.com", "UI/UX obsessed. Crafting pixel-perfect mobile experiences daily.", "Flutter, Kotlin, Figma", "https://i.pravatar.cc/150?img=47", 1, 30, false));
        users.add(new User(3, "Rohan Mehta", "rohan@example.com", "ML researcher. Turning messy datasets into meaningful models.", "Python, TensorFlow, OpenCV", "https://i.pravatar.cc/150?img=3", 3, 20, false));
        users.add(new User(4, "Sneha Patel", "sneha@example.com", "Full-stack dev. REST APIs by day, side projects by night.", "React, Node.js, MongoDB", "https://i.pravatar.cc/150?img=48", 4, 55, false));
        users.add(new User(5, "Kiran Rao", "kiran@example.com", "Cybersecurity student. CTF player. Ethical hacker in training.", "Python, Kali Linux, Metasploit", "https://i.pravatar.cc/150?img=5", 2, 15, false));
        users.add(new User(6, "Ananya Krishnan", "ananya@example.com", "Data scientist. Kaggle competitor. I speak fluent DataFrame.", "R, Python, Tableau, SQL", "https://i.pravatar.cc/150?img=49", 1, 40, false));
        users.add(new User(7, "Vikram Singh", "vikram@example.com", "Cloud architect. AWS certified. Automating everything possible.", "AWS, Terraform, Kubernetes", "https://i.pravatar.cc/150?img=7", 3, 25, false));
        users.add(new User(8, "Saanvi Joshi", "saanvi@example.com", "Design thinking advocate. Making apps people actually enjoy using.", "UI/UX, Figma, Adobe XD", "https://i.pravatar.cc/150?img=50", 2, 35, false));
        return users;
    }

    public static List<Project> getProjects() {
        List<Project> projects = new ArrayList<>();
        projects.add(new Project(1, "Campus Event Manager App", "Android app to manage college events with QR check-in, push notifications, and real-time updates for students and admins.", "Java, Android, Firebase", "Arjun Sharma", "Bangalore", "2 hours ago", 12.9716, 77.5946, 3, false));
        projects.add(new Project(2, "AI Resume Screener", "NLP tool that automatically ranks resumes against job descriptions. Built for college placement cell automation.", "Python, NLP, Machine Learning", "Rohan Mehta", "Hyderabad", "5 hours ago", 17.3850, 78.4867, 2, false));
        projects.add(new Project(3, "Smart Canteen Pre-Order", "Web + mobile app for students to pre-order canteen food, skip queues, and track orders in real time.", "React, Node.js, MongoDB", "Sneha Patel", "Chennai", "1 day ago", 13.0827, 80.2707, 4, false));
        projects.add(new Project(4, "Face Recognition Attendance", "Automated classroom attendance using deep learning face detection. Works with existing CCTV infrastructure.", "Python, OpenCV, TensorFlow", "Kiran Rao", "Pune", "2 days ago", 18.5204, 73.8567, 3, false));
        projects.add(new Project(5, "Peer Tutoring Marketplace", "Platform connecting students for peer tutoring with scheduling, ratings, and video call integration.", "Flutter, Firebase, Dart", "Priya Nair", "Mumbai", "3 days ago", 19.0760, 72.8777, 5, false));
        projects.add(new Project(6, "Student Mental Health Tracker", "Anonymous mood tracking app with weekly insights, guided meditation sessions, and counselor connect feature.", "Android, Room DB, MPAndroidChart", "Ananya Krishnan", "Delhi", "4 days ago", 28.7041, 77.1025, 2, false));
        projects.add(new Project(7, "Hackathon Team Finder Bot", "Telegram bot that matches students for hackathons based on skill compatibility and timezone overlap.", "Python, Telegram API, SQL", "Vikram Singh", "Kolkata", "5 days ago", 22.5726, 88.3639, 2, false));
        projects.add(new Project(8, "AR Campus Tour Guide", "Augmented reality app overlaying building info on camera feed. Perfect for freshers orientation week.", "Unity, ARCore, C#", "Saanvi Joshi", "Jaipur", "1 week ago", 26.9124, 75.7873, 4, false));
        return projects;
    }

    public static List<Notification> getNotifications() {
        List<Notification> notifications = new ArrayList<>();
        notifications.add(new Notification(1, "Arjun Sharma sent you a connection request", "connection", "Just now", false));
        notifications.add(new Notification(2, "You were invited to join 'AI Resume Screener'", "project", "10 minutes ago", false));
        notifications.add(new Notification(3, "Priya Nair accepted your connection request", "connection", "1 hour ago", true));
        notifications.add(new Notification(4, "Rohan Mehta posted a project matching your skills", "project", "3 hours ago", true));
        notifications.add(new Notification(5, "Your application to 'Campus Event App' was viewed", "project", "5 hours ago", true));
        notifications.add(new Notification(6, "Sneha Patel sent you a message", "message", "Yesterday", true));
        notifications.add(new Notification(7, "2 people viewed your profile today", "profile", "Yesterday", true));
        notifications.add(new Notification(8, "New project: Smart Attendance in your skill area", "project", "2 days ago", true));
        return notifications;
    }

    public static User getLoggedInUser() {
        return new User(0, "Avinash Krishna Nekkanti", "akchowdary21@gmail.com", "Cyber Security undergraduate at Amrita Vishwa Vidyapeetham with a strong interest in building secure, reliable, and scalable systems. Proficient in Python, C, and Java, with hands-on experience in academic research and practical security concepts.", "Cyber Security, Project Management, Java", "android.resource://com.skillbridge.app/drawable/avinash_krishna", 5, 500, true);
    }
}