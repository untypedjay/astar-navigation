package swe4.entities;

public class SphericMath {
  public static double earthMeanRadius = 6371.0*1000.0;

  public static double degToRad(double alpha) {
    return alpha*Math.PI/180;
  }

  public static double angularDistance(SphericPoint p1, SphericPoint p2) {

    double lat1 = degToRad(p1.getLatitude());
    double lat2 = degToRad(p2.getLatitude());
    double long1 = degToRad(p1.getLongitude());
    double long2 = degToRad(p2.getLongitude());
    double x = Math.sin(lat1) * Math.sin(lat2) +
      Math.cos(lat1) * Math.cos(lat2) * Math.cos(long1 - long2);
    x = Math.min(1, Math.max(0, x));
    return Math.acos(x);
  }

  public static double earthDistance(SphericPoint p1, SphericPoint p2) {
    return earthMeanRadius*angularDistance(p1, p2);
  }
}