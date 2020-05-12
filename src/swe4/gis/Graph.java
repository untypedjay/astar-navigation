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

  public Collection<Edge> findShortestPath(long idStartVertex, long idTargetVertex) {
    PriorityQueue<Vertex> vertexQueue = new PriorityQueue<>(new SortByBestGuess());
    vertices.get(idStartVertex).setCost(0);
    vertices.get(idStartVertex).setBestGuess(heuristicDistanceBetween(idStartVertex, idTargetVertex));
    vertexQueue.add(vertices.get(idStartVertex));
    HashMap<Long, Vertex> previousVertexOf = new HashMap<>();

    while (!vertexQueue.isEmpty()) {
      Vertex current = vertexQueue.poll();
      if (current == vertices.get(idTargetVertex)) {
        resetScores();
        return reconstructPath(previousVertexOf, vertices.get(idTargetVertex));
      }

      for (Vertex neighbor : getNeighborsOf(current)) {
        double costToNeighbor = 0;
        try {
          costToNeighbor = current.getCost() + distanceBetween(current, neighbor);

        } catch (InvalidVertexIdException e) {
          e.printStackTrace();
        }
        if (costToNeighbor < neighbor.getCost()) {
          previousVertexOf.put(neighbor.getId(), current);
          neighbor.setCost(costToNeighbor);
          neighbor.setBestGuess(costToNeighbor + heuristicDistanceBetween(neighbor.getId(), idTargetVertex));
          if (!vertexQueue.contains(neighbor)) {
            vertexQueue.add(neighbor);
          }
        }
      }
    }
    System.out.println("this shit should not execute");
    return new ArrayList<>();
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

  private Collection<Edge> reconstructPath(HashMap<Long, Vertex> previousVertexMap, Vertex vertex) {
    List<Edge> path = new LinkedList<>();
    Vertex current = vertex;
    Vertex previous = vertex;
    while (previousVertexMap.get(current.getId()) != null) {
      previous = previousVertexMap.get(current.getId());
      path.add(edges.get(getEdgeName(previous, current)));
      current = previous;
    }
    Collections.reverse(path);""
    return path;
  }

  private HashSet<Vertex> getNeighborsOf(Vertex vertex) {
    HashSet<Vertex> neighbors = new HashSet<>();
    for (Vertex potentialNeighbor : vertices.values()) {
      if (edgeExists(vertex, potentialNeighbor)) {
        neighbors.add(potentialNeighbor);
      }
    }
    return neighbors;
  }

  private boolean edgeExists(Vertex start, Vertex end) {
    for (Edge edge : edges.values()) {
      if ((edge.getStart() == start && edge.getEnd() == end) || (edge.getStart() == end && edge.getEnd() == start)) {
        return true;
      }
    }
    return false;
  }

  private String getEdgeName(Vertex start, Vertex end) {
    for (Edge edge : edges.values()) {
      if ((edge.getStart() == start && edge.getEnd() == end) || (edge.getStart() == end && edge.getEnd() == start)) {
        return edge.getName();
      }
    }
    return "";
  }
}
