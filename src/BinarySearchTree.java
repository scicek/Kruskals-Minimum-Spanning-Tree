import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;

/**********************************************
 * Written by: Simon Cicek                    *
 * Last changed: 2012-04-28                   *
 *                                            *
 * A class implementing a binary search tree. *
 **********************************************/

public class BinarySearchTree<E>
{
    // Inner clss representing a node
    protected class Node
    {
        public E element;
        public Node leftNode;
        public Node rightNode;

        public Node (E element)
        {
            this.element = element;
            this.leftNode = null;
            this.rightNode = null;
        }
    }

    // Ways to traverse the tree
    public static final int PREORDER = -1;
    public static final int INORDER = 0;
    public static final int POSTORDER = 1;

    // vänstra eller högra länken
    private static final int LEFT = -1;
    private static final int RIGHT = 1;
    
    private Comparator comp;
    protected Node root;

    public BinarySearchTree()
    {
        root = null;
        comp = new DefaultComparator();
    }
    
    public BinarySearchTree(Comparator c)
    {
        root = null;
        comp = c;
    }
    
    // Set the comparator
    public void setComparator(Comparator c)
    {
        if(c == null)
            this.comp = new DefaultComparator();
        else
            this.comp = c;
    }

    // Check if the tree is empty
    public boolean isEmpty ()
    {
        return root == null;
    }

    // Return the size of the tree (public interface)
    public int size ()
    {
        return size (root);
    }
    
    // Use recursion to find the size of the tree 
    private int size (Node root)
    {
        int    numberOfElements = 0;
        if (root != null)
            // Use recursion to count every node
            numberOfElements = 1 + size (root.leftNode) + size (root.rightNode);

        return numberOfElements;
    }
    
    // Get the node containing the given element
    protected Node nodeOf (E element)
    {
        Node currentNode = root;
        Node elementNode = null; 
        
        // While there are more nodes
        while (currentNode != null)
        {
            // If the node has been found
            if (comp.compare(element,currentNode.element) == 0)
            {
                elementNode = currentNode;
                break;
            }
            // If element < the element in the current node
            else if (comp.compare(element,currentNode.element) < 0)
                // Visit the current nodes left child
                currentNode = currentNode.leftNode;
            else 
                // Visit the current nodes right child
                currentNode = currentNode.rightNode;
        }

        return elementNode;
    }

    // Add the given element to the tree
    public void add (E element)
    {
        Node newNode = new Node (element);
        
        // Tree is empty
        if (root == null)
            root = newNode;
        else
        {
            Node current = root;
            boolean done = false;
            
            while (!done)
            {
                // Element to be added is <= the element of the current node
                // and should go to the left of the nurrent node
                if (comp.compare(element, current.element) <= 0)
                {
                    // If the current node has no left child
                    if (current.leftNode == null)
                    {
                        current.leftNode = newNode;
                        done = true;
                    }
                    else
                        // Visit the current nodes left child
                        current = current.leftNode;
                }
                // The element to be added should go to the right of the current node
                else 
                {
                    // If the current node has no right child
                    if (current.rightNode == null)
                    {
                        current.rightNode = newNode;
                        done = true;
                    }
                    else                        
                        // Visit the current nodes right child
                        current = current.rightNode;
                }
            }
        }
    }

    // Returns the given element (mostly useful when the tree contains key-value pairs)
    public E get(E element)
    {
        return get(element,root);
    }
    
    // Use recursion to find the given element
    private E get(E element, Node root)
    {
        // Element to be returned
        E ele = null;
        // Base case: function called on an empty tree
        if(root != null)
        {
            // Base case: element found in the root
            if(comp.compare(element, root.element) == 0)
                ele = root.element;
            // Given element is smaller than the element of the root, search through
            // the tree with root = the given roots left child
            else if(comp.compare(element, root.element) < 0)
                ele = get(element,root.leftNode);
            // Given element is smaller than the element of the root, search through
            // the tree with root = the given roots right child
            else    
                ele = get(element,root.rightNode);
        }
        
        return ele;
    }
    
    // Check if the given element exists in the tree
    public boolean contains (E element)
    {
        return this.nodeOf (element) != null;
    }
    
