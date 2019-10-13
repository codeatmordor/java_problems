package org.gks.problems.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectedComponentsAdjacencyList {

  static class Edge {
    int from;
    int to;
    int weight;

    /**
     * @param from
     * @param to
     * @param weight
     */
    public Edge(int from, int to, int weight) {
      super();
      this.from = from;
      this.to = to;
      this.weight = weight;
    }
  }

  static int countConnectedComponents(Map<Integer, List<Edge>> graph, int n) {

    UnionFind uf = new UnionFind(n);

    for (int i = 0; i < n; i++) {
      List<Edge> edges = graph.get(i);
      if (edges != null) {
        for (Edge edge : edges) {
          uf.unify(edge.from, edge.to);
        }
      }
    }

    return uf.components();
  }

  // Finding connected components example
  public static void main(String[] args) {

    final int numNodes = 7;
    Map<Integer, List<Edge>> graph = new HashMap<>();

    // Setup a graph with four connected components
    // namely: {0,1,2}, {3,4}, {5}, {6}
    addUndirectedEdge(graph, 0, 1, 1);
    addUndirectedEdge(graph, 1, 2, 1);
    addUndirectedEdge(graph, 2, 0, 1);
    addUndirectedEdge(graph, 3, 4, 1);
    addUndirectedEdge(graph, 5, 5, 1); // Self loop

    int components = countConnectedComponents(graph, numNodes);
    System.out.printf("Number of components: %d\n", components);
  }

  // Helper method to setup graph
  private static void addUndirectedEdge(Map<Integer, List<Edge>> graph, int from, int to,
      int cost) {
    List<Edge> list = graph.get(from);
    if (list == null) {
      list = new ArrayList<>();
      graph.put(from, list);
    }
    list.add(new Edge(from, to, cost));
    list.add(new Edge(to, from, cost));
  }
}


class UnionFind {
  private int size;

  // used to track size of each component
  private int[] sz;

  // id[i] points to the parent of i, if id[i]=i then i is the root node
  private int[] id;

  private int numComponents;

  public UnionFind(int size) {

    if (size <= 0)
      throw new IllegalArgumentException("Size <= 0 is not allowed");

    this.size = numComponents = size;
    sz = new int[size];
    id = new int[size];

    for (int i = 0; i < size; i++) {
      id[i] = i; // Link to itself (self root)
      sz[i] = 1; // Each component is originally of size one
    }
  }

  // Find which component/set 'p' belongs to, takes amortized constant time.
  public int find(int p) {
    // Find the root of the component/set
    int root = p;
    while (root != id[root])
      root = id[root];

    // Compress the path leading back to the root.
    // Doing this operation is called "path compression"
    // and is what gives us amortized constant time complexity.
    while (p != root) {
      int next = id[p];
      id[p] = root;
      p = next;
    }

    return root;
  }

  // Return whether or not the elements 'p' and
  // 'q' are in the same components/set.
  public boolean connected(int p, int q) {
    return find(p) == find(q);
  }

  // Return the size of the components/set 'p' belongs to
  public int componentSize(int p) {
    return sz[find(p)];
  }

  // Return the number of elements in this UnionFind/Disjoint set
  public int size() {
    return size;
  }

  // Returns the number of remaining components/sets
  public int components() {
    return numComponents;
  }

  // Unify the components/sets containing elements 'p' and 'q'
  public void unify(int p, int q) {

    int root1 = find(p);
    int root2 = find(q);

    // These elements are already in the same group!
    if (root1 == root2)
      return;

    // Merge smaller component/set into the larger one.
    if (sz[root1] < sz[root2]) {
      sz[root2] += sz[root1];
      id[root1] = root2;
    } else {
      sz[root1] += sz[root2];
      id[root2] = root1;
    }

    // Since the roots found are different we know that the
    // number of components/sets has decreased by one
    numComponents--;
  }
}

