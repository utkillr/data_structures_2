package Lab_6;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by kostya on 17.05.17.
 */
public class Problem_A {
    public static void main(String[] args) {
        try (
                BufferedReader in = new BufferedReader(
                        new FileReader("search1.in")
                );
                BufferedWriter out = new BufferedWriter(
                        new FileWriter("search1.out")
                )
        ) {

            String subLine = in.readLine();
            String line = in.readLine();

            List<Integer> enterances = naiveSubString(line, subLine);

            out.write(enterances.size() + "\n");
            for (Integer index : enterances) {
                out.write(index + " ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Integer> naiveSubString(String line, String subLine) {
        List<Integer> res = new LinkedList<>();
        for (int i = 0; i < line.length() - subLine.length() + 1; i++) {
            boolean ok = true;
            for (int j = 0; j < subLine.length(); j++) {
                if (line.charAt(i + j) != subLine.charAt(j)) {
                    ok = false;
                    break;
                }
            }
            if (ok) res.add(i + 1);
        }
        return res;
    }
}
