package swe4.gis;

import swe4.entities.Edge;
import swe4.entities.Vertex;
import swe4.exceptions.InvalidVertexIdException;

import java.util.Collection;
import java.util.UUID;

public class Graph {
  public long addVertex(double longitude, double latitude) {

    long id = UUID.randomUUID().toString().hashCode();
    return id;
  }

  public void addEdge(String name, long startVertexId, long endVertexId, double length) throws InvalidVertexIdException {

  }

//  public Collection<Vertex> getVertices() {
//    return new Collection<Vertex>();
//  }

//  public Collection<Edge> getEdges() {
//
//  }
//
//  public Collection<Edge> findShortestPath(long idStartVertex, long idTargetVertex) {
//    // a star here
//  }

  public double pathLength(Collection<Edge> path) {
    return 0;
  }
}
