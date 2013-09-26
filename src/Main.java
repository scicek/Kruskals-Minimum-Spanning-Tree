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
        
        System.out.println("\nResult: " + g.minimumSpandingTree(true));
    }
}
