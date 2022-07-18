package ahodanenok.yandex.contest.cup2020.backend.qualification;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * YandexCup2020.Backend.Qualification.E
 * todo: медленный
 */
public class E_Slow {

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("input.txt"));

        String[] parts = reader.readLine().split(" ");
        int rowCount = Integer.parseInt(parts[0]);
        int columnCount = Integer.parseInt(parts[1]);
        int likenessFactor = Integer.parseInt(reader.readLine());

        int sameCount = 0;
        List<Row> rows = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {
            Row row = Row.parse(reader.readLine(), columnCount);
            if (row.getFilledCount() >= likenessFactor) {
                for (Row prev : rows) {
                    if (row.sameAs(prev, likenessFactor)) {
                        sameCount++;
                    }
                }

                rows.add(row);
            }
        }

        System.out.println(sameCount);
    }

    private static class Row {

        static Row parse(String str, int columnCount) {
            String[] columns = new String[columnCount];
            Arrays.fill(columns, "");

            String[] parsedColumns = str.split("\\t");
            System.arraycopy(parsedColumns, 0, columns, 0, parsedColumns.length);

            return new Row(columns);
        }

        private final String[] columns;
        private int filledCount;

        public Row(String[] columns) {
            this.columns = columns;
            for (String col : columns) {
                if (!col.isEmpty()) {
                    filledCount++;
                }
            }
        }

        public int getFilledCount() {
            return filledCount;
        }

        public boolean sameAs(Row other, int likenessFactor) {
            int matchedCount = 0;
            for (int i = 0; i < columns.length; i++) {
                String colA = columns[i];
                String colB = other.columns[i];

                if (!colA.isEmpty() && !colB.isEmpty() && !colA.equals(colB)) {
                    return false;
                }

                if (!colA.isEmpty() && colA.equals(colB)) {
                    matchedCount++;
                }
            }

            return matchedCount >= likenessFactor;
        }
    }
}
