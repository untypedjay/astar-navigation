package swe4.entities;

public class Vertex {
  private final long id;
  private double x;
  private double y;
  private double cost;
  private double bestGuess;

  public Vertex(double x, double y, long id) {
    this.id = id;
    this.x = x;
    this.y = y;
    this.cost = Double.POSITIVE_INFINITY;
    this.bestGuess = Double.POSITIVE_INFINITY;
  }

  public long getId() {
    return id;
  }

  public double getX() {
    return x;
  }

  public void setX(double x) {
    this.x = x;
  }

  public double getY() {
    return y;
  }

  public void setY(double y) {
    this.y = y;
  }

  public double getCost() {
    return cost;
  }

  public void setCost(double cost) {
    if (cost > 0) {
      this.cost = cost;
    }
  }

  public double getBestGuess() {
    return bestGuess;
  }

  public void setBestGuess(double bestGuess) {
    if (bestGuess >= this.cost) {
      this.bestGuess = bestGuess;
    }
  }
}
