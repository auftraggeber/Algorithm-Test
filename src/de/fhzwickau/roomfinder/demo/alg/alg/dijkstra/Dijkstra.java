package de.fhzwickau.roomfinder.demo.alg.alg.dijkstra;

import de.fhzwickau.roomfinder.demo.alg.alg.Algorithm;
import de.fhzwickau.roomfinder.demo.alg.alg.Path;
import de.fhzwickau.roomfinder.demo.alg.graph.Edge;
import de.fhzwickau.roomfinder.demo.alg.graph.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class Dijkstra extends Algorithm {

    private Queue<Path> paths;
    private Path pathToFinalNode;
    private Map<Node, Integer> nodeLenghts = new HashMap<>();

    /**
     * Der Konstruktor muss von jedem Kind ganauso übernommen werden, dass Test ablaufen kann.
     *
     * @param startNode Der Startknoten.
     * @param endNode   Der Endknoten.
     */
    public Dijkstra(Node startNode, Node endNode) {
        super(startNode, endNode);

        paths = dataType();
    }

    private Queue<Path> dataType() {
        return new PriorityQueue<Path>((o1,o2)-> {

            if (o1.getLength() < o2.getLength()) {
                return -1;
            } else if (o1.getLength() > o2.getLength()) {
                return 1;
            } else if (o1.getNodes().size() > o2.getNodes().size()) {
                return 1;
            } else if (o1.getNodes().size() < o2.getNodes().size()) {
                return -1;
            } else if (!o1.equals(o2)) {
                return -1;
            }

            return 0;
        });
    }

    @Override
    public void start() {
        super.start();

        paths.add(new Path(null, getStartNode(), 0));
        nodeLenghts.put(getStartNode(), 0);

        calcNextPaths();
    }

    private void calcNextPaths() {

        while (!paths.isEmpty()) {
            Path current = paths.poll();
            Node currentEnd = current.getLast();

            for (Edge conns : currentEnd.getEdges()) {
                Path toNext = new Path(current, conns.getOther(currentEnd), conns.getLength());

                if (nodeLenghts.get(toNext.getLast()) != null && nodeLenghts.get(toNext.getLast()) < toNext.getLength())
                    continue;

                if (pathToFinalNode == null || pathToFinalNode.getLength() > toNext.getLength()) {

                    if (toNext.getLast().getId() == getEndNode().getId()) {
                        pathToFinalNode = toNext;
                    }
                    else {
                        nodeLenghts.put(toNext.getLast(), toNext.getLength());
                        paths.add(toNext);
                    }
                }

            }
        }

        end();
    }



    @Override
    public int getDistance() {
        return pathToFinalNode.getLength();
    }

    @Override
    public Path getCalculatedPath() {
        return pathToFinalNode;
    }
}
