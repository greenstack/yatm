package dj.yatm.model;

/**
 * Created by Joseph Newman on 3/21/2019.
 */
public enum ListItemEvent {
    /**
     * Used when the item is created.
     */
    Create,
    /**
     * Used when the item is updated.
     */
    Update,
    /**
     * Used when the item is to be deleted.
     */
    Delete,
    /**
     * Do not bother the observer with your trifles!
     */
    None,
}
