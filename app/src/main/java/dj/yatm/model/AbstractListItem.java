package dj.yatm.model;


import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;

import static java.time.Instant.now;

/**
 * An abstract implementation of the IListItem.
 */
public abstract class AbstractListItem implements IListItem, Serializable {
    long id = 0;
    private String title;
    private String category;
    private Date creation = Date.from(now());
    private Date dueDate;
    private int priority;
    Long parentId;

    /**
     * A list of observers looking at this item.
     */
    private transient HashSet<IListItemObserver> observers;

    AbstractListItem(IListItemObserver observer) {
        observers = new HashSet<>();
        observers.add(observer);
    }

    protected AbstractListItem(String title, int priority, String category, Date dueDate) {
        this(TaskListContract.getInstance());
        this.title = title;
        this.priority = priority;
        this.category = category;
        this.dueDate = dueDate;
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