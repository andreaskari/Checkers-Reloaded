package creatures;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.awt.Color;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;
import huglife.Impassible;
import huglife.Empty;

/** Tests the plip class   
 *  @authr FIXME
 */

public class TestClorus {

    @Test
    public void testBasics() {
        Clorus p = new Clorus(2);
        assertEquals(2, p.energy(), 0.01);
        assertEquals(new Color(34, 0, 231), p.color());
        p.move();
        assertEquals(1.97, p.energy(), 0.01);
        p.move();
        assertEquals(1.94, p.energy(), 0.01);
        p.stay();
        assertEquals(1.93, p.energy(), 0.01);
        p.stay();
        assertEquals(1.92, p.energy(), 0.01);
    }

    @Test
    public void testReplicate() {
        Clorus p = new Clorus(2);
        assertEquals(2, p.energy(), 0.01);
        Clorus offspring = p.replicate();
        assertEquals(1, p.energy(), 0.01);
        assertEquals(1, offspring.energy(), 0.01);
        Clorus grandClorus = offspring.replicate();
        assertEquals(1, p.energy(), 0.01);
        assertEquals(0.5, offspring.energy(), 0.01);
        assertEquals(0.5, grandClorus.energy(), 0.01);
        assertNotSame(p, offspring);
        assertNotSame(offspring, grandClorus);
    }

    @Test
    public void testChoose() {
        Clorus p = new Clorus(1.2);
        HashMap<Direction, Occupant> surrounded = new HashMap<Direction, Occupant>();
        surrounded.put(Direction.TOP, new Impassible());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());

        Action actual = p.chooseAction(surrounded);
        Action expected = new Action(Action.ActionType.STAY);

        assertEquals(expected, actual);
    }

    @Test
    public void testChoose2() {
        Clorus p = new Clorus(1.2);
        HashMap<Direction, Occupant> nearlySurrounded = new HashMap<Direction, Occupant>();
        nearlySurrounded.put(Direction.TOP, new Empty());
        nearlySurrounded.put(Direction.BOTTOM, new Impassible());
        nearlySurrounded.put(Direction.LEFT, new Impassible());
        nearlySurrounded.put(Direction.RIGHT, new Impassible());

        Action actual = p.chooseAction(nearlySurrounded);
        Action expected = new Action(Action.ActionType.REPLICATE, Direction.TOP);

        assertEquals(expected, actual);
    }

    @Test
    public void testChoose3() {
        Clorus p = new Clorus(0.6);
        HashMap<Direction, Occupant> nearlySurrounded = new HashMap<Direction, Occupant>();
        nearlySurrounded.put(Direction.TOP, new Plip());
        nearlySurrounded.put(Direction.BOTTOM, new Empty());
        nearlySurrounded.put(Direction.LEFT, new Impassible());
        nearlySurrounded.put(Direction.RIGHT, new Impassible());

        Action actual = p.chooseAction(nearlySurrounded);
        Action expected = new Action(Action.ActionType.ATTACK, Direction.TOP);

        assertEquals(expected, actual);
    }

    public static void main(String[] args) {
        System.exit(jh61b.junit.textui.runClasses(TestClorus.class));
    }
} 
