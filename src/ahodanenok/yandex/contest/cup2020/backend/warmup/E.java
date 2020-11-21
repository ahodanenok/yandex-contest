package ahodanenok.yandex.contest.cup2020.backend.warmup;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * YandexCup2020.Backend.WarmUp.E
 */
public class E {

    public static void main(String[] args) throws Exception {
        //generate();
        BufferedReader reader = new BufferedReader(new FileReader("input.txt"));

        int n = Integer.parseInt(reader.readLine());
        Network network = new Network();
        for (int i = 0; i < n; i++) {
            String[] parts = reader.readLine().split(" ");
            int s1 = Integer.parseInt(parts[0]);
            int s2 = Integer.parseInt(parts[1]);

            network.connect(s1, s2);
        }

        //network.printDebugInfo();

        int q = Integer.parseInt(reader.readLine());
        StringBuilder results = new StringBuilder();
        for (int i = 0; i < q; i++) {
            String[] parts = reader.readLine().split(" ");
            int downloadTo = Integer.parseInt(parts[0]);
            int fc = Integer.parseInt(parts[1]);

            parts = reader.readLine().split(" ");
            List<Integer> reachable = new ArrayList<>();
            for (int s = 0; s < fc; s++) {
                int server = Integer.parseInt(parts[s]);
                if (network.isConnected(downloadTo, server)) {
                    reachable.add(server);
                }
            }

            results.append(reachable.size());
            if (!reachable.isEmpty()) {
                for (Integer server : reachable) {
                    results.append(" ");
                    results.append(server);
                }
            }
            results.append("\n");
        }
        System.out.println(results);
    }

    // union-find с оглядкой на высоту дерева для его балансировки при добавлении новых серверов
    private static class Network {

        private int lastServerIdx = 0;
        private Map<Integer, Integer> mappings = new HashMap<>();
        private List<Integer> clusters = new ArrayList<>();

        // для балансировки дерева
        private List<Integer> heights = new ArrayList<>();

        void connect(int s1, int s2) {
            int c1 = addCluster(s1);
            int c2 = addCluster(s2);

            if (c1 != c2) {
                int h1 = heights.get(c1);
                int h2 = heights.get(c2);
                if (h1 < h2) {
                    clusters.set(c1, c2);
                    heights.set(c2, Math.max(h2, h1 + 1));
                } else {
                    clusters.set(c2, c1);
                    heights.set(c1, Math.max(h1, h2 + 1));
                }
            }
        }

        private int addCluster(int s) {
            if (!mappings.containsKey(s)) {
                // каждый сервер ассоциируется с числом начиная с 0
                // для возможности использования его как индекс массива
                mappings.put(s, lastServerIdx++);
                int c = mappings.get(s);
                clusters.add(c, c);
                heights.add(c, 1);
            }

            return getCluster(mappings.get(s));
        }

        private int getCluster(int idx) {
            int current = idx;
            while (clusters.get(current) != current) {
                current = clusters.get(current);
            }

            return current;
        }

        boolean isConnected(int s1, int s2) {
            return getCluster(mappings.get(s1)) == getCluster(mappings.get(s2));
        }

        public void printDebugInfo() {
            System.out.println("Mappings:");
            for (Map.Entry<Integer, Integer> entry : mappings.entrySet()) {
                System.out.println(entry.getKey() + " -> " + entry.getValue());
            }

            System.out.println();
            System.out.println("Clusters");
            System.out.println(clusters);
        }
    }

    private static void generate() throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("test.txt"))) {

            int linksCount = 1000000;
            writer.write(linksCount + "\n");
            for (int i = 1; i <= linksCount; i++) {
                writer.write((i + 1000000) + " " + (i + 1000001) + "\n");
            }

            int q = 1000;
            int s = 100;
            writer.write(q + "\n");
            for (int i = 1; i <= q; i++) {
                writer.write((i + 1000000) + " " + s + "\n");
                for (int j = 1; j <= s; j++) {
                    writer.write((j + 1000000) + "");
                    if (j < s) {
                        writer.write(" ");
                    }
                }
                writer.write("\n");
            }

            writer.flush();
        }
    }
}
