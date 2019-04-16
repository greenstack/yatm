package dj.yatm.model;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;

/**
 * An abstract implementation of the IListItem.
 */
public abstract class AbstractListItem implements IListItem, Serializable {
    long id = 0;
    private String title;
    private String category;
    private LocalDate creation = LocalDate.now();
    private LocalDate dueDate;
    private int priority;
    Long parentId;

    /**
     * A list of observers looking at this item.
     */
    private transient HashSet<IListItemObserver> observers;

    private AbstractListItem(IListItemObserver observer) {
        observers = new HashSet<>();
        observers.add(observer);
    }

    protected AbstractListItem(String title, int priority, String category, LocalDate dueDate) {
        this(TaskListContract.getInstance());
        this.title = title;
        this.priority = priority;
        this.category = category;
        this.dueDate = dueDate;
    }

    @Override
    public void delete() {
        if (observers == null) {
            observers = new HashSet<>();
            observers.add(TaskListContract.getInstance());
        }
        notify(ListItemEvent.Delete);
    }

    final protected void notify(ListItemEvent event) {
        for (IListItemObserver observer :
                observers) {
            observer.Update(this, event);
        }
    }

    final public void addObserver(IListItemObserver observer) {
        // This can happen if the Item's been deserialized.
        if (observers == null) observers = new HashSet<>();
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
    final public LocalDate getCreation() {
        return creation;
    }

    final void setCreation(LocalDate creation) {this.creation = creation;}

    @Override
    final public long getId() {
        return id;
    }

    @Override
    public final void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        notify(ListItemEvent.Update);
    }

    @Override
    public final LocalDate getDueDate() {
        return this.dueDate;
    }

    @Override
    public final boolean isLate() {
        return false;//dueDate >LocalDate.now();
    }

    public static final Comparator<AbstractListItem> TITLE_ORDER = new Comparator<AbstractListItem>() {
        @Override
        public int compare(AbstractListItem abstractListItem, AbstractListItem t1) {
            return abstractListItem.title.compareTo(t1.title);
        }
    };

    public static final Comparator<AbstractListItem> PRIORITY_ORDER = new Comparator<AbstractListItem>() {
        @Override
        public int compare(AbstractListItem abstractListItem, AbstractListItem t1) {
            int order = Integer.compare(abstractListItem.priority, t1.priority);
            return compareWrapper(order, abstractListItem, t1);
        }
    };

    public static final Comparator<AbstractListItem> DUE_DATE_ORDER = new Comparator<AbstractListItem>() {
        @Override
        public int compare(AbstractListItem abstractListItem, AbstractListItem t1) {
            if (abstractListItem.dueDate == null && t1.dueDate == null)
                return 0;
            else if (abstractListItem.dueDate == null)
                return 1;
            else if (t1.dueDate == null)
                return -1;
            int order = abstractListItem.dueDate.compareTo(t1.dueDate);
            return compareWrapper(order, abstractListItem, t1);
        }
    };

    public static final Comparator<AbstractListItem> CREATED_ORDER = new Comparator<AbstractListItem>() {
        @Override
        public int compare(AbstractListItem abstractListItem, AbstractListItem t1) {
            return abstractListItem.creation.compareTo(t1.creation);
        }
    };

    /**
     * Sorts by category.
     */
    public static final Comparator<AbstractListItem> CATEGORY_ORDER = new Comparator<AbstractListItem>() {
        @Override
        public int compare(AbstractListItem abstractListItem, AbstractListItem t1) {
            int order = abstractListItem.category.compareTo(t1.category);
            return compareWrapper(order, abstractListItem, t1);
        }
    };

    /**
     * Sorts first by completion, then by title.
     */
    public static final Comparator<AbstractListItem> COMPLETED_ORDER = new Comparator<AbstractListItem>() {
        @Override
        public int compare(AbstractListItem abstractListItem, AbstractListItem t1) {
            int completenessComparison = Boolean.compare(abstractListItem.isComplete(), t1.isComplete());
            return compareWrapper(completenessComparison, abstractListItem, t1);
        }
    };

    private static int compareWrapper(int order, AbstractListItem abstractListItem, AbstractListItem t1) {
        return order == 0 ? TITLE_ORDER.compare(abstractListItem, t1) : order;
    }
}