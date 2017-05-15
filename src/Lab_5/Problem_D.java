package Lab_5;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by kostya on 15.05.17.
 */
public class Problem_D {
    static class Edge {
        int to, minCapacity, maxCapacity, reverse, flow;

        public Edge(int to, int reverse, int minCapacity, int maxCapacity) {
            this.to = to;
            this.reverse = reverse;
            this.maxCapacity = maxCapacity;
            this.minCapacity = minCapacity;
        }
    }

    static class EdgeShortCut {
        int from, to, minCapacity;

        public EdgeShortCut(int from, int to, int minCapacity) {
            this.from = from;
            this.to = to;
            this.minCapacity = minCapacity;
        }
    }

    static ArrayList<Edge>[] graph;
    static int[] distance;
    static int size;
    static int m;
    static int[] ptr;

    //checked
    public static void addEdge(int from, int to, int minCapacity, int maxCapacity) {
        graph[from].add(new Edge(to, graph[to].size(), minCapacity, maxCapacity));
        graph[to].add(new Edge(from, graph[from].size() - 1, minCapacity, 0));
    }

    //checked
    private static int findMaxFlow(int from, int to) {
        int res = 0;
        distance = new int[size];
        while (BFS()) {
            ptr = new int[size];
            while (true) {
                long flow = DFS(ptr, from, to, Long.MAX_VALUE);
                if (flow == 0)
                    break;
                res += flow;
            }
        }
        return res;
    }

    //checked
    private static boolean BFS() {
        Arrays.fill(distance, -1);
        distance[0] = 0;
        int[] Q = new int[graph.length];
        int q_size = 0;
        Q[q_size++] = 0;
        for (int i = 0; i < q_size; i++) {
            int v = Q[i];
            for (Edge edge : graph[v]) {
                if (distance[edge.to] < 0 && edge.flow < edge.maxCapacity) {
                    distance[edge.to] = distance[v] + 1;
                    Q[q_size++] = edge.to;
                }
            }
        }
        return distance[size - 1] >= 0;
    }

    //checked
    //ptr[v] is where we stopped before v
    private static long DFS(int[] ptr, int from, int to, long flow) {
        if (from == to) return flow;

        for (; ptr[from] < graph[from].size(); ++ptr[from]) {
            Edge edge = graph[from].get(ptr[from]);
            if (distance[edge.to] == distance[from] + 1 && edge.flow < edge.maxCapacity) {
                long innerFlow = DFS(ptr, edge.to, to, Math.min(flow, edge.maxCapacity - edge.flow));
                if (innerFlow > 0) {
                    edge.flow += innerFlow;
                    graph[edge.to].get(edge.reverse).flow -= innerFlow;
                    return innerFlow;
                }
            }
        }

        return 0;
    }


    public static void main(String[] args) {

        try (
                BufferedReader in = new BufferedReader(
                        new FileReader("circulation.in")
                );
                PrintWriter out = new PrintWriter("circulation.out");
        ) {

            String line = in.readLine();
            String[] numbers = line.split(" ");
            size = Integer.parseInt(numbers[0]) + 2;
            m = Integer.parseInt(numbers[1]);

            graph = new ArrayList[size];
            for (int i = 0; i < size; i++) {
                graph[i] = new ArrayList<>();
            }

            EdgeShortCut[] edgeShortCuts = new EdgeShortCut[m];

            int from, to, maxCap, minCap;

            for (int index = 0; index < m; index++) {
                line = in.readLine();
                numbers = line.split(" ");
                from = Integer.parseInt(numbers[0]);
                to = Integer.parseInt(numbers[1]);
                minCap = Integer.parseInt(numbers[2]);
                maxCap = Integer.parseInt(numbers[3]);
                addEdge(0, to, 0, minCap);
                addEdge(from, size - 1, 0, minCap);
                addEdge(from, to, 0, maxCap - minCap);
                edgeShortCuts[index] = new EdgeShortCut(from, to, minCap);
            }

            boolean circulated = circulation();

            if (circulated) {
                out.println("YES");
                for (EdgeShortCut edgeShortCut : edgeShortCuts) {
                    for (Edge edge : graph[edgeShortCut.from]) {
                        if (edge.to == edgeShortCut.to) {
                            out.println(Long.toString(edge.flow + edgeShortCut.minCapacity));
                            break;
                        }
                    }
                }
            } else {
                out.println("NO");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean circulation() {
        long flow = findMaxFlow(0, size - 1);

        for (Edge edge : graph[0]) {
            if (edge.flow < edge.maxCapacity)
                return false;
        }
        return true;
    }

}
