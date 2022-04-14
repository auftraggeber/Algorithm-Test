package de.fhzwickau.roomfinder.demo.alg.alg;

import de.fhzwickau.roomfinder.demo.alg.graph.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Path {

    private List<Node> nodes;
    private int length = 0;

    public Path(Path toExtend, Node newNode, int addLength) {
        nodes = new ArrayList<>();

        if (toExtend != null) {
            nodes.addAll(toExtend.getNodes());
            length = toExtend.getLength();
        }

        nodes.add(newNode);

        this.length += addLength;
    }

    public Node getLast() {
        return nodes.get(nodes.size()-1);
    }

    public List<Node> getNodes() {
        return Collections.unmodifiableList(nodes);
    }

    public int getLength() {
        return length;
    }
}
