package edu.berkeley.cs186.database.concurrency;

import java.util.*;

/**
 * A waits for graph for the lock manager (used to detect if
 * deadlock will occur and throw a DeadlockException if it does).
 */
public class WaitsForGraph {

  // We store the directed graph as an adjacency list where each node (transaction) is
  // mapped to a list of the nodes it has an edge to.
  private Map<Long, ArrayList<Long>> graph;

  public WaitsForGraph() {
    graph = new HashMap<Long, ArrayList<Long>>();
  }

  public boolean containsNode(long transNum) {
    return graph.containsKey(transNum);
  }

  protected void addNode(long transNum) {
    if (!graph.containsKey(transNum)) {
      graph.put(transNum, new ArrayList<Long>());
    }
  }

  protected void addEdge(long from, long to) {
    if (!this.edgeExists(from, to)) {
      ArrayList<Long> edges = graph.get(from);
      edges.add(to);
    }
  }

  protected void removeEdge(long from, long to) {
    if (this.edgeExists(from, to)) {
      ArrayList<Long> edges = graph.get(from);
      edges.remove(to);
    }
  }

  protected boolean edgeExists(long from, long to) {
    if (!graph.containsKey(from)) {
      return false;
    }
    ArrayList<Long> edges = graph.get(from);
    return edges.contains(to);
  }

  /**
   * Checks if adding the edge specified by to and from would cause a cycle in this
   * WaitsForGraph. Does not actually modify the graph in any way.
   * @param from the transNum from which the edge points
   * @param to the transNum to which the edge points
   * @return
   */
  protected boolean edgeCausesCycle(long from, long to) {
    //TODO: Implement Me!!
    Set<Long> visited = new HashSet<>();
    Stack<Long> stack = new Stack<>();
    // add the edge
    if (!this.containsNode(from)) {
      this.addNode(from);
    }
    if (!this.containsNode(to)) {
      this.addNode(to);
    }
    this.addEdge(from, to);
    stack.add(to);
    while (!stack.empty()) {
      long popped = stack.pop();
      if (popped == from) {
        this.removeEdge(from, to);
        return true;
      }
      for (long l : graph.get(popped)) {
        if (!visited.contains(l)) {
          stack.add(l);
          visited.add(l);
        }

      }
    }
    this.removeEdge(from, to);
    return false;
  }

}
