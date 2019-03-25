package dj.yatm.model;

/**
 * Created by Joseph Newman on 3/21/2019.
 */
public interface IListItemObserver {
    /**
     * Updates the observer with the specified data.
     * @param sender the subject triggering the event.
     * @param event the event the subject is firing.
     */
    void Update(AbstractListItem sender, ListItemEvent event);
}
