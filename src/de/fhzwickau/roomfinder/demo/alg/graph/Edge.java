package de.fhzwickau.roomfinder.demo.alg.graph;

public class Edge {

    private int length;
    private Node n1, n2;

    protected Edge(Node n1, Node n2, int length) {
        if (n2.getId() == n1.getId())
            throw new IllegalArgumentException("n1 and n2 must differ.");

        if (length < 0)
            throw new IllegalArgumentException("length has to be greater or equal to zero.");

        this.length = length;

        this.n1 = n1;
        this.n2 = n2;

        n1.addEdge(this);
        n2.addEdge(this);

        //System.out.println("Edge (L: " + length + ") between #" + n1.getId() + " and #" + n2.getId());
    }

    public int getLength() {
        return length;
    }

    public Node getOther(Node that) {
        if (that.getId() == n1.getId())
            return n2;

        else if (that.getId() == n2.getId())
            return n1;

        return null;
    }

}
