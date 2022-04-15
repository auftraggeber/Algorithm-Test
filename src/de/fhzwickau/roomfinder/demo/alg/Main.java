package de.fhzwickau.roomfinder.demo.alg;

import de.fhzwickau.roomfinder.demo.alg.alg.Algorithm;
import de.fhzwickau.roomfinder.demo.alg.alg.bellford.BellFord;
import de.fhzwickau.roomfinder.demo.alg.alg.bellford.HashMapBellFord;
import de.fhzwickau.roomfinder.demo.alg.alg.dijkstra.AStar;
import de.fhzwickau.roomfinder.demo.alg.alg.dijkstra.Dijkstra;
import de.fhzwickau.roomfinder.demo.alg.graph.Graph;
import de.fhzwickau.roomfinder.demo.alg.graph.Node;

import java.awt.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Main {

    private static final Class<? extends Algorithm>[] CLASSES = new Class[]{Dijkstra.class, AStar.class/*, HashMapBellFord.class, BellFord.class*/};
    private static int CYCLES = 100, NODES = 7000, BRANCH_ODDS = 10, MAX_BRANCHES_PER_NODE = 3, BRANCH_TO_EXISTING_NODE_ODDS = 30;


    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, InterruptedException {


        CYCLES = getInput("Test-cycles", 5);
        NODES = getInput("Approx. nodes per graph", 300);
        BRANCH_ODDS = getInput("Branch odds (odds for second branch)", 1);
        MAX_BRANCHES_PER_NODE = getInput("Max. branches per node", 2);
        BRANCH_TO_EXISTING_NODE_ODDS = getInput("Odds for branches to existing node", 2);


        List<Graph> graphs = buildGraphs(CYCLES,NODES,BRANCH_ODDS,MAX_BRANCHES_PER_NODE, BRANCH_TO_EXISTING_NODE_ODDS);

        /*
            Zeiten ausgeben.
         */
        for (Class<? extends Algorithm> clazz : CLASSES) {
            Integer[] times = testAlgorithm(graphs, clazz);

            int average = getAverage(times);
            int median = getMedian(times);

            System.out.println("\n\n       --------------[RESULT FOR " + clazz.getSimpleName() + "]--------------\n");

            System.out.println("Average time: " + average + "ms.");
            System.out.println("Median time: " + median + "ms.");

            /*
            for (Class<? extends Algorithm> other : classes) {
                if (other.equals(clazz))
                    continue;

                Integer[] oTimes = (classSpeed.containsKey(other)) ? classSpeed.get(other) : new Integer[0];

                int oAverage = getAverage(oTimes);
                int oMedian = getMedian(oTimes);

                float averageRatio = (float) Math.round((float) oAverage * 100 / average) / 100;
                float medianRatio = (float) Math.round((float) oMedian * 100 / median) / 100;

                System.out.println("Average: " + averageRatio + " times faster than " + other.getSimpleName());
                System.out.println("Median: " + medianRatio + " times faster than " + other.getSimpleName());
            }*/

        }



    }

    private static int getInput(String title, int minValue) {
        int i = minValue-1;

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(title + " (min. " + minValue + "): ");
            if (scanner.hasNext()) {
                try {
                    i = Integer.parseInt(scanner.next());

                    if (i >= minValue)
                        break;
                }catch (RuntimeException ex) {}
            }
        }

        return i;
    }

    private static int getAverage(Integer[] times) {
        int a = 0;
        for (int t: times) {

            a += t;
        }

        return a / times.length;
    }

    private static int getMedian(Integer[] times) {
        Integer[] t = times.clone();
        Arrays.sort(t);

        return ((t[t.length / 2 -1] + t[t.length / 2]) / 2);
    }

    private static List<Graph> buildGraphs(final int amount, final int nodes, final int branchOdds, final int maxBranchesPerNode, final int branchToExistingNodeOdds) throws InterruptedException {
        System.out.println();
        System.out.println("BUILDING GRAPHS...");
        List<Graph> list = new ArrayList<>();

        String print = "";

        for (int i = 0; i < amount; i++) {

            for (int j = 0; j < print.length(); j++)
                System.out.print("\b");

            Graph g = new Graph(nodes, branchOdds, maxBranchesPerNode, branchToExistingNodeOdds);
            g.build();

            print = (i+1) + "/" + amount + " finished";

            System.out.print(print);

            list.add(g);
            
            Thread.sleep(1);
        }

        System.out.println("\n");

        return list;
    }

    /**
     * Testet die Algorithmen.
     * @param graphs Liste aller Graphen, die abarbeitet werden sollen.
     * @param classToTest Die Algorithmus-Klasse, die getestet werden soll.
     * @return Alle Zeiten zu den Algorithmus.
     */
    private static Integer[] testAlgorithm(final List<Graph> graphs, Class<? extends Algorithm> classToTest) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, InterruptedException {
        Integer[] classSpeed = new Integer[graphs.size()];

        int i = 0;
        AtomicLong time = new AtomicLong();
        AtomicInteger fi = new AtomicInteger();

        System.out.println("\n\n------------------[STARTING TEST FOR " + classToTest.getSimpleName() + "]------------------\n");

        for (Graph g : graphs) {
            fi.set(i);

            Node s = g.getRandomNode();
            Node e = g.getRandomNode();

            while (e.equals(s)) e = g.getRandomNode();

            /*
             * Für Algo. testen und Ergebnisse norieren.
             */

            Constructor<? extends Algorithm> constructor = classToTest.getDeclaredConstructor(Node.class, Node.class);
            constructor.setAccessible(true);
            Algorithm algorithm = constructor.newInstance(s, e);

            Runnable timeRunnable = () -> {

                long lastTimestamp = 0;

                String beforeString = "";

                while (algorithm.getEndTimestamp() <= 0) {

                    String removeString = "";

                    for (int l = 0; l < beforeString.length(); l++)
                        removeString += "\b";

                    System.out.print(removeString);

                    float p = ((float) (fi.get() + 1) / graphs.size()) * 20;

                    beforeString = "PROCESS: ----[";

                    for (int j = 0; j <= (int) p; j++)
                        beforeString += "|";

                    if (p < 20) {
                        for (int j = (int) p; j < 20; j++)
                            beforeString += " ";
                    }

                    beforeString += "]---[" + (fi.get() + 1) + "/" + graphs.size() + "]---- ";


                    if (algorithm.getStartTimestamp() > 0) {
                        if (lastTimestamp <= 0)
                            lastTimestamp = algorithm.getStartTimestamp();

                        long now = (algorithm.getEndTimestamp() > 0) ? algorithm.getEndTimestamp() : System.currentTimeMillis();

                        time.set(time.get() + (now - lastTimestamp));
                        lastTimestamp = System.currentTimeMillis();
                    }

                    beforeString += time.get() + "ms";

                    System.out.print(beforeString);
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            };

            Thread timeThread = new Thread(timeRunnable);
            timeThread.start();

            algorithm.start();

            timeThread.join();


            classSpeed[i] = (int) (algorithm.getEndTimestamp() - algorithm.getStartTimestamp());

            if (i + 1 < graphs.size())
                for (int l = 0; l < 1000; l++) System.out.print("\b");

            i++;
        }

        return classSpeed;
    }

}
