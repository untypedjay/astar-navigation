package swe4.tests;

import swe4.gis.EdgeData;
import swe4.gis.Graph;
import swe4.gis.GraphReader;
import swe4.gis.GraphWriter;
import swe4.gis.osm.OsmReader;
import swe4.osm.OsmWriter;

import java.io.IOException;

public class OsmReaderTest {

  public static void main(String[] args) {
    try (GraphReader graphReader = new OsmReader("resources/streets_linz.csv")) {
      Graph linz = new Graph();
      while (graphReader.hasMoreEdges()) {
        EdgeData e = graphReader.nextEdge();
        if (e.getName() != "") {
          System.out.println(e.getName());
        }
        linz.addVertex(e.getStartId(), e.getStart());
        linz.addVertex(e.getEndId(), e.getEnd());
        linz.addEdge(e);
      }
      GraphWriter linzWriter = new OsmWriter();
      try {
        linzWriter.writeGraph(linz, "resources/linzgraph");
        linzWriter.writePath(linz.getEdges(), "resources/linzpath");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
