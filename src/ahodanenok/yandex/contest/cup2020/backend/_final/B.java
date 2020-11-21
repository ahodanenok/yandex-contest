package ahodanenok.yandex.contest.cup2020.backend._final;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * YandexCup2020.Backend.Final.B
 */
public class B {

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        String[] parts = line.split(" ");

        int userLimit = Integer.parseInt(parts[0]);
        int serviceLimit = Integer.parseInt(parts[1]);
        int duration = Integer.parseInt(parts[2]);

        Map<String, LinkedList<Integer>> userRequests = new HashMap<>();
        LinkedList<Integer> serviceRequests = new LinkedList<>();

        while (!"-1".equals(line = reader.readLine())) {
            parts = line.split(" ");
            int time = Integer.parseInt(parts[0]);
            String userId = parts[1];

            if (!userRequests.containsKey(userId)) {
                userRequests.put(userId, new LinkedList<>());
            }

            int start = Math.max(time - duration, 0);
            while (!serviceRequests.isEmpty() && serviceRequests.peek() < start) {
                serviceRequests.removeFirst();
            }

            while (!userRequests.get(userId).isEmpty() && userRequests.get(userId).peek() < start) {
                userRequests.get(userId).removeFirst();
            }

            if (userRequests.get(userId).size() >= userLimit) {
                System.out.println("429");
                System.out.flush();
                continue;
            }

            if (serviceRequests.size() >= serviceLimit) {
                System.out.println("503");
                System.out.flush();
                continue;
            }

            serviceRequests.add(time);
            userRequests.get(userId).add(time);

            System.out.println("200");
            System.out.flush();
        }
    }
}
