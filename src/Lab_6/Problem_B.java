package Lab_6;

import java.io.*;
import java.util.LinkedList;

/**
 * Created by kostya on 17.05.17.
 */
public class Problem_B {

    static int p = 27751;
    static int x = 2;

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

            LinkedList<Integer> enterances = RabinKarp(line, subLine);

            out.write(enterances.size() + "\n");
            for (Integer index : enterances) {
                out.write(index + " ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static LinkedList<Integer> RabinKarp(String line, String subLine) {
        LinkedList<Integer> res = new LinkedList<>();

        if (line.length() < subLine.length()) return res;

        char[] lineChar = line.toCharArray();
        char[] subLineChar = subLine.toCharArray();

        int subHash = hash(subLineChar, 0, subLineChar.length - 1);
        int[] hashes = countHashes(lineChar, subLineChar);

        for (int i = 0; i < lineChar.length - subLineChar.length + 1; i++) {
            if (hashes[i] != subHash) continue;
            if (innerSearch(lineChar, subLineChar, i)) res.add(i + 1);
        }
        return res;
    }

    private static boolean innerSearch(char[] line, char[] subLine, int i) {
        for (int j = 0; j < subLine.length; j++) {
            if (line[i + j] != subLine[j]) {
                return false;
            }
        }
        return true;
    }

    private static int hash(char[] line, int start, int finish) {
        int pow = 1;
        int hash = 0;
        for (int i = finish; i >= start; i--) {
            hash = (hash + pow * (int) line[i]) % p;
            pow = (pow * x) % p;
        }
        return hash;
    }

    private static int[] countHashes(char[] line, char[] subLine) {
        int n = line.length;
        int m = subLine.length;
        int[] hashes = new int[n - m + 1];
        hashes[0] = hash(line, 0, m - 1);
        int maxPow = powMod(x, m, p);
        for (int i = 0; i < n - m; i++) {
            hashes[i + 1] = ((hashes[i] * x - line[i] * maxPow % p + line[i + m]) + p) % p;
        }
        return hashes;
    }

    private static int powMod(int x, int m, int p) {
        int res = 1;
        for (int i = 0; i < m; i++) {
            res = (res * x) % p;
        }
        return res;
    }
}