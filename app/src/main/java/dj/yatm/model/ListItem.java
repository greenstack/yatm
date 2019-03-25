package dj.yatm.model;

import java.util.ArrayList;

/**
 * Created by Joseph on 3/21/2019.
 */
public class ListItem extends AbstractListItem {

    ArrayList<AbstractListItem> subTasks;

    /**
     * Creates a new instance of ListItem.
     */
    public ListItem(int id, Iterable<IListItemObserver> observers) {
        super(id, observers);
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
        item.parentId = id;
        subTasks.add(item);
    }

    /**
     * Removes the item from the list.
     * @param item the item to be removed.
     * @return true if the item was successfully removed.
     */
    public boolean removeItem(AbstractListItem item) {
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
            else ((TodoItem)ali).setComplete(completeness);
        }
    }

    /**
     * Gets the list item at the specific index.
     * @param index The index of the item to grab.
     * @return the item at the index.
     */
    public AbstractListItem getItemByIndex(int index) {
        return subTasks.get(index);
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
            if (item instanceof TodoItem)
                count++;
            else if (item instanceof ListItem)
                count += ((ListItem)item).countAllItems();
        }
        return count;
    }
}
