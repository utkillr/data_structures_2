package Lab_1;

import java.io.*;
import java.util.*;

public class Problem_E {
    public static class MyMap {
        public MyMap(int nodes) {
            list = new ArrayList<>();
            distances = new int[nodes];
            for (int i = 0; i<nodes; i++) {
                list.add(new LinkedList<>());
                distances[i] = Integer.MAX_VALUE;
            }
        }

        private int[] distances;

        ArrayList<LinkedList<Integer>> list;

        public void put(int key, int value) {
            list.get(key).add(value);
        }

        public List<Integer> get(int key) {
            return list.get(key);
        }

        public void setDistance(int index, int value) {
            distances[index] = value;
        }

        public int[] getDistances() {
            return distances;
        }
    }


    public static void main(String[] args) {
        try (
                BufferedReader in = new BufferedReader(
                        new FileReader("pathbge1.in")
                        //new FileReader("input.txt")
                );
                BufferedWriter out = new BufferedWriter(
                        new FileWriter("pathbge1.out")
                )
        ) {

            String line = in.readLine();
            String[] numbers = line.split(" ");
            int n = Integer.parseInt(numbers[0]);
            int m = Integer.parseInt(numbers[1]);

            MyMap map = getMap(n, m, in);

            BFS(map, 0, 0);

            for (int i = 0; i<n; i++) {
                out.write(map.getDistances()[i] + " ");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static MyMap getMap(int nodes, int edges, BufferedReader reader) throws IOException {
        String line;
        String[] numbers;

        MyMap map = new MyMap(nodes);

        while (edges > 0) {
            line = reader.readLine();
            numbers = line.split(" ");
            int i = Integer.parseInt(numbers[0]) - 1;
            int j = Integer.parseInt(numbers[1]) - 1;

            map.put(i, j);
            map.put(j, i);

            edges--;
        }

        return map;
    }

    //make non-recursive
    private static void BFS(MyMap map, int source, int distance) {
        if (map.getDistances()[source] < distance) return;

        map.setDistance(source, distance);

        distance++;

        Queue<Integer> queue = new PriorityQueue<>();
        queue.add(source);
        while (!queue.isEmpty()) {
            int node = queue.poll();
            List<Integer> list = map.get(node);
            for (int i = 0; i<list.size(); i++) {
                int nextNode = map.get(node).get(i);
                int newDistance = map.getDistances()[node] + 1;
                if (map.getDistances()[nextNode] > map.getDistances()[node] + 1) {
                    map.setDistance(nextNode, newDistance);
                    queue.add(nextNode);
                }
            }
        }
    }
}
