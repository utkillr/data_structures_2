package Lab_3;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by kostya on 10.04.17.
 */
public class Problem_D {
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

    public enum Color {
        WHITE,
        BLACK,
        GREY
    }

    public static void main(String[] args) {
        try (
                BufferedReader in = new BufferedReader(
                        new FileReader("chinese.in")
                );
                BufferedWriter out = new BufferedWriter(
                        new FileWriter("chinese.out")
                )
        ) {

            String line = in.readLine();
            String[] numbers = line.split(" ");
            int n = Integer.parseInt(numbers[0]);
            int m = Integer.parseInt(numbers[1]);

            ArrayList<Edge> edges = new ArrayList<>();
            ArrayList<ArrayList<Integer>> adj = new ArrayList<>();
            for (int i = 0; i < n; i++) adj.add(new ArrayList<>());

            for (int index = 0; index < m; index++) {
                line = in.readLine();
                numbers = line.split(" ");
                int i = Integer.parseInt(numbers[0]);
                int j = Integer.parseInt(numbers[1]);
                int k = Integer.parseInt(numbers[2]);
                edges.add(new Edge(i - 1, j - 1, k));
                adj.get(i - 1).add(j - 1);
            }

            if (!DFS(0, adj, n)) {
                out.write("NO");
            } else {
                out.write("YES\n");
                long weight = find_MST(edges, n, 0);
                out.write(Long.toString(weight));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean DFS(int root, ArrayList<ArrayList<Integer>> adj, int n) {
        Color[] colors = new Color[n];
        for (int i = 0; i < n; i++) {
            colors[i] = Color.WHITE;
        }
        int visited = DFS_Visit(adj, root, colors);

        return visited == n;
    }

    private static int DFS_Visit(ArrayList<ArrayList<Integer>> adj, int i, Color[] colors) {
        colors[i] = Color.GREY;
        int visited = 0;
        for (Integer vertex : adj.get(i)) {
            if (colors[vertex] == Color.WHITE) {
                visited += DFS_Visit(adj, vertex, colors);
            }
        }

        colors[i] = Color.BLACK;
        return visited + 1;
    }




    private static int[] condensate(ArrayList<Edge> edges, ArrayList<ArrayList<Integer>> adj, int n) {
        Pair[] closed = DFS_Cond(adj, n);
        Arrays.sort(closed);
        ArrayList<ArrayList<Integer>> newAdj = new ArrayList<>();
        for (int i = 0; i < n; i++) newAdj.add(new ArrayList<>());
        for (Edge edge : edges) {
            newAdj.get(edge.to).add(edge.from);
        }
        return DFS_Transposed_Cond(newAdj, n, closed);
    }

    private static Pair[] DFS_Cond(ArrayList<ArrayList<Integer>> adj, int n) {
        Color[] colors = new Color[n];
        for (int i = 0; i < n; i++) {
            colors[i] = Color.WHITE;
        }
        Pair[] closed = new Pair[n];
        int time = 0;
        for (int i = 0; i < n; i++) {
            if (colors[i] == Color.WHITE)
                time = DFS_Visit_Cond(adj, i, colors, closed, time);
        }


        return closed;
    }

    private static int DFS_Visit_Cond(ArrayList<ArrayList<Integer>> adj, int i, Color[] colors, Pair[] closed, int time) {
        colors[i] = Color.GREY;
        time++;
        for (Integer vertex : adj.get(i)) {
            if (colors[vertex] == Color.WHITE) {
                time = DFS_Visit_Cond(adj, vertex, colors, closed, time);
            }
        }

        colors[i] = Color.BLACK;
        closed[i] = new Pair(i, time);
        return time + 1;
    }

    private static int[] DFS_Transposed_Cond(ArrayList<ArrayList<Integer>> adj, int n, Pair[] closed) {
        Color[] colors = new Color[n];
        for (int i = 0; i < n; i++) {
            colors[i] = Color.WHITE;
        }

        int components[] = new int[n];
        int component = -1;

        for (int i = 0; i < n; i++) {
            if (colors[closed[i].vertix] == Color.WHITE) {
                DFS_Visit_Transposed_Cond(adj, closed[i].vertix, colors, ++component, components);
            }
        }

        return components;
    }

    private static void DFS_Visit_Transposed_Cond(ArrayList<ArrayList<Integer>> adj, int i, Color[] colors, int component, int[] components) {
        colors[i] = Color.GREY;
        components[i] = component;
        for (Integer vertex : adj.get(i)) {
            if (colors[vertex] == Color.WHITE) {
                DFS_Visit_Transposed_Cond(adj, vertex, colors, component, components);
            }
        }

        colors[i] = Color.BLACK;
    }

    private static int min(int a, int b) {
        if (a < b) return a;
        else return b;
    }

    private static int getMax(int[] arr) {
        int max = Integer.MIN_VALUE;
        for (int a : arr) {
            if (a > max) max = a;
        }
        return max;
    }

    private static long find_MST(ArrayList<Edge> edges, int n, int root) {
        long res = 0;
        int[] minEdge = new int[n];
        for (int i = 0; i < n; i++) minEdge[i] = Integer.MAX_VALUE;

        edges.forEach(edge ->
            minEdge[edge.to] = min(edge.weight, minEdge[edge.to])
        );

        for (int i = 0; i < n; i++) {
            if (i != root) {
                res += minEdge[i];
            }
        }

        ArrayList<Edge> zeroEdges = new ArrayList<>();
        ArrayList<ArrayList<Integer>> zeroAdj = new ArrayList<>();
        for (int i = 0; i < n; i++) zeroAdj.add(new ArrayList<>());
        for (Edge edge : edges) {
            if (edge.weight == minEdge[edge.to]) {
                zeroEdges.add(new Edge(edge.from, edge.to, 0));
                zeroAdj.get(edge.from).add(edge.to);
            }
            edge.weight -= minEdge[edge.to];
        }

        if (DFS(root, zeroAdj, n)) return res;

        int[] newComponents = condensate(zeroEdges, zeroAdj, n);

        ArrayList<Edge> newEdges = new ArrayList<>();
        for (Edge edge : edges) {
            if (newComponents[edge.from] != newComponents[edge.to]) {
                newEdges.add(new Edge(newComponents[edge.from], newComponents[edge.to], edge.weight));
            }
        }

        res += find_MST(newEdges, getMax(newComponents) + 1, newComponents[root]);
        return res;
    }
}