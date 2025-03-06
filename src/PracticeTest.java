import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * JUnit test suite for the Practice class.
 */
public class PracticeTest {

  /**
   * Simple container for vertices of the complex graph.
   */
  static class GraphData {
    Vertex<Integer> v3;
    Vertex<Integer> v7;
    Vertex<Integer> v12;
    Vertex<Integer> v34;
    Vertex<Integer> v56;
    Vertex<Integer> v78;
    Vertex<Integer> v91;
    Vertex<Integer> v45;
    Vertex<Integer> v23;
    Vertex<Integer> v67;

    GraphData(Vertex<Integer> v3, Vertex<Integer> v7, Vertex<Integer> v12, Vertex<Integer> v34,
              Vertex<Integer> v56, Vertex<Integer> v78, Vertex<Integer> v91,
              Vertex<Integer> v45, Vertex<Integer> v23, Vertex<Integer> v67) {
      this.v3 = v3;
      this.v7 = v7;
      this.v12 = v12;
      this.v34 = v34;
      this.v56 = v56;
      this.v78 = v78;
      this.v91 = v91;
      this.v45 = v45;
      this.v23 = v23;
      this.v67 = v67;
    }
  }

  /**
   * Builds the complex graph as follows:
   * https://auberonedu.github.io/graph-explore/graph_site/viz.html
   *
   *   v3.neighbors  = { v7, v34 }
   *   v7.neighbors  = { v12, v45, v34, v56 }
   *   v12.neighbors = { v7, v56, v78 }
   *   v34.neighbors = { v34, v91 }    (self-loop on v34)
   *   v56.neighbors = { v78 }
   *   v78.neighbors = { v91 }
   *   v91.neighbors = { v56 }
   *   v45.neighbors = { v23 }
   *   v23.neighbors = { }           (leaf)
   *   v67.neighbors = { v91 }
   *
   * We use v3 as the starting vertex.
   */
  private GraphData buildComplexGraph() {
    Vertex<Integer> v3  = new Vertex<>(3);
    Vertex<Integer> v7  = new Vertex<>(7);
    Vertex<Integer> v12 = new Vertex<>(12);
    Vertex<Integer> v34 = new Vertex<>(34);
    Vertex<Integer> v56 = new Vertex<>(56);
    Vertex<Integer> v78 = new Vertex<>(78);
    Vertex<Integer> v91 = new Vertex<>(91);
    Vertex<Integer> v45 = new Vertex<>(45);
    Vertex<Integer> v23 = new Vertex<>(23);
    Vertex<Integer> v67 = new Vertex<>(67);

    v3.neighbors  = new ArrayList<>(Arrays.asList(v7, v34));
    v7.neighbors  = new ArrayList<>(Arrays.asList(v12, v45, v34, v56));
    v12.neighbors = new ArrayList<>(Arrays.asList(v7, v56, v78));
    v34.neighbors = new ArrayList<>(Arrays.asList(v34, v91)); // self-loop on v34
    v56.neighbors = new ArrayList<>(Arrays.asList(v78));
    v78.neighbors = new ArrayList<>(Arrays.asList(v91));
    v91.neighbors = new ArrayList<>(Arrays.asList(v56));
    v45.neighbors = new ArrayList<>(Arrays.asList(v23));
    v23.neighbors = new ArrayList<>();
    v67.neighbors = new ArrayList<>(Arrays.asList(v91));

    return new GraphData(v3, v7, v12, v34, v56, v78, v91, v45, v23, v67);
  }

  // ---------------------------
  // Tests for printVertexVals
  // ---------------------------

  @Test
  public void testPrintVertexValsComplexGraph_OutputCorrect() {
    Practice practice = new Practice();
    GraphData graph = buildComplexGraph();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    TeeOutputStream tee = new TeeOutputStream(originalOut, baos);
    System.setOut(new PrintStream(tee));

    practice.printVertexVals(graph.v3);

    System.out.flush();
    System.setOut(originalOut);

    String output = baos.toString();
    String[] lines = output.split(System.lineSeparator());
    Set<String> printed = new HashSet<>();
    for (String line : lines) {
      if (!line.trim().isEmpty()) {
        printed.add(line.trim());
      }
    }
    Set<String> expected = new HashSet<>(Arrays.asList("3", "7", "34", "12", "45", "56", "78", "91", "23"));
    assertEquals(expected, printed, "printVertexVals for complex graph did not print the expected values");
  }

