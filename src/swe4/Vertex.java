package swe4;

public class Vertex {
  private long id;
  private SphericPoint coordinates;
  private double cost;
  private double bestGuess;

  public Vertex(long id, SphericPoint coordinates) {
    this.id = id;
    this.coordinates = coordinates;
    this.cost = Double.POSITIVE_INFINITY;
    this.bestGuess = Double.POSITIVE_INFINITY;
  }

  public Vertex(Vertex other) {
    this(other.getId(), other.getCoordinates());
  }

  public long getId() {
    return id;
  }

  public SphericPoint getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(SphericPoint coordinates) {
    this.coordinates = coordinates;
  }

  public double getCost() {
    return cost;
  }

  public void setCost(double cost) {
    if (cost >= 0) {
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
