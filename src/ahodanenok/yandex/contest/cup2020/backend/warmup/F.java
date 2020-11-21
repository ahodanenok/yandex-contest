package ahodanenok.yandex.contest.cup2020.backend.warmup;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

/**
 * YandexCup2020.Backend.WarmUp.F
 */
public class F {

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("input.txt"));

        String serverUrl = reader.readLine();
        int port = Integer.parseInt(reader.readLine());
        int a = Integer.parseInt(reader.readLine());
        int b = Integer.parseInt(reader.readLine());

        URL url = new URL(serverUrl + ":" + port + "?a=" + a + "&b=" + b);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
            JSONParser parser = new JSONParser();
            JSONArray array = (JSONArray) parser.parse(in);
            int sum = 0;
            for (Object n : array) {
                sum += (Long) n;
            }

            System.out.println(sum);
        }
    }
}
