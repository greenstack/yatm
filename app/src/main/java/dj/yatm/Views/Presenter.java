package dj.yatm.Views;

import dj.yatm.model.ListItem;
import dj.yatm.model.ListItemEvent;
import dj.yatm.model.TaskListContract;

public class Presenter {
    private TaskListContract contract;

    public Presenter(){
        this.contract = TaskListContract.get();
    }


    public void updateTask(ListItem listItem){
        this.contract.Update(listItem,ListItemEvent.Update);
    }

    public void createTask(ListItem listItem){
        this.contract.Update(listItem, ListItemEvent.Create);
    }


}
