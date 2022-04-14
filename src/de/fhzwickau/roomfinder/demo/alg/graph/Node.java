package de.fhzwickau.roomfinder.demo.alg.graph;

import de.fhzwickau.roomfinder.demo.alg.alg.Path;

import java.util.*;

public class Node {

    private static int lastID = -1;

    private static List<Node> nodes = new ArrayList<>();

    public static Node getNode(int id) {
        if (id < lastID && id < nodes.size()) {
            Node node = nodes.get(id);

            if (node.getId() == id)
                return node;
        }

        return null;
    }

    public static void clear() {
        lastID = -1;
        nodes = new ArrayList<>();
    }

    private int id;
    private Set<Edge> edges = new HashSet<>();

    private Path dijkstraPath;

    public Node() {
        id = ++lastID;

        //System.out.println("Node " + id + " created");

        nodes.add(this);
    }

    public void connectTo(Node node, int length) {

        if (node.getId() != getId())
            new Edge(this, node, length);
    }

    protected void addEdge(Edge e) {
        edges.add(e);
    }

    public Set<Edge> getEdges() {
        return Collections.unmodifiableSet(edges);
    }

    public int getId() {
        return id;
    }
}
