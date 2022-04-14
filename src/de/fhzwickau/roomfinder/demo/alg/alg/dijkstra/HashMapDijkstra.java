package de.fhzwickau.roomfinder.demo.alg.alg.dijkstra;

import de.fhzwickau.roomfinder.demo.alg.alg.Path;
import de.fhzwickau.roomfinder.demo.alg.graph.Node;

import java.util.HashMap;
import java.util.Map;

public class HashMapDijkstra extends Dijkstra {
    public HashMapDijkstra(Node startNode, Node endNode) {
        super(startNode, endNode);
    }

    @Override
    public Map<Node, Path> dataType() {
        return new HashMap<>();
    }
}
