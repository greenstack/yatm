package dj.yatm.model;


import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;

//import androidx.room.ColumnInfo;
//import androidx.room.Entity;
//import androidx.room.Ignore;
//import androidx.room.PrimaryKey;

import static java.time.Instant.now;

/**
 * An abstract implementation of the IListItem.
 */
public abstract class AbstractListItem implements IListItem, Serializable {
    //@PrimaryKey
    long id;
    //@ColumnInfo(name = "title")
    private String title;
    //@ColumnInfo(name = "category")
    private String category;
    //@ColumnInfo(name = "creation_date")
    private Date creation;
    //@ColumnInfo(name = "due_date")
    private Date dueDate;
    //@ColumnInfo(name = "priority")
    private int priority;
    //@ColumnInfo(name = "parent_id")
    protected Long parentId;

    /**
     * A list of observers looking at this item.
     */
    //@Ignore
    HashSet<IListItemObserver> observers;

    AbstractListItem() {
        creation = Date.from(now());
        observers = new HashSet<>();
    }

    AbstractListItem(IListItemObserver observer) {
        creation = Date.from(now());
        observers = new HashSet<>();
        if (observer != null)
            observers.add(observer);
    }

    AbstractListItem(Iterable<IListItemObserver> observers) {
        creation = Date.from(now());
        this.observers = new HashSet<>();
        for (IListItemObserver observer :
                observers) {
            this.observers.add(observer);
        }
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

    final public void addObserver(IListItemObserver observer) {
        observers.add(observer);
    }

    @Override
    final public void setCategory(String category) {
        this.category = category;
        notify(ListItemEvent.Update);
    }

    @Override
    final public String getCategory() {
        return category;
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
    final public Date getCreation() {
        return creation;
    }

    final void setCreation(Date creation) {this.creation = creation;}

    @Override
    final public long getId() {
        return id;
    }

    @Override
    public final void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public final Date getDueDate() {
        return this.dueDate;
    }

    @Override
    public final boolean isLate() {
        return dueDate.before(Date.from(now()));
    }
}