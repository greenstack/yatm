package dj.yatm.model;

/**
 * Represents a base List Item.
 */
public interface IListItem {
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
    java.util.Date getCreation();

    /**
     * Retrieves the item's description.
     * @return The description of this item.
     */
    String getDescription();

    /**
     * Sets this item's description.
     * @param description The description of the list item.
     */
    void setDescription(String description);
}