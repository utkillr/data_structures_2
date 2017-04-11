package Lab_3;

import java.io.*;

/**
 * Created by kostya on 09.04.17.
 */
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

            int[] powers = new int[n];

            for (int index = 0; index < m; index++) {
                line = in.readLine();
                numbers = line.split(" ");
                int i = Integer.parseInt(numbers[0]);
                int j = Integer.parseInt(numbers[1]);
                powers[i-1]++;
                powers[j-1]++;
            }

            for (int power : powers) {
                out.write(power + " ");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
