package de.fhzwickau.roomfinder.demo.alg.alg.bellford;

import de.fhzwickau.roomfinder.demo.alg.alg.Path;
import de.fhzwickau.roomfinder.demo.alg.graph.Node;

import java.util.HashMap;
import java.util.Map;

public class HashMapBellFord extends BellFord {
    public HashMapBellFord(Node startNode, Node endNode) {
        super(startNode, endNode);
    }

    @Override
    public Map<Node, Path> dataType() {
        return new HashMap<>();
    }
}
