package de.fhzwickau.roomfinder.demo.alg.alg.bellford;

import de.fhzwickau.roomfinder.demo.alg.alg.Algorithm;
import de.fhzwickau.roomfinder.demo.alg.alg.Path;
import de.fhzwickau.roomfinder.demo.alg.graph.Edge;
import de.fhzwickau.roomfinder.demo.alg.graph.Node;

import java.util.Map;
import java.util.TreeMap;

public class BellFord extends Algorithm {

    Map<Node, Path> paths;

    public BellFord(Node startNode, Node endNode) {
        super(startNode, endNode);
        paths = dataType();
    }

    public Map<Node, Path> dataType() {
        return new TreeMap<Node, Path>((o1, o2) -> {

            if (o1.getId() < o2.getId()) {
                return -1;
            } else if (o1.getId() > o2.getId()) {
                return 1;
            }

            return 0;
        });
    }

    @Override
    public void start() {
        super.start();

        paths.put(getStartNode(), new Path(null, getStartNode(), 0));

        calcPaths(getStartNode());
    }

    private int getCurrentPathLength(Node toNode) {

        if (paths.containsKey(toNode))
            return paths.get(toNode).getLength();

        return -1;
    }

    private boolean pathIsShorterThanCurrent(Path path, Node toNode) {
        return getCurrentPathLength(toNode) == -1 || path.getLength() < getCurrentPathLength(toNode);
    }

    private void calcPaths(Node node) {
        for (Edge e : node.getEdges()) {
            Node other = e.getOther(node);

            Path path = new Path(paths.get(node), other, e.getLength());

            if (pathIsShorterThanCurrent(path, other) && pathIsShorterThanCurrent(path, getEndNode())) {
                paths.put(other, path);
                calcPaths(other);
            }
        }

        if (node.getId() == getStartNode().getId())
            end();
    }

    @Override
    public int getDistance() {
        return getCurrentPathLength(getEndNode());
    }

    @Override
    public Path getCalculatedPath() {
        return paths.get(getEndNode());
    }
}
