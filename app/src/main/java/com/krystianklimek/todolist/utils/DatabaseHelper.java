package com.krystianklimek.todolist.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.krystianklimek.todolist.model.TaskModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by: Krystian Klimek
 * Date: 19.06.2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "tasksInfo";

    // Contacts table name
    private static final String TABLE_TASKS = "tasks";

    // Shops Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_URL_TO_ICON = "url_to_icon";
    private static final String KEY_CREATED = "created";
    private static final String KEY_TIME_END = "time_end";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_URL_TO_ICON + " TEXT,"
                + KEY_CREATED + " TEXT,"
                + KEY_TIME_END + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        // Creating tables again
        onCreate(db);
    }

    // Adding new task
    public void addTask(TaskModel task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, task.getTitle());
        values.put(KEY_DESCRIPTION, task.getDescription());
        values.put(KEY_URL_TO_ICON, task.getUrl_to_icon());
        values.put(KEY_CREATED, task.getCreated());
        values.put(KEY_TIME_END, task.getTime_end());

        // Inserting Row
        db.insert(TABLE_TASKS, null, values);
        db.close(); // Closing database connection
    }

    // Getting one task
    public TaskModel getTask(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_TASKS,
                new String[]{ KEY_ID, KEY_TITLE, KEY_DESCRIPTION, KEY_URL_TO_ICON, KEY_CREATED, KEY_TIME_END},
                KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        TaskModel task = new TaskModel(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
        // return shop
        return task;
    }

    // Getting All Tasks
    public List<TaskModel> getAllTasks() {
        List<TaskModel> taskList = new ArrayList<TaskModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TaskModel task = new TaskModel();
                task.setId(Integer.parseInt(cursor.getString(0)));
                task.setTitle(cursor.getString(1));
                task.setDescription(cursor.getString(2));
                task.setUrl_to_icon(cursor.getString(3));
                task.setCreated(cursor.getString(4));
                task.setTime_end(cursor.getString(5));
                // Adding task to list
                taskList.add(task);
            } while (cursor.moveToNext());
        }

        // return task list
        return taskList;
    }

    // Getting task Count
    public int getTasksCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TASKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    // Updating a task
    public int updateTask(TaskModel task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, task.getTitle());
        values.put(KEY_DESCRIPTION, task.getDescription());
        values.put(KEY_URL_TO_ICON, task.getUrl_to_icon());
        values.put(KEY_CREATED, task.getCreated());
        values.put(KEY_TIME_END, task.getTime_end());

        // updating row
        return db.update(TABLE_TASKS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(task.getId())});
    }

    // Deleting a shop
    public void deleteTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

    public static List<TaskModel> sortTasks(List<TaskModel> taskModels) {
        Collections.sort(taskModels, new Comparator<TaskModel>() {
            @Override
            public int compare(TaskModel o1, TaskModel o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });

        return taskModels;
    }
}
