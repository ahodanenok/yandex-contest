package ahodanenok.yandex.contest.cup2020.backend._final;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * YandexCup2020.Backend.Final.A
 */
public class A {

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("input.txt"));

        Books books = new Books();
        StringBuilder out = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            if (line.equals("add")) {
                String site = reader.readLine();
                String bookJson = reader.readLine();
                books.add(site, bookJson);
            } else if (line.equals("find")) {
                String[] parts = reader.readLine().split(" ");
                String site = parts[0];
                String id = parts[1];
                books.find(site, id, out);
            } else {
                throw new IllegalStateException(line);
            }
        }

        System.out.print(out);
    }

    private static class Books {

        private static final String PROBLEM_ID_PROPERTY = "id";
        private static final String SECTIONS_PROPERTY = "sections";
        private static final String PROBLEMS_PROPERTY = "problems";

        private static final String SECTION_PREFIX = "s";
        private static final String PROBLEM_PREFIX = "p";

        private final JSONParser parser = new JSONParser();

        // structure -> problem path -> similar problems
        // opportunistic - hashCode may be equal for different structures, but it worked :)
        private final Map<Integer, Map<Integer, List<Problem>>> groupingIndex = new HashMap<>();
        // site -> problem id -> similar problems
        private final Map<String, Map<String, List<Problem>>> lookupIndex = new HashMap<>();

        void add(String site, String bookJson) throws ParseException {
            JSONObject book = (JSONObject) parser.parse(bookJson);

            LinkedList<String> structure = new LinkedList<>();
            extractStructure(book, structure);
            extractProblems(book, site, structure.hashCode(), new LinkedList<>());
        }

        // structure: s<count>-...-p<count>-...-s<count>-p<count>
        //
        // for book
        // {"title":"math 8 class Merzlyak","sections":[{"title":"chapter 1","problems":[{"id":"1"},{"id":"2"}]},{"title":"chapter 2","problems":[{"id":"4"}]}]}
        // structure is s2-p2-p1
        void extractStructure(Object obj, LinkedList<String> structure) {
            if (obj instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) obj;
                if (map.containsKey(SECTIONS_PROPERTY)) {
                    List<?> sections = (List<?>) map.get(SECTIONS_PROPERTY);
                    structure.addLast(SECTION_PREFIX + sections.size());
                    for (Object section : sections) {
                        extractStructure(section, structure);
                    }
                } else if (map.containsKey(PROBLEMS_PROPERTY)) {
                    structure.addLast(PROBLEM_PREFIX + ((List<?>) map.get(PROBLEMS_PROPERTY)).size());
                } else {
                    throw new IllegalStateException(structure + " -> " + obj);
                }
            } else {
                throw new IllegalStateException(structure + " -> " + obj);
            }
        }

        // path: s<pos>-s<pos>-...-p<pos>
        private void extractProblems(Object obj, String site, Integer structure, LinkedList<String> path) {
            if (obj instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) obj;
                if (map.containsKey(SECTIONS_PROPERTY)) {
                    int i = 0;
                    for (Object section : (List<?>) map.get(SECTIONS_PROPERTY)) {
                        path.addLast(SECTION_PREFIX + i++);
                        extractProblems(section, site, structure, path);
                        path.removeLast();
                    }
                } else if (map.containsKey(PROBLEMS_PROPERTY)) {
                    int i = 0;
                    for (Object problemObj : (List<?>) map.get(PROBLEMS_PROPERTY)) {
                        path.addLast(PROBLEM_PREFIX + i++);

                        Map<?, ?> problem = (Map<?, ?>) problemObj;

                        List<Problem> similar = groupingIndex
                                .computeIfAbsent(structure, __ -> new HashMap<>())
                                .computeIfAbsent(path.hashCode(), __ -> new ArrayList<>());
                        similar.add(new Problem(site, problem.get(PROBLEM_ID_PROPERTY).toString()));

                        lookupIndex.computeIfAbsent(site, __ -> new HashMap<>())
                                .put(problem.get(PROBLEM_ID_PROPERTY).toString(), similar);

                        path.removeLast();
                    }
                } else {
                    throw new IllegalStateException(path + " -> " + obj);
                }
            } else {
                throw new IllegalStateException(path + " -> " + obj);
            }
        }

        void find(String site, String id, StringBuilder out) {
            List<Problem> similar = null;
            if (lookupIndex.containsKey(site)) {
                similar = lookupIndex.get(site).get(id);
            }

            if (similar == null) {
                out.append("0").append("\n");
                return;
            }

            out.append(similar.size() - 1).append("\n");
            similar.stream()
                    .filter(p -> !p.site.equals(site) || !p.id.equals(id))
                    .forEach(p -> out.append(p).append("\n"));
        }
    }

    private static class Problem {

        final String site;
        final String id;

        public Problem(String site, String id) {
            this.site = site;
            this.id = id;
        }

        @Override
        public String toString() {
            return site + " " + id;
        }
    }
}
