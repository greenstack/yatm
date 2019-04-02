package dj.yatm.model;

import java.util.ArrayList;
//import androidx.room.Entity;
//import androidx.room.Ignore;

/**
 * Created by Joseph on 3/21/2019.
 */
//@Entity(tableName = "tasks")
public class ListItem extends AbstractListItem {
    //@Ignore
    ArrayList<AbstractListItem> subTasks;

    public ListItem() {
        super();
        subTasks = new ArrayList<>();
    }

    public ArrayList<ListItem> getSubTasks() {
        ArrayList<ListItem> list = new ArrayList<>();
        for (int i = 0; i < this.subTasks.size(); i++){
            list.add((ListItem)this.subTasks.get(i));
        }
        return list;
    }

    public ListItem(IListItemObserver observer) {
        super(observer);
        subTasks = new ArrayList<>();
    }

    /**
     * Creates a new instance of ListItem.
     */
    public ListItem(Iterable<IListItemObserver> observers) {
        super(observers);
        subTasks = new ArrayList<>();
    }

    /**
     * Determines if all items in this list are complete or not.
     * @return True if all subtasks in this list are complete.
     */
    @Override
    public boolean isComplete() {
        for (AbstractListItem ali:
             subTasks) {
            if (!ali.isComplete()) return false;
        }
        return true;
    }

    /**
     * Inserts a new item to this sublist.
     * @param item the item to be added.
     */
    public void addItem(AbstractListItem item) {
        addItem(item, true);
    }

    void addItem(AbstractListItem item, boolean notify) {
        item.parentId = id;
        item.notify(notify ? ListItemEvent.Update : ListItemEvent.None);
        subTasks.add(item);
    }

    /**
     * Removes the item from the list and the database.
     * @param item the item to be removed.
     * @return true if the item was successfully removed.
     */
    public boolean removeItem(AbstractListItem item) {
        item.delete();
        return subTasks.remove(item);
    }

    /**
     * Sets all items to the specified completeness.
     * @param completeness The completeness state of the item.
     * @param recurse Pass in true to complete sublists as well.
     */
    public void setCompletenessForAll(boolean completeness, boolean recurse) {
        for (AbstractListItem ali :
                subTasks) {
            if (ali instanceof ListItem && recurse) ((ListItem) ali).setCompletenessForAll(completeness,true);
            else continue;
        }
    }

    /**
     * Determines how many subtasks there are. Does not count recursively.
     * @return the number of subtasks.
     */
    public int countItems() {
        return subTasks.size();
    }

    /**
     * Determines, recursively, how many subtasks are in this list.
     * @return The total number of items, including subtasks.
     */
    public int countAllItems() {
        int count = 0;
        for (AbstractListItem item :
                subTasks) {
            ListItem casted = (ListItem)item;
            if (casted.countItems() == 0)
                count++;
            else
                count += casted.countAllItems();
        }
        return count;
    }

    public AbstractListItem getAt(int index) {
        return subTasks.get(index);
    }
}
