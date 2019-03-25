package dj.yatm.model;

import android.support.annotation.NonNull;

import java.util.Date;
import java.util.HashSet;

import static java.time.Instant.now;

/**
 * An abstract implementation of the IListItem.
 */
public abstract class AbstractListItem implements IListItem {
    int id;
    private String title;
    private String description;
    @NonNull
    private Date creation;
    private int priority;

    /**
     * A list of observers looking at this item.
     */
    HashSet<IListItemObserver> observers;

    protected int parentId;

    AbstractListItem(int id, Iterable<IListItemObserver> observers) {
        creation = Date.from(now());
        this.observers = new HashSet<>();
        for (IListItemObserver observer :
                observers) {
            this.observers.add(observer);
        }
        this.id = id;
        notify(ListItemEvent.Create);
    }

    @Override
    public void delete() {
        notify(ListItemEvent.Delete);
    }

    final protected void notify(ListItemEvent event) {
        for (IListItemObserver observer :
                observers) {
            observer.Update(this, event);
        }
    }

    @Override
    final public void setDescription(String description) {
        this.description = description;
        notify(ListItemEvent.Update);
    }

    @Override
    final public String getDescription() {
        return description;
    }

    @Override
    final public void setTitle(String title) {
        this.title = title;
        notify(ListItemEvent.Update);
    }

    @Override
    final public String getTitle() {
        return title;
    }

    @Override
    final public void setPriority(int priority) {
        this.priority = priority;
        notify(ListItemEvent.Update);
    }

    @Override
    final public int getPriority() {
        return priority;
    }

    @Override
    @NonNull
    final public Date getCreation() {
        return creation;
    }

    @Override
    final public int getId() {
        return id;
    }

    /**
     * Sets this item's id.
     * @param id
     */
    final void setId(int id) {
        this.id = id;
        notify(ListItemEvent.Update);
    }
}