package swe4;

import swe4.gis.*;
import swe4.osm.OsmWriter;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.channels.ClosedByInterruptException;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("Welcome! Loading street data...");
        try (GraphReader graphReader = new swe4.gis.osm.OsmReader("resources/streets_linz.csv")) {
            Graph linz = new Graph();
            TreeSet<String> streetNames = new TreeSet<>();
            while (graphReader.hasMoreEdges()) {
                EdgeData e = graphReader.nextEdge();
                streetNames.add(e.getName());
                linz.addVertex(e.getStartId(), e.getStart());
                linz.addVertex(e.getEndId(), e.getEnd());
                linz.addEdge(e);
            }
            printStreetNames(streetNames);

            Scanner scanner = new Scanner(System.in);
            String startingPoint;
            String destination;
            String routeMode;
            System.out.print("\nEnter starting point from above: ");
            startingPoint = scanner.nextLine();
            if (!streetNames.contains(startingPoint)) {
                System.out.println("Oups, looks like your starting point has a typo!");
                return;
            }
            System.out.print("Enter destination from above: ");
            destination = scanner.nextLine();
            if (!streetNames.contains(destination)) {
                System.out.println("Oups, looks like your destination has a typo!");
                return;
            }
            System.out.println("Do you want to find the [s]hortest or the [f]astest route?");
            routeMode = scanner.nextLine();
            if (routeMode.contains("s")) {
                routeMode = "shortest";
            } else if (routeMode.contains("f")) {
                routeMode = "fastest";
            } else {
                System.out.println("Sorry, invalid routing mode!");
                return;
            }
            System.out.println("Finding the " + routeMode + " way from " + startingPoint + " to " + destination + "...");
            GraphWriter linzWriter = new OsmWriter();
            if (routeMode == "shortest") {
                linzWriter.writePath(linz.findShortestPath(linz.getVertexId(startingPoint), linz.getVertexId(destination)), "resources/output");
            } else if (routeMode == "fastest") {
                TimeCalculator tc = new TimeCalculator();
                linzWriter.writePath(linz.findMinimalPath(linz.getVertexId(startingPoint), linz.getVertexId(destination), tc), "resources/output");
            }
            File htmlFile = new File("resources/output.html");
            Desktop.getDesktop().browse(htmlFile.toURI());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printStreetNames(TreeSet<String> names) {
        Iterator<String> namesIterator = names.iterator();
        while (namesIterator.hasNext()) {
            for (int j = 0; j < 4; ++j) {
                String name = namesIterator.next();
                int whitespace = 43 - name.length();
                System.out.print(name);
                for (int k = 0; k < whitespace; ++k) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }
}
