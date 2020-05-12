package swe4.gis;

import swe4.Edge;
import swe4.Vertex;

public interface CostCalculator {
  double costs(Edge edge); // benötigte zeit
  double estimatedCosts(Vertex v1, Vertex v2); // luftlinie autobahn
}
