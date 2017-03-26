package Lab_2;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Problem_A {

    public enum Color {
        WHITE,
        BLACK,
        GREY
    }

    private static class Graph {
        Color[] color;
        int[] parent;
        int[] opened;
        int[] closed;
        ArrayList<LinkedList<Integer>> adj;

        int vertixes;
        int edges;

        boolean isCycled;

        //needed in this work
        int vertixesLeft;

        public Graph(int vertixes, int edges) {
            this.vertixes = vertixes;
            this.edges = edges;

            isCycled = false;

            color = new Color[vertixes];
            parent = new int[vertixes];
            opened = new int[vertixes];
            closed = new int[vertixes];
            adj = new ArrayList<>();
            for (int i = 0; i<vertixes; i++)
                adj.add(new LinkedList<>());
        }

        public void addEdge(int from, int to) {
            adj.get(from).add(to);
        }
    }

    private static void DFS(Graph graph, int[] list) {
        for (int i = 0; i < graph.color.length; i++) {
            graph.color[i] = Color.WHITE;
            graph.parent[i] = -1;
        }
        int time = 0;
        graph.vertixesLeft = graph.vertixes;
        for (int i = 0; i < graph.color.length; i++) {
            if (graph.color[i] == Color.WHITE)
                DFS_Visit(graph, i, time, list);
        }
    }

    private static void DFS_Visit(Graph graph, int i, int time, int[] list) {
        if (graph.isCycled) return;
        graph.color[i] = Color.GREY;
        time++;
        graph.opened[i] = time;
        for (Integer vertix : graph.adj.get(i)) {
            if (graph.color[vertix] == Color.WHITE) {
                graph.parent[vertix] = i;
                DFS_Visit(graph, vertix, time, list);
            } else {
                if (graph.color[vertix] == Color.GREY) {
                    graph.isCycled = true;
                    return;
                }
            }
        }
        graph.color[i] = Color.BLACK;
        graph.closed[i] = time;
        list[--graph.vertixesLeft] = i;
    }

    private static int[] Topological_Sort(Graph graph) {
        int[] list = new int[graph.vertixes];
        DFS(graph, list);
        return list;
    }


    public static void main(String[] args) {
        try (
                BufferedReader in = new BufferedReader(
                        new FileReader("topsort.in")
                );
                BufferedWriter out = new BufferedWriter(
                        new FileWriter("topsort.out")
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

            int[] list = Topological_Sort(graph);

            if (graph.isCycled) {
                out.write("-1");
            } else {
                for (int vertix : list) {
                    out.write(vertix + 1 + " ");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}