  @Test
  public void testPrintVertexValsSingleNode_OutputCorrect() {
    Practice practice = new Practice();
    Vertex<Integer> single = new Vertex<>(42);
    single.neighbors = new ArrayList<>();

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    TeeOutputStream tee = new TeeOutputStream(originalOut, baos);
    System.setOut(new PrintStream(tee));

    practice.printVertexVals(single);

    System.out.flush();
    System.setOut(originalOut);

    String output = baos.toString();
    String[] lines = output.split(System.lineSeparator());
    Set<String> printed = new HashSet<>();
    for (String line : lines) {
      if (!line.trim().isEmpty()) {
        printed.add(line.trim());
      }
    }
    Set<String> expected = new HashSet<>(Arrays.asList("42"));
    assertEquals(expected, printed, "printVertexVals for a single node should print '42'");
  }

  // ---------------------------
  // Tests for reachable
  // ---------------------------

  @Test
  public void testReachableComplexGraph_ReturnsCorrectVertices() {
    Practice practice = new Practice();
    GraphData graph = buildComplexGraph();

    Set<Vertex<Integer>> result = practice.reachable(graph.v3);
    Set<Integer> actualData = new HashSet<>();
    for (Vertex<Integer> v : result) {
      actualData.add(v.data);
    }
    Set<Integer> expectedData = new HashSet<>(Arrays.asList(3, 7, 34, 12, 45, 56, 78, 91, 23));
    assertEquals(expectedData, actualData, "reachable for complex graph did not return the expected vertices");
  }

  @Test
  public void testReachable_NullInputReturnsEmptySet() {
    Practice practice = new Practice();
    Set<Vertex<Integer>> result = practice.reachable(null);
    assertNotNull(result, "reachable(null) should return an empty set, not null");
    assertTrue(result.isEmpty(), "reachable(null) should return an empty set");
  }

  // ---------------------------
  // Tests for max
  // ---------------------------

  @Test
  public void testMaxComplexGraph_ReturnsMaxValue() {
    Practice practice = new Practice();
    GraphData graph = buildComplexGraph();
    int maxVal = practice.max(graph.v3);
    assertEquals(91, maxVal, "max for complex graph should be 91");
  }

  @Test
  public void testMax_NullInputReturnsMinValue() {
    Practice practice = new Practice();
    int result = practice.max(null);
    assertEquals(Integer.MIN_VALUE, result, "max(null) should return Integer.MIN_VALUE");
  }

  // ---------------------------
  // Tests for leaves
  // ---------------------------

  @Test
  public void testLeavesComplexGraph_ReturnsOnlyLeaf() {
    Practice practice = new Practice();
    GraphData graph = buildComplexGraph();

    Set<Vertex<Integer>> leaves = practice.leaves(graph.v3);
    Set<Integer> actualLeaves = new HashSet<>();
    for (Vertex<Integer> v : leaves) {
      actualLeaves.add(v.data);
    }
    Set<Integer> expectedLeaves = new HashSet<>(Arrays.asList(23));
    assertEquals(expectedLeaves, actualLeaves, "leaves for complex graph should only contain vertex 23");
  }

  @Test
  public void testLeavesSingleNode_ReturnsSingleNodeAsLeaf() {
    Practice practice = new Practice();
    Vertex<Integer> single = new Vertex<>(42);
    single.neighbors = new ArrayList<>();

    Set<Vertex<Integer>> leaves = practice.leaves(single);
    assertEquals(1, leaves.size(), "A single node graph should have one leaf");
    assertEquals(42, leaves.iterator().next().data, "The leaf of a single node graph should be 42");
  }

