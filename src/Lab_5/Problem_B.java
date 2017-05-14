package Lab_5;

import java.io.*;

/**
 * Created by kostya on 12.05.17.
 */
public class Problem_B {
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

        public Graph(int n, int m, int k) {
            edges = new Edge[k + n + m];
            flow = new int[n + m + 2][n + m + 2];
            capacity = new int[n + m + 2][n + m + 2];
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
                        new FileReader("matching.in")
                );
                BufferedWriter out = new BufferedWriter(
                        new FileWriter("matching.out")
                )
        ) {

            String line = in.readLine();
            String[] numbers = line.split(" ");
            int n = Integer.parseInt(numbers[0]);
            int m = Integer.parseInt(numbers[1]);
            int k = Integer.parseInt(numbers[2]);

            Graph graph = new Graph(n, m, k);

            /*from s to a*/
            for (int j = 0; j < n; j++) {
                graph.addEdge(j, 0, j + 1, 1);
            }
            /*from a to b*/
            for (int index = n; index < n + k; index++) {
                line = in.readLine();
                numbers = line.split(" ");
                int from = Integer.parseInt(numbers[0]);
                int to = Integer.parseInt(numbers[1]) + n;
                graph.addEdge(index, from, to, 1);
            }
            /*from b to t*/
            int from = n + 1;
            for (int j = n + k; j < n + k + m; j++, from++) {
                graph.addEdge(j, from, n + m + 1, 1);
            }

            int flow = FordFulkerson(graph, 0, n + m + 1, n + m + 2);

            out.write(Integer.toString(flow));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
