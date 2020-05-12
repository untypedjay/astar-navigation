package swe4.tests;

import org.junit.jupiter.api.Test;
import swe4.Edge;
import swe4.SphericPoint;
import swe4.Vertex;
import swe4.gis.TimeCalculator;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimeCalculatorTest {
  @Test
  public void testCosts() {
    TimeCalculator tc = new TimeCalculator();
    Vertex linz = new Vertex(1, new SphericPoint(48.320000, 14.300000));
    Vertex wels = new Vertex(2, new SphericPoint(48.160000, 14.020000));
    Edge linzWels = new Edge(linz, wels, "lnzwel", 28, (short) 3);
    assertEquals(0.4, tc.costs(linzWels));
  }

  @Test
  public void testEstimatedCosts() {
    TimeCalculator tc = new TimeCalculator();
    Vertex linz = new Vertex(1, new SphericPoint(48.320000, 14.300000));
    Vertex wels = new Vertex(2, new SphericPoint(48.160000, 14.020000));
    assertEquals(0.27, tc.estimatedCosts(linz, wels), 0.1);
  }
}
