package ahodanenok.yandex.contest.cup2020.algorithm.warmup;

/**
 * YandexCup2020.Algorithm.WarmUp.B
 */
public class B {

    public static void main(String[] args) {
        String word = new java.util.Scanner(System.in).nextLine();
        String minPalindrome = null;

        for (int i = 0; i < word.length(); i++) {

            // проверить на минимальные палиндромы вида aa, aba - они простейшие,
            // все палиндромы большей длины будут содержать один из них в центре
            String p = null;
            if (matches(word, i, i + 1)) {
                p = word.substring(i, i + 2);
            }

            // todo: этот вариант возможно неактуален, так в центре 2 повторяющихся символа, обрабатываемых предыдущим вариантом
            else if (matches(word, i, i + 1) && matches(word, i - 1, i + 2)) {
                p = word.substring(i - 1, i + 3);
            } else if (matches(word, i, i + 2)) {
                p = word.substring(i, i + 3);
            }

            if (p == null) continue;

            // todo: зачем 2 условия?
            if (minPalindrome == null ||  minPalindrome.length() >= p.length()) {
                if (minPalindrome == null
                        || minPalindrome.length() > p.length()
                        || p.compareTo(minPalindrome) < 0) {
                    minPalindrome = p;
                }
            }
        }

        if (minPalindrome != null) {
            System.out.println(minPalindrome);
        } else {
            System.out.println(-1);
        }
    }

    private static boolean matches(String word, int a, int b) {
        if (a < 0) return false;
        if (b >= word.length()) return false;
        return word.charAt(a) == word.charAt(b);
    }
}
