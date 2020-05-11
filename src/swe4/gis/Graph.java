package swe4.gis;

import swe4.entities.Edge;
import swe4.entities.Vertex;
import swe4.exceptions.InvalidVertexIdException;

import java.lang.reflect.Array;
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
    Vertex vertex = new Vertex(longitude, latitude, UUID.randomUUID().toString().hashCode());
    vertices.put(vertex.getId(), vertex);
    return vertex.getId();
  }

  public void addEdge(String name, long startVertexId, long endVertexId, double length) throws InvalidVertexIdException {
    if (!vertices.containsKey(startVertexId) || !vertices.containsKey(endVertexId)) {
      throw new InvalidVertexIdException();
    } else {
      edges.put(name, new Edge(name, startVertexId, endVertexId, length));
    }
  }

  public HashMap<Long, Vertex> getVertices() {
    return vertices;
  }

  public HashMap<String, Edge> getEdges() {
    return edges;
  }

  public LinkedList<Edge> findShortestPath(long idStartVertex, long idTargetVertex) {
    PriorityQueue<Vertex> vertexQueue = new PriorityQueue<>(new SortByBestGuess());
    vertexQueue.add(vertices.get(idStartVertex));
    vertices.get(idStartVertex).setCost(0);
    vertices.get(idStartVertex).setBestGuess(heuristicDistanceBetween(idStartVertex, idTargetVertex));
    HashMap<Long, Long> previousVertex = new HashMap<>();

    while (!vertexQueue.isEmpty()) {
      Vertex current = new Vertex(vertexQueue.poll());
      if (current.getId() == idTargetVertex) {
        resetScores();
        return reconstructPath(previousVertex, idTargetVertex);
      }

      for (Vertex neighbor : getNeighborsOf(current)) {
        double costToNeighbor = 0;
        try {
          costToNeighbor = current.getCost() + distanceBetween(current.getId(), neighbor.getId());
        } catch (InvalidVertexIdException e) {
          e.printStackTrace();
        }
        if (costToNeighbor < neighbor.getCost()) {
          previousVertex.put(neighbor.getId(), current.getId());
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
    double x1 = vertices.get(idStartVertex).getX();
    double y1 = vertices.get(idStartVertex).getY();
    double x2 = vertices.get(idTargetVertex).getX();
    double y2 = vertices.get(idTargetVertex).getY();
    return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
  }

  private double distanceBetween(long idStartVertex, long idTargetVertex) throws InvalidVertexIdException {
    return edges.get(getEdgeName(idStartVertex, idTargetVertex)).getLength();
  }

  private void resetScores() {
    for (Vertex vertex : vertices.values()) {
      vertex.setCost(Double.POSITIVE_INFINITY);
      vertex.setBestGuess(Double.POSITIVE_INFINITY);
    }
  }

  private LinkedList<Edge> reconstructPath(HashMap<Long, Long> previousVertex, long vertexId) {
    LinkedList<Edge> path = new LinkedList<>();
    try {
      long previousVertexId = previousVertex.get(vertexId);
      path.addFirst(edges.get(getEdgeName(previousVertexId, vertexId)));
      vertexId = previousVertexId;
      while (previousVertex.get(vertexId) != null) {
        previousVertexId = previousVertex.get(vertexId);
        path.addFirst(edges.get(getEdgeName(previousVertexId, vertexId)));
        vertexId = previousVertexId;
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
        if (getEdgeName(vertex.getId(), potentialNeighbor.getId()) != null || getEdgeName(potentialNeighbor.getId(), vertex.getId()) != null) {
          neighbors.add(potentialNeighbor);
        }
      } catch (InvalidVertexIdException ignore) {}
    }
    return neighbors;
  }

  private String getEdgeName(long idStartVertex, long idTargetVertex) throws InvalidVertexIdException {
    for (Edge edge : edges.values()) {
      if (edge.getStartVertexId() == idStartVertex && edge.getEndVertexId() == idTargetVertex) {
        return edge.getName();
      }
    }
    throw new InvalidVertexIdException();
  }
}
