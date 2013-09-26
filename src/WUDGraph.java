import java.util.ArrayList;
import java.util.Collections;

public class WUDGraph<E>
{
    private static class Node
    {
        // Index of a neighbour
        public int neighbourIndex;
        // Weight of the edge
        public int edgeWeight;
        // Next node
        public Node nextNode;

        public Node (int neighbourIndex, int edgeWeight)
        {
            this.neighbourIndex = neighbourIndex;
            this.edgeWeight = edgeWeight;
            this.nextNode = null;
        }
    }
    
    // Default capacity of the graph
    public static final int DEFAULT_CAPACITY = 100;

    // Value used to enlargen the graph
    public static final int    ENLARGE_VALUE = 25;  // 25%

    // The vertices of the graph
    private E[] vertices;

    // Sequences of neighbours
    private Node[] adjacencySequences;

    // Last index of the graph
    private int lastIndex = -1;

    public WUDGraph()
    {
        vertices = (E[]) new Object[DEFAULT_CAPACITY];
        adjacencySequences = new Node[DEFAULT_CAPACITY];
    }

    public WUDGraph(int initialCapacity)
    {
        vertices = (E[]) new Object[initialCapacity];
        adjacencySequences = new Node[initialCapacity];
    }

    public WUDGraph (E[] vertices)
    {
        this.vertices = (E[]) new Object[vertices.length];
        
        for (int index = 0; index < vertices.length; index++)
            this.vertices[index] = vertices[index];
        
        adjacencySequences = new Node[vertices.length];
        lastIndex = vertices.length - 1;
    }
    
    // Checks if the graph is empty
    public boolean isEmpty ()
    {
        return lastIndex == -1;
    }

    // Returns the amount of vertices in the graph
    public int size ()
    {
        return lastIndex + 1;
    }

    // Enlargens the capacity of the graph
    protected void enlarge ()
    {
        // The new capacity of the graph
        int newLength = 1 + vertices.length + ENLARGE_VALUE * vertices.length / 100;

        E[] newVertices = (E[]) new Object[newLength];
        Node[] newAdjacencySequences = new Node[newLength];
        
        for (int index = 0; index <= lastIndex; index++)
        {
            newVertices[index] = vertices[index];
            vertices[index] = null;
            newAdjacencySequences[index] = adjacencySequences[index];
            adjacencySequences[index] = null;
        }

        vertices = newVertices;
        adjacencySequences = newAdjacencySequences;
    }

    // Returns the index of the given vertex, or -1 if the vertex is not found
    protected int indexOf (E vertex)
    {
        int indexOfVertex = -1;
        for (int index = 0; index <= lastIndex; index++)
        {
            if (vertex.equals(vertices[index]))
            {
                indexOfVertex = index;
                break;
            }
        }

        return indexOfVertex;
    }

    // Checks if the graph contains the given vertex
    public boolean containsVertex (E vertex)
    {
        return this.indexOf(vertex) != -1;
    }
    
    // Adds a vertex as long as the same vertex does not already exist in the graph
    public void addVertex (E vertex)
    {
        if (!this.containsVertex (vertex))
        {
            // Enlargen the graph if needed
            if (lastIndex == vertices.length - 1)
                this.enlarge ();

            lastIndex = lastIndex + 1;
            vertices[lastIndex] = vertex;
        }
    }

    // Returns a view of all the vertices in the graph
    public E[] verticesView ()
    {
        E[] allVertices = (E[]) new Object[lastIndex + 1];
        for (int index = 0; index < allVertices.length; index++)
            allVertices[index] = this.vertices[index];

        return allVertices;
    }


    // Returns the neighbours of the given vertex
    public E[] getNeighbours (E vertex) throws IllegalArgumentException
    {
        int index = this.indexOf (vertex);

        // Check if the vertex exists in the graph
        if (index < 0)
            throw new IllegalArgumentException (vertex + " was not found!");


        Node node = this.adjacencySequences[index];
        int countNeighbours = 0;
        while(node != null)
        {
            countNeighbours++;
            node = node.nextNode;
        }

        E[] neighbours = (E[]) new Object[countNeighbours];
        node = this.adjacencySequences[index];
        int neighbourIndex = 0;
        
        while(node != null)
        {
            neighbours[neighbourIndex++] = vertices[node.neighbourIndex];
            node = node.nextNode;
        }

        return neighbours;
    }


    // Checks if an edge exists between the given vertices 
    public boolean hasEdge (E vertex1, E vertex2) throws IllegalArgumentException
    {
        int index1 = this.indexOf (vertex1);
        if (index1 < 0)
            throw new IllegalArgumentException (vertex1 + " was not found!");
        int index2 = this.indexOf (vertex2);
        if (index2 < 0)
            throw new IllegalArgumentException (vertex2 + " was not found!");
      
        Node node = adjacencySequences[index1];
        boolean hasEdge = false;
        while (!hasEdge && node != null)
        {
            if (node.neighbourIndex == index2)
                hasEdge = true;
            else
                node = node.nextNode;
        }

        return hasEdge;
    }