  @Test
  public void testLeaves_NullInputReturnsEmptySet() {
    Practice practice = new Practice();
    Set<Vertex<Integer>> leaves = practice.leaves(null);
    assertNotNull(leaves, "leaves(null) should not return null");
    assertTrue(leaves.isEmpty(), "leaves(null) should return an empty set");
  }

  // ---------------------------
  // Tests for hasStrictlyIncreasingPath
  // ---------------------------

  @Test
  public void testHasStrictlyIncreasingPath_TrueForComplexGraph() {
    Practice practice = new Practice();
    GraphData graph = buildComplexGraph();
    // For example: 3 -> 7 -> 12 -> 78 -> 91 is strictly increasing.
    assertTrue(practice.hasStrictlyIncreasingPath(graph.v3, graph.v91),
        "Expected a strictly increasing path from vertex 3 to vertex 91");
  }

  @Test
  public void testHasStrictlyIncreasingPath_FalseForComplexGraph() {
    Practice practice = new Practice();
    GraphData graph = buildComplexGraph();
    // There is no strictly increasing path from 12 to 7.
    assertFalse(practice.hasStrictlyIncreasingPath(graph.v12, graph.v7),
        "Expected no strictly increasing path from vertex 12 to vertex 7");
  }

  @Test
  public void testHasStrictlyIncreasingPath_FalseForNoPath() {
    Practice practice = new Practice();
    GraphData graph = buildComplexGraph();
    // There is no path from 45 to 56.
    assertFalse(practice.hasStrictlyIncreasingPath(graph.v45, graph.v56),
        "Expected no strictly increasing path from vertex 45 to vertex 56");
  }

  @Test
  public void testHasStrictlyIncreasingPath_FalseForNonIncreasingPath() {
    Practice practice = new Practice();
    GraphData graph = buildComplexGraph();
    // There is no strictly increasing path from 67 to 78.
    assertFalse(practice.hasStrictlyIncreasingPath(graph.v67, graph.v78),
        "Expected no strictly increasing path from vertex 67 to vertex 78");
  }

  @Test
  public void testHasStrictlyIncreasingPath_TrivialPath_ReturnsTrue() {
    Practice practice = new Practice();
    GraphData graph = buildComplexGraph();
    // A vertex should have a strictly increasing path to itself.
    assertTrue(practice.hasStrictlyIncreasingPath(graph.v3, graph.v3),
        "A vertex should have a strictly increasing path to itself");
  }

  @Test
  public void testHasStrictlyIncreasingPath_NullStartThrowsException() {
    Practice practice = new Practice();
    GraphData graph = buildComplexGraph();
    assertThrows(NullPointerException.class,
        () -> practice.hasStrictlyIncreasingPath(null, graph.v7),
        "hasStrictlyIncreasingPath(null, v7) should throw NullPointerException");
  }

  @Test
  public void testHasStrictlyIncreasingPath_NullEndThrowsException() {
    Practice practice = new Practice();
    GraphData graph = buildComplexGraph();
    assertThrows(NullPointerException.class,
        () -> practice.hasStrictlyIncreasingPath(graph.v3, null),
        "hasStrictlyIncreasingPath(v3, null) should throw NullPointerException");
  }

  // ---------------------------
  // Additional tests: Two-node graphs
  // ---------------------------

  @Test
  public void testTwoNodeGraphIncreasing_PrintVertexVals() {
    Practice practice = new Practice();
    Vertex<Integer> a = new Vertex<>(5);
    Vertex<Integer> b = new Vertex<>(10);
    a.neighbors = new ArrayList<>(Arrays.asList(b));
    b.neighbors = new ArrayList<>();

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    TeeOutputStream tee = new TeeOutputStream(originalOut, baos);
    System.setOut(new PrintStream(tee));
    practice.printVertexVals(a);
    System.out.flush();
    System.setOut(originalOut);
    String output = baos.toString();
    String[] lines = output.split(System.lineSeparator());
    Set<String> printed = new HashSet<>();
    for (String line : lines) {
      if (!line.trim().isEmpty()) {
        printed.add(line.trim());
      }
    }
    Set<String> expected = new HashSet<>(Arrays.asList("5", "10"));
    assertEquals(expected, printed, "printVertexVals for two-node increasing graph should print '5' and '10'");
  }

