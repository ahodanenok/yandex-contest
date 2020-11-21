package ahodanenok.yandex.contest.cup2020.algorithm.warmup;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Files;

/**
 * YandexCup2020.Algorithm.WarmUp.С
 */
public class C {

    private static float x;
    private static float y;

    public static void main(String[] args) throws Exception {
        long t = System.currentTimeMillis();

        BufferedReader scanner = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(
                Files.readAllBytes(FileSystems.getDefault().getPath("input.txt")))));

        int n = Integer.parseInt(scanner.readLine());
        if (n < 3) {
            System.out.println("Yes");
            return;
        }

        readTargetCenter(scanner);
        float x1 = x;
        float y1 = y;

        float x2;
        float y2;
        while (true) {
            boolean read = readTargetCenter(scanner);
            if (!read || x1 != x || y1 != y) {
                x2 = x;
                y2 = y;
                break;
            }
        }

        // Все мишение возможно разделить на две части с одинаковой площадью,
        // если есть прямая (y = kx + b), которая  проходит через центры всех мишеней
        float k = (y1 - y2) / (x1 - x2);
        float b = -k * x2 + y2;
        while (readTargetCenter(scanner)) {
            if (Math.abs((k * x + b) - y) > 0.0000000001) {
                System.out.println("No");
                return;
            }
        }

        System.err.println(System.currentTimeMillis() - t);
        System.out.println("Yes");
    }

    private static boolean readTargetCenter(BufferedReader scanner) throws Exception {
        String line = scanner.readLine();
        if (line == null) {
            return false;
        }

        // не использую string.split для оптимизации
        if ('0' == line.charAt(0)) {
            int idx = line.indexOf(' ', 3) + 1;
            x = Integer.parseInt(line.substring(idx, (idx = line.indexOf(' ', idx)))); idx++;
            y = Integer.parseInt(line.substring(idx));
        } else if ('1' == line.charAt(0)) {
            int idx = 2;
            // :(
            int x1 = Integer.parseInt(line.substring(idx, (idx = line.indexOf(' ', idx)))); idx++;
            int y1 = Integer.parseInt(line.substring(idx, (idx = line.indexOf(' ', idx)))); idx++;
            int x2 = Integer.parseInt(line.substring(idx, (idx = line.indexOf(' ', idx)))); idx++;
            int y2 = Integer.parseInt(line.substring(idx, (idx = line.indexOf(' ', idx)))); idx++;
            int x3 = Integer.parseInt(line.substring(idx, (idx = line.indexOf(' ', idx)))); idx++;
            int y3 = Integer.parseInt(line.substring(idx, (idx = line.indexOf(' ', idx)))); idx++;
            int x4 = Integer.parseInt(line.substring(idx, (idx = line.indexOf(' ', idx)))); idx++;
            int y4 = Integer.parseInt(line.substring(idx));

            int minX = Math.min(Math.min(Math.min(x1, x2), x3), x4);
            int maxX = Math.max(Math.max(Math.max(x1, x2), x3), x4);
            int minY = Math.min(Math.min(Math.min(y1, y2), y3), y4);
            int maxY = Math.max(Math.max(Math.max(y1, y2), y3), y4);

            x = minX + (maxX - minX) / 2f;
            y = minY + (maxY - minY) / 2f;
        } else {
            throw new IllegalStateException();
        }

        return true;
    }
}
