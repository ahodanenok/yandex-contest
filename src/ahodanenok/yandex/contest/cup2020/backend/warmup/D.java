package ahodanenok.yandex.contest.cup2020.backend.warmup;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * YandexCup2020.Backend.WarmUp.D
 */
public class D {

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int commitsCount = Integer.parseInt(reader.readLine());
        if (commitsCount == 1) {
            outputBroken(1);
            return;
        }

        int prevBroken = -1;
        int lo = 1, hi = commitsCount;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            boolean currBroken = isBroken(reader, mid);
            if (mid == prevBroken) {
                break;
            }

            if (currBroken) {
                hi = mid - 1;
                prevBroken = mid;
            } else {
                lo = mid + 1;
            }
        }

        outputBroken(prevBroken);
    }

    private static boolean isBroken(BufferedReader reader, int commitNum) throws Exception {
        System.out.println(commitNum);
        System.out.flush();
        return "0".equals(reader.readLine());
    }

    private static void outputBroken(int commitNum) {
        System.out.println("! " + commitNum);
    }
}