  @Test
  public void testTwoNodeGraphIncreasing_Reachable() {
    Practice practice = new Practice();
    Vertex<Integer> a = new Vertex<>(5);
    Vertex<Integer> b = new Vertex<>(10);
    a.neighbors = new ArrayList<>(Arrays.asList(b));
    b.neighbors = new ArrayList<>();

    Set<Vertex<Integer>> reachable = practice.reachable(a);
    Set<Integer> actualData = new HashSet<>();
    for (Vertex<Integer> v : reachable) {
      actualData.add(v.data);
    }
    Set<Integer> expected = new HashSet<>(Arrays.asList(5, 10));
    assertEquals(expected, actualData, "reachable for two-node increasing graph should contain 5 and 10");
  }

  @Test
  public void testTwoNodeGraphIncreasing_Max() {
    Practice practice = new Practice();
    Vertex<Integer> a = new Vertex<>(5);
    Vertex<Integer> b = new Vertex<>(10);
    a.neighbors = new ArrayList<>(Arrays.asList(b));
    b.neighbors = new ArrayList<>();
    assertEquals(10, practice.max(a), "max for two-node increasing graph should be 10");
  }

  @Test
  public void testTwoNodeGraphIncreasing_Leaves() {
    Practice practice = new Practice();
    Vertex<Integer> a = new Vertex<>(5);
    Vertex<Integer> b = new Vertex<>(10);
    a.neighbors = new ArrayList<>(Arrays.asList(b));
    b.neighbors = new ArrayList<>();

    Set<Vertex<Integer>> leaves = practice.leaves(a);
    assertEquals(1, leaves.size(), "leaves for two-node increasing graph should return one leaf");
    assertEquals(10, leaves.iterator().next().data, "The leaf of two-node increasing graph should be 10");
  }

  @Test
  public void testTwoNodeGraphIncreasing_StrictlyIncreasingPath() {
    Practice practice = new Practice();
    Vertex<Integer> a = new Vertex<>(5);
    Vertex<Integer> b = new Vertex<>(10);
    a.neighbors = new ArrayList<>(Arrays.asList(b));
    b.neighbors = new ArrayList<>();
    assertTrue(practice.hasStrictlyIncreasingPath(a, b),
        "There should be a strictly increasing path from 5 to 10");
  }

  @Test
  public void testTwoNodeGraphNonIncreasing_StrictlyIncreasingPath() {
    Practice practice = new Practice();
    Vertex<Integer> a = new Vertex<>(10);
    Vertex<Integer> b = new Vertex<>(5);
    a.neighbors = new ArrayList<>(Arrays.asList(b));
    b.neighbors = new ArrayList<>();
    assertFalse(practice.hasStrictlyIncreasingPath(a, b),
        "There should be no strictly increasing path from 10 to 5");
  }

  // ====================================================
  // TeeOutputStream inner class for capturing output
  // ====================================================
  // Used for testing purposes so you can still see your print statements when debugging.
  // You do not need to modify this.
  static class TeeOutputStream extends OutputStream {
    private final OutputStream first;
    private final OutputStream second;

    public TeeOutputStream(OutputStream first, OutputStream second) {
      this.first = first;
      this.second = second;
    }

    @Override
    public void write(int b) {
      try {
        first.write(b);
        second.write(b);
      } catch (Exception e) {
        throw new RuntimeException("Error writing to TeeOutputStream", e);
      }
    }

    @Override
    public void flush() {
      try {
        first.flush();
        second.flush();
      } catch (Exception e) {
        throw new RuntimeException("Error flushing TeeOutputStream", e);
      }
    }

    @Override
    public void close() {
      try {
        first.close();
        second.close();
      } catch (Exception e) {
        throw new RuntimeException("Error closing TeeOutputStream", e);
      }
    }
  }
}
