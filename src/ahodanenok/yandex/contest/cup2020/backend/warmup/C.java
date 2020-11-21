package ahodanenok.yandex.contest.cup2020.backend.warmup;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * YandexCup2020.Backend.WarmUp.C
 */
public class C {

    public static void main(String[] args) throws Exception {

        BufferedReader reader = new BufferedReader(new FileReader("input.txt"));

        String[] parts = reader.readLine().split(" ");
        int points = Integer.parseInt(parts[0]);;
        int cardsCount = Integer.parseInt(parts[1]);

        parts = reader.readLine().split(" ");
        List<Integer> cards = new ArrayList<>();
        for (int i = 0; i < cardsCount; i++) {
            cards.add(Integer.parseInt(parts[i]));
        }

        int petyaPoints = 0;
        int vasyaPoints = 0;
        for (int card : cards) {
            if (card % 5 == 0 && card % 3 == 0) {
                continue;
            } else if (card % 5 == 0) {
                vasyaPoints++;
            } else if (card % 3 == 0) {
                petyaPoints++;
            }

            if (petyaPoints == points || vasyaPoints == points) {
                break;
            }
        }

        if (petyaPoints > vasyaPoints) {
            System.out.println("Petya");
        } else if (vasyaPoints > petyaPoints) {
            System.out.println("Vasya");
        } else {
            System.out.println("Draw");
        }
    }
}
