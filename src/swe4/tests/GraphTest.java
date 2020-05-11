package swe4.tests;


import org.junit.jupiter.api.Test;
import swe4.Edge;
import swe4.exceptions.InvalidVertexIdException;
import swe4.gis.Graph;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GraphTest {
  @Test
  public void testAddVertex() {
    Graph g = new Graph();
    g.addVertex(3.4, 3.5);
  }

  @Test
  public void testFindShortestPathFromDornbirnToVienna() {
    Graph austria = new Graph();
    long bregenz = austria.addVertex(47500000, 9760000);
    long dornbirn = austria.addVertex(47410000, 9740000);
    long feldkirch = austria.addVertex(47250000, 9590000);
    long salzburg = austria.addVertex(47810000, 13050000);
    long innsbruck = austria.addVertex(47270000, 11400000);
    long villach = austria.addVertex(46620000, 13880000);
    long wels = austria.addVertex(48160000, 14020000);
    long linz = austria.addVertex(48320000, 14300000);
    long steyr = austria.addVertex(48050000, 14420000);
    long klagenfurt = austria.addVertex(46620000, 14300000);
    long leoben = austria.addVertex(47380000, 15010000);
    long graz = austria.addVertex(47070000, 15440000);
    long krems = austria.addVertex(48410000, 15610000);
    long stPoelten = austria.addVertex(48210000, 15630000);
    long wien = austria.addVertex(48210000, 16400000);
    long wienerNeustadt = austria.addVertex(47810000, 16230000);
    long eisenstadt = austria.addVertex(47840000, 16530000);

    try {
      austria.addEdge("bgzdbn", bregenz, dornbirn, 10);
      austria.addEdge("dbnfkh", dornbirn, feldkirch, 25);
      austria.addEdge("fkhinb", feldkirch, innsbruck, 158);
      austria.addEdge("bgzszg", bregenz, salzburg, 330);
      austria.addEdge("inbszg", innsbruck, salzburg, 166);
      austria.addEdge("inbvil", innsbruck, villach, 289);
      austria.addEdge("szgvil", salzburg, villach, 194);
      austria.addEdge("szgwel", salzburg, wels, 107);
      austria.addEdge("szgsty", salzburg, steyr, 130);
      austria.addEdge("szglbn", salzburg, leoben, 227);
      austria.addEdge("wellnz", wels, linz, 28);
      austria.addEdge("welsty", wels, steyr, 41);
      austria.addEdge("welkla", wels, klagenfurt, 225);
      austria.addEdge("vilkla", villach, klagenfurt, 40);
      austria.addEdge("lnzsty", linz, steyr, 37);
      austria.addEdge("styvil", steyr, villach, 257);
      austria.addEdge("stykla", steyr, klagenfurt, 220);
      austria.addEdge("stykrm", steyr, krems, 121);
      austria.addEdge("styspn", steyr, stPoelten, 108);
      austria.addEdge("stylbn", steyr, leoben, 152);
      austria.addEdge("wellbn", wels, leoben, 156);
      austria.addEdge("lnzkrm", linz, krems, 139);
      austria.addEdge("lnzspn", linz, stPoelten, 125);
      austria.addEdge("lbngrz", leoben, graz, 61);
      austria.addEdge("lbnwns", leoben, wienerNeustadt, 108);
      austria.addEdge("grzwns", graz, wienerNeustadt, 144);
      austria.addEdge("wnsest", wienerNeustadt, eisenstadt, 32);
      austria.addEdge("wnswie", wienerNeustadt, wien, 63);
      austria.addEdge("wieest", wien, eisenstadt, 61);
      austria.addEdge("spnwns", stPoelten, wienerNeustadt, 94);
      austria.addEdge("krmwie", krems, wien, 77);
      austria.addEdge("krmspn", krems, stPoelten, 27);
      austria.addEdge("spnwie", stPoelten, wien, 65);
    } catch (InvalidVertexIdException e) {
      e.printStackTrace();
    }

    LinkedList<Edge> dornbirnWien = new LinkedList<>();
    for (Edge edge : austria.findShortestPath(dornbirn, wien)) {
      dornbirnWien.add(edge);
    }
    assertEquals("bgzdbn", dornbirnWien.getFirst().getName());
    assertEquals("bgzszg", dornbirnWien.get(1).getName());
    assertEquals("szgsty", dornbirnWien.get(2).getName());
    assertEquals("styspn", dornbirnWien.get(3).getName());
    assertEquals("spnwie", dornbirnWien.getLast().getName());
    assertEquals(643, austria.pathLength(dornbirnWien));
    assertEquals(5, dornbirnWien.size());
  }
}
