package Lab_1;

import java.io.*;

public class Problem_A {
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

            listToMatrix(n, m, in, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void listToMatrix(int nodes, int edges, BufferedReader reader, BufferedWriter writer) throws IOException {
        int[][] matrix = new int[nodes][nodes];
        String line;
        String[] numbers;

        while (edges > 0) {
            line = reader.readLine();
            numbers = line.split(" ");
            int i = Integer.parseInt(numbers[0]) - 1;
            int j = Integer.parseInt(numbers[1]) - 1;
            matrix[i][j] = 1;
            edges--;
        }

        for (int i = 0; i<matrix.length; i++) {
            for (int j = 0; j<matrix[i].length; j++) {
                writer.write("" + matrix[i][j] + " ");
            }
            writer.newLine();
        }
    }
}
