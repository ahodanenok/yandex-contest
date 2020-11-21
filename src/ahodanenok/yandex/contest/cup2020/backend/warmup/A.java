package ahodanenok.yandex.contest.cup2020.backend.warmup;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

/**
 * YandexCup2020.Backend.WarmUp.A
 */
public class A {

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
        String j = reader.readLine();
        String s = reader.readLine();
        int count = 0;

        Set<Character> jewellery = new HashSet<>();
        for (int ij = 0; ij < j.length(); ij++) {
            jewellery.add(j.charAt(ij));
        }

        for (char cj : jewellery) {
            for (int is = 0; is < s.length(); is++) {
                if (s.charAt(is) == cj) {
                    count++;
                }
            }
        }

        System.out.println(count);
    }
}
