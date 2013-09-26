import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/***********************************************************
 * Written by: Simon Cicek                                 *
 * Last changed: 2012-04-28                                *
 *                                                         *
 * A class implementing a set                              *
 * using a binary search tree as underlying datastructure. *
 ***********************************************************/

public class BSTSet<E> implements Set<E>
{
    // The elements of the set
    private BinarySearchTree elements;
    
    public BSTSet()
    {
        elements = new BinarySearchTree();
    }
    
    public BSTSet(Comparator c)
    {
        elements = new BinarySearchTree(c);
    }

    public BSTSet(Set<E> set)
    {
        elements = new BinarySearchTree();
        for (E element : set)
            elements.add(element);
    }
    
    // Set the comparator
    public void setComparator(Comparator c)
    {
        elements.setComparator(c);
    }
    
    public void balance()
    {
        elements.balance();
    }
    
    // Check if the set is empty
    @Override
    public boolean isEmpty() 
    {
        return elements.isEmpty();
    }

    // Return the size of the set
    @Override
    public int size() 
    {
        return elements.size();
    }

    // Check if the given element exists in the set
    @Override
    public boolean contains(E element) 
    {
        return elements.contains(element);
    }

    // Add the given element
    @Override
    public void add(E element) 
    {
        if(elements.contains(element))
            return;
        else
            elements.add(element);
    }

    // Remove the given element
    @Override
    public void remove(E element) 
    {
        elements.remove(element);
    }

    // Clear the set of elements
    @Override
    public void clear() 
    {
        elements.clear();
    }
    
    // Checks if this set is a subset of the given set 
    @Override
    public boolean isSubsetOf(Set<E> set) 
    {
        boolean isSubset = true;
        E[] list = (E[]) this.elements.toArray(BinarySearchTree.INORDER);
        
        // Checks if all the elements in this set exists in the given set
        for (int index = 0; index < elements.size(); index++)
            if (!set.contains (list[index]))
            {
                isSubset = false;
                break;
            }

        return isSubset;
    }
    
    // Returns the union of this set and the given set
    @Override
    public Set<E> union(Set<E> set) 
    {
        Set<E> u = new BSTSet(set);
        Iterator itr = this.iterator();
        
        while(itr.hasNext())
            u.add((E) itr.next());

        return u;
    }

    // Returns the intersection of this set and the given set
    @Override
    public Set<E> intersection(Set<E> set) 
    {
        Set<E> i = new BSTSet();
        Iterator itr = this.iterator();
        
        while(itr.hasNext())
        {
            E e = (E) itr.next();
            if (set.contains(e))
                i.add (e);
        }

        return i;
    }
    
    // Returns the difference of this set and the given set
    @Override
    public Set<E> difference(Set<E> set) 
    {
        Set<E> d = new BSTSet();
        Iterator itr = this.iterator();
        
        while(itr.hasNext())
        {
            E e = (E) itr.next();
            if(!set.contains(e))
                d.add (e);
        }
        return d;
    }

    // Override the toString method in order to print the set in a pretty way
    @Override
    public String toString()
    {
        StringBuilder s = new StringBuilder();
        Iterator itr = this.iterator();
        s.append("{");
        while(itr.hasNext())
        {
            s.append(itr.next());
            if(itr.hasNext())
                s.append(", ");
        }
        s.append("}");
        return s.toString();
    }
    
    // Return an iterator over the set
    @Override
    public Iterator<E> iterator() 
    {
        return this.new SetIterator();
    }
    
    // A representation of an iterator 
    private class SetIterator implements Iterator<E>
    {
        // the set represented as an array (sorted)
        E[] e;
        
        // The index of the next element
        protected int nextIndex;
        
        // The index of the element that was last deleted
        protected int lastReturnedIndex;
        
        public SetIterator()
        {
            nextIndex = 0;
            lastReturnedIndex = -1;
            // Iterate over the set in a sorted manner
            e = (E[]) elements.toArray(BinarySearchTree.INORDER);
        }
        
        // Checks if there are anymore elements in the set
        public boolean hasNext()
        {
            // Check if there are more elements
            return nextIndex <= e.length - 1;
        }
        
        // Returns the next element in the set, if there is another element
        public E next() throws NoSuchElementException
        {
            // There are no more elements
            if (!this.hasNext ())
                throw new NoSuchElementException ("No more elements left");

            // Get the next element in the set
            E element = e[nextIndex];
            lastReturnedIndex = nextIndex;
            nextIndex++;

            return element;
        }

        // Removes the element that was last returned
        public void remove() throws IllegalStateException
        {
            // There is no element to be removed
            if (lastReturnedIndex == -1)
                throw new IllegalStateException ("No element to remove");
            // Remove the element from the tree;
            BSTSet.this.remove(e[lastReturnedIndex]);
            e[lastReturnedIndex] = null;
            nextIndex--;
            lastReturnedIndex = -1;
        }
    }
}