    // Returns the weight of the edge between the given vertices, if one exists.
    public int edgeWeight (E vertex1, E vertex2) throws IllegalArgumentException
    {
        int index1 = this.indexOf (vertex1);
        if (index1 < 0)
            throw new IllegalArgumentException (vertex1 + " was not found!");
        int index2 = this.indexOf (vertex2);
        if (index2 < 0)
            throw new IllegalArgumentException (vertex2 + " was not found!");

        Node node = adjacencySequences[index1];
        int edgeWeight = -1;
        while(node != null)
        {
            if (node.neighbourIndex == index2)
            {
                edgeWeight = node.edgeWeight;
                break;
            }
            else
                node = node.nextNode;
        }

        return edgeWeight;
    }


    // Adds the given node to the sequence of the given index, basically adding a neighbour.
    protected void addNode (Node node, int index)
    {
        Node currentNode = adjacencySequences[index];
        if (currentNode == null)
            adjacencySequences[index] = node;
        else
        {
            Node previousNode = null;
            while (currentNode != null && currentNode.edgeWeight < node.edgeWeight)
            {
                previousNode = currentNode;
                currentNode = currentNode.nextNode;
            }

            if (previousNode == null)
                adjacencySequences[index] = node;
            else
                previousNode.nextNode = node;
    
            node.nextNode = currentNode;
        }
    }


    // Removes a node containing the second parameter 
    //from the sequence decided by the first paramater
    protected void removeNode (int seq, int neighbourIndex)
    {
        Node currentNode = adjacencySequences[seq];
        Node previousNode = null;
        while (currentNode != null && currentNode.neighbourIndex != neighbourIndex)
        {
            previousNode = currentNode;
            currentNode = currentNode.nextNode;
        }

        if (currentNode != null)
        {
            if (previousNode != null)
                previousNode.nextNode = currentNode.nextNode;
            else
                adjacencySequences[seq] = currentNode.nextNode;
        }
    }

    // Adds and edge between the given vertices with the given weight
    public void addEdge (E vertex1, E vertex2, int edgeWeight) throws IllegalArgumentException
    {
        if(vertex1 == vertex2)
            return;
        
        int index1 = this.indexOf (vertex1);
        if (index1 < 0)
            throw new IllegalArgumentException (vertex1 + " was not found!");
        
        int index2 = this.indexOf (vertex2);
        if (index2 < 0)
            throw new IllegalArgumentException (vertex2 + " was not found!");


        if (this.hasEdge (vertex1, vertex2))
        {
            this.removeNode (index1, index2);
            this.removeNode (index2, index1);
        }
        
        Node node = new Node(index2, edgeWeight);
        this.addNode(node, index1);
        node = new Node(index1, edgeWeight);
        this.addNode(node, index2);
    }


    // Removes an edge between two vertices
    public void removeEdge (E vertex1, E vertex2) throws IllegalArgumentException
    {
        int index1 = this.indexOf (vertex1);
        if (index1 < 0)
            throw new IllegalArgumentException (vertex1 + " was not found!");
        
        int index2 = this.indexOf (vertex2);
        if (index2 < 0)
            throw new IllegalArgumentException (vertex2 + " was not found!");

	this.removeNode (index1, index2);
        this.removeNode (index2, index1);
    }


    // Removes all the edges of the given vertex
    public void removeEdges (E vertex) throws IllegalArgumentException
    {
        int index = this.indexOf (vertex);
        if (index < 0)
            throw new IllegalArgumentException (vertex + " was not found!");

        Node currentNode = adjacencySequences[index];
        while (currentNode != null)
        {
            this.removeNode (currentNode.neighbourIndex, index);
            currentNode = currentNode.nextNode;
        }

        adjacencySequences[index] = null;
    }

    // Removes the given vertext from the graph
    public void removeVertex (E vertex)
    {
        int indexOfVertex = this.indexOf (vertex);
        if (indexOfVertex != -1)
        {
            this.removeEdges (vertex);
	    for (int index = indexOfVertex + 1; index <= lastIndex; index++)
            {
                vertices[index - 1] = vertices[index];
                adjacencySequences[index - 1] = adjacencySequences[index];
            }
            
            vertices[lastIndex] = null;
            adjacencySequences[lastIndex] = null;
            lastIndex--;
            
            for (int index = 0; index <= lastIndex; index++)
            {
                Node    node = adjacencySequences[index];
                while (node != null)
                {
                    if (node.neighbourIndex > indexOfVertex)
                        node.neighbourIndex--;
                    node = node.nextNode;
                }
            }
	}
    }

