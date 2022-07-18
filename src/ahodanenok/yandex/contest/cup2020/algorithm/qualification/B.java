package ahodanenok.yandex.contest.cup2020.algorithm.qualification;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * YandexCup2020.Algorithm.Qualification.B
 */
public class B {

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
        String str = reader.readLine();
        int count = 0;
        boolean currentLowerCase = true;
        for (int i = 0; i < str.length(); ) {
            char ch = str.charAt(i);
            if (isLower(ch) == currentLowerCase || ch == ' ') {
                count++;
                i++;
                continue;
            }

            int end = runEnd(str, i);
            int len = end - i;
            if (len > 3 || len == 3
                    && (end == str.length() || runEnd(str, end) - end < 3 && runEnd(str, end) != str.length())) {
                count += 2 + len;
                currentLowerCase = !currentLowerCase;
            } else {
                count += 2 * len;
            }

            i = end;
        }

        System.out.println(count);
    }

    private static int runEnd(String str, int pos) {
        boolean lowerCase = isLower(str.charAt(pos));
        int end = pos;
        while (end < str.length()) {
            char ch = str.charAt(end);
            if (ch != ' ' && lowerCase != isLower(ch)) {
                break;
            }

            end++;
        }

        return end;
    }

    private static boolean isLower(char ch) {
        return ch >= 'a' && ch <= 'z';
    }
}
