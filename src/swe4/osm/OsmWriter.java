package swe4.gis.osm;

import swe4.gis.Edge;
import swe4.gis.Graph;
import swe4.gis.GraphWriter;
import swe4.gis.Vertex;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

public class OsmWriter implements GraphWriter {

  private static final String HEADER_HTML =
    "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
    "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\n" +
    "<head>\n" +
    "<title>Map</title>\n" +
    "<meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\" />\n" +
    "<meta http-equiv=\"content-script-type\" content=\"text/javascript\" />\n" +
    "<meta http-equiv=\"content-style-type\" content=\"text/css\" />\n" +
    "<meta http-equiv=\"content-language\" content=\"en\" />\n" +
    "<meta name=\"generator\" content=\"Easymap\" />\n" +
    "<link rel=\"stylesheet\" type=\"text/css\" href=\"map.css\" />\n" +
    "\n" +
    "<script type=\"text/javascript\" src=\"openlayers/OpenLayers.js\"></script>\n" +
    "<script type=\"text/javascript\" src=\"http://www.openstreetmap.org/openlayers/OpenStreetMap.js\"></script>\n" +
    "<script type=\"text/javascript\" src=\"util.js\"></script>\n" +
    " \n" +
    "<script type=\"text/javascript\">\n" +
    "//<![CDATA[\n" +
    "var map;\n" +
    "\n" +
    "var showPopupOnHover = false;\n" +
    "text = new Array(\"Show map information\",\"Hide map information\");\n" +
    "\n" +
    "function drawmap() {\n" +
    "    OpenLayers.Lang.setCode('de');\n" +
    "    \n" +
    "    map = new OpenLayers.Map('map', {\n" +
    "        projection: new OpenLayers.Projection(\"EPSG:900913\"),\n" +
    "        displayProjection: new OpenLayers.Projection(\"EPSG:4326\"),\n" +
    "        controls: [\n" +
    "\t\tnew OpenLayers.Control.MouseDefaults(),\n" +
    "\t\tnew OpenLayers.Control.Attribution()],\n" +
    "        maxExtent:\n" +
    "            new OpenLayers.Bounds(-20037508.34,-20037508.34,\n" +
    "                                    20037508.34, 20037508.34),\n" +
    "        numZoomLevels: 18,\n" +
    "        maxResolution: 156543,\n" +
    "        units: 'meters'\n" +
    "    });\n" +
    "// Add more controls..\n" +
    "map.addControl(new OpenLayers.Control.LayerSwitcher());\n" +
    "map.addControl(new OpenLayers.Control.PanZoomBar());\n" +
    "\n" +
    "// Position and zoom of the map";

  private static String HTML_HEADER_MARKERS =
    "checkForPermalink();\n" +
    "// Add layers\n" +
    "layer_markers = new OpenLayers.Layer.Markers(\"Marker\", { projection: new OpenLayers.Projection(\"EPSG:4326\"),visibility: true, displayInLayerSwitcher: false });\n" +
    "layer_vectors = new OpenLayers.Layer.Vector(\"Drawings\", { displayInLayerSwitcher: false } );map.addLayer(layer_vectors);map.addLayer(layer_markers)\n" +
    "layers = new Array();\n" +
    "layer_layerMapnik = new OpenLayers.Layer.OSM.Mapnik(\"Mapnik\");\n" +
    "map.addLayer(layer_layerMapnik)\n" +
    "layers.push(new Array(layer_layerMapnik,'layer_layerMapnik'));\n" +
    "setLayer(0);\n" +
    "\n" +
    "// Jump to the right position and zoom..\n" +
    "jumpTo(lon,lat,zoom);\n" +
    "\n" +
    "// Add used maker icons..\n" +
    "icons = new Array();\n" +
    "icons[0] = new Array('http://openlayers.org/api/img/marker.png','21','25','0.5','1');\n" +
    "\n" +
    "// Add markers";
  private static final String HTML_FOOTER =
    "\n" +
    "// Nochmal was..\n" +
    "jumpTo(lon, lat, zoom);\n" +
    "\n" +
    "checkUtilVersion(4);\n" +
    "}\n" +
    "\n" +
    "//]]>\n" +
    "    </script>\n" +
    "</head>\n" +
    "<body onload=\"drawmap();\">\n" +
    "\t<div id=\"descriptionToggle\" onclick=\"toggleInfo()\">Show map information</div>\n" +
    "\t<div id=\"description\" class=\"hide\">No map description has been entered</div>\n" +
    "\t<div id=\"map\"></div>\n" +
    "\t<div class=\"hide\"><p>Wenn Sie diesen Text sehen, dann haben Sie die HTML-Datei ohne die zusätzlich benötigten Dateien geöffnet. Wenn Sie versucht haben die Karte herunterzuladen, wurde das Downloadfenster möglicherweise aus irgendeinem Grund nicht geöffnet. Speichern Sie in diesem Fall einfach diese Seite ab.</p><p>If you see this text, you opened the map HTML-File without the necessary files. If you tried to download the map, the download dialog may not have opened for some reason. In this case just save this page.</p></div>\n" +
    "</body>\n" +
    "</html>\n";
  private int zoomLevel;

