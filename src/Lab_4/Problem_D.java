package Lab_4;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kostya on 20.04.17.
 */
public class Problem_D {
    final static long INF = Long.MAX_VALUE/2;

    private static class Vertex {
        double distance;
        Vertex parent;
        int number;

        public Vertex(int number) {
            this.number = number;
            distance = Double.MAX_VALUE;
            parent = null;
        }
    }

    private static class Edge implements Comparable<Edge> {
        int from;
        int to;
        long weight;

        public Edge(int from, int to, long weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        public Edge() {
            from = 0;
            to = 0;
            weight = 0;
        }

        @Override
        public int compareTo(Edge o) {
            return Long.compare(weight, o.weight);
        }
    }

    private static class Graph {
        int vertixCount;
        int edgeCount;

        Vertex[] vertices;
        Edge[] edges;

        public Graph(int n, int m) {
            vertixCount = n;
            edgeCount = m;
            vertices = new Vertex[n];
            edges = new Edge[m];
            for (int i = 0; i < n; i++) {
                vertices[i] = new Vertex(i + 1);
            }
        }
    }


    private static void bellman_ford(Graph graph, int s) {
        graph.vertices[s].distance = 0;
        for (int i = 0; i < graph.vertixCount - 1; i++) {
            for (Edge edge : graph.edges) {
                Vertex from = graph.vertices[edge.from - 1];
                Vertex to = graph.vertices[edge.to - 1];
                if (to.distance > from.distance + edge.weight) {
                    to.distance = from.distance + edge.weight;
                    to.parent = from;
                }
            }
        }

        for (int i = 0; i < graph.vertixCount - 1; i++) {
            for (Edge edge : graph.edges) {
                Vertex from = graph.vertices[edge.from - 1];
                Vertex to = graph.vertices[edge.to - 1];
                if ((to.distance != Double.MIN_VALUE && to.distance > from.distance + edge.weight || from.distance == Double.MIN_VALUE))
                    to.distance = Double.MIN_VALUE;
            }
        }
    }

    public static void main(String[] args) {
        try (
                BufferedReader in = new BufferedReader(
                        new FileReader("path.in")
                );
                BufferedWriter out = new BufferedWriter(
                        new FileWriter("path.out")
                )
        ) {

            String line = in.readLine();
            String[] numbers = line.split(" ");
            int n = Integer.parseInt(numbers[0]);
            int m = Integer.parseInt(numbers[1]);
            int s = Integer.parseInt(numbers[2]) - 1;

            Graph graph = new Graph(n, m);

            for (int index = 0; index < m; index++) {
                line = in.readLine();
                numbers = line.split(" ");
                int from = Integer.parseInt(numbers[0]);
                int to = Integer.parseInt(numbers[1]);
                long w = Long.parseLong(numbers[2]);
                graph.edges[index] = new Edge(from, to, w);
            }

            bellman_ford(graph, s);

            for (Vertex vertex : graph.vertices) {
                if (vertex.distance == Double.MIN_VALUE) out.write("-\n");
                else if (vertex.distance == Double.MAX_VALUE) out.write("*\n");
                else out.write((long)vertex.distance + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
