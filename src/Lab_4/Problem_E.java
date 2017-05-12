package Lab_4;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kostya on 25.04.17.
 */
public class Problem_E {

    public static int[] parent;

    public static void main(String[] args) {
        try (
                BufferedReader in = new BufferedReader(
                        new FileReader("negcycle.in")
                );
                BufferedWriter out = new BufferedWriter(
                        new FileWriter("negcycle.out")
                )
        ) {

            String line = in.readLine();
            String[] numbers;
            int n = Integer.parseInt(line);

            int[][] weights = new int[n][n];

            for (int i = 0; i < n; i++) {
                line = in.readLine();
                numbers = line.split(" ");
                for (int j = 0; j < n; j++) {
                    weights[i][j] = Integer.parseInt(numbers[j]);
                }
            }

            int cycleBegin = getNegativeCycle(weights, n);

            if (cycleBegin == -1) out.write("NO");
            else {
                int cycleEnd = cycleBegin;
                for (int i = 0; i < n; i++) {
                    cycleEnd = parent[cycleEnd];
                }
                List<Integer> list = new ArrayList<>();
                for (int i = cycleEnd ; ; i = parent[i]) {
                    list.add(i);
                    if (i == cycleEnd && list.size() > 1) break;
                }
                out.write("YES\n");
                out.write(list.size() + "\n");
                for (int i = list.size() - 1; i >= 0; i--) {
                    out.write((list.get(i) + 1) + " ");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getNegativeCycle(int[][] weights, int count) {
        int[] distance = new int[count];
        parent = new int[count];
        Arrays.fill(distance, Integer.MAX_VALUE/2);
        Arrays.fill(parent, -1);
        distance[0] = 0;
        int cycleBegin = -1;
        for (int k = 0; k < count; k++) {
            cycleBegin = -1;
            for (int i = 0; i < count; i++) {
                for (int j = 0; j < count; j++) {
                    if (distance[j] > distance[i] + weights[i][j]) {
                        parent[j] = i;
                        distance[j] = distance[i] + weights[i][j];
                        cycleBegin = j;
                    }
                }
            }
        }
        return cycleBegin;
    }
}
