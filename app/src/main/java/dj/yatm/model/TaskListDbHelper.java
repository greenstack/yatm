package dj.yatm.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Joseph Newman on 4/3/2019.
 */
public class TaskListDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TaskList.db";

    private SQLiteDatabase db;

    private static TaskListDbHelper instance;

    private TaskListDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase();
    }

    public static TaskListDbHelper init(Context context) {
        if (instance != null) return instance;
        instance = new TaskListDbHelper(context);
        return instance;
    }

    public static TaskListDbHelper get() {
        return instance;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TaskListContract.SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TaskListContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    long createTask(ListItem li) {
        Log.d("yatm", "Create");
        return 0;
        //return helper.getDb().insert(TaskEntry.TABLE_NAME, null, buildValues(li));
    }

    void updateTask(ListItem li) {
        Log.d("yatm", "Update");
        /*helper.getDb().update(
                TaskEntry.TABLE_NAME, buildValues(li),
                TaskEntry._ID + " LIKE ?",
                new String[] {String.valueOf(li.id)});*/
    }

    void deleteTask(@org.jetbrains.annotations.NotNull ListItem li) {
        Log.d("yatm", "Delete");
        /*helper.getDb().delete(
                TaskEntry.TABLE_NAME,
                TaskEntry._ID + " == ?",
                new String[]{String.valueOf(li.id)});*/
    }
}