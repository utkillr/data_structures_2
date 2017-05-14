package Lab_5;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kostya on 12.05.17.
 */
public class Problem_A {

    private static class Edge {
        int from;
        int to;
        int capacity;

        public Edge(int from, int to, int capacity) {
            this.from = from;
            this.to = to;
            this.capacity = capacity;
        }
    }

    private static class Graph {
        Edge[] edges;
        int[][] flow;
        int[][] capacity;

        public Graph(int n, int m) {
            edges = new Edge[m];
            flow = new int[n][n];
            capacity = new int[n][n];
        }

        public void addEdge(int index, int from, int to, int capacity) {
            this.edges[index] = new Edge(from, to, capacity);
            this.capacity[from][to] = capacity;
        }
    }

    private static int FordFulkerson(Graph g, int s, int t, int n) {
        for (Edge e : g.edges) {
            g.flow[e.from][e.to] = 0;
            g.flow[e.to][e.from] = 0;
        }
        boolean[] visited = new boolean[n];
        for (int i = 0; i < n; i++) visited[i] = false;
        int flow = 0;
        int d_flow;
        while ((d_flow = findPath(g, visited, s, t, Integer.MAX_VALUE)) != 0) {
            for (int i = 0; i < n; i++) visited[i] = false;
            flow += d_flow;
        }
        return flow;
    }

    private static int findPath(Graph graph, boolean[] visited, int from, int to, int flow) {
        if (from == to)
            return flow;
        visited[from] = true;
        for (int vertex = 0; vertex < visited.length; vertex++)
            if (!visited[vertex] && graph.capacity[from][vertex] > 0) {
                int d_flow = findPath(graph, visited, vertex, to, Math.min(flow, graph.capacity[from][vertex]));
                if (d_flow > 0) {
                    graph.capacity[from][vertex] -= d_flow;
                    graph.capacity[vertex][from] += d_flow;
                    return d_flow;
                }
            }
        return 0;
    }

    public static void main(String[] args) {
        try (
                BufferedReader in = new BufferedReader(
                        new FileReader("maxflow.in")
                );
                BufferedWriter out = new BufferedWriter(
                        new FileWriter("maxflow.out")
                )
        ) {

            String line = in.readLine();
            String[] numbers = line.split(" ");
            int n = Integer.parseInt(numbers[0]);
            int m = Integer.parseInt(numbers[1]);

            Graph graph = new Graph(n, m);

            for (int index = 0; index < m; index++) {
                line = in.readLine();
                numbers = line.split(" ");
                for (int j = 0; j < n; j++) {
                    int from = Integer.parseInt(numbers[0]);
                    int to = Integer.parseInt(numbers[1]);
                    int cap = Integer.parseInt(numbers[2]);
                    graph.addEdge(index, from - 1, to - 1, cap);
                }
            }

            int flow = FordFulkerson(graph, 0, n - 1, n);

            out.write(Integer.toString(flow));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
