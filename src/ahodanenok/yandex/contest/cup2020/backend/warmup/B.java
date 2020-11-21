package ahodanenok.yandex.contest.cup2020.backend.warmup;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

/**
 * YandexCup2020.Backend.WarmUp.B
 */
public class B {

    public static void main(String[] args) throws Exception {
        //generate();
        BufferedReader reader = new BufferedReader(new FileReader("input.txt"));

        String[] parts = reader.readLine().split(" ");
        int alarmsCount = Integer.parseInt(parts[0]);
        int delay = Integer.parseInt(parts[1]);
        int wakeupCount = Integer.parseInt(parts[2]);

        TreeSet<Integer> alarms = normalizeAlarms(readAlarms(reader, alarmsCount), delay);

        long lo = wakeupCount, hi = Long.MAX_VALUE;
        while (true) {
            long mid = lo + (hi - lo) / 2;
            boolean wokenUpPrev = calculateFiredCount(alarms, delay, mid - 1) >= wakeupCount;
            boolean wokenUpCurrent = calculateFiredCount(alarms, delay, mid) >= wakeupCount;

            if (wokenUpCurrent && wokenUpPrev) {
                hi = mid;
            } else if (!wokenUpCurrent && !wokenUpPrev) {
                lo = mid;
            } else if (!wokenUpPrev) {
                System.out.println(mid);
                break;
            } else {
                throw new IllegalStateException();
            }
        }
    }

    private static List<Integer> readAlarms(BufferedReader reader, int alarmsCount) throws Exception {
        List<Integer> alarmsInput = new ArrayList<>();

        String line = reader.readLine();
        int from = 0;
        for (int i = 0, len = line.length(); i < len; i++) {
            if (line.charAt(i) == ' ') {
                alarmsInput.add(Integer.parseInt(line.substring(from, i)));
                from = i + 1;
            }

            if (alarmsInput.size() == alarmsCount) {
                break;
            }
        }
        if (alarmsInput.size() < alarmsCount) {
            alarmsInput.add(Integer.parseInt(line.substring(from)));
        }

        List<Integer> alarms = new ArrayList<>(new HashSet<>(alarmsInput));
        alarms.sort(Comparator.naturalOrder());

        return alarms;
    }

    // убрать повторяющиеся будильники
    private static TreeSet<Integer> normalizeAlarms(List<Integer> alarms, int delay) {
        TreeSet<Integer> result = new TreeSet<>();
        Set<Integer> remainders = new HashSet<>();
        for (int a : alarms) {
            // два будильника с одинаковым остатком по задержке являются дублями,
            // удаляем тот который начинается позже, т.к он будет постоянно дублировать начавшийся раньше
            if (remainders.add(a % delay)) {
                result.add(a);
            }
        }

        return result;
    }

    private static long calculateFiredCount(TreeSet<Integer> alarms, int delay, long atTime) {
        long firedCount = 0;

        for (int a : alarms) {
            if (atTime < a) {
                break;
            }

            firedCount += (atTime - a) / delay;
            firedCount++; // учесть начальное срабатывание будильника в момент времени t=a
        }

        return firedCount;
    }

    private static void generate() throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("test.txt"))) {
            int alarmsCount = 100000;
            int delay = 99999999;//1000000000;
            int wakeupCount = 1000000000;

            writer.write(alarmsCount + " "  + delay + " " + wakeupCount + "\n");
            for (int i = 1, a = 1; i <= alarmsCount; i++) {
                writer.write(a + "000");
                a++;
                if (i < alarmsCount) {
                    writer.write(" ");
                }
            }

            writer.flush();
        }
    }
}