  public OsmWriter() {
    this(18);
  }

  public OsmWriter(int zoomLevel) {
    this.zoomLevel = zoomLevel;
  }

  @Override
  public void writePath(Collection<Edge> edges, String fileName) throws IOException {
    if (edges.size() < 1) throw new IllegalArgumentException("Empty path!");

    Path htmlFile = Paths.get(String.format("bin/%s.html", fileName));
    Files.deleteIfExists(htmlFile);

    Locale defaultLocale = Locale.getDefault();
    Locale.setDefault(Locale.ENGLISH);
    try (PrintStream ps = new PrintStream(new FileOutputStream(htmlFile
      .toFile()))) {
      ps.println(HEADER_HTML);
      Iterator<Edge> it = edges.iterator();
      Edge firstEdge = it.next();
      Edge lastEdge;
      if (it.hasNext()) {
        lastEdge = it.next();
        while (it.hasNext()) {
          lastEdge = it.next();
        }
      }
      else {
        lastEdge = firstEdge;
      }

      ps.format("lon = %f;%n", firstEdge.getStart().getCoordinates()
                                        .getLongitude());
      ps.format("lat = %f;%n", firstEdge.getEnd().getCoordinates()
                                        .getLatitude());
      ps.format("zoom = %d;%n", zoomLevel);

      ps.println(HTML_HEADER_MARKERS);

      ps.format("addMarker(layer_markers,%f,%f,\"<b>Start</b><p />\",false," +
                "0);%n",
        firstEdge.getStart().getCoordinates().getLongitude(),
        firstEdge.getStart().getCoordinates().getLatitude());
      ps.format("addMarker(layer_markers,%f,%f,\"<b>Ende</b><p />\",false,0)" +
                ";%n",
        lastEdge.getEnd().getCoordinates().getLongitude(),
        lastEdge.getEnd().getCoordinates().getLatitude());

      ps.println("geometries = new Array();\n" +
                 "geometries.push(\n" +
                 "\tdrawLine([");
      it = edges.iterator();
      while (it.hasNext()) {
        Edge edge = it.next();
        ps.format("[%f, %f]",
          edge.getStart().getCoordinates().getLongitude(),
          edge.getStart().getCoordinates().getLatitude());
        if (it.hasNext()) {
          ps.print(", ");
        }
      }
      ps.println("], {strokeColor:\"#0000FF\",strokeWidth: 3,fillColor: " +
                 "\"#0000FF\",fillOpacity: 0.4}));");

      ps.println(HTML_FOOTER);
    }
    finally {
      Locale.setDefault(defaultLocale);
    }
  }

  @Override
  public void writeGraph(Graph graph, String fileName) throws IOException {
    if (graph.getVertices().size() < 1)
      throw new IllegalArgumentException("Empty path!");

    Path htmlFile = Paths.get(String.format("bin/%s.html", fileName));
    Files.deleteIfExists(htmlFile);

    Locale defaultLocale = Locale.getDefault();
    Locale.setDefault(Locale.ENGLISH);
    try (PrintStream ps = new PrintStream(new FileOutputStream(htmlFile
      .toFile()))) {
      ps.println(HEADER_HTML);
      Vertex anyVertex = graph.getVertices().iterator().next();

      ps.format("lon = %f;%n", anyVertex.getCoordinates().getLongitude());
      ps.format("lat = %f;%n", anyVertex.getCoordinates().getLatitude());
      ps.format("zoom = %d;%n", zoomLevel);

      ps.println(HTML_HEADER_MARKERS);

      for (Vertex v : graph.getVertices()) {
        ps.format("addMarker(layer_markers,%f,%f,\"%d\",false,0);%n",
          v.getCoordinates().getLongitude(),
          v.getCoordinates().getLatitude(),
          v.getId());
      }

      ps.println("geometries = new Array();\n");

      for (Edge e : graph.getEdges()) {
        ps.printf("geometries.push(drawLine([[%f, %f],[%f, %f]], " +
                  "{strokeColor:\"#0000FF\",strokeWidth: 3,fillColor: " +
                  "\"#0000FF\",fillOpacity: 0.4}));",
          e.getStart().getCoordinates().getLongitude(),
          e.getStart().getCoordinates().getLatitude(),
          e.getEnd().getCoordinates().getLongitude(),
          e.getEnd().getCoordinates().getLatitude()
        );
      }

      ps.println(HTML_FOOTER);
    }
    finally {
      Locale.setDefault(defaultLocale);
    }
  }
}
