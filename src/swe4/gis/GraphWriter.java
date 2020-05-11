package swe4.gis;

import java.io.IOException;
import java.util.Collection;

public interface GraphWriter {
  void writePath(Collection<Edge> edges, String fileName) throws IOException;
  void writeGraph(Graph graph, String fileName) throws IOException;
}
