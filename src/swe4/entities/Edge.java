package swe4.entities;

public class Edge {
  private final String name;
  private final long startVertexId;
  private final long endVertexId;
  private final double length;

  public Edge(String name, long startVertexId, long endVertexId, double length) {
    this.name = name;
    this.startVertexId = startVertexId;
    this.endVertexId = endVertexId;
    this.length = length;
  }

  public String getName() {
    return name;
  }

  public long getStartVertexId() {
    return startVertexId;
  }

  public long getEndVertexId() {
    return endVertexId;
  }

  public double getLength() {
    return length;
  }
}
