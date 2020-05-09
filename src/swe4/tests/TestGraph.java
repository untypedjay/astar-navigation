package swe4.tests;


import org.junit.jupiter.api.Test;
import swe4.gis.Graph;

public class TestGraph {
  @Test
  public void testAddVertex() {
    Graph g = new Graph();
    g.addVertex(3.4, 3.5);
  }
}
