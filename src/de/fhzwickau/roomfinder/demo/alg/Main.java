package de.fhzwickau.roomfinder.demo.alg;

import de.fhzwickau.roomfinder.demo.alg.alg.Algorithm;
import de.fhzwickau.roomfinder.demo.alg.alg.bellford.BellFord;
import de.fhzwickau.roomfinder.demo.alg.alg.bellford.HashMapBellFord;
import de.fhzwickau.roomfinder.demo.alg.alg.dijkstra.Dijkstra;
import de.fhzwickau.roomfinder.demo.alg.graph.Graph;
import de.fhzwickau.roomfinder.demo.alg.graph.Node;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {


    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {


        Map<Class<? extends Algorithm>, Integer[]> classSpeed = testAlgorithms(100,7000, BellFord.class, HashMapBellFord.class, Dijkstra.class);


        /*
            Zeiten ausgeben.
         */
        for (Class<? extends Algorithm> clazz : classSpeed.keySet()) {
            Integer[] times = (classSpeed.containsKey(clazz)) ? classSpeed.get(clazz) : new Integer[0];

            int average = getAverage(times);
            int median = getMedian(times);

            System.out.println("\n\n--------------[RESULT FOR " + clazz.getSimpleName() + "]--------------\n");

            System.out.println("Average time: " + average + "ms.");
            System.out.println("Median time: " + median + "ms.");

            for (Class<? extends Algorithm> other : classSpeed.keySet()) {
                if (other.equals(clazz))
                    continue;

                Integer[] oTimes = (classSpeed.containsKey(other)) ? classSpeed.get(other) : new Integer[0];

                int oAverage = getAverage(oTimes);
                int oMedian = getMedian(oTimes);

                float averageRatio = (float) Math.round((float) oAverage * 100 / average) / 100;
                float medianRatio = (float) Math.round((float) oMedian * 100 / median) / 100;

                System.out.println("Average: " + averageRatio + " times faster than " + other.getSimpleName());
                System.out.println("Median: " + medianRatio + " times faster than " + other.getSimpleName());
            }

        }



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

    /**
     * Testet die Algorithmen.
     * @param cycles Wie viele Durchgänge getestet werden sollen.
     * @param nodes Die Anzahl an Knoten.
     * @param classesToTest Die Algorithmus-Klassen, die verglichen werden sollen.
     * @return Alle Zeiten zu den Algorithmen.
     */
    private static Map<Class<? extends Algorithm>, Integer[]> testAlgorithms(final int cycles, final int nodes, Class<? extends Algorithm> ... classesToTest) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Map<Class<? extends Algorithm>, Integer[]> classSpeed = new HashMap<>();

        for (int i = 0; i < cycles; i++) {

            Node.clear();

            Graph g = new Graph(nodes,5,3, 10);
            g.build();


            System.out.println("\n\n--------------[STARTING FOR " + nodes + " NODES " + "(" + (i+1) + " / " + cycles + ")]--------------\n");
            Node s = Node.getNode(Graph.randomInt(0,nodes - 1));
            Node e = Node.getNode(Graph.randomInt(0, nodes - 1));

            /*
             * Für jeden Algo. testen und Ergebnisse norieren.
             */
            for (Class<? extends Algorithm> clazz : classesToTest) {
                Constructor<? extends Algorithm> constructor = clazz.getDeclaredConstructor(Node.class, Node.class);
                constructor.setAccessible(true);
                Algorithm algorithm = constructor.newInstance(s,e);

                algorithm.start();

                Integer[] times = (classSpeed.containsKey(clazz)) ? classSpeed.get(clazz) : new Integer[cycles];
                times[i] = (int) (algorithm.getEndTimestamp() - algorithm.getStartTimestamp());
                classSpeed.put(clazz, times);
            }
        }

        return classSpeed;
    }

}
