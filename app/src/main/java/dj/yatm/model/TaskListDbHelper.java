package dj.yatm.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.List;

/**
 * Created by Joseph Newman on 4/3/2019.
 */
public class TaskListDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "TaskList.db";

    static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + TaskEntry.TABLE_NAME + " (" +
                    TaskEntry._ID + " INTEGER PRIMARY KEY, " +
                    TaskEntry.COLUMN_NAME_TITLE + " TEXT, " +
                    TaskEntry.COLUMN_NAME_PARENT_ID + " INTEGER, " +
                    TaskEntry.COLUMN_NAME_PRIORITY + " INTEGER, " +
                    TaskEntry.COLUMN_NAME_CREATED_DATE + " TEXT, " +
                    TaskEntry.COLUMN_NAME_CATEGORY + " TEXT, " +
                    TaskEntry.COLUMN_NAME_COMPLETED + " INTEGER DEFAULT 0, " +
                    TaskEntry.COLUMN_NAME_DUE_DATE + " TEXT)";

    static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TaskEntry.TABLE_NAME;

    private static TaskListDbHelper instance;
    public static TaskListDbHelper init(Context context) {
        instance = new TaskListDbHelper(context);
        return instance;
    }
    public static TaskListDbHelper getInstance(){
        return instance;
    }

    private TaskListDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void totalReset() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void close() {
        // TODO: REMOVE THESE LINES!
        // Reset the database on each relaunch of the app.
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
        db.close();
    }

    private ContentValues buildValues(ListItem li) {
        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_NAME_TITLE, li.getTitle());
        values.put(TaskEntry.COLUMN_NAME_PRIORITY, li.getPriority());
        values.put(TaskEntry.COLUMN_NAME_PARENT_ID, li.parentId);
        values.put(TaskEntry.COLUMN_NAME_DUE_DATE,
                li.getDueDate() != null ? li.getDueDate().toString() : null);
        values.put(TaskEntry.COLUMN_NAME_CREATED_DATE, li.getCreation().toString());
        return values;
    }

    long createTask(ListItem li) {
        Log.d("yatm", "Creating " + li.getTitle());
        //return 0;
        long id = this.getWritableDatabase().insert(TaskEntry.TABLE_NAME, null, buildValues(li));
        Log.d("yatm", li.getTitle() + " was created with id " + String.valueOf(id));
        return id;
    }

    void updateTask(ListItem li) {
        Log.d("yatm", "Updating " + li.getTitle() + ": parent id is " + li.parentId);
        this.getWritableDatabase().update(
                TaskEntry.TABLE_NAME, buildValues(li),
                TaskEntry._ID + " LIKE ?",
                new String[] {String.valueOf(li.id)});
    }

    void deleteTask(@org.jetbrains.annotations.NotNull ListItem li) {
        Log.d("yatm", "Delete");
        this.getWritableDatabase().delete(
                TaskEntry.TABLE_NAME,
                TaskEntry._ID + " == ?",
                new String[]{String.valueOf(li.id)});
    }

    public ListItem getItem(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                TaskEntry._ID,
                TaskEntry.COLUMN_NAME_COMPLETED,
                TaskEntry.COLUMN_NAME_CREATED_DATE,
                TaskEntry.COLUMN_NAME_DUE_DATE,
                TaskEntry.COLUMN_NAME_PARENT_ID,
                TaskEntry.COLUMN_NAME_PRIORITY,
                TaskEntry.COLUMN_NAME_TITLE,
                TaskEntry.COLUMN_NAME_CATEGORY
        };

        String selection = TaskEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        String sortOrder = TaskEntry._ID + " DESC";
        Cursor cursor = db.query(
                TaskEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        cursor.moveToNext();
        long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(TaskEntry._ID));
        return fromCursor(cursor);
    }

    private ListItem fromCursor(Cursor cursor) {
        ListItem read = new ListItem();
        long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(TaskEntry._ID));
        read.id = itemId;
        read.isComplete = cursor.getInt(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_COMPLETED)) == 1;
        read.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_TITLE)));
        read.parentId = cursor.getLong(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_PARENT_ID));
        read.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_CATEGORY)));
        return read;
    }

    public void buildTree(ListItem parent) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                TaskEntry._ID,
                TaskEntry.COLUMN_NAME_COMPLETED,
                TaskEntry.COLUMN_NAME_CREATED_DATE,
                TaskEntry.COLUMN_NAME_DUE_DATE,
                TaskEntry.COLUMN_NAME_PARENT_ID,
                TaskEntry.COLUMN_NAME_PRIORITY,
                TaskEntry.COLUMN_NAME_TITLE,
                TaskEntry.COLUMN_NAME_CATEGORY
        };

        String selection = TaskEntry.COLUMN_NAME_PARENT_ID + " = ?";
        String[] selectionArgs = { String.valueOf(parent.id) };

        String sortOrder = TaskEntry.COLUMN_NAME_PARENT_ID + " DESC";
        Cursor cursor = db.query(
                TaskEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        while (cursor.moveToNext()) {
            ListItem child = fromCursor(cursor);
            buildTree(child);
            parent.addItem(child, false);
        }
    }

    public static class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_PRIORITY = "priority";
        public static final String COLUMN_NAME_CREATED_DATE = "created_date";
        public static final String COLUMN_NAME_COMPLETED = "complete";
        public static final String COLUMN_NAME_DUE_DATE = "due_date";
        public static final String COLUMN_NAME_PARENT_ID = "parent_id";
        public static final String COLUMN_NAME_CATEGORY = "category";
    }
}