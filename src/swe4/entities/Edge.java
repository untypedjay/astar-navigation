package swe4.entities;

public class Edge {
  private Vertex start;
  private Vertex end;
  private String name;
  private double length;
  private short category;

  public Edge(String name, long startVertexId, long endVertexId, double length) {
    this.name = name;
    this.startVertexId = startVertexId;
    this.endVertexId = endVertexId;
    this.length = length;
  }

  public Vertex getStart() {
    return start;
  }

  public void setStart(Vertex start) {
    this.start = start;
  }

  public Vertex getEnd() {
    return end;
  }

  public void setEnd(Vertex end) {
    this.end = end;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getLength() {
    return length;
  }

  public void setLength(double length) {
    this.length = length;
  }

  public short getCategory() {
    return category;
  }

  public void setCategory(short category) {
    this.category = category;
  }
}
