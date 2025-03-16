import java.util.HashSet;
import java.util.Set;

/**
 * A utility class providing various graph traversal methods using DFS.
 */
public class Practice {

  /**
   * Prints the value of every vertex reachable from the given starting vertex,
   * including the starting vertex itself. Each value is printed on a separate line.
   * The order of printing is unimportant.
   *
   * Each vertex's value should be printed only once, even if it is reachable via multiple paths.
   * It is guaranteed that no two vertices will have the same value.
   *
   * If the given vertex is null, this method prints nothing.
   *
   * @param vertex The starting vertex for the traversal.
   */
  public <T> void printVertexVals(Vertex<T> vertex) {
    if(vertex == null)return;

    Set<Vertex<T>> visited = new HashSet<>();

    printVals(vertex, visited);
  }
  private <T> void printVals(Vertex<T> vertex, Set<Vertex<T>> visited){
    if(vertex == null) return;
    if(visited.contains(vertex)) return;

    System.out.println(vertex.data);
    visited.add(vertex);

    if(vertex.neighbors != null){
      for(Vertex<T> neighbor : vertex.neighbors){
        printVals(neighbor, visited);
      }
    }
  }

  /**
   * Returns a set of all vertices reachable from the given starting vertex,
   * including the starting vertex itself.
   *
   * If the given vertex is null, an empty set is returned.
   *
   * @param vertex The starting vertex for the traversal.
   * @return A set containing all reachable vertices, or an empty set if vertex is null.
   */
  public <T> Set<Vertex<T>> reachable(Vertex<T> vertex) {
    if(vertex == null) return new HashSet<>();

    Set<Vertex<T>> reachable = new HashSet<>();

    reachable(vertex, reachable);
    return reachable;
  }
  private <T> void reachable(Vertex<T> vertex, Set<Vertex<T>> reachable){
    if( vertex == null || reachable.contains(vertex)) return;

    reachable.add(vertex);
    if(vertex.neighbors == null) return;
    for(Vertex<T> neighbor : vertex.neighbors){
      reachable(neighbor, reachable);
    }
  }

  /**
   * Returns the maximum value among all vertices reachable from the given starting vertex,
   * including the starting vertex itself.
   *
   * If the given vertex is null, the method returns Integer.MIN_VALUE.
   *
   * @param vertex The starting vertex for the traversal.
   * @return The maximum value of any reachable vertex, or Integer.MIN_VALUE if vertex is null.
   */
  public int max(Vertex<Integer> vertex) {
    if(vertex == null) return Integer.MIN_VALUE;

    Set<Vertex<Integer>> visited = new HashSet<>();

    return max(vertex, visited);
  }
  private int max(Vertex<Integer> vertex, Set<Vertex<Integer>> visited){
    if(vertex == null) return Integer.MIN_VALUE;
    if(visited.contains(vertex)) return Integer.MIN_VALUE;
    visited.add(vertex);
    int maxVal = vertex.data;

    for(Vertex<Integer> neighbor : vertex.neighbors){
    
      maxVal =  Math.max(max(neighbor, visited), maxVal);
    }
    return maxVal;
  }

  /**
   * Returns a set of all leaf vertices reachable from the given starting vertex.
   * A vertex is considered a leaf if it has no outgoing edges (no neighbors).
   *
   * The starting vertex itself is included in the set if it is a leaf.
   *
   * If the given vertex is null, an empty set is returned.
   *
   * @param vertex The starting vertex for the traversal.
   * @return A set containing all reachable leaf vertices, or an empty set if vertex is null.
   */
  public <T> Set<Vertex<T>> leaves(Vertex<T> vertex) {
    if(vertex == null) return new HashSet<>();
    Set<Vertex<T>> leaVertexs = new HashSet<>();
    if(vertex.neighbors == null) {
      leaVertexs.add(vertex);
      return leaVertexs;
    }
    leavesHelper(vertex, leaVertexs);
    return leaVertexs;
  }
  private <T> void leavesHelper(Vertex<T> vertex, Set<Vertex<T>> leaves){
    if(vertex == null) return;
    if(vertex.neighbors != null){
      for(Vertex<T> neighbor : vertex.neighbors){
        leavesHelper(neighbor, leaves);
      }
    }
    leaves.add(vertex);
  }

  /**
   * Determines whether there exists a strictly increasing path from the given start vertex
   * to the target vertex.
   *
   * A path is strictly increasing if each visited vertex has a value strictly greater than
   * (not equal to) the previous vertex in the path.
   *
   * If either start or end is null, a NullPointerException is thrown.
   *
   * @param start The starting vertex.
   * @param end The target vertex.
   * @return True if a strictly increasing path exists, false otherwise.
   * @throws NullPointerException if either start or end is null.
   */
  public boolean hasStrictlyIncreasingPath(Vertex<Integer> start, Vertex<Integer> end) {
    return false;
  }
}
