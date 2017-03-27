package Lab_2;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class Problem_D {

    public enum Color {
        WHITE,
        BLACK,
        GREY
    }

    public static class Pair implements Comparable<Pair> {
        int vertix;
        int timestamp;

        public Pair(int vertix, int timestamp) {
            this.vertix = vertix;
            this.timestamp = timestamp;
        }

        //reversive comparator
        @Override
        public int compareTo(Pair o) {
            if (timestamp > o.timestamp) {
                return -1;
            } else if (timestamp < o.timestamp) {
                return 1;
            } else return 0;
        }
    }

    private static class Graph {
        Color[] color;
        int[] parent;
        int[] opened;
        Pair[] closed;
        ArrayList<LinkedList<Integer>> adj;
        int[] component;

        int vertixes;
        int edges;
        int components;

        boolean isCycled;

        public Graph(int vertixes, int edges) {
            this.vertixes = vertixes;
            this.edges = edges;

            isCycled = false;

            color = new Color[vertixes];
            parent = new int[vertixes];
            opened = new int[vertixes];
            closed = new Pair[vertixes];
            component = new int[vertixes];
            adj = new ArrayList<>();
            for (int i = 0; i<vertixes; i++)
                adj.add(new LinkedList<>());
        }

        public void addEdge(int from, int to) {
            adj.get(from).add(to);
        }

        public void sortVertixes() {
            Arrays.sort(closed);
        }

        public void transpose() {
            ArrayList<LinkedList<Integer>> adjTransposed = new ArrayList<>(adj.size());
            for (int i = 0; i<vertixes; i++)
                adjTransposed.add(new LinkedList<>());

            for (int i = 0; i < adj.size(); i++) {
                LinkedList<Integer> list = adj.get(i);
                for (int j = 0; j<list.size(); j++) {
                    int vertix = list.get(j);
                    LinkedList<Integer> listTransposed = adjTransposed.get(vertix);
                    listTransposed.add(i);
                }
            }
            adj = adjTransposed;
        }
    }

    private static void DFS(Graph graph) {
        for (int i = 0; i < graph.color.length; i++) {
            graph.color[i] = Color.WHITE;
            graph.parent[i] = -1;
        }
        int time = 0;
        for (int i = 0; i < graph.color.length; i++) {
            if (graph.color[i] == Color.WHITE)
                time = DFS_Visit(graph, i, time);
        }
    }

    private static void DFS_Transposed(Graph graph) {
        for (int i = 0; i < graph.color.length; i++) {
            graph.color[i] = Color.WHITE;
            graph.parent[i] = -1;
        }
        int component = 0;
        for (int i = 0; i < graph.color.length; i++) {
            if (graph.color[graph.closed[i].vertix] == Color.WHITE) {
                DFS_Visit_Transposed(graph, graph.closed[i].vertix, ++component);
                graph.components = component;
            }
        }
    }

    private static int DFS_Visit(Graph graph, Integer i, int time) {
        graph.color[i] = Color.GREY;
        time++;
        graph.opened[i] = time;
        for (Integer vertix : graph.adj.get(i)) {
            if (graph.color[vertix] == Color.WHITE) {
                graph.parent[vertix] = i;
                time = DFS_Visit(graph, vertix, time);
            }
        }
        graph.color[i] = Color.BLACK;
        graph.closed[i] = new Pair(i, time);
        return time + 1;
    }

    private static void DFS_Visit_Transposed(Graph graph, Integer i, int component) {
        graph.color[i] = Color.GREY;
        graph.component[i] = component;
        for (Integer vertix : graph.adj.get(i)) {
            if (graph.color[vertix] == Color.WHITE) {
                graph.parent[vertix] = i;
                DFS_Visit_Transposed(graph, vertix, component);
            }
        }
        graph.color[i] = Color.BLACK;
    }


    public static void main(String[] args) {
        try (
                BufferedReader in = new BufferedReader(
                        new FileReader("cond.in")
                );
                BufferedWriter out = new BufferedWriter(
                        new FileWriter("cond.out")
                )
        ) {

            String line = in.readLine();
            String[] numbers = line.split(" ");
            int n = Integer.parseInt(numbers[0]);
            int m = Integer.parseInt(numbers[1]);

            Graph graph = new Graph(n, m);

            while (m-- > 0) {
                line = in.readLine();
                numbers = line.split(" ");
                int i = Integer.parseInt(numbers[0]) - 1;
                int j = Integer.parseInt(numbers[1]) - 1;
                graph.addEdge(i, j);
            }

            DFS(graph);
            graph.sortVertixes();
            graph.transpose();
            DFS_Transposed(graph);

            out.write(graph.components + "\n");
            for (int i = 0; i < graph.vertixes; i++) {
                out.write(graph.component[i] + " ");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}