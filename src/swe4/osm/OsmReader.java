package swe4.gis.osm;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

import swe4.SphericPoint;
import swe4.gis.GraphReader;
import swe4.gis.EdgeData;

public class OsmReader implements GraphReader, AutoCloseable {

	private Scanner csvScanner;

  public OsmReader(String csvFile) throws IOException {
  	
  	csvScanner = new Scanner(new File(csvFile), "iso-8859-1");
  	csvScanner.useLocale(Locale.GERMAN);
  	csvScanner.useDelimiter(";");
  	csvScanner.nextLine(); // ignore heading
  }

  public boolean hasMoreEdges() {
    return csvScanner.hasNext();
  }

	private short speedToCategory(int speed) {
		
		if (speed >= 120) return 1;
		if (speed >= 100) return 2;
		if (speed >= 80) return 4;
		if (speed >= 70) return 4;
		if (speed >= 50) return 5;
		if (speed >= 40) return 6;
		if (speed >= 30) return 7;
		return 8;
	}

  public EdgeData nextEdge() throws IOException {

    //////////////////////////////////////////////////////
    // read edge data from csv file
    //////////////////////////////////////////////////////

    // read line
  	// gid	class_id	osm_id	source_osm	target_osm	length_m	name	x1	y1	x2	y2	one_way	maxspeed_forward	maxspeed_backward		cost	reverse_cost	cost_s	reverse_cost_s	

  	csvScanner.next(); // gid
  	csvScanner.next(); // class_id
  	long osm_id = csvScanner.nextLong();
  	long source_id = csvScanner.nextLong();
  	long target_id = csvScanner.nextLong();
  	double length = csvScanner.nextDouble();
  	String name = csvScanner.next();
  	double source_x = csvScanner.nextDouble();
  	double source_y = csvScanner.nextDouble();
  	double target_x = csvScanner.nextDouble();
  	double target_y = csvScanner.nextDouble();
  	int one_way = csvScanner.nextInt();
  	int max_speed = csvScanner.nextInt();

  	csvScanner.nextLine();	
  	
		//		System.out.printf("gid=%d, length=%f, source_id=%d, target_id=%d source_coord=(%f,%f), target_coord=(%f,%f) speed=%d%n", 
		//				osm_id, length, source_id, target_id, source_x, source_y, target_x, target_y, max_speed);

    SphericPoint start = new SphericPoint(source_x, source_y);
    SphericPoint end = new SphericPoint(target_x, target_y);

    EdgeData edge = new EdgeData();
    edge.setId(osm_id);
    edge.setName(name);
    edge.setLength(length);
    edge.setStart(start);
    edge.setEnd(end);
    edge.setStartId(source_id);
    edge.setEndId(target_id);
    edge.setCategory(speedToCategory(max_speed));
    edge.setOneWay(one_way != 0);
    
    return edge;
  }

  public void close() throws IOException {
    if (csvScanner != null) csvScanner.close();
  }
}
