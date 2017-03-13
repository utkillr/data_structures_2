package Lab_1;

import java.io.*;
import java.util.*;

public class Problem_F {

    public enum Direction {
        UP,
        RIGHT,
        DOWN,
        LEFT
    }

    public enum Ability {
        FREE,
        LOCKED
    }

    public static class Pair implements Comparable<Pair> {
        int first;
        int second;

        Pair(int a, int b) {
            first = a;
            second = b;
        }

        public int getFirst() {
            return first;
        }

        public int getSecond() {
            return second;
        }

        @Override
        public int compareTo(Pair o) {
            if (first > o.getFirst()) {
                return 2;
            } else if (first < o.getFirst()) {
                return -2;
            } else {
                if (second > o.getSecond()) {
                    return 1;
                } else if (second < o.getSecond()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }
    }

    public static class Maze {
        public Maze(int height, int width) {
            this.height = height;
            this.width = width;
            list = new Ability[height][width];
            distances = new int[height][width];
            from = new Pair[height][width];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    distances[i][j] = Integer.MAX_VALUE;
                }
            }
        }

        int height;
        int width;

        private int[][] distances;
        private Ability[][] list;
        private Pair[][] from;
        private Pair start;
        private Pair finish;

        public void put(int i, int j, Ability ability) {
            list[i][j] = ability;
        }

        public Ability get(int i, int j) {
            return list[i][j];
        }

        public void setDistance(int i, int j, int value) {
            distances[i][j] = value;
        }

        public int[][] getDistances() {
            return distances;
        }

        public void setStart(int a, int b) {
            start = new Pair(a, b);
        }

        public void setFinish(int a, int b) {
            finish = new Pair(a, b);
        }

        public Pair getStart() {
            return start;
        }

        public Pair getFinish() {
            return finish;
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }

        public void setFrom(int x, int y, Pair from) {
            this.from[x][y] = from;
        }

        public Pair getFrom(int x, int y) {
            return from[x][y];
        }
    }


    public static void main(String[] args) {
        try (
                BufferedReader in = new BufferedReader(
                        new FileReader("input.txt")
                );
                BufferedWriter out = new BufferedWriter(
                        new FileWriter("output.txt")
                )
        ) {

            String line = in.readLine();
            String[] numbers = line.split(" ");
            int n = Integer.parseInt(numbers[0]);
            int m = Integer.parseInt(numbers[1]);

            Maze maze = getMaze(n, m, in);

            BFS(maze);

            Pair start = maze.getStart();

            if (maze.getDistances()[start.getFirst()][start.getSecond()] == Integer.MAX_VALUE) {
                out.write("-1\n");
                return;
            }

            int distance = maze.getDistances()[start.getFirst()][start.getSecond()];
            out.write(distance + "\n");

            for (int i = 0; i < distance; i++) {
                Pair next = maze.getFrom(start.getFirst(), start.getSecond());
                if (next.getFirst() == start.getFirst() + 1) {
                    out.write('D');
                } else if (next.getFirst() == start.getFirst() - 1) {
                    out.write('U');
                } else if (next.getSecond() == start.getSecond() + 1) {
                    out.write('R');
                } else {
                    out.write('L');
                }
                start = next;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Maze getMaze(int height, int width, BufferedReader reader) throws IOException {
        char[] line;

        Maze maze = new Maze(height, width);

        for (int i = 0; i < height; i++) {
            line = reader.readLine().toCharArray();
            for (int j = 0; j<line.length; j++) {
                char c = line[j];
                switch (c) {
                    case 'T':
                        maze.put(i, j, Ability.FREE);
                        maze.setFinish(i, j);
                        break;
                    case 'S':
                        maze.put(i, j, Ability.FREE);
                        maze.setStart(i, j);
                        break;
                    case '.':
                        maze.put(i, j, Ability.FREE);
                        break;
                    case '#':
                        maze.put(i, j, Ability.LOCKED);
                        break;
                }
            }
        }

        return maze;
    }

    private static void BFS(Maze maze) {
        Pair finish = maze.getFinish();

        maze.setDistance(finish.getFirst(), finish.getSecond(), 0);

        Queue<Pair> queue = new PriorityQueue<>();
        queue.add(finish);
        while (!queue.isEmpty()) {
            Pair cell = queue.poll();
            int x = cell.getFirst();
            int y = cell.getSecond();

            //adding neighbors
            List<Pair> neighbors = new ArrayList<>();
            if (x > 0 && maze.get(x - 1, y) == Ability.FREE)
                neighbors.add(new Pair(x - 1, y));
            if (x < maze.getHeight() - 1 && maze.get(x + 1, y) == Ability.FREE)
                neighbors.add(new Pair(x + 1, y));
            if (y > 0 && maze.get(x, y - 1) == Ability.FREE)
                neighbors.add(new Pair(x, y - 1));
            if (y < maze.getWidth() - 1 && maze.get(x, y + 1) == Ability.FREE)
                neighbors.add(new Pair(x, y + 1));

            for (int i = 0; i<neighbors.size(); i++) {
                Pair nextNode = neighbors.get(i);
                int next_x = nextNode.getFirst();
                int next_y = nextNode.getSecond();
                int newDistance = maze.getDistances()[x][y] + 1;
                if (maze.getDistances()[next_x][next_y] > newDistance) {
                    maze.setDistance(next_x, next_y, newDistance);
                    maze.setFrom(next_x, next_y, cell);
                    queue.add(nextNode);
                }
            }
        }
    }
}
