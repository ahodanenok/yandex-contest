package ahodanenok.yandex.contest.cup2020.algorithm.warmup;

import java.io.*;
import java.util.*;

/**
 * YandexCup2020.Algorithm.WarmUp.F
 */
public class F {

    private static boolean DEBUG = true;
    private static boolean TIMINGS = false;

    public static void main(String[] args) throws Exception {
        //generate();
        BufferedReader reader = new BufferedReader(new FileReader("input.txt"));

        String[] firstLine = reader.readLine().split(" ");
        int n = Integer.parseInt(firstLine[0]);
        int m = Integer.parseInt(firstLine[1]);
        int k = Integer.parseInt(firstLine[2]);

        long t = System.currentTimeMillis();

        TreeMap<Integer, TreeSet<Integer>> columns = new TreeMap<>();

        String line;
        int x, y, idx;
        for (int i = 0; i < k; i++) {
            line = reader.readLine();
            x = Integer.parseInt(line.substring(0, (idx = line.indexOf(' '))));
            y = Integer.parseInt(line.substring(idx + 1));

            columns.computeIfAbsent(x, __ -> new TreeSet<>()).add(y);
        }
        if (TIMINGS) System.out.println("preparing input - " + (System.currentTimeMillis() - t));

        if (DEBUG) {
            for (Map.Entry<Integer, TreeSet<Integer>> column : columns.entrySet()) {
                System.out.println(column.getKey() + ": " + column.getValue());
            }
        }

        t = System.currentTimeMillis();
        System.out.println(bleach(columns, n, m));
        if (TIMINGS) System.out.println("bleaching - " + (System.currentTimeMillis() - t));
    }

    private static int bleach(TreeMap<Integer, TreeSet<Integer>> columns, int width, int height) {
        int steps = 0;
        if (columns.isEmpty()) {
            return steps;
        }

        Map.Entry<Integer, TreeSet<Integer>> lastDirtyColumn = columns.lastEntry();
        // если не заполнена ячейка в нижнем правом углу, то первым дейстивем будет инверсия в черные
        if (lastDirtyColumn.getKey() != width - 1 || lastDirtyColumn.getValue().first() != 0) {
            steps++;
        }

        List<Generation> generations = new ArrayList<>();
        for (Map.Entry<Integer, TreeSet<Integer>> column : columns.descendingMap().entrySet()) {
            if (DEBUG) {
                System.out.println();
                System.out.println("-- iteration start --");
                System.out.println("Column=" + column.getKey());
            }

            int firstRow = column.getValue().first();
            int lastRow = column.getValue().first() - 1;

            int generationIdx = 0;
            Generation currentGeneration = null;
            for (Integer row : column.getValue()) {
                boolean consecutiveRow = row - 1 == lastRow;
                if (DEBUG) System.out.println("  Row=" + row + ", consecutive=" + consecutiveRow);

                if (currentGeneration != null && (!consecutiveRow || !currentGeneration.contains(row, column.getKey(), consecutiveRow, firstRow))) {
                    if (DEBUG) System.out.print("  Updating generation: " + currentGeneration);
                    currentGeneration.lastColumn = column.getKey();
                    currentGeneration.lastColumnFirstRow = firstRow;
                    currentGeneration.lastColumnLastRow = lastRow;
                    if (DEBUG) System.out.println(" -> " + currentGeneration);
                    currentGeneration = null;
                    firstRow = row;
                    consecutiveRow = true;
                } else if (currentGeneration != null) {
                    if (DEBUG) System.out.println("  Using generation: " + currentGeneration);
                }

                if (currentGeneration == null) {
                    if (DEBUG) System.out.print("  Searching for generation...");

                    generationIdx = -1;
                    int lo = 0;
                    int hi = generations.size();
                    while (true) {
                        int mid = lo + (hi - lo) / 2;
                        if (mid >= generations.size()) {
                            break;
                        }

                        boolean rightContains = generations.get(mid).contains(row, column.getKey(), consecutiveRow, firstRow);
                        if (mid == lo && rightContains) {
                            generationIdx = mid;
                            break;
                        }

                        if (mid == lo && hi - lo == 1) {
                            break;
                        }

                        boolean leftContains = generations.get(mid - 1).contains(row, column.getKey(), consecutiveRow, firstRow);
                        if (leftContains && rightContains) {
                            hi = mid;
                        } else if (!leftContains && !rightContains) {
                            lo = mid;
                        } else if (leftContains) {
                            generationIdx = mid - 1;
                            break;
                        } else {
                            generationIdx = mid;
                            break;
                        }
                    }

                    if (generationIdx >= 0) {
                        currentGeneration = generations.get(generationIdx);
                    }

                    if (DEBUG && currentGeneration != null) {
                        System.out.println("found at idx=" + generationIdx + ": " + currentGeneration);
                    }

                    if (DEBUG && currentGeneration == null) {
                        System.out.println("not found");
                    }
                }

                if (currentGeneration == null) {
                    int number = !generations.isEmpty() ? generations.get(generations.size() - 1).number + 1 : 1;
                    currentGeneration = new Generation(number);
                    currentGeneration.lastColumn = width;
                    currentGeneration.lastColumnFirstRow = height;
                    currentGeneration.lastColumnLastRow = height;
                    if (DEBUG) System.out.println("  Adding new generation: " + currentGeneration);
                    generations.add(currentGeneration);
                }

                lastRow = row;
                if (DEBUG) System.out.println();
            }

            if (DEBUG) System.out.print("  Updating generation after all rows processed: " + currentGeneration);
            currentGeneration.lastColumn = column.getKey();
            currentGeneration.lastColumnFirstRow = firstRow;
            currentGeneration.lastColumnLastRow = lastRow;
            if (DEBUG) System.out.println(" -> " + currentGeneration);

            if (DEBUG) System.out.println("-- iteration end --");
        }

        return steps + generations.get(generations.size() - 1).number * 2 - 1;
    }

