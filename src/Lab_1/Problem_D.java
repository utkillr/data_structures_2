package Lab_1;

import java.io.*;
import java.util.*;

public class Problem_D {
    public static class MyMap {
        public MyMap(int nodes) {
            list = new ArrayList<>();
            colors = new int[nodes];
            maxColor = 0;
            for (int i = 0; i<nodes; i++) {
                list.add(new LinkedList<>());
            }
        }

        private int[] colors;

        private int maxColor;

        ArrayList<LinkedList<Integer>> list;

        public void put(int key, int value) {
            list.get(key).add(value);
        }

        public List<Integer> get(int key) {
            return list.get(key);
        }

        public void setColor(int index, int value) {
            colors[index] = value;
            if (value > maxColor) maxColor = value;
        }

        public int[] getColors() {
            return colors;
        }
    }


    public static void main(String[] args) {
        try (
                BufferedReader in = new BufferedReader(
                        new FileReader("components.in")
                //          new FileReader("input.txt")
                );
                BufferedWriter out = new BufferedWriter(
                        new FileWriter("components.out")
                )
        ) {

            String line = in.readLine();
            String[] numbers = line.split(" ");
            int n = Integer.parseInt(numbers[0]);
            int m = Integer.parseInt(numbers[1]);

            MyMap map = getMap(n, m, in);

            for (int i = 0; i<n; i++) {
                if (map.getColors()[i] == 0) {
                    int color = map.maxColor + 1;
                    BFS(map, i, color);
                }
            }

            out.write(map.maxColor + "\n");
            for (int i = 0; i<n; i++) {
                out.write(map.getColors()[i] + " ");
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

    private static void BFS(MyMap map, int source, int color) {
        map.setColor(source, color);
        Queue<Integer> queue = new PriorityQueue<>();
        queue.add(source);
        while (!queue.isEmpty()) {
            int node = queue.poll();
            List<Integer> list = map.get(node);
            for (int i = 0; i<list.size(); i++) {
                int nextNode = map.get(node).get(i);
                if (map.getColors()[nextNode] == 0) {
                    map.setColor(nextNode, color);
                    queue.add(nextNode);
                }
            }
        }
    }
}
