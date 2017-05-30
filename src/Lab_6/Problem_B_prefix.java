package Lab_6;

import java.io.*;
import java.util.LinkedList;

/**
 * Created by kostya on 17.05.17.
 */
public class Problem_B_prefix {
    public static void main(String[] args) {
        try (
                BufferedReader in = new BufferedReader(
                        new FileReader("search2.in")
                );
                BufferedWriter out = new BufferedWriter(
                        new FileWriter("search2.out")
                )
        ) {

            String subLine = in.readLine();
            String line = in.readLine();

            line = subLine + "#" + line;

            LinkedList<Integer> res = prefix(line, subLine.length());

            out.write(res.size() + "\n");
            for (int elem : res) {
                out.write(elem + " ");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static LinkedList<Integer> prefix(String s, int size) {
        LinkedList<Integer> res = new LinkedList<>();
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
            if (p[i] == size)
                res.add(i + 1 - 2 * size);
        }
        return res;
    }
}
