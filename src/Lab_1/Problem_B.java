package Lab_1;

import java.io.*;

public class Problem_B {
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
            int n = Integer.parseInt(line);

            int[][] matrix = createMatrix(n, in, out);
            boolean isOriented = checkIfOriented(matrix);

            if (isOriented) {
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

    private static int[][] createMatrix(int nodes, BufferedReader reader, BufferedWriter writer) throws IOException {
        int[][] matrix = new int[nodes][nodes];
        String line;
        String[] numbers;

        for (int i = 0; i<nodes; i++) {
            line = reader.readLine();
            numbers = line.split(" ");
            for (int j = 0; j < nodes; j++)
                matrix[i][j] = Integer.parseInt(numbers[j]);
        }

        return matrix;
    }

    private static boolean checkIfOriented(int[][] matrix) {
        for (int i = 0; i<matrix.length; i++) {
            if (matrix[i][i] == 1) {
                return false;
            }
            for (int j = i+1; j<matrix[i].length; j++) {
                if (matrix[i][j] != matrix[j][i])
                    return false;
            }
        }
        return true;
    }
}
