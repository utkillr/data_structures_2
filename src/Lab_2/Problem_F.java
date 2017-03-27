package Lab_2;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Problem_F {

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
        boolean winning[];
        ArrayList<LinkedList<Integer>> adj;

        int vertixes;
        int edges;

        boolean isCycled;

        public Graph(int vertixes, int edges) {
            this.vertixes = vertixes;
            this.edges = edges;

            color = new Color[vertixes];
            parent = new int[vertixes];
            opened = new int[vertixes];
            closed = new int[vertixes];
            winning = new boolean[vertixes];
            adj = new ArrayList<>();
            for (int i = 0; i<vertixes; i++) {
                adj.add(new LinkedList<>());
            }
        }

        public void addEdge(int from, int to) {
            adj.get(from).add(to);
        }
    }

    private static void DFS(Graph graph, int start) {
        for (int i = 0; i < graph.color.length; i++) {
            graph.color[i] = Color.WHITE;
            graph.parent[i] = -1;
        }
        int time = 0;
        DFS_Visit(graph, start, time);
    }

    private static void DFS_Visit(Graph graph, int i, int time) {
        if (graph.isCycled) return;
        graph.color[i] = Color.GREY;
        time++;
        graph.opened[i] = time;
        if (graph.adj.get(i).isEmpty()) {
            graph.winning[i] = false;
        }
        for (Integer vertix : graph.adj.get(i)) {
            if (graph.color[vertix] == Color.WHITE) {
                graph.parent[vertix] = i;
                DFS_Visit(graph, vertix, time);
            }
        }
        graph.winning[i] = false;
        for (Integer vertix : graph.adj.get(i)) {
            graph.winning[i] = graph.winning[i] || !graph.winning[vertix];
        }
        graph.color[i] = Color.BLACK;
        graph.closed[i] = time;
    }

    public static void main(String[] args) {
        try (
                BufferedReader in = new BufferedReader(
                        new FileReader("game.in")
                );
                BufferedWriter out = new BufferedWriter(
                        new FileWriter("game.out")
                )
        ) {

            String line = in.readLine();
            String[] numbers = line.split(" ");
            int n = Integer.parseInt(numbers[0]);
            int m = Integer.parseInt(numbers[1]);
            int s = Integer.parseInt(numbers[2]);

            Graph graph = new Graph(n, m);

            while (m-- > 0) {
                line = in.readLine();
                numbers = line.split(" ");
                int i = Integer.parseInt(numbers[0]) - 1;
                int j = Integer.parseInt(numbers[1]) - 1;
                graph.addEdge(i, j);
            }

            DFS(graph, s-1);

            if (graph.winning[s-1]) {
                out.write("First player wins");
            } else {
                out.write("Second player wins");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}