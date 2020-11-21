package ahodanenok.yandex.contest.cup2020.algorithm.warmup;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * YandexCup2020.Algorithm.WarmUp.D
 */
public class D {

    private static final long MULTIPLIER = 1000000000;

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("input.txt"));

        String line = reader.readLine();

        int idx;
        int width = Integer.parseInt(line.substring(0, (idx = line.indexOf(' '))));

        idx++;
        int height = Integer.parseInt(line.substring(idx));

        String a = reader.readLine();
        String b = reader.readLine();

        char maxDigitA = max(a, width);
        char maxDigitB = max(b, height);

        int xMaxFirst = 0;
        while (a.charAt(xMaxFirst + xMaxFirst) != maxDigitA) xMaxFirst++;

        int xMaxLast = width - 1;
        while (a.charAt(xMaxLast + xMaxLast) != maxDigitA) xMaxLast--;

        int yMax = height - 1;
        while (b.charAt(yMax + yMax) != maxDigitB) yMax--;
/*

    a2, a4 - cтолбцы с максимальной цифрой из A
    b3     - столбец с максимальной цифрой из B

       v     v
    a1 a2 a3 a4 a5      b1 b1 b1 b1 b1
    a1 a2 a3 a4 a5      b2 b2 b2 b2 b2
    a1 a2 a3 a4 a5     >b3 b3 b3 b3 b3
    a1 a2 a3 a4 a5      b4 b4 b4 b4 b4
    a1 a2 a3 a4 a5      b5 b5 b5 b5 b5

    Сумма:
    - сложить числа между a2, a4 находящиеся на b3
    - добавить сумму столбца a2 (или a4, они равны, первая часть берется до b3 из a2, остальная из a4 начиная с b3)
    - добавить сумму чисел в первой строке до a2
    - добавить сумму чисел в последней строке начиная с a4

    Путь в общем виде:

    * *
      *
      * * *
          *
          * *

    Варианты (частные случаи из общего вида):

    * * * * *         *              * * *            *
            *         *                  *            *
            *         *                  *            * * * * *
            *         *                  *                    *
            *         * * * * *          * * *                *
 */
        long sum = 0;
        sum += sum(a, b, 0, xMaxFirst, 0, 1);
        sum += sum(a, b, xMaxFirst, xMaxFirst + 1, 0, yMax);
        sum += sum(a, b, xMaxFirst, xMaxLast, yMax, yMax + 1);
        sum += sum(a, b, xMaxLast, xMaxLast + 1, yMax, height - 1);
        sum += sum(a, b, xMaxLast, width, height - 1, height);
        System.out.println(sum);
    }

    private static char max(String s, int length) {
        char max = '0';
        char ch;
        for (int i = 0; i < length; i++) {
            ch = s.charAt(i + i);
            if (ch > max) {
                max = ch;
            }
        }

        return max;
    }

    private static long sum(String a, String b, int xStart, int xEnd, int yStart, int yEnd) {
        long sum = 0;
        for (int x = xStart; x < xEnd; x++) {
            for (int y = yStart; y < yEnd; y++) {
                sum += (a.charAt(x + x) - '0') * MULTIPLIER + (b.charAt(y + y) - '0');
            }
        }

        return sum;
    }
}