    // Clears the graph of vertices and edges
    public void clear ()
    {
        for (int index = 0; index <= lastIndex; index++)
        {
            vertices[index] = null;
            adjacencySequences[index] = null;
        }
        lastIndex = -1;
    }

    public WUDGraph minimumSpandingTree(boolean printInfo)
    {
        boolean activateInfo = printInfo;
        // Local class needed for the algorithm
        class Edge implements Comparable
        {
            int vertex,preVertex,SPWeight;
            public Edge(int vertex, int preVertex, int SPWeight) 
            {
                this.vertex = vertex;
                this.preVertex = preVertex;
                this.SPWeight = SPWeight;
            }
            
            @Override
            public String toString()
            {
                return "(" + vertices[this.vertex] + ", " + 
                       vertices[this.preVertex] + ", " + this.SPWeight + ")";
            }
            
            @Override
            public int compareTo(Object o)
            {
                Edge pn = (Edge) o;
                if(this.SPWeight == pn.SPWeight)
                    return 0;
                else if(this.SPWeight < pn.SPWeight)
                    return -1;
                else
                    return 1;
            }
        }
        
        WUDGraph g = this;
        WUDGraph mst = new WUDGraph();
        // Number of vertices
        int cv = g.size();
        // Number of included edges
        int cie = 0;
        int[] sets = new int[cv];
        int set1,set2;
        ArrayList<Edge> edges = new ArrayList();
        for(int i = 0; i < cv; i++)
            mst.addVertex(g.vertices[i]);

        for(int i = 0; i < g.adjacencySequences.length; i++)
        {
            Node current = g.adjacencySequences[i];
            while(current != null)
            {
                Comparable c1 = (Comparable) vertices[i];
                Comparable c2 = (Comparable) vertices[current.neighbourIndex];
                if(c1.compareTo(c2) <= 0)
                    edges.add(new Edge(i,current.neighbourIndex,current.edgeWeight));
                current = current.nextNode;
            }
        }
        // Sort the list
        Collections.sort(edges);
        for(int i = 0; i < sets.length;i++)
            sets[i] = i;
        
        // Print information
        if(activateInfo)
        {
            System.out.println("Initial phase:");
            System.out.println("CV: " + cv);
            System.out.println("MST: " + mst);
            System.out.println("Edges: " + edges);
            System.out.print("Sets: ");
            for(int i : sets)
                System.out.print(i + ", ");
            System.out.println("\nCIE: " + cie);
        }
        int iteration = 1;
        while(cie < cv - 1)
        {
            if(activateInfo)
                System.out.println("\nIteration: " + iteration++);
            
            Edge e = edges.get(0);
            edges.remove(0);
            set1 = sets[e.vertex];
            set2 = sets[e.preVertex];
            if(set1 != set2)
            {
                mst.addEdge(g.vertices[e.vertex], g.vertices[e.preVertex], e.SPWeight);
                cie++;
                for(int i = 0; i < sets.length;i++)
                    if(sets[i] == set2)
                        sets[i] = set1;
            }
            
            // Print information
            if(activateInfo)
            {
                System.out.println("Edge: " + e);
                System.out.println("set1: " + set1 + ", set2: " + set2);
                System.out.println("MST: " + mst);
                System.out.println("CIE: " + cie);
                System.out.print("Sets: ");
                for(int i : sets)
                    System.out.print(i + ", ");
                System.out.print("\n");
            }
        }
        return mst;
    }
    
    @Override
    public String toString()
    {
        String verticesString = "Vertices: {";
        for (int index = 0; index < lastIndex; index++)
            verticesString += vertices[index] + ", ";
        if (lastIndex >= 0)
            verticesString += vertices[lastIndex];
        verticesString += "}";
        String    edgesString = "{";
        int    counter = 0; 
        for (int index = 0; index <= lastIndex; index++)
        {
            Node    node = adjacencySequences[index];
            while (node != null)
            {
                Comparable c1 = (Comparable) vertices[index];
                Comparable c2 = (Comparable) vertices[node.neighbourIndex];
                if(c1.compareTo(c2) <= 0)
                {
                    if (counter > 0  &&  counter % 5 == 0)
                        edgesString += "\n   ";
                    edgesString += "(" + vertices[index] + ", " + 
                                   vertices[node.neighbourIndex] + ", " + 
                                   node.edgeWeight + "), ";
                    counter++;
                }
                node = node.nextNode;
            }   
        }
        if (edgesString.length () > 1)
            edgesString = edgesString.substring (0,edgesString.length () - 2);
        String string = verticesString + ", Edges: " + edgesString + "}";
        return string;
    }
}