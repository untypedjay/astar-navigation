package swe4.gis;

import swe4.Edge;
import swe4.SphericPoint;
import swe4.Vertex;
import swe4.exceptions.InvalidVertexIdException;
import java.util.*;

public class Graph {
  private HashMap<Long, Vertex> vertices;
  private HashMap<String, Edge> edges;

  private class SortByBestGuess implements Comparator<Vertex> {
    @Override
    public int compare(Vertex o1, Vertex o2) {
      if (o1.getBestGuess() <= o2.getBestGuess()) {
        return -1;
      } else {
        return 1;
      }
    }
  }

  public Graph() {
    this.vertices = new HashMap<>();
    this.edges = new HashMap<>();
  }

  public long addVertex(double longitude, double latitude) {
    Vertex vertex = new Vertex(UUID.randomUUID().toString().hashCode(), new SphericPoint(longitude, latitude));
    vertices.put(vertex.getId(), vertex);
    return vertex.getId();
  }

  public void addEdge(String name, long startVertexId, long endVertexId, double length) throws InvalidVertexIdException {
    if (!vertices.containsKey(startVertexId) || !vertices.containsKey(endVertexId)) {
      throw new InvalidVertexIdException();
    } else {
      edges.put(name, new Edge(vertices.get(startVertexId), vertices.get(endVertexId), name, length));
    }
  }

  public Collection<Vertex> getVertices() {
    return vertices.values();
  }

  public Collection<Edge> getEdges() {
    return edges.values();
  }

  public LinkedList<Edge> findShortestPath(long idStartVertex, long idTargetVertex) {
    PriorityQueue<Vertex> vertexQueue = new PriorityQueue<>(new SortByBestGuess());
    vertexQueue.add(vertices.get(idStartVertex));
    vertices.get(idStartVertex).setCost(0);
    vertices.get(idStartVertex).setBestGuess(heuristicDistanceBetween(idStartVertex, idTargetVertex));
    HashMap<Long, Vertex> previousVertex = new HashMap<>();

    while (!vertexQueue.isEmpty()) {
      Vertex current = new Vertex(vertexQueue.poll());
      if (current.getId() == idTargetVertex) {
        resetScores();
        return reconstructPath(previousVertex, vertices.get(idTargetVertex));
      }

      for (Vertex neighbor : getNeighborsOf(current)) {
        double costToNeighbor = 0;
        try {
          costToNeighbor = current.getCost() + distanceBetween(current, neighbor);
        } catch (InvalidVertexIdException e) {
          e.printStackTrace();
        }
        if (costToNeighbor < neighbor.getCost()) {
          previousVertex.put(neighbor.getId(), current);
          neighbor.setCost(costToNeighbor);
          neighbor.setBestGuess(costToNeighbor + heuristicDistanceBetween(neighbor.getId(), idTargetVertex));
          if (!vertexQueue.contains(neighbor)) {
            vertexQueue.add(neighbor);
          }
        }
      }
    }
    return null;
  }

  public double pathLength(Collection<Edge> path) {
    double pathLength = 0;
    for (Edge edge : path) {
      pathLength += edge.getLength();
    }
    return pathLength;
  }

  private double heuristicDistanceBetween(long idStartVertex, long idTargetVertex) {
    double x1 = vertices.get(idStartVertex).getCoordinates().getLongitude();
    double y1 = vertices.get(idStartVertex).getCoordinates().getLatitude();
    double x2 = vertices.get(idTargetVertex).getCoordinates().getLongitude();
    double y2 = vertices.get(idTargetVertex).getCoordinates().getLatitude();
    return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
  }

  private double distanceBetween(Vertex start, Vertex end) throws InvalidVertexIdException {
    return edges.get(getEdgeName(start, end)).getLength();
  }

  private void resetScores() {
    for (Vertex vertex : vertices.values()) {
      vertex.setCost(Double.POSITIVE_INFINITY);
      vertex.setBestGuess(Double.POSITIVE_INFINITY);
    }
  }

  private LinkedList<Edge> reconstructPath(HashMap<Long, Vertex> previousVertexMap, Vertex vertex) {
    LinkedList<Edge> path = new LinkedList<>();
    try {
      Vertex previousVertex;
      while (previousVertexMap.get(vertex.getId()) != null) {
        previousVertex = previousVertexMap.get(vertex.getId());
        path.addFirst(edges.get(getEdgeName(previousVertex, vertex)));
        vertex = previousVertex;
      }
    } catch (InvalidVertexIdException e) {
      e.printStackTrace();
    }
    return path;
  }

  private HashSet<Vertex> getNeighborsOf(Vertex vertex) {
    HashSet<Vertex> neighbors = new HashSet<>();
    for (Vertex potentialNeighbor : vertices.values()) {
      try {
        if (getEdgeName(vertex, potentialNeighbor) != null || getEdgeName(potentialNeighbor, vertex) != null) {
          neighbors.add(potentialNeighbor);
        }
      } catch (InvalidVertexIdException ignore) {}
    }
    return neighbors;
  }

  private String getEdgeName(Vertex start, Vertex end) throws InvalidVertexIdException {
    for (Edge edge : edges.values()) {
      if (edge.getStart() == start && edge.getEnd() == end) {
        return edge.getName();
      }
    }
    throw new InvalidVertexIdException();
  }
}
