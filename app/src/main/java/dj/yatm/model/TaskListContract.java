package dj.yatm.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.io.Serializable;

/**
 * Created by Joseph Newman on 4/2/2019.
 */
public final class TaskListContract implements IListItemObserver, Serializable {
    private TaskListContract() {}

    static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + TaskEntry.TABLE_NAME + " (" +
                    TaskEntry._ID + " INTEGER PRIMARY KEY, " +
                    TaskEntry.COLUMN_NAME_TITLE + " TEXT, " +
                    TaskEntry.COLUMN_NAME_PARENT_ID + " INTEGER, " +
                    TaskEntry.COLUMN_NAME_PRIORITY + " INTEGER, " +
                    TaskEntry.COLUMN_NAME_CREATED_DATE + " TEXT, " +
                    TaskEntry.COLUMN_NAME_COMPLETED + " INTEGER DEFAULT 0" +
                    TaskEntry.COLUMN_NAME_DUE_DATE + " TEXT)";

    static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TaskEntry.TABLE_NAME;

    @Override
    public void Update(AbstractListItem sender, ListItemEvent event) {
        ListItem task = (ListItem)sender;
        switch (event) {
            case Create:
                long id = createTask(task);
                task.id = id;
                break;
            case Delete:
                deleteTask(task);
                break;
            case Update:
                updateTask(task);
                break;
            case None:
            default:
                break;
        }
    }

    private long createTask(ListItem li) {
        return TaskListDbHelper.get().createTask(li);
    }

    private void deleteTask(ListItem li) {
        TaskListDbHelper.get().deleteTask(li);
    }

    private void updateTask(ListItem li) {
        TaskListDbHelper.get().updateTask(li);
    }

    private static TaskListContract instance;
    public static TaskListContract get() {
        return instance;
    }

    public static TaskListContract init(Context context) {
        if (instance == null) {
            instance = new TaskListContract();
            TaskListDbHelper.init(context);
        }
        return instance;
    }

    private ContentValues buildValues(ListItem li) {
        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_NAME_TITLE, li.getTitle());
        values.put(TaskEntry.COLUMN_NAME_PRIORITY, li.getPriority());
        values.put(TaskEntry.COLUMN_NAME_PARENT_ID, li.parentId);
        values.put(TaskEntry.COLUMN_NAME_DUE_DATE, li.getDueDate().toString());
        values.put(TaskEntry.COLUMN_NAME_CREATED_DATE, li.getCreation().toString());
        return values;
    }

    public static class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_PRIORITY = "priority";
        public static final String COLUMN_NAME_CREATED_DATE = "created_date";
        public static final String COLUMN_NAME_COMPLETED = "complete";
        public static final String COLUMN_NAME_DUE_DATE = "due_date";
        public static final String COLUMN_NAME_PARENT_ID = "parent_id";
    }
}
