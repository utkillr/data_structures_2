package Lab_2;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Problem_C {

    public enum Color {
        WHITE,
        BLACK1,
        BLACK2;

        public static Color convert(Color color) {
            if (color == BLACK1) return BLACK2;
            if (color == BLACK2) return BLACK1;
            return color;
        }
    }

    private static class Graph {
        Color[] color;
        int[] parent;
        int[] opened;
        int[] closed;
        ArrayList<LinkedList<Integer>> adj;

        int vertixes;
        int edges;

        boolean isBipartite;

        public Graph(int vertixes, int edges) {
            this.vertixes = vertixes;
            this.edges = edges;

            isBipartite = true;

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

    private static void DFS(Graph graph) {
        for (int i = 0; i < graph.color.length; i++) {
            graph.color[i] = Color.WHITE;
            graph.parent[i] = -1;
        }
        Color color = Color.BLACK1;
        for (int i = 0; i < graph.color.length; i++) {
            if (graph.color[i] == Color.WHITE) {
                DFS_Visit(graph, i, color);
                color = Color.convert(color);
            }
        }
    }

    private static void DFS_Visit(Graph graph, int i, Color color) {
        if (!graph.isBipartite) return;
        graph.color[i] = color;
        for (Integer vertix : graph.adj.get(i)) {
            if (graph.color[vertix] == Color.WHITE) {
                graph.parent[vertix] = i;
                DFS_Visit(graph, vertix, Color.convert(color));
                if (!graph.isBipartite) return;
            } else {
                if (graph.color[vertix] == color) {
                    graph.isBipartite = false;
                    return;
                }
            }
        }
    }

    public static void main(String[] args) {
        try (
                BufferedReader in = new BufferedReader(
                        new FileReader("bipartite.in")
                );
                BufferedWriter out = new BufferedWriter(
                        new FileWriter("bipartite.out")
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
                graph.addEdge(j, i);
            }

            DFS(graph);

            if (graph.isBipartite) out.write("YES");
            else out.write("NO");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}