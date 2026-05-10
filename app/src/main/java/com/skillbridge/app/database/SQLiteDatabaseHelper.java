/*
 * SkillBridge Android Application
 * Copyright (c) 2026
 * Developed by:
 * Nekkanti Venkata Avinash Krishna
 * Kommana Dheeraj
 *
 * Unauthorized academic re-submission or commercial reuse is prohibited.
 */
package com.skillbridge.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.skillbridge.app.model.Project;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SQLiteDatabaseHelper extends SQLiteOpenHelper {

    private static SQLiteDatabaseHelper instance;

    private static final String DATABASE_NAME = "skillbridge.db";
    private static final int DATABASE_VERSION = 3;

    private static final String TABLE_POSTED_PROJECTS = "posted_projects";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_SKILLS_REQUIRED = "skills_required";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_TEAM_SIZE = "team_size";
    private static final String COLUMN_TIME_POSTED = "time_posted";

    private static final String TABLE_CONNECTIONS = "connections";
    private static final String COLUMN_CONN_ID = "conn_id";
    private static final String COLUMN_USER_NAME = "user_name";

    private SQLiteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized SQLiteDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SQLiteDatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_POSTED_PROJECTS_TABLE = "CREATE TABLE " + TABLE_POSTED_PROJECTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_SKILLS_REQUIRED + " TEXT,"
                + COLUMN_LOCATION + " TEXT,"
                + COLUMN_TEAM_SIZE + " INTEGER,"
                + COLUMN_TIME_POSTED + " TEXT" + ")";
        db.execSQL(CREATE_POSTED_PROJECTS_TABLE);

        String CREATE_CONNECTIONS_TABLE = "CREATE TABLE " + TABLE_CONNECTIONS + "("
                + COLUMN_CONN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_NAME + " TEXT UNIQUE" + ")";
        db.execSQL(CREATE_CONNECTIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CONNECTIONS + "("
                    + COLUMN_CONN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_USER_NAME + " TEXT UNIQUE" + ")");
        } else if (oldVersion < 3) {
             db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CONNECTIONS + "("
                    + COLUMN_CONN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_USER_NAME + " TEXT UNIQUE" + ")");
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CONNECTIONS + "("
                + COLUMN_CONN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_NAME + " TEXT UNIQUE" + ")");
    }

    public long insertProject(String title, String description, String skills, String location, int teamSize) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_SKILLS_REQUIRED, skills);
        values.put(COLUMN_LOCATION, location);
        values.put(COLUMN_TEAM_SIZE, teamSize);
        String timePosted = new SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(new Date());
        values.put(COLUMN_TIME_POSTED, timePosted);

        return db.insert(TABLE_POSTED_PROJECTS, null, values);
    }

    public List<Project> getAllPostedProjects() {
        List<Project> projectList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_POSTED_PROJECTS + " ORDER BY " + COLUMN_ID + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Project project = new Project(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SKILLS_REQUIRED)),
                        "You",
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME_POSTED)),
                        0.0, 0.0,
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TEAM_SIZE)),
                        false
                );
                projectList.add(project);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return projectList;
    }

    public int deleteProject(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_POSTED_PROJECTS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public int getProjectCount() {
        String countQuery = "SELECT * FROM " + TABLE_POSTED_PROJECTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void addConnection(String userName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, userName);
        db.insertWithOnConflict(TABLE_CONNECTIONS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public void removeConnection(String userName) {
        if (isAuthor(userName)) return;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONNECTIONS, COLUMN_USER_NAME + " = ?", new String[]{userName});
    }

    public int getConnectionCount() {
        try {
            String countQuery = "SELECT * FROM " + TABLE_CONNECTIONS;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            int count = cursor.getCount();
            cursor.close();
            // Include authors in the count
            return count + 2;
        } catch (Exception e) {
            return 2; // Always at least authors
        }
    }

    public boolean isConnected(String userName) {
        if (isAuthor(userName)) return true;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_CONNECTIONS, null, COLUMN_USER_NAME + " = ?", new String[]{userName}, null, null, null);
            boolean exists = cursor.getCount() > 0;
            cursor.close();
            return exists;
        } catch (Exception e) {
            return false;
        }
    }
    
    private boolean isAuthor(String userName) {
        return "Avinash Krishna Nekkanti".equals(userName) || "K. Dheeraj".equals(userName);
    }

    public int updateProject(int id, String title, String description, String skills) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_SKILLS_REQUIRED, skills);

        return db.update(TABLE_POSTED_PROJECTS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }
}