package ahodanenok.yandex.contest.cup2020.backend.qualification;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * YandexCup2020.Backend.Qualification.D
 */
public class D {

    @SuppressWarnings("unchecked") // simple-json использует raw типы
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("input.txt"));

        int feedCount = Integer.parseInt(reader.readLine());

        JSONArray combinedOffers = new JSONArray();
        JSONParser parser = new JSONParser();
        for (int i = 0; i < feedCount; i++) {
            JSONObject feed = (JSONObject) parser.parse(reader.readLine());
            JSONArray offers = (JSONArray) feed.get("offers");
            combinedOffers.addAll(offers);
        }

        combinedOffers.sort((a, b) -> {
            JSONObject offerA = (JSONObject) a;
            JSONObject offerB = (JSONObject) b;
            int priceCompare = Long.compare((Long) offerA.get("price"), (Long) offerB.get("price"));
            if (priceCompare == 0) {
                return ((String) offerA.get("offer_id")).compareTo((String) offerB.get("offer_id"));
            } else {
                return priceCompare;
            }
        });

        JSONObject stream = new JSONObject();
        stream.put("offers", combinedOffers);
        System.out.println(stream.toJSONString());
    }
}
