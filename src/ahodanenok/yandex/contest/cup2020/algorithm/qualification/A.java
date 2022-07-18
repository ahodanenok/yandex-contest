package ahodanenok.yandex.contest.cup2020.algorithm.qualification;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.StringJoiner;

/**
 * YandexCup2020.Algorithm.Qualification.A
 */
public class A {

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
        String s = reader.readLine();

        if (s.length() == 2) {
            s += s.charAt(0);
        }

        int start = 0, end = 0;
        for (int i = 0; i < s.length(); i++) {
            if (i + 2 < s.length() && s.charAt(i) == s.charAt(i + 2) && s.charAt(i) != s.charAt(i + 1)) {
                start = i;
                end = i + 2;
                break;
            }
        }

        Set<Character> alphabet = new LinkedHashSet<>();
        alphabet.add(s.charAt(start));
        if (start + 1 < s.length()) {
            alphabet.add(s.charAt(start + 1));
        }

        if (start > 0 || end < s.length() - 1) {
            boolean startFixed = false;
            boolean endFixed = false;
            while (!startFixed || !endFixed) {
                if (start - 1 >= 0 && !startFixed) {
                    char letter = s.charAt(start - 1);
                    if (alphabet.contains(letter)) {
                        System.out.println("No solution");
                        return;
                    }

                    alphabet.add(letter);

                    int newStart = start - (end - start + 1) - 1;
                    if (newStart >= 0) {
                        start = newStart;
                    } else {
                        startFixed = true;
                    }
                }

                if (end + 1 < s.length() && !endFixed) {
                    char letter = s.charAt(end + 1);
                    if (alphabet.contains(letter)) {
                        System.out.println("No solution");
                        return;
                    }

                    alphabet.add(letter);

                    int newEnd = end + (end - start + 1) + 1;
                    if (newEnd < s.length()) {
                        end = newEnd;
                    } else {
                        endFixed = true;
                    }
                }
            }
        }

        for (char ch = 'A'; ch <= 'Z'; ch++) {
            alphabet.add(ch);
        }

        StringJoiner alphabetJoiner = new StringJoiner("");
        for (Character ch : alphabet) {
            alphabetJoiner.add(ch + "");
        }

        System.out.println(alphabetJoiner);
        //System.out.println(start + " " + end);
        //System.out.println(end - start + 1 + 1);

        int p = 1;
        if (start > 0) {
            p += end - start + 1;
        }
        System.out.println(p);
    }

}
