package dj.yatm.model;

import java.util.List;

/**
 * Created by Joseph on 3/21/2019.
 *
 * Implements the Observer pattern to keep the database synced with the data within the app itself.
 */
public interface IDatabaseAccessObject extends IListItemObserver{
    /**
     * Gets the item with the specified id.
     * @param id The id of the item.
     * @return the item from the database with the specific id.
     */
    IListItem getItem(int id);

    /**
     * Loads in all items. The items in the list are the top-level items, with no parent.
     * @return all the items in the database, organized the way they should be.
     */
    List<IListItem> loadAll();

    /**
     * Adds the item to the database.
     * @param item the item to add to the database.
     */
    void createItem(IListItem item);

    /**
     * Updates the item to correspond to this item.
     * @param item the item to update in the database.
     */
    void updateItem(IListItem item);

    /**
     * Removes the specified item from the database.
     * @param item the item to remove.
     */
    void deleteItem(IListItem item);
}
