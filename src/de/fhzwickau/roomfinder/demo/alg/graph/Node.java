package de.fhzwickau.roomfinder.demo.alg.graph;

import de.fhzwickau.roomfinder.demo.alg.alg.Path;

import java.util.*;

public class Node {

    private static int lastID = -1;


    private int id;
    private Set<Edge> edges = new HashSet<>();

    private Path dijkstraPath;

    public Node() {
        id = ++lastID;

        //System.out.println("Node " + id + " created");

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
