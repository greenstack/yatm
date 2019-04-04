package dj.yatm.Views;

import java.util.List;

import dj.yatm.model.ListItem;
import dj.yatm.model.ListItemEvent;
import dj.yatm.model.TaskListContract;
import dj.yatm.model.TaskListDbHelper;

public class Presenter {
    private TaskListContract contract;
    private TaskListDbHelper dbHelper;

    public Presenter(){
        this.contract = TaskListContract.get();
        this.dbHelper = TaskListDbHelper.getInstance();
    }

    public ListItem rebuildTree(ListItem listItem){
        return this.dbHelper.buildTreeFromId(listItem.getId());

    }


    public void updateTask(ListItem listItem){
        this.contract.Update(listItem,ListItemEvent.Update);
    }

    public void createTask(ListItem listItem){
        listItem.addObserver(this.contract);
        this.contract.Update(listItem, ListItemEvent.Create);
    }


}
