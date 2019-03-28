package dj.yatm.model;

import java.util.Date;

/**
 * Represents a base List Item.
 */
public interface IListItem {
    /**
     * Retrieves the item's id.
     * @return the id of this item.
     */
    int getId();

    /**
     * Gets this item's priority.
     * @return The priority of this item.
     */
    int getPriority();

    /**
     * Sets the item's priority.
     * @param priority the priority of the item.
     */
    void setPriority(int priority);

    /**
     * Gets the name of this item.
     * @return The title of the item.
     */
    String getTitle();

    /**
     * Sets the title/name of this item.
     * @param title The name of the item.
     */
    void setTitle(String title);

    /**
     * Determines if this item has been completed.
     * @return True if the item considers itself complete.
     */
    boolean isComplete();

    /**
     * Gets the time that this item was created.
     * @return The time of the item's creation.
     */
    Date getCreation();

    /**
     * Sets the due date of this item.
     */
    void setDueDate(Date dueDate);

    /**
     * Gets the date this item should be completed by.
     * @return the due date of this item.
     */
    Date getDueDate();

    /**
     * Determines if this item is overdue.
     * @return True if the current time is after the due date.
     */
    boolean isLate();

    /**
     * Retrieves the item's category.
     * @return The category of this item.
     */
    String getCategory();

    /**
     * Sets this item's category.
     * @param category the category of the list item.
     */
    void setCategory(String category);

    /**
     * Causes the item to be deleted. Notifies observers of the deletion.
     */
    void delete();
}