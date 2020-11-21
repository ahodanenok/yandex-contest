package ahodanenok.yandex.contest.cup2020.backend._final;

import java.io.FileInputStream;

/**
 * YandexCup2020.Backend.Final.G
 * todo: застрял на 18 тесте, непонятно почему некорректный результат
 */
public class G {

    public static void main(String[] args) throws Exception {
        int indexLength;
        byte[] index = new byte[1_000_000];
        try (FileInputStream in = new FileInputStream("index.bin")) {
            indexLength = in.read(index);
        }

        int count = 0;
        StringBuilder sb = new StringBuilder();

        next:
        for (int i = 0, n = indexLength - 4; i < n; i++) {
            byte a = index[i];
            byte b = index[i + 1];
            byte c = index[i + 2];
            byte d = index[i + 3];

            long len = toLong(a, b, c, d);
            if (len <= 0 || len > indexLength) {
                continue;
            }

            int endIdx = i + 4 + (int) len * 4;
            if (endIdx > indexLength) {
                continue;
            }

            int totalStrLength = 0;
            for (int j = i + 4; j < endIdx; j += 4) {
                byte offsetA = index[j];
                byte offsetB = index[j + 1];
                byte offsetC = index[j + 2];
                byte offsetD = index[j + 3];

                long offset = toLong(offsetA, offsetB, offsetC, offsetD);
                if (offset < 0 && Math.abs(offset) > indexLength || offset >= indexLength) {
                    continue next;
                }

                if (offset < 0) {
                    offset = indexLength + offset;
                }

                byte strLength = index[(int) offset];
                if (strLength < 0) {
                    continue next;
                }

                if (offset + strLength >= indexLength) {
                    continue next;
                }

                for (int m = 0; m < strLength; m++) {
                    byte ch = index[(int) offset + m + 1];
                    if (ch < 9) {
                        continue next;
                    }
                }

                totalStrLength += strLength;
            }

            count++;
            sb.append(i).append(" ").append(totalStrLength).append("\n");
        }

        System.out.println(count);
        if (count > 0) {
            System.out.println(sb);
        }
    }

    private static long toLong(byte a, byte b, byte c, byte d) {
        long n = d;
        n <<= 8;

        n |= c;
        n <<= 8;

        n |= b;
        n <<= 8;

        n |= a;

        return n;
    }
}