    private static class Generation {

        int number;
        int lastColumn;
        int lastColumnFirstRow;
        int lastColumnLastRow;

        public Generation(int number) {
            this.number = number;
        }

        boolean contains(int row, int column, boolean consecutiveRow, int firstRow) {
            boolean consecutiveColumn = column + 1 == lastColumn;
            if (consecutiveColumn && firstRow > lastColumnFirstRow || !consecutiveColumn && firstRow >= lastColumnFirstRow) {
                return false;
            }

            if (!consecutiveRow) {
                return false;
            }

            return consecutiveColumn && row <= lastColumnLastRow || !consecutiveColumn && row < lastColumnFirstRow;
        }

        public String toString() {
            return "Generation: " + number + ", lastColumn=" + lastColumn + " [" + lastColumnFirstRow + "-" + lastColumnLastRow + "]";
        }
    }

    private static void generate() throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("test.txt"))) {

            int count = 0;
            StringBuilder sb = new StringBuilder();

            /**
             *   #_#_#
             *   _____
             *   #_#_#
             */
//            int width = 2000;
//            int height = 2000;
//            for (int col = 0; col < width; col += 2) {
//                for (int row = 0; row < height; row += 2) {
//                    sb.append(col + " " + row + "\n");
//                    count++;
//                }
//            }

            // шахматная доска
//            int width = 1000;
//            int height = 1000;
//            for (int row = 0; row < height; row++) {
//                for (int col = row % 2 == 0 ? 0 : 1; col < width; col += 2) {
//                    sb.append(col + " " + row + "\n");
//                    count++;
//                }
//            }

            // столбы
//            int width = 1000;
//            int height = 1000;
//            for (int row = height - 1; row >= 0; row--) {
//                for (int col = 0; col < width; col += 2) {
//                    sb.append(col + " " + row + "\n");
//                    count++;
//                }
//            }

            // диагональ
//            int width = 50000;//000;
//            int height = 50000;//000;
//            for (int row = height - 1; row >= 0; row--) {
//                sb.append(row + " " + (width - row - 1) + "\n");
//                count++;
//            }

//            int width = 500000;//000;
//            int height = 2;//000;
//            for (int col = 0; col < width; col += 2) {
//                sb.append(col + " 0" + "\n");
//                count++;
//                sb.append((col + 1) + " 1" + "\n");
//                count++;
//            }

            // подпорка вначале
//            int width = 500000;
//            int height = 500000;
//            for (int row = height - 1; row >= 0; row--) {
//                sb.append(row + " " + (width - row - 1) + "\n");
//                if (row < height - 1 ) {
//                    sb.append("0 " + row + "\n");
//                    count++;
//                }
//                if (row < height - 2 ) {
//                    sb.append("1 " + row + "\n");
//                    count++;
//                }
//                count++;
//            }

            int width = 1000;
            int height = 1000;
            for (int row = height - 1; row >= 0; row--) {
                for (int col = 0; col < width; col += 2) {
                    sb.append(col + " " + row + "\n");
                    count++;
                }
                for (int i = row; i >= 0; i -= 2) {
                    sb.append((width - row - 1) + " " + i + "\n");
                    count++;
                }
            }

//            int width = 500;
//            int height = 500;
//            for (int row = height - 1; row >= 0; row--) {
//                sb.append(row + " " + row + "\n");
//                if (row - 1 > 0) {
//                    sb.append(row + " " + (row - 1));
//                }
//                count++;
//            }

            writer.write(width + " " + height + " " + count + "\n");
            writer.write(sb.toString());
            writer.flush();
        }
    }

}
