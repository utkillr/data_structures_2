package Lab_4;

import javafx.util.Pair;

import java.io.*;
import java.util.*;

/**
 * Created by kostya on 20.04.17.
 */
public class Problem_C {

    static long INF = Long.MAX_VALUE / 2;

    static class Vertex {
        long distance;
        boolean visited;
        List<Pair<Vertex, Long>> adj;
        int number;

        public Vertex(int number) {
            this.number = number;
            distance = INF;
            visited = false;
            adj = new ArrayList<>();
        }
    }

    public static void main(String[] args) {
        try (
                BufferedReader in = new BufferedReader(
                        new FileReader("pathbgep.in")
                );
                BufferedWriter out = new BufferedWriter(
                        new FileWriter("pathbgep.out")
                )
        ) {

            String line = in.readLine();
            String[] numbers = line.split(" ");
            int n = Integer.parseInt(numbers[0]);
            int m = Integer.parseInt(numbers[1]);

            Vertex[] vertices = new Vertex[n];
            for (int i = 0; i < n; i++) {
                vertices[i] = new Vertex(i);
            }

            for (int index = 0; index < m; index++) {
                line = in.readLine();
                numbers = line.split(" ");
                int from = Integer.parseInt(numbers[0]) - 1;
                int to = Integer.parseInt(numbers[1]) - 1;
                long w = Long.parseLong(numbers[2]);
                vertices[from].adj.add(new Pair<>(vertices[to], w));
                vertices[to].adj.add(new Pair<>(vertices[from], w));
            }

            dijxtra(vertices);

            for (Vertex vertex : vertices) {
                out.write(vertex.distance + " ");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void dijxtra(Vertex[] vertices) {
        vertices[0].distance = 0;
        PriorityQueue<Pair<Integer, Long>> queue = new PriorityQueue<>(Comparator.comparingLong(pair -> pair.getValue()));
        for (Vertex vertex : vertices) {
            queue.offer(new Pair<Integer, Long>(vertex.number, vertex.distance));
        }
        while (!queue.isEmpty()) {
            Vertex current = vertices[queue.poll().getKey()];
            if (current.visited) continue;
            current.visited = true;
            for (Pair<Vertex, Long> connected : current.adj) {
                Vertex vertex = connected.getKey();
                long weight = connected.getValue();
                if (!vertex.visited && current.distance + weight < vertex.distance) {
                    vertex.distance = current.distance + weight;
                    queue.offer(new Pair<Integer, Long>(vertex.number, current.distance + weight));
                }
            }
        }
    }
}
