package dj.yatm.model;

/**
 * Created by Joseph on 3/21/2019.
 */
public class TodoItem extends AbstractListItem {
    private boolean complete = false;

    @Override
    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public void toggleComplete() {
        complete = !complete;
    }
}
