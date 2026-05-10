/*
 * SkillBridge Android Application
 * Copyright (c) 2026
 * Developed by:
 * Nekkanti Venkata Avinash Krishna
 * Kommana Dheeraj
 *
 * Unauthorized academic re-submission or commercial reuse is prohibited.
 */
package com.skillbridge.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
    private static final String PREF_NAME = "SkillBridgePrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_PASSWORD = "userPassword";
    private static final String KEY_USER_BIO = "userBio";
    private static final String KEY_USER_SKILLS = "userSkills";
    private static final String KEY_PROFILE_URI = "profileUri";

    private static SharedPreferencesManager instance;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    private SharedPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static synchronized SharedPreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesManager(context.getApplicationContext());
        }
        return instance;
    }

    public void setLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void saveUserName(String name) {
        editor.putString(KEY_USER_NAME, name);
        editor.apply();
    }

    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, "Avinash");
    }

    public void saveUserEmail(String email) {
        editor.putString(KEY_USER_EMAIL, email);
        editor.apply();
    }

    public String getUserEmail() {
        return sharedPreferences.getString(KEY_USER_EMAIL, "akc@gmail.com");
    }

    public void saveUserPassword(String password) {
        editor.putString(KEY_USER_PASSWORD, password);
        editor.apply();
    }

    public String getUserPassword() {
        return sharedPreferences.getString(KEY_USER_PASSWORD, "");
    }

    public void saveUserBio(String bio) {
        editor.putString(KEY_USER_BIO, bio);
        editor.apply();
    }

    public String getUserBio() {
        return sharedPreferences.getString(KEY_USER_BIO, "Full-stack developer and project enthusiast.");
    }

    public void saveUserSkills(String skills) {
        editor.putString(KEY_USER_SKILLS, skills);
        editor.apply();
    }

    public String getUserSkills() {
        return sharedPreferences.getString(KEY_USER_SKILLS, "Java, Python, SQL, DBMS");
    }

    public void saveProfileImageUri(String uri) {
        editor.putString(KEY_PROFILE_URI, uri);
        editor.apply();
    }

    public String getProfileImageUri() {
        return sharedPreferences.getString(KEY_PROFILE_URI, null);
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }
}