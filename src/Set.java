import java.util.Iterator;

/*************************************
 * Written by: Simon Cicek           *
 * Last changed: 2012-04-26          *
 *                                   *
 * The interface representing a set. *                             
 *************************************/

public interface Set<E> extends Iterable<E>
{
    // Checks if the set is empty
    boolean isEmpty ();
    
    // Returns the size of the set
    int size ();

    // Checks if the set contains the given element
    boolean contains (E element);

    // Adds the given element if it does not exist in the set
    void add (E element);

    // Removes the given element from the set, if it exists in the set
    void remove (E element);
    
    // Clears the set of elements
    void clear ();

    // Checks if this set is a subset of the given set
    boolean isSubsetOf (Set<E> set);

    // Returns the union between this set and the given set
    Set<E> union (Set<E> set);

    // Returns the intersection between this set and the given set
    Set<E> intersection (Set<E> set);
    
    // Returns the difference between this set and the given set
    Set<E> difference (Set<E> set);

    // Returns an iterator
    Iterator<E> iterator ();
}
