package Lab_5;

import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by kostya on 13.05.17.
 */
public class Problem_C {

    static class Edge {
        int from, to, capacity, number, reverse;
        long flow;

        public Edge(int from, int to, int number, int reverse, int capacity) {
            this.from = from;
            this.to = to;
            this.number = number;
            this.reverse = reverse;
            this.capacity = capacity;
        }
    }

    static ArrayList<Edge>[] graph;
    static int[] distance;
    static int size;
    static int[] ptr;

    public static void addEdge(int from, int to, int number, int capacity) {
        graph[from].add(new Edge(from, to, number, graph[to].size(), capacity));
        graph[to].add(new Edge(to, from, number, graph[from].size() - 1, 0));
    }


    private static void findMaxFlow(int from, int to) {
        distance = new int[size];
        while (BFS()) {
            ptr = new int[size];
            while (true) {
                long flow = DFS(ptr, from, to, Long.MAX_VALUE);
                if (flow == 0)
                    break;
            }
        }
    }

    private static boolean BFS() {
        Arrays.fill(distance, -1);
        distance[0] = 0;
        int[] Q = new int[graph.length];
        int q_size = 0;
        Q[q_size++] = 0;
        for (int i = 0; i < q_size; i++) {
            int v = Q[i];
            for (Edge edge : graph[v]) {
                if (distance[edge.to] < 0 && edge.flow < edge.capacity) {
                    distance[edge.to] = distance[v] + 1;
                    Q[q_size++] = edge.to;
                }
            }
        }
        return distance[size - 1] >= 0;
    }


    //ptr[v] is where we stopped before v
    private static long DFS(int[] ptr, int from, int to, long flow) {
        if (from == to) return flow;

        for (; ptr[from] < graph[from].size(); ++ptr[from]) {
            Edge edge = graph[from].get(ptr[from]);
            if (distance[edge.to] == distance[from] + 1 && edge.flow < edge.capacity) {
                long innerFlow = DFS(ptr, edge.to, to, Math.min(flow, edge.capacity - edge.flow));
                if (innerFlow > 0) {
                    edge.flow += innerFlow;
                    graph[edge.to].get(edge.reverse).flow -= innerFlow;
                    return innerFlow;
                }
            }
        }

        return 0;
    }


    private static Pair<Long, StringBuilder> fullDecomposition(int from) {
        long count = 0;
        StringBuilder result = new StringBuilder();

        Pair<Long, LinkedList<Edge>> p = simpleDecomposition(from);

        while (p != null) {
            result.append(p.getKey()).append(" ").append(p.getValue().size()).append(" ");

            for (Edge edge : p.getValue()) {
                result.append(edge.number).append(" ");
            }
            result.append("\n");
            count++;
            p = simpleDecomposition(from);
        }
        return new Pair<>(count, result);
    }


    private static Pair<Long, LinkedList<Edge>> simpleDecomposition(int from) {
        LinkedList<Edge> passed = new LinkedList<>();
        int[] visited = new int[size];
        int v = from;
        while (visited[v] == 0) {
            if (v == size - 1) {
                break;
            }
            Edge edge = null;
            for (int i = 0; i < graph[v].size(); i++) {
                if (graph[v].get(i).flow > 0) {
                    edge = graph[v].get(i);
                    break;
                }
            }
            if (edge == null) {
                return null;
            }
            passed.add(edge);
            visited[v] = 1;
            v = edge.to;
        }
        if (visited[v] == 1) {
            while (passed.getFirst().from != v) {
                passed.removeFirst();
            }
        }

        long minFlow = Long.MAX_VALUE;
        for (Edge edge : passed) {
            if (edge.flow < minFlow) {
                minFlow = edge.flow;
            }
        }
        for (Edge edge : passed) {
            edge.flow = edge.flow - minFlow;
        }

        return new Pair<>(minFlow, passed);
    }


    public static void main(String[] args) {

        try (
                BufferedReader in = new BufferedReader(
                        new FileReader("decomposition.in")
                );
                PrintWriter out = new PrintWriter("decomposition.out");
        ) {

            String line = in.readLine();
            String[] numbers = line.split(" ");
            size = Integer.parseInt(numbers[0]);
            int m = Integer.parseInt(numbers[1]);

            graph = new ArrayList[size];
            for (int i = 0; i < size; i++) {
                graph[i] = new ArrayList<>();
            }

            int from, to, cap;

            for (int index = 0; index < m; index++) {
                line = in.readLine();
                numbers = line.split(" ");
                from = Integer.parseInt(numbers[0]) - 1;
                to = Integer.parseInt(numbers[1]) - 1;
                cap = Integer.parseInt(numbers[2]);
                addEdge(from, to, index + 1, cap);
            }

            findMaxFlow(0, size - 1);

            Pair<Long, StringBuilder> result = fullDecomposition(0);

            out.println(result.getKey());
            out.print(result.getValue());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
