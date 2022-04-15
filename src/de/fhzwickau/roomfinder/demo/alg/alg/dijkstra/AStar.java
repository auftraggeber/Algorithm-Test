package de.fhzwickau.roomfinder.demo.alg.alg.dijkstra;

import de.fhzwickau.roomfinder.demo.alg.alg.Path;
import de.fhzwickau.roomfinder.demo.alg.graph.Node;

import java.util.PriorityQueue;
import java.util.Queue;

public class AStar extends Dijkstra{
    /**
     * Der Konstruktor muss von jedem Kind ganauso übernommen werden, dass Test ablaufen kann.
     *
     * @param startNode Der Startknoten.
     * @param endNode   Der Endknoten.
     */
    public AStar(Node startNode, Node endNode) {
        super(startNode, endNode);
    }

    @Override
    protected Queue<Path> dataType() {
        return new PriorityQueue<Path>((o1, o2)-> {
            int o1L = (int) (o1.getLength() * getFactor(o1.getLast()));
            int o2L = (int) (o2.getLength() * getFactor(o2.getLast()));

            if (o1L < o2L) {
                return -1;
            } else if (o1L > o2L) {
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

    private float getFactor(Node offNode) {
        int maxNodeId = Math.max(getStartNode().getId(), getEndNode().getId());
        int minNodeId = Math.min(getStartNode().getId(), getEndNode().getId());

        if (maxNodeId > offNode.getId() && minNodeId < offNode.getId())
            return 0.4F;

        return 1F;
    }
}