    // Removes the given element from the tree
    public void remove (E element)
    {
        Node elementNode   = root;
        Node elementParent = null;
        int compareValue   = 0;
        int leftOrRight    = 0;
		
        while (elementNode != null)
        {
            compareValue = comp.compare(element, elementNode.element);
            
            // Element was found at the current node
            if (compareValue == 0)  
                break;
            // Element is smaller than the element of the current node
            else if (compareValue < 0)
            {
                // Visit the current nodes left child and refer to the current node as parent
                elementParent = elementNode;
                elementNode = elementNode.leftNode;
                // We moved down the left side of the tree
                leftOrRight = LEFT;
            }
            // Element is larger than the element of the current node
            else
            {
                // Visit the current nodes right child and refer to the current node as parent
                elementParent = elementNode;
                elementNode = elementNode.rightNode;
                // We moved down the right side of the tree
                leftOrRight = RIGHT;
            }
        }
        
        // Element was not found
        if (elementNode == null)
            return;
	
        // Special case: Element to be removed is in the root
        if (elementNode == root)
        {
            // If the root has no left child, set it's right child as root
            if (root.leftNode == null)
                root = root.rightNode;
            else
            {
                Node node = root.leftNode;
                Node parentNode = root;

                // Traverse the right side of the tree with root = old roots left child
                while (node.rightNode != null)
                {
                    parentNode = node;
                    node = node.rightNode;
                }
                
                // If the current nodes parent is the root 
                // then we have not moved
                if (parentNode == root)
                {
                    // Make the roots left child the new root and connect the right
                    // side of the tree to the new nodes right child
                    node.rightNode = root.rightNode;
                    root = node;
                }
                // We have moved down the tree
                else
                {
                    // Replace the root element with that of the node we stopped at
                    root.element = node.element;
                    // Replace the parents right node with the left child of its old right child
                    parentNode.rightNode = node.leftNode;
                }
            }
        }
        // The node of the element has no left child
        else if (elementNode.leftNode == null)
        {
            // The last move we did was to the left
            if (leftOrRight == LEFT)
                elementParent.leftNode = elementNode.rightNode;
            // The last move we did was to the right
            else
                elementParent.rightNode = elementNode.rightNode;
        }
        else if (elementNode.rightNode == null)
        {
            // The last move we did was to the left
            if (leftOrRight == LEFT)
                elementParent.leftNode = elementNode.leftNode;
            // The last move we did was to the right
            else
                elementParent.rightNode = elementNode.leftNode;
        }
        else
        {
            // Target the tree to the left of the node containing the given element
            Node node = elementNode.leftNode;
            Node nodeParent = elementNode;
            
            // Move down the right side of the tree
            while(node.rightNode != null)
            {
                nodeParent = node;
                node = node.rightNode;
            }
            // Replace the element in the root of this tree with 
            // the element of the last leaf in the right side
            elementNode.element = node.element;
            
            // If we have not moved
            if (nodeParent == elementNode)
                // Disconnect the left child of the elementNode
                nodeParent.leftNode = node.leftNode;
            else
                // Disconnect the right child of the elementNode
                nodeParent.rightNode = node.leftNode;
        }
    }

    // Clear the tree
    public void clear ()
    {
        root = null;
    }

    // Define how to print the tree (public interface)
    public String toString ()
    {
        StringBuilder string = new StringBuilder();
        toString (root, string);

        return string.toString ();
    }
    
    // Use recursion to define how to print the tree
    private void toString (Node tree, StringBuilder string)
    {
        if (tree != null)
        {
            toString (tree.leftNode, string);
            string.append (tree.element + " ");
            toString (tree.rightNode, string);
        }
    }
    
    // Return a queue representation of the tree, parameter describes the traversal order
    public Deque<E> toQueue (int order) throws IllegalArgumentException
    {
        // Only allow legit orders
        if (order != PREORDER  &&  order != INORDER  && order != POSTORDER)
            throw new IllegalArgumentException ("illegal argument: " + order);

        Deque<E>    queue = new ArrayDeque();
        
        // Call the right function given an order
        switch (order)
        {
            case PREORDER:
                preOrder (root, queue);
                break;
            case INORDER:
                inOrder (root, queue);
                break;
            case POSTORDER:
                postOrder (root, queue);
                break;
        }

        return queue;
    }
    
    // Return an array view of the tree given an order
    public E[] toArray(int order)
    {
        if (order != PREORDER  &&  order != INORDER  && order != POSTORDER)
            throw new IllegalArgumentException ("illegal argument: " + order);
        
        return (E[]) this.toQueue(order).toArray();
    }

    // Traverse the tree in the order: Preorder, adding the lements to the queue
    protected void preOrder (Node tree, Deque<E> queue)
    {
        if (tree != null)
        {
            queue.addLast (tree.element);
            preOrder (tree.leftNode, queue);
            preOrder (tree.rightNode, queue);
        }
    }
    
    // Traverse the tree in the order: Inorder, adding the lements to the queue
    protected void inOrder (Node tree, Deque<E> queue)
    {
        if (tree != null)
        {
            inOrder (tree.leftNode, queue);
            queue.addLast (tree.element);
            inOrder (tree.rightNode, queue);
        }
    }

    // Traverse the tree in the order: postorder, adding the lements to the queue
    protected void postOrder (Node tree, Deque<E> queue)
    {
        if (tree != null)
        {
            postOrder (tree.leftNode, queue);
            postOrder (tree.rightNode, queue);
            queue.addLast (tree.element);
        }
    }

    // Balance the tree, giving it a better structure and thus better search time
    public void balance ()
    {
        E[] array = this.toArray(INORDER);
        
        // Clear the root
        this.clear();

        // Add the elements in the array to the tree in a balanced order
        this.add(array, 0, array.length - 1);
    }

    // Add all the elements in the given array between the given indices to the tree
    private void add (E[] array, int lowIndex, int highIndex)
    {
        // There is only one element to be added
        if (lowIndex == highIndex)
            this.add(array[lowIndex]);
        // There are only two elements to be added
        else if (lowIndex + 1 == highIndex)
        {
            this.add (array[lowIndex]);
            this.add (array[highIndex]);
        }
        // There are more than 2 elements to be added
        else
        {
            // Find the middle element
            int middleIndex = (lowIndex + highIndex) / 2;
            // Add it to the tree
            this.add(array[middleIndex]);

            // Use recursion to populate the left/right side of the tree
            this.add(array, lowIndex, middleIndex - 1);
            this.add(array, middleIndex + 1, highIndex);
        }
    }
    
    // A binary search tree is useless if elements can not be compared.
    // this class is used by default if no other comparator is given
    private class DefaultComparator implements Comparator
    {
        @Override
        public int compare(Object o1, Object o2) 
        {
            try
            {
                Comparable c1 = (Comparable) o1;
                Comparable c2 = (Comparable) o2;
                return c1.compareTo(c2);
            }
            catch(Exception e)
            {
                throw new ClassCastException("Objects could not be compared");
            }
        }        
    }
}