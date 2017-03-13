package Lab_1;

import java.io.*;

public class Problem_C {
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

            boolean hasParallels = checkIfHasParallels(n, m, in);

            if (hasParallels) {
                out.write("YES");
            } else {
                out.write("NO");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean checkIfHasParallels(int nodes, int edges, BufferedReader reader) throws IOException {
        int[][] matrix = new int[nodes][nodes];
        String line;
        String[] numbers;

        while (edges > 0) {
            line = reader.readLine();
            numbers = line.split(" ");
            int i = Integer.parseInt(numbers[0]) - 1;
            int j = Integer.parseInt(numbers[1]) - 1;

            if (matrix[i][j] == 1) return true;
            matrix[i][j] = 1;
            matrix[j][i] = 1;

            edges--;
        }

        return false;
    }
}
