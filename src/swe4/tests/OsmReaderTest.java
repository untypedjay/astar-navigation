package swe4.gis.test;

import swe4.gis.EdgeData;
import swe4.gis.GraphReader;
import swe4.gis.osm.OsmReader;

public class OsmReaderTest {

  public static void main(String[] args) {
    try (GraphReader graphReader = new OsmReader("resources/streets_linz.csv")) {
      while (graphReader.hasMoreEdges()) {
        EdgeData e = graphReader.nextEdge();
        System.out.println(e);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
