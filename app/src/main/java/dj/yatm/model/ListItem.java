package dj.yatm.model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Joseph on 3/21/2019.
 */
public class ListItem extends AbstractListItem {
    ArrayList<AbstractListItem> subTasks;

    boolean isComplete = false;

    public ArrayList<ListItem> getSubTasks() {
        ArrayList<ListItem> list = new ArrayList<>();
        for (int i = 0; i < this.subTasks.size(); i++){
            list.add((ListItem)this.subTasks.get(i));
        }
        return list;
    }

    public ListItem(String title, int priority, String category, Date dueDate) {
        this(title, priority, category, dueDate, true);
    }

    public ListItem(String title, int priority, String category, Date dueDate, boolean notify) {
        super(TaskListContract.getInstance());
        setTitle(title);
        setPriority(priority);
        setCategory(category);
        setDueDate(dueDate);
        if (notify) notify(ListItemEvent.Create);
        subTasks = new ArrayList<>();
    }

    public void notifyOfCreation() {
        notify(ListItemEvent.Create);
    }

    /**
     * Determines if all items in this list are complete or not.
     * @return True if all subtasks in this list are complete.
     */
    @Override
    public boolean isComplete() {
        if (subTasks.size() == 0) return isComplete;
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

    public void addItem(AbstractListItem item, boolean notify) {
        item.parentId = id;
        if (notify) item.notify(ListItemEvent.Update);
        subTasks.add(item);
    }

    /**
     * Removes the item from the list and the database.
     * @param item the item to be removed.
     * @return true if the item was successfully removed.
     */
    public boolean removeItem(AbstractListItem item) {
        boolean result = subTasks.remove(item);
        if (result) item.delete();
        return result;
    }

    /**
     * Sets all items to the specified completeness.
     * @param completeness The completeness state of the item.
     * @param recurse Pass in true to complete sublists as well.
     */
    public void setCompletenessForAll(boolean completeness, boolean recurse) {
        if (subTasks.size() == 0) {
            isComplete = completeness;
            notify(ListItemEvent.Update);
        }
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

    @NotNull
    @Override
    public String toString() {
        return getTitle() + "; id: " + id + "; parent_id: " + parentId;
    }

    public AbstractListItem getAt(int index) {
        return subTasks.get(index);
    }
}
