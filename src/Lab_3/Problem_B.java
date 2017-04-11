package Lab_3;

import java.io.*;
import java.util.*;

/**
 * Created by kostya on 09.04.17.
 */
public class Problem_B {

    private static double EPS = 1e-9;

    private static class MyQueue extends LinkedList<Integer> {
        public int getMin(Graph graph) {
            int minIndex = 0;
            double minKey = Double.MAX_VALUE;
            for (int elem : this) {
                if (minKey > graph.keys[elem]) {
                    minKey = graph.keys[elem];
                    minIndex = elem;
                }
            }
            this.remove((Object)minIndex);
            return minIndex;
        }

        public MyQueue(int n) {
            for (int i = 0; i<n; i++) {
                this.add(i);
            }
        }
    }

    private static class Pair {
        int x;
        int y;

        public Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Pair() {
            x = 0;
            y = 0;
        }

        public void set(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public double getLength(Pair b) {
            return Math.sqrt((x-b.x)*(x-b.x)+(y-b.y)*(y-b.y));
        }
    }

    private static class Graph {
        int[] parent;
        double[] keys;
        Pair[] coords;

        ArrayList<LinkedList<Integer>> adj;

        int vertixes;
        int edges;

        public Graph(int vertixes, int edges) {
            this.vertixes = vertixes;
            this.edges = edges;

            parent = new int[vertixes];
            keys = new double[vertixes];

            coords = new Pair[vertixes];

            for (int i = 0; i < vertixes; i++)
                coords[i] = new Pair();

            adj = new ArrayList<>();
            for (int i = 0; i<vertixes; i++)
                adj.add(new LinkedList<>());
        }

        public void addEdge(int from, int to) {
            adj.get(from).add(to);
        }

        public double weight(int vertix_a, int vertix_b) {
            return coords[vertix_a].getLength(coords[vertix_b]);
        }
    }

    public static void main(String[] args) {
        try (
                BufferedReader in = new BufferedReader(
                        new FileReader("spantree2.in")
                );
                BufferedWriter out = new BufferedWriter(
                        new FileWriter("spantree2.out")
                )
        ) {

            String line = in.readLine();
            String[] numbers;
            int n = Integer.parseInt(line);

            Graph graph = new Graph(n, 0);

            for (int index = 0; index < n; index++) {
                line = in.readLine();
                numbers = line.split(" ");
                int i = Integer.parseInt(numbers[0]);
                int j = Integer.parseInt(numbers[1]);
                graph.coords[index].set(i, j);
            }

            MST_Prim(graph);

            double weight = 0;
            for (int i = 1; i<n; i++) {
                weight += graph.keys[i];
            }

            out.write(Double.toString(weight));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void MST_Prim(Graph graph) {
        for (int i = 0; i < graph.vertixes; i++) {
            graph.keys[i] = Double.MAX_VALUE;
            graph.parent[i] = -1;
        }
        graph.keys[0] = 0;

        MyQueue queue = new MyQueue(graph.vertixes);

        while (!queue.isEmpty()) {
            int pooled = queue.getMin(graph);
            queue.forEach(vertix -> {
                if (graph.weight(pooled, vertix) < graph.keys[vertix]) {
                    graph.parent[vertix] = pooled;
                    graph.keys[vertix] = graph.weight(pooled, vertix);
                }
            });
        }
    }
}