package swe4.gis;

import swe4.SphericPoint;

public class EdgeData {
  private long         id;
  private long         startId;
  private long         endId;
  private SphericPoint start;
  private SphericPoint end;
  private String       name;
  private short        type;
  private short        category;
  private double       length;
  private boolean      isOneWay;

  public SphericPoint getStart() {
    return start;
  }

  public void setStart(SphericPoint start) {
    this.start = start;
  }

  public SphericPoint getEnd() {
    return end;
  }

  public void setEnd(SphericPoint end) {
    this.end = end;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getStartId() {
    return startId;
  }

  public void setStartId(long startId) {
    this.startId = startId;
  }

  public long getEndId() {
    return endId;
  }

  public void setEndId(long endId) {
    this.endId = endId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getType() {
    return type;
  }

  public void setType(short type) {
    this.type = type;
  }

  public short getCategory() {
    return category;
  }

  public void setCategory(short category) {
    this.category = category;
  }

  public double getLength() {
    return length;
  }

  public void setLength(double length) {
    this.length = length;
  }

  public boolean isOneWay() {
		return isOneWay;
	}

	public void setOneWay(boolean isOneWay) {
		this.isOneWay = isOneWay;
	}

	@Override
  public String toString() {
    return String.format(
        "id=%s, name=\"%s\" start=%s, end=%s, length=%.0f type=%d, category=%d", id, name,
        start, end, length, type, category);
  }
}
