package dj.yatm.model;

import java.util.Date;

import static java.time.Instant.now;

public abstract class AbstractListItem implements IListItem {
    private String title;
    protected String description;
    private Date creation;
    private int priority;

    public AbstractListItem() {
        this.creation = Date.from(now());
    }

    @Override
    final public void setDescription(String description) {
        this.description = description;
    }

    @Override
    final public String getDescription() {
        return description;
    }

    @Override
    final public void setTitle(String title) {
        this.title = title;
    }

    @Override
    final public String getTitle() {
        return title;
    }

    @Override
    final public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    final public int getPriority() {
        return priority;
    }

    @Override
    final public Date getCreation() {
        return creation;
    }
}