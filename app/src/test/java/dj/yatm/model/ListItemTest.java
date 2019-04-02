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
        list = new ListItem();
    }

    @Test
    public void isComplete() {
        assertEquals(false, list.isComplete());
    }

    @Test
    public void addItem() {
    }

    @Test
    public void removeItem() {
    }
}