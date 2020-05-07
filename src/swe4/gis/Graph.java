package swe4.gis;

import swe4.entities.Edge;
import swe4.entities.Vertex;
import swe4.exceptions.InvalidVertexIdException;

import java.util.Collection;

public class Graph {
  public int addVertex(double longitude, double latitude) {
    return 0; // id of new vertex
  }

  public void addEdge(String name, long startVertexId, long endVertexId, double length) throws InvalidVertexIdException {

  }

  public Collection<Vertex> getVertices() {
    return new Collection<Vertex>();
  }

  public Collection<Edge> getEdges() {

  }

  public Collection<Edge> findShortestPath(long idStartVertex, long idTargetVertex) {
    // a star here
  }

  public double pathLength(Collection<Edge> path) {
    return 0;
  }
}
