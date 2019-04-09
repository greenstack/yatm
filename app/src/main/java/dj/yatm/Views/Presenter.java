package dj.yatm.Views;

import dj.yatm.model.ListItem;
import dj.yatm.model.ListItemEvent;
import dj.yatm.model.TaskListContract;
import dj.yatm.model.TaskListDbHelper;

public class Presenter {
    private TaskListContract contract;
    private TaskListDbHelper dbHelper;

    public Presenter(){
        this.contract = TaskListContract.getInstance();
        this.dbHelper = TaskListDbHelper.getInstance();
    }

    public ListItem rebuildTree(ListItem listItem){
        return this.dbHelper.buildTreeFromId(listItem.getId());
    }

    /**
     * Retrieves the entire tree of task items.
     * @return The entire tree.
     */
    public ListItem getTree() {
        // The root tree, which MUST not be deleted, has an id of 1.
        return this.dbHelper.buildTreeFromId(1);
    }


    public void updateTask(ListItem listItem){
        this.contract.Update(listItem,ListItemEvent.Update);
    }
}
