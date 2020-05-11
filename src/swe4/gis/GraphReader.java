package swe4.gis;

import java.io.IOException;

public interface GraphReader extends AutoCloseable {
	boolean hasMoreEdges();
	EdgeData nextEdge() throws IOException;
}