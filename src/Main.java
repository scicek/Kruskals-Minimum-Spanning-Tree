/***************************
 * Written by: Simon Cicek
 * Last changed: 2012-03-28
 ***************************/

public class Main 
{
    public static void main(String[] args)
    {
        WUDGraph g = new WUDGraph();
        g.addVertex("Stockholm");
        g.addVertex("Göteborg");
        g.addVertex("Malmö");
        g.addVertex("Uppsala");
        g.addVertex("Västerås");
        
        g.addEdge("Stockholm", "Uppsala", 70);
        g.addEdge("Stockholm", "Malmö", 613);
        g.addEdge("Göteborg", "Uppsala", 453);
        g.addEdge("Göteborg", "Stockholm", 471);
        g.addEdge("Göteborg", "Västerås", 376);
        g.addEdge("Uppsala", "Göteborg", 452);
        g.addEdge("Uppsala", "Malmö", 679);
        g.addEdge("Västerås", "Malmö", 599);
        
        /*g.addVertex("A");
        g.addVertex("B");
        g.addVertex("C");
        g.addVertex("D");
        g.addVertex("E");
        g.addVertex("F");
        
        g.addEdge("A", "B", 10);
        g.addEdge("A", "C", 20);
        g.addEdge("A", "E", 25);
        g.addEdge("A", "F", 50);
        g.addEdge("B", "C", 11);
        g.addEdge("C", "D", 12);
        g.addEdge("D", "E", 13);
        g.addEdge("D", "B", 30);
        g.addEdge("E", "F", 14);
        
        /*
        g.addEdge("A", "C", 20);
        g.addEdge("A", "B", 30);
        g.addEdge("A", "D", 40);
        g.addEdge("B", "D", 20);
        g.addEdge("B", "E", 50);
        g.addEdge("C", "D", 10);
        g.addEdge("C", "E", 90);
        */
        System.out.println("\nResult: " + g.minimumSpandingTree(true));
    }
}
