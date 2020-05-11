package swe4;

public class SphericPoint {
  private double longitude;
  private double latitude;

  public SphericPoint() {
  }

  public SphericPoint(double longitude, double latitude) {
    this.longitude = longitude;
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  @Override
  public String toString() {
    return String.format("(%.5f,%.5f)", longitude, latitude);
  }
}
