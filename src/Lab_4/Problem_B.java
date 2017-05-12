package Lab_4;

import java.io.*;
import java.util.Arrays;

/**
 * Created by kostya on 20.04.17.
 */
public class Problem_B {
    private static class Graph {
        int vertixes;

        long[][] weight;

        public Graph(int vertixes) {
            this.vertixes = vertixes;
            weight = new long[vertixes][vertixes];
            for (int i = 0; i < vertixes; i++) {
                Arrays.fill(weight[i], Long.MAX_VALUE / 2);
            }
        }

        public void addEdge(int from, int to, long w) {
            weight[from][to] = w;
        }
    }

    private static void floyd_warshall(Graph graph) {
        for (int k = 0; k < graph.vertixes; k++) {
            for (int i = 0; i < graph.vertixes; i++) {
                for (int j = 0; j < graph.vertixes; j++) {
                    if (i == j) graph.weight[i][j] = 0;
                    else graph.weight[i][j] = Math.min(
                            graph.weight[i][j],
                            graph.weight[i][k] + graph.weight[k][j]
                    );
                }
            }
        }
    }

    public static void main(String[] args) {
        try (
                BufferedReader in = new BufferedReader(
                        new FileReader("pathsg.in")
                );
                BufferedWriter out = new BufferedWriter(
                        new FileWriter("pathsg.out")
                )
        ) {

            String line = in.readLine();
            String[] numbers = line.split(" ");
            int n = Integer.parseInt(numbers[0]);
            int m = Integer.parseInt(numbers[1]);

            Graph graph = new Graph(n);

            for (int index = 0; index < m; index++) {
                line = in.readLine();
                numbers = line.split(" ");
                int from = Integer.parseInt(numbers[0]) - 1;
                int to = Integer.parseInt(numbers[1]) - 1;
                int w = Integer.parseInt(numbers[2]);
                graph.addEdge(from, to, w);
            }

            floyd_warshall(graph);

            for (int i = 0; i < graph.vertixes; i++) {
                for (int j = 0; j < graph.vertixes; j++) {
                    out.write(graph.weight[i][j] + " ");
                }
                out.write("\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
