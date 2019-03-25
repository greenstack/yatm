package dj.yatm.model;

/**
 * Created by Joseph on 3/21/2019.
 */
public class TodoItem extends AbstractListItem {

    private boolean complete = false;

    /**
     * Determines if this item is complete.
     * @return true if the item is complete.
     */
    @Override
    public boolean isComplete() {
        return complete;
    }

    /**
     * Marks this list item as complete.
     * @param complete whether or not this item is complete.
     */
    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    /**
     * Reverses the completed flag on this item.
     */
    public void toggleComplete() {
        complete = !complete;
    }

    public TodoItem(int id, Iterable<IListItemObserver> observers) {
        super(id, observers);
    }
}
