package de.fhzwickau.roomfinder.demo.alg.graph;

public class Graph {

    private final int nodes;
    private final int branchOdds;
    private final int maxBranchesPerNode;
    private final int branchToExistingNodeOdds;

    private Node continueWithNode;

    public static int randomInt(final int min, final int max) {
        int i = (int) (Math.random() * (max - min));

        return i + min;
    }

    private static boolean probabiltyOccured(final int odds) {
        int i = randomInt(1, odds);
        return i <= 1;
    }

    /**
     * Setzt die Parameter für den ungerichteten Graphen.
     * @param nodes Gibt an, wie viele Knoten der Graph haben soll.
     * @param branchOdds Gibt die Chancen an, dass ein Knoten mehr Verzweigungen hat. Jeder x-te Knoten hat eine 2. Verzweigung.
     * @param maxBranchesPerNode Gibt die maximale Anzahl an Verzeigungen pro Knoten an (steht in Verbindung mit branchOdds).
     * @param branchToExistingNodeOdds Gibt die Chancen an, dass sich ein Knoten mit einem schon existierenden Knoten verbindet.
     */
    public Graph(final int nodes, final int branchOdds, final int maxBranchesPerNode, final int branchToExistingNodeOdds) {
        this.nodes = nodes;
        this.branchOdds = branchOdds;
        this.maxBranchesPerNode = maxBranchesPerNode;
        this.branchToExistingNodeOdds = branchToExistingNodeOdds;
    }

    public void build() {

        createNode(null);

        while (continueWithNode != null) {
            Node node = continueWithNode;
            continueWithNode = null;

            createNode(node);
        }

    }

    /**
     * Erstellt und verknüpft Konten rekursiv.
     * @param fromNode Der vorhergehende Knoten.
     */
    private void createNode(Node fromNode) {
        Node node = new Node();

        if (fromNode != null) {
            fromNode.connectTo(node, randomInt(10,300));
        }

        if (node.getId() < nodes) {
            int i = maxBranchesPerNode;

            do {

                // erste Kante darf nicht zu einem alten knoten verbinden, da sonst kreis (deshalb i < maxBranchesPerNode)
                if (i < maxBranchesPerNode && probabiltyOccured(branchToExistingNodeOdds)) {
                    Node randNode = Node.getNode(randomInt(0, node.getId() -1));

                    if (randNode != null) {
                        node.connectTo(randNode, randomInt(100, 700));
                    }
                }
                else {
                    if (node.getId() % 10000 == 9999) {
                        continueWithNode = node;
                        return;
                    }

                    createNode(node);
                }

                i--;
            } while (probabiltyOccured(branchOdds) && i > 0);

        }


    }


}
