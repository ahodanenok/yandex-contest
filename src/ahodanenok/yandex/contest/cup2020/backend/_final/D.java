package ahodanenok.yandex.contest.cup2020.backend._final;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

/**
 * YandexCup2020.Backend.Final.D
 */
public class D {

    private static final boolean DEBUG = false;

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        Map<String, String> values = new HashMap<>();
        TreeMap<Integer, List<SetCommand>> pendingByTime = new TreeMap<>();
        LinkedHashMap<Integer, List<SetCommand>> arrivedByTime = new LinkedHashMap<>();

        String line;
        while (!"-1".equals(line = reader.readLine())) {
            String[] parts = line.split("\\t");

            int currentTime = Integer.parseInt(parts[0]);
            String cmd = parts[1];
            if (DEBUG) System.out.printf("%nreceived new command at time=%d%n", currentTime);

            applyPending(values, pendingByTime, currentTime);

            if ("set".equals(cmd)) {
                int setAtTime = Integer.parseInt(parts[2]);
                String key = parts[3];
                String newValue = parts.length > 4 ? parts[4] : "";
                if (DEBUG) System.out.printf("  %s(%s -> %s at t=%d)%n", cmd, key, newValue, setAtTime);

                SetCommand setCmd = new SetCommand(key, newValue);
                pendingByTime.computeIfAbsent(setAtTime, __ -> new ArrayList<>()).add(setCmd);
                arrivedByTime.computeIfAbsent(currentTime, __ -> new ArrayList<>()).add(setCmd);

                String value = values.get(key);
                if (value != null) {
                    System.out.println("true\t" + value);
                } else {
                    System.out.println("false");
                }
                System.out.flush();
            } else if ("get".equals(cmd)) {
                String key = parts[2];
                if (DEBUG) System.out.printf("  %s(key=%s)%n", cmd, key);

                String value = values.get(key);
                if (value != null) {
                    System.out.println("true\t" + value);
                } else {
                    System.out.println("false");
                }
                System.out.flush();
            } else if ("cancel".equals(cmd)) {
                int cancelAtTime = Integer.parseInt(parts[2]);
                if (DEBUG) System.out.printf("  %s(t=%d)%n", cmd, cancelAtTime);

                int count = 0;
                if (arrivedByTime.containsKey(cancelAtTime)) {
                    for (SetCommand setCmd : arrivedByTime.get(cancelAtTime)) {
                        if (!setCmd.cancelled && !setCmd.executed) {
                            setCmd.cancelled = true;
                            count++;
                        }
                    }
                }

                if (count > 0) {
                    System.out.println("true");
                } else {
                    System.out.println("false");
                }
            } else {
                throw new IllegalStateException(cmd);
            }
        }
    }

    private static void applyPending(Map<String, String> values, TreeMap<Integer, List<SetCommand>> pending, int currentTime) {
        if (DEBUG && !pending.isEmpty()) {
            System.out.println("  pending:");
            pending.entrySet().forEach(entry -> System.out.println("    " + entry));
        }

        Iterator<Map.Entry<Integer, List<SetCommand>>> iterator = pending.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, List<SetCommand>> entry = iterator.next();
            if (entry.getKey() <= currentTime) {
                if (DEBUG) System.out.printf("  applying pending set commands for time=%d%n", entry.getKey());

                for (SetCommand cmd : entry.getValue()) {
                    if (!cmd.cancelled) {
                        values.put(cmd.key, cmd.value);
                        cmd.executed = true;
                        if (DEBUG) System.out.printf("    %s%n", cmd);
                    }
                }

                iterator.remove();
            } else {
                break;
            }
        }
    }

    private static class SetCommand {

        final String key;
        final String value;
        boolean cancelled;
        boolean executed;

        public SetCommand(String key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return key + " -> " + value;
        }
    }
}
