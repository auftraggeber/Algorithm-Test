package de.fhzwickau.roomfinder.demo.alg.alg;

import de.fhzwickau.roomfinder.demo.alg.graph.Node;

public abstract class Algorithm {

    private Node startNode, endNode;
    private long startTimestamp, endTimestamp;

    /**
     * Der Konstruktor muss von jedem Kind ganauso übernommen werden, dass Test ablaufen kann.
     * @param startNode Der Startknoten.
     * @param endNode Der Endknoten.
     */
    public Algorithm(Node startNode, Node endNode) {
        this.startNode = startNode;
        this.endNode = endNode;
    }

    /**
     * Start. Nimmt Zeit auf und gibt Ausgabe an Konsole.
     */
    public void start() {
        startTimestamp = System.currentTimeMillis();

        //System.out.println("Started " + getClass().getSimpleName() + " from #" + startNode.getId() + " to #" + endNode.getId());
    }

    /**
     * Ende. Stoppt Zeit und gibt Ausgabe an Konsole.
     */
    public void end() {
        endTimestamp = System.currentTimeMillis();

        long diff = endTimestamp - startTimestamp;

        /*
        System.out.println("");
        System.out.println(getClass().getSimpleName() + " needed " + diff + "ms.");
        System.out.println(getClass().getSimpleName() + " calculated distance: " +  getDistance());

        StringBuilder pathString = new StringBuilder();

        for (Node node : getCalculatedPath().getNodes()) {
            if (!pathString.toString().equals(""))
                pathString.append(" - ");

            pathString.append("#").append(node.getId());
        }

        System.out.println("\n" + getClass().getSimpleName() + " calculated path: \n" + pathString);
        System.out.println("-----------------------------------------------------\n");
        */
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public Node getStartNode() {
        return startNode;
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }

    public Node getEndNode() {
        return endNode;
    }

    public abstract int getDistance();
    public abstract Path getCalculatedPath();
}
