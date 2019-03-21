package dj.yatm.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joseph on 3/21/2019.
 */
public class ListItem extends AbstractListItem {

    List<AbstractListItem> subTasks;

    public ListItem() {
        super();
        subTasks = new ArrayList<>();
    }

    @Override
    public boolean isComplete() {
        for (AbstractListItem ali:
             subTasks) {
            if (!ali.isComplete()) return false;
        }
        return true;
    }
}
