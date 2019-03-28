package dj.yatm.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by parad on 3/21/2019.
 */
public class ListItemTest {

    ListItem list;

    @Before
    public void setup(){
        list = new ListItem(0, null);
        TodoItem ti1 = new TodoItem(1, null);
        ti1.setTitle("TI1");
        ti1.setPriority(0);

        list.addItem(ti1);
    }

    @Test
    public void isComplete() {
        assertEquals(false, list.isComplete());
        ((TodoItem)list.getItemByIndex(0)).toggleComplete();
        assertEquals(true, list.isComplete());
        ListItem sublist = new ListItem(0, null);
        TodoItem subItem1 = new TodoItem(0, null);
        subItem1.toggleComplete();
        sublist.addItem(subItem1);
        TodoItem subItem2 = new TodoItem(0, null);
        sublist.addItem(subItem2);
        list.addItem(sublist);
        assertEquals(false, list.isComplete());
        subItem2.toggleComplete();
        assertEquals(true, list.isComplete());

        AbstractListItem ali = new ListItem(0, null);
        AbstractListItem ali2 = new TodoItem(0, null);
    }

    @Test
    public void addItem() {
    }

    @Test
    public void removeItem() {
    }
}