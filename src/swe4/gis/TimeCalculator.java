package swe4.gis;

import swe4.Edge;
import swe4.SphericMath;
import swe4.Vertex;

public class TimeCalculator implements CostCalculator {

  @Override
  public double costs(Edge edge) { // time it takes to drive the edge
    double velocity;
    switch (edge.getCategory()) {
      case 1: velocity = 130;
        break;
      case 2: velocity = 100;
        break;
      case 3: velocity = 70;
        break;
      case 4: velocity = 50;
        break;
      case 5: velocity = 30;
        break;
      case 6: velocity = 10;
        break;
      default: velocity = 0;
        break;
    }
    return calculateTimeInHours(edge.getLength(), velocity);
  }

  @Override
  public double estimatedCosts(Vertex v1, Vertex v2) {
    double linearDistance = SphericMath.earthDistance(v1.getCoordinates(), v2.getCoordinates()) * 0.001;
    return calculateTimeInHours(linearDistance, 130);
  }

  private double calculateTimeInHours(double distanceInKilometres, double velocityInKilometresPerHour) {
    return distanceInKilometres / velocityInKilometresPerHour;
  }
}
