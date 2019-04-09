package dj.yatm.model;

/**
 * Created by Joseph Newman on 4/2/2019.
 */
public final class TaskListContract implements IListItemObserver {
    private TaskListContract() {}

    private static TaskListContract instance;
    public static TaskListContract getInstance() {
        if (instance == null) instance = new TaskListContract();
        return instance;
    }

    @Override
    public void Update(AbstractListItem sender, ListItemEvent event) {
        ListItem task = (ListItem)sender;
        switch (event) {
            case Create:
                task.id = createTask(task);
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
        return TaskListDbHelper.getInstance().createTask(li);
    }

    private void deleteTask(ListItem li) {
        TaskListDbHelper.getInstance().deleteTask(li);
    }

    private void updateTask(ListItem li) {
        TaskListDbHelper.getInstance().updateTask(li);
    }
}
