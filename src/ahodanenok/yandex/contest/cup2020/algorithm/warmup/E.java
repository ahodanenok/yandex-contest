package ahodanenok.yandex.contest.cup2020.algorithm.warmup;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * YandexCup2020.Algorithm.WarmUp.E
 */
public class E {

    private static boolean DEBUG = false;

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("input.txt"));

        int nodesCount = Integer.parseInt(reader.readLine());
        Node[] nodes = new Node[nodesCount + 1];

        String line;
        int from, to, idx;
        int[] links = new int[nodesCount + 1];
        for (int i = 0, cables = nodesCount - 1; i < cables; i++) {
            line = reader.readLine();
            if (DEBUG) System.out.println(line);
            from = Integer.parseInt(line.substring(0, (idx = line.indexOf(' '))));
            to = Integer.parseInt(line.substring(idx + 1));

            if (links[from] != 0) {
                int currentFrom = from;
                int currentTo = links[from];
                while (currentTo != 0) {
                    int nextFrom = currentTo;
                    int nextTo = links[currentTo];
                    links[currentTo] = currentFrom;
                    currentFrom = nextFrom;
                    currentTo = nextTo;
                }
            }

            links[from] = to;
        }

        if (DEBUG) {
            System.out.println();
            for (int i = 1; i < links.length; i++) {
                System.out.println(i + " -> " + links[i]);
            }
        }

        Node root = null;
        Node fromNode, toNode;
        for (int i = 1; i < nodes.length; i++) {
            fromNode = nodes[i];
            if (fromNode == null) {
                fromNode = new Node(i);
                nodes[fromNode.num] = fromNode;
            }

            if (links[i] == 0) {
                root = fromNode;
                continue;
            }

            toNode = nodes[links[i]];
            if (toNode == null) {
                toNode = new Node(links[i]);
                nodes[toNode.num] = toNode;
            }

            fromNode.parent = toNode.num;
            toNode.children.add(fromNode.num);

            nodes[fromNode.num] = fromNode;
            nodes[toNode.num] = toNode;
        }

        if (root == null) {
            throw new IllegalStateException("root is null");
        }

        calcHeight(root, nodes);
        if (DEBUG) {
            System.out.println();
            for (int i = 1; i < nodes.length; i++) {
                System.out.println(nodes[i]);
            }
        }
        findStorageNodes(root, nodes);
    }

    // найти точку в дереве от которой до любого узла одинаковое число шагов (+/-1) - разделить поподам
    // сделать эту точку новым корнем дерева
    // разделить пополам первое поддерево с максимальной глубиной - первое расположение сервера
    // разделить пополам второе поддерево с максимальной глубиной - второе расположение сервера
    private static void findStorageNodes(Node root, Node[] nodes) {
        if (DEBUG) System.out.println();
        Node splitPoint = splitByTwo(root, nodes, Integer.MAX_VALUE);
        if (DEBUG) System.out.println("splitPoint: " + splitPoint);

        Node firstHalf;
        Node secondHalf;
        // если точка разделения дерева пополам имеет родителя, разворачиваем все что сверху ее в поддерево
        // необходимо, т.к алгоритм идет только вниз, а оставшиеся сверху могут повлиять на выбор точек
        if (splitPoint.parent != 0) {
            reparent(nodes[splitPoint.parent], splitPoint, nodes);
            calcHeight(splitPoint, nodes);
            root = splitPoint;
        }

        firstHalf = root;
        secondHalf = nodes[root.children.remove(0)];

        if (!root.children.isEmpty()) {
            root.height = nodes[root.children.get(0)].height + 1;
        } else {
            root.height = 0;
        }

        Node firstStorage = splitByTwo(firstHalf, nodes, Integer.MAX_VALUE);
        if (DEBUG) System.out.println("firstStorage: " + firstStorage);

        Node secondStorage = splitByTwo(secondHalf, nodes, Integer.MAX_VALUE);;
        if (DEBUG) System.out.println("secondStorage: " + secondStorage);

        System.out.println(firstStorage.num + " " + secondStorage.num);
    }

    // находит точку в дереве из которой до любого узла не более (n / 2) + 1 шагов,
    // где n - максимальное расстояние между двумя любыми точками в дереве (ширина)
    private static Node splitByTwo(Node current, Node[] nodes, int stepsLeft) {
        if (stepsLeft == 0 || current.children.isEmpty()) {
            return current;
        }

        int splitSize = nodes[current.children.get(0)].height + 1;
        if (current.children.size() > 1) {
            splitSize += nodes[current.children.get(1)].height + 1;
        }
        splitSize /= 2;

        stepsLeft = Math.min(stepsLeft, (nodes[current.children.get(0)].height + 1 - splitSize));
        if (stepsLeft == 0) {
            return current;
        }

        return splitByTwo(nodes[current.children.get(0)], nodes, stepsLeft - 1);
    }

    private static void reparent(Node n, Node p, Node[] nodes) {
        if (n == null) {
            return;
        }

        reparent(nodes[n.parent], n, nodes);

        n.parent = p.num;
        Iterator<Integer> iterator = n.children.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() == p.num) {
                iterator.remove();
                break;
            }
        }

        p.children.add(n.num);
    }

    // дочерние узлы будут расположены в node.children в порядке убывания их высоты
    private static void calcHeight(Node current, Node[] nodes) {
        if (current.children.isEmpty()) {
            current.height = 0;
            return;
        }

        int maxHeight = 0;
        for (int ch : current.children) {
            calcHeight(nodes[ch], nodes);
            maxHeight = Math.max(maxHeight, nodes[ch].height + 1);
        }

        current.height = maxHeight;
        current.children.sort((a, b) -> nodes[b].height - nodes[a].height);
    }

    private static class Node {

        int num;
        int height;
        int parent;
        List<Integer> children = new ArrayList<>();

        public Node(int num) {
            this.num = num;
        }

        public String toString() {
            return num + " -> " + parent + ", height: " + height + ", children: " + children;
        }
    }
}
