package dj.yatm.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

/**
 * Created by Joseph Newman on 4/3/2019.
 */
public class TaskListDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "TaskList.db";

    private SQLiteDatabase readableDB;
    private SQLiteDatabase writableDB;
    /**
     * List of all columns for the database to query on an item load.
     */
    private static final String[] FULL_PROJECTION = {
        TaskEntry._ID,
        TaskEntry.COLUMN_NAME_COMPLETED,
        TaskEntry.COLUMN_NAME_CREATED_DATE,
        TaskEntry.COLUMN_NAME_DUE_DATE,
        TaskEntry.COLUMN_NAME_PARENT_ID,
        TaskEntry.COLUMN_NAME_PRIORITY,
        TaskEntry.COLUMN_NAME_TITLE,
        TaskEntry.COLUMN_NAME_CATEGORY,
    };

    /**
     * String that builds the tables in the database.
     */
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + TaskEntry.TABLE_NAME + " (" +
                    TaskEntry._ID + " INTEGER PRIMARY KEY, " +
                    TaskEntry.COLUMN_NAME_TITLE + " TEXT, " +
                    TaskEntry.COLUMN_NAME_PARENT_ID + " INTEGER, " +
                    TaskEntry.COLUMN_NAME_PRIORITY + " INTEGER, " +
                    TaskEntry.COLUMN_NAME_CREATED_DATE + " INTEGER, " +
                    TaskEntry.COLUMN_NAME_CATEGORY + " TEXT, " +
                    TaskEntry.COLUMN_NAME_COMPLETED + " INTEGER DEFAULT 0, " +
                    TaskEntry.COLUMN_NAME_DUE_DATE + " TEXT)";

    /**
     * String that removes the table from the database.
     */
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TaskEntry.TABLE_NAME;

    private static TaskListDbHelper instance;
    public static void init(Context context) {
        instance = new TaskListDbHelper(context);
    }
    public static TaskListDbHelper getInstance(){
        return instance;
    }

    /**
     * Creates a new TaskListDbHelper based on the context.
     * @param context
     */
    private TaskListDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        writableDB = getWritableDatabase();
        readableDB = getReadableDatabase();
    }

    /**
     * Destroys and rebuilds the database tables.
     */
    public void totalReset() {
        writableDB.execSQL(SQL_DELETE_ENTRIES);
        writableDB.execSQL(SQL_CREATE_ENTRIES);
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
        writableDB.close();
        readableDB.close();
        instance = null;
    }

    private ContentValues buildValues(ListItem li) {
        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_NAME_TITLE, li.getTitle());
        values.put(TaskEntry.COLUMN_NAME_PRIORITY, li.getPriority());
        values.put(TaskEntry.COLUMN_NAME_PARENT_ID, li.parentId);
        values.put(TaskEntry.COLUMN_NAME_DUE_DATE,
                li.getDueDate() != null ? li.getDueDate().toEpochDay() : null);
        values.put(TaskEntry.COLUMN_NAME_CREATED_DATE, li.getCreation().toString());
        values.put(TaskEntry.COLUMN_NAME_COMPLETED, li.isComplete());
        values.put(TaskEntry.COLUMN_NAME_CATEGORY, li.getCategory());
        return values;
    }

    /**
     * Inserts the task into the database.
     * @param li the task to insert.
     * @return the id of the newly inserted item to the database.
     */
    long createTask(ListItem li) {
        Log.d("yatm", "Creating " + li.getTitle());
        //return 0;
        long id = writableDB.insert(TaskEntry.TABLE_NAME, null, buildValues(li));
        Log.d("yatm", li.getTitle() + " was created with id " + String.valueOf(id));
        return id;
    }

    /**
     * Updates the task in the database.
     * @param li the item to update.
     */
    void updateTask(ListItem li) {
        Log.d("yatm", "Updating " + li.getTitle() + ": parent id is " + li.parentId);
        writableDB.update(
                TaskEntry.TABLE_NAME, buildValues(li),
                TaskEntry._ID + " == ?",
                new String[] {String.valueOf(li.id)});
    }

    /**
     * Deletes the specified task from the database.
     * @param li the item to delete from the database.
     */
    void deleteTask(@org.jetbrains.annotations.NotNull ListItem li) {
        Log.d("yatm", "Delete");
        writableDB.delete(
                TaskEntry.TABLE_NAME,
                TaskEntry._ID + " == ?",
                new String[]{String.valueOf(li.id)});
    }

    /**
     * Gets the listItem from the database with the given id, unpopulated.
     * @param id the id of the listItem to load
     * @return the item from the database, if it exists.
     */
    public ListItem getItem(long id) {
        String selection = TaskEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        String sortOrder = TaskEntry._ID + " DESC";
        Cursor cursor = readableDB.query(
                TaskEntry.TABLE_NAME,
                FULL_PROJECTION,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        cursor.moveToNext();
        ListItem read = fromCursor(cursor);
        cursor.close();
        return read;
    }

    /**
     * Builds a tree from the specified id.
     * @param id the id of the listItem being read.
     * @return the tree of the item.
     */
    public ListItem buildTreeFromId(long id) {
        Log.d("yatm", "building tree for " + id);
        ListItem root = getItem(id);
        buildTree(root);
        return root;
    }

    /**
     * Takes a cursor and builds a ListItem from it.
     * @param cursor the cursor reading the SQLite database.
     * @return the ListItem from the cursor's data.
     */
    private ListItem fromCursor(Cursor cursor) throws CursorIndexOutOfBoundsException {
        Long epochDay = cursor.getLong(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_DUE_DATE));
        LocalDate date = null;
        if (epochDay != null && epochDay != 0)
            date = LocalDate.ofEpochDay(epochDay);

        ListItem read = new ListItem(
                cursor.getString(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_TITLE)),
                cursor.getInt(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_PRIORITY)),
                cursor.getString(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_CATEGORY)),
                date,
                false
        );
        long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(TaskEntry._ID));
        read.id = itemId;
        read.isComplete = cursor.getInt(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_COMPLETED)) == 1;
        read.parentId = cursor.getLong(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_PARENT_ID));
        return read;
    }

    /**
     * Inserts the tree into the parent. Assumes an empty parent.
     * @param parent the parent to build the tree around.
     */
    public void buildTree(ListItem parent) {
        String selection = TaskEntry.COLUMN_NAME_PARENT_ID + " = ?";
        String[] selectionArgs = { String.valueOf(parent.id) };

        String sortOrder = TaskEntry.COLUMN_NAME_PARENT_ID + " DESC";
        Cursor cursor = readableDB.query(
                TaskEntry.TABLE_NAME,
                FULL_PROJECTION,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        while (cursor.moveToNext()) {
            try {
                ListItem child = fromCursor(cursor);
                buildTree(child);
                parent.addItem(child, false);
            } catch (CursorIndexOutOfBoundsException kyubei) {
                break;
            }
        }
        cursor.close();
    }

    /**
     * Contains the table and column names of the database used by YATM.
     */
    public static class TaskEntry implements BaseColumns {
        static final String TABLE_NAME = "tasks";
        static final String COLUMN_NAME_TITLE = "title";
        static final String COLUMN_NAME_PRIORITY = "priority";
        static final String COLUMN_NAME_CREATED_DATE = "created_date";
        static final String COLUMN_NAME_COMPLETED = "complete";
        static final String COLUMN_NAME_DUE_DATE = "due_date";
        static final String COLUMN_NAME_PARENT_ID = "parent_id";
        static final String COLUMN_NAME_CATEGORY = "category";
    }
}