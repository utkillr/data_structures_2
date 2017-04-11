package Lab_3;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class Problem_C {

    private static double EPS = 1e-9;

    private static class MyQueue extends LinkedList<Integer> {
        public int getMin(Graph graph) {
            int minIndex = 0;
            int minKey = Integer.MAX_VALUE;
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

    private static class Edge implements Comparable<Edge> {
        int from;
        int to;
        int weight;

        public Edge(int from, int to, int weight) {
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
            return Integer.compare(weight, o.weight);
        }
    }

    private static class Graph {
        int[] parent;
        int[] keys;
        int[] weights;

        Edge[] edgeList;

        int vertexes;
        int edges;

        int minWeight;

        public Graph(int vertexes, int edges) {
            this.vertexes = vertexes;
            this.edges = edges;

            this.minWeight = 0;

            parent = new int[vertexes];
            keys = new int[vertexes];
            weights = new int[vertexes];

            edgeList = new Edge[edges];
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
            String[] numbers = line.split(" ");
            int n = Integer.parseInt(numbers[0]);
            int m = Integer.parseInt(numbers[1]);

            Graph graph = new Graph(n, m);

            for (int index = 0; index < m; index++) {
                line = in.readLine();
                numbers = line.split(" ");
                int i = Integer.parseInt(numbers[0]);
                int j = Integer.parseInt(numbers[1]);
                int k = Integer.parseInt(numbers[2]);
                graph.edgeList[index] = new Edge(i - 1, j - 1, k);
            }

            MST_Kruskal(graph);

            out.write(Integer.toString(graph.minWeight));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void MST_Kruskal(Graph graph) {
        Arrays.sort(graph.edgeList, Edge :: compareTo);
        int[] treeNumber = new int[graph.vertexes];
        for (int i = 0; i < graph.vertexes; i++) {
            treeNumber[i] = i;
        }

        for (int i = 0; i < graph.edgeList.length; i++) {
            Edge edge = graph.edgeList[i];
            if (treeNumber[edge.from] != treeNumber[edge.to]) {
                graph.minWeight += edge.weight;

                int oldTreeNumber = treeNumber[edge.to];
                int newTreeNumber = treeNumber[edge.from];

                for (int j = 0; j < treeNumber.length; j++) {
                    if (treeNumber[j] == oldTreeNumber)
                        treeNumber[j] = newTreeNumber;
                }
            }
        }
    }
}