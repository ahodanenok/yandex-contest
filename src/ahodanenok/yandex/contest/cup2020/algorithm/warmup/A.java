package ahodanenok.yandex.contest.cup2020.algorithm.warmup;

import java.util.Scanner;

/**
 * YandexCup2020.Algorithm.WarmUp.A
 */
public class A {


    public static final int NUMBERS_SELECTED = 10;
    public static final int TICKET_LENGTH = 6;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        byte[] numbers = new byte[NUMBERS_SELECTED];
        for (int i = 0; i < 10; i++) numbers[i] = scanner.nextByte();

        int n = scanner.nextInt();
        for (int i = 0; i < n; i++) {
            int matches = 0;
            for (int j = 0; j < TICKET_LENGTH; j++) {
                byte tb = scanner.nextByte();
                for (byte nb : numbers) {
                    if (tb == nb) {
                        matches++;
                    }
                }
            }

            if (matches > 2) {
                System.out.println("Lucky");
            } else {
                System.out.println("Unlucky");
            }
        }
    }
}
