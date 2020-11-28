package ahodanenok.yandex.contest.cup2020.backend._final;

import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * YandexCup2020.Backend.Final.E
 */
@SuppressWarnings("unchecked") // simple-json использует raw типы
public class E {

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("input.txt"));

        JSONParser parser = new JSONParser();
        List<?> ourEmployee = (List<?>) parser.parse(reader.readLine());
        Object obj = parser.parse(reader);

        List<?> values = each(obj, null);

        List<Map<Object, Object>> result = new ArrayList<>();
        for (Object item : values) {
            if (!(item instanceof Map)) {
                continue;
            }

            Map<Object, Object> converted = convert((Map<Object, Object>) item, ourEmployee);
            if (converted != null) {
                result.add(converted);
            }
        }

        System.out.println(JSONValue.toJSONString(result));
    }

    private static Map<Object, Object> convert(Map<Object, Object> item, List<?> ourEmployee) {
        TreeMap<Object, Object> result = new TreeMap<>();

        extractCost(item, result);
        if (!result.containsKey("cost")) {
            return null;
        }

        result.put("description", extractDescription(item));
        result.put("index", item.get("index"));
        result.put("reviewers", extractReviewers(item, ourEmployee));
        result.put("works", extractWorks(item));
        result.put("authors", extractAuthors(item));
        result.put("range", extractRange(item));
        result.put("reviewer", extractReviewer(item));
        result.put("last", extractLast(item));
        result.put("remains", extractRemains(item));

        return result;
    }

    private static Object extractDescription(Map<Object, Object> item) {
        return value(item, "base-info", "description");
    }

    private static Object extractReviewers(Map<?, ?> item, List<?> ourEmployee) {
        List<?> reviewers = each(item, "reviewers");

        Collection<Comparable<Object>> names = new LinkedHashSet<>();
        reviewers.forEach(r -> collectNames(r, names));
        names.removeAll(ourEmployee);

        return names.stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
    }

    private static void collectNames(Object obj, Collection<Comparable<Object>> names) {
        Object value = value(obj, "name");
        if (value instanceof Comparable) {
            names.add((Comparable<Object>) value);
        }

        Collection<?> objects;
        if (obj instanceof Map) {
            objects = ((Map<?, ?>) obj).values();
        } else if (obj instanceof List) {
            objects = (List<?>) obj;
        } else {
            objects = Collections.emptyList();
        }

        objects.forEach(o -> collectNames(o, names));
    }

    private static Object extractWorks(Map<?, ?> item) {
        List<Map<?, ?>> result = new ArrayList<>();

        List<?> works = each(item, "works");
        for (Object workObj : works) {
            if (!(workObj instanceof Map)) {
                return Collections.emptyList();
            }

            List<?> charters = each(workObj, "charters");
            for (Object charter : charters) {
                TreeMap<Object, Object> resultItem = new TreeMap<>();
                resultItem.put(value(workObj, "author"), charter);

                result.add(resultItem);
            }
        }

        return result;
    }

    private static Object extractAuthors(Map<?, ?> item) {
        List<Object> result = new ArrayList<>();
        result.add(item.get("author"));
        result.addAll(each(item, "co-authors"));

        return result;
    }

    private static Object extractRange(Map<?, ?> item) {
        List<?> values = list(item, "values");
        if (values == null) {
            return Collections.emptyList();
        }

        List<Object> result = new ArrayList<>();
        if (!values.isEmpty()) {
            result.add(values.get(values.size() - 1));
            result.add(values.get(0));
        } else {
            result.add(null);
            result.add(null);
        }

        return result;
    }

    private static void extractCost(Map<?, ?> item, Map<Object, Object> to) {
        if (!(item.get("time") instanceof List)) {
            return;
        }

        List<?> time = (List<?>) item.get("time");
        if (time.isEmpty()) {
            return;
        }

        Object a = time.get(time.size() - 1);
        if (!(a instanceof Number)) {
            return;
        }

        Object b = time.get(0);
        if (!(b instanceof Number)) {
            return;
        }

        Object price = item.get("price") != null ? item.get("price") : 5;
        if (!(price instanceof Number) && !(price instanceof String)) {
            return;
        }

        Number diff = (((Number) a).doubleValue() - ((Number) b).doubleValue());
        if (price instanceof String) {
            int len = diff.intValue();
            if (len == 0) {
                to.put("cost", null);
                return;
            }

            StringBuilder sb = new StringBuilder();
            String priceStr = (String) price;
            if (priceStr.length() > 0) {
                while (sb.length() < len) {
                    sb.append(priceStr);
                }
            }

            to.put("cost", sb.toString());
            return;
        }

        Number result = diff.doubleValue() * ((Number) price).doubleValue();
        if (result.longValue() == result.doubleValue()) {
            to.put("cost", result.longValue());
        } else {
            to.put("cost", result.doubleValue());
        }
    }

    private static Object extractReviewer(Map<?, ?> item) {
        return value(item, "base-info", "reviewer.name");
    }

    private static Object extractLast(Map<?, ?> item) {
        List<?> results = list(item, "results");
        if (results == null) {
            return Collections.emptyList();
        }

        if (!results.isEmpty()) {
            Object last = results.get(results.size() - 1);
            return Collections.singletonList(value(last, "url"));
        } else {
            return Collections.singletonList(null);
        }
    }

    private static Object extractRemains(Map<?, ?> item) {
        List<?> results = list(item, "results");
        if (results == null || results.size() < 2) {
            return Collections.emptyList();
        }

        results = results.subList(0, results.size() - 1);

        List<Object> urls = new ArrayList<>();
        for (Object result : results) {
            if (result instanceof Map) {
                urls.add(value(result, "url"));
            }
        }

        return urls;
    }

    private static Object value(Object obj, String... fields) {
        Object current = obj;
        for (String field : fields) {
            if (!(current instanceof Map)) {
                return null;
            }

            current = ((Map<?, ?>) current).get(field);
        }

        return current;
    }

    private static List<?> list(Map<?, ?> item, String property) {
        Object list = item.get(property);
        if (item.containsKey(property) && !(list instanceof List)) {
            return null;
        }

        return list != null ? (List<?>) list : Collections.emptyList();
    }

    private static List<?> each(Object obj, String property) {
        Object source;
        if (obj instanceof Map && property != null) {
            source = ((Map<?, ?>) obj).get(property);
        } else {
            source = obj;
        }

        if (source instanceof Map) {
            return ((Map<?, ?>) source).values().stream().distinct().collect(Collectors.toList());
        } else if (source instanceof List) {
            return (List<?>) source;
        } else {
            return Collections.emptyList();
        }
    }
}
