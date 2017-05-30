package Lab_6;

import java.io.*;

/**
 * Created by kostya on 17.05.17.
 */
public class Problem_C {
    public static void main(String[] args) {
        try (
                BufferedReader in = new BufferedReader(
                        new FileReader("prefix.in")
                );
                BufferedWriter out = new BufferedWriter(
                        new FileWriter("prefix.out")
                )
        ) {

            String line = in.readLine();
            int[] res = prefix(line);
            for (int elem : res) {
                out.write(elem + " ");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int[] prefix(String s) {
        int[] p = new int[s.length()];
        int k;
        p[0] = 0;
        for (int i = 1; i < s.length(); i++) {
            k = p[i - 1];
            while (k > 0 && s.charAt(i) != s.charAt(k))
                k = p[k - 1];
            if (s.charAt(i) == s.charAt(k))
                k++;
            p[i] = k;
        }
        return p;
    }
}
