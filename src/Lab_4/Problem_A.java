package Lab_4;

import java.io.*;
import java.util.Arrays;

public class Problem_A {
    private static class Graph {
        int vertixes;

        long[][] weight;
        long[] distance;
        boolean[] used;

        public Graph(int vertixes) {
            this.vertixes = vertixes;
            distance = new long[vertixes];
            weight = new long[vertixes][vertixes];
            used = new boolean[vertixes];
        }

        public void addEdge(int from, int to, long w) {
            weight[from][to] = w;
        }
    }

    private static int getNextVertex(Graph graph) {
        int vertex = -1;
        for (int i = 0; i < graph.vertixes; i++) {
            if (!graph.used[i] &&
                    (vertex == -1 ||
                            graph.distance[i] < graph.distance[vertex]))
                vertex = i;
        }
        return vertex;
    }

    private static void dijkstra(Graph graph, int s) {
        Arrays.fill(graph.distance, Long.MAX_VALUE);
        Arrays.fill(graph.used, false);

        graph.distance[s] = 0;
        int current = getNextVertex(graph);
        while (current != -1) {
            graph.used[current] = true;
            for (int i = 0; i < graph.vertixes; i++) {
                long currentWeight = graph.weight[current][i];
                if (currentWeight == -1 || i == current) continue;
                if (graph.distance[current] + currentWeight < graph.distance[i])
                    graph.distance[i] = graph.distance[current] + currentWeight;
            }

            current = getNextVertex(graph);
        }
    }

    public static void main(String[] args) {
        try (
                BufferedReader in = new BufferedReader(
                        new FileReader("pathmgep.in")
                );
                BufferedWriter out = new BufferedWriter(
                        new FileWriter("pathmgep.out")
                )
        ) {

            String line = in.readLine();
            String[] numbers = line.split(" ");
            int n = Integer.parseInt(numbers[0]);
            int s = Integer.parseInt(numbers[1]);
            int f = Integer.parseInt(numbers[2]);

            Graph graph = new Graph(n);

            for (int index = 0; index < n; index++) {
                line = in.readLine();
                numbers = line.split(" ");
                for (int j = 0; j < n; j++) {
                    graph.addEdge(index, j, Long.parseLong(numbers[j]));
                }
            }

            dijkstra(graph, s - 1);
            if (graph.distance[f - 1] != Long.MAX_VALUE) {
                out.write(Long.toString(graph.distance[f - 1]));
            } else {
                out.write("-1");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
