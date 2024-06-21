package IntegralCalc;

//Name: Thomas Zeller Net ID: TJZ230000
import java.util.ArrayList;
public class BinTree<T extends Comparable<T>> {
 private Node<T> root;
 //Default constructor
 public BinTree() {
 }
 //Overloaded constructor
 public BinTree(T value) {
     root = new Node<T>(value);
 }
 /**
  * If the root is null insert the value at the root.
  * Else call the recursive insert helper method.
  */
 public void insert(T value) {
     if(root == null) {
         root = new Node<T>(value);
     } else {
         insert(value, root);
     }
 }
 /**
  * If the value we are inserting is less than the current node. We need to look
  * left of the current node. If the left child of the current node is null, insert
  * a new node with the value we are inserting. If there is a left child we will recursivly 
  * call this method so we can expore the left subtree. If the value is greater than or equal
  * to the current node, explore the right subtree with the same process as the left.
  */
 private void insert(T value, Node<T> node) {
     if(value.compareTo(node.getValue()) < 0) {
         if(node.getLeft() == null) {
             node.setLeft(new Node<T>(value));
         } else {
             insert(value, node.getLeft());
         }
     } else {
         if(node.getRight() == null) {
             node.setRight(new Node<T>(value));
         } else {
             insert(value, node.getRight());
         }
     }
 }
 /**
  * Call the recursive search helper method.
  */
 public T search(T target) {
     return search(target, root);
 }
 /**
  * If the current node has the same value as the target, return that nodes value.
  * If it is less explore the left subtree. If else explore the right. If the
  * node is not found return null.
  */
 private T search(T target, Node<T> node) {
     if(node != null) { //base case is if the current node is null
         if(node.getValue().compareTo(target) == 0) {
             return node.getValue();
         } else if(target.compareTo(node.getValue()) < 0 ) {
             return search(target, node.getLeft()); //Recusive call to the left
         } else {
             return search(target, node.getRight()); //Recursive call to the right
         }
     }
     //If not found return null
     return null;
 }
 /**
  * This method is a helper method for remove. It works the same as search except
  * it returns the node instead of teh nodes value.
  */
 private Node<T> searchNode(T target) {
     return searchNode(target, root);
 }
 /**
  * This method works the same as search except it returns the node instead of teh nodes value.
  */
 private Node<T> searchNode(T target, Node<T> node) {
     if(node != null) {
         if(node.getValue().compareTo(target) == 0) {
             return node;
         } else if(target.compareTo(node.getValue()) < 0 ) {
             return searchNode(target, node.getLeft());
         } else {
             return searchNode(target, node.getRight());
         }
     }
     return null;
 }
 /**
  * Helper method for remove. Calls recursive get parent helper method.
  */
 private Node<T> getParent(Node<T> child) {
     return getParent(child, root);
 }
 /**
  * Checks to see if any of the children of current node equal the child node. If they
  * do return that node. If not then check to see if the current node is greater than the child
  * node. If so go explore the left subtree. If else explore the right subtree.
  */
 private Node<T> getParent(Node<T> child, Node<T> node) {
     if(node != null) {
         if((node.getLeft() != null && node.getLeft().compareTo(child) == 0) || (node.getRight() != null && node.getRight().compareTo(child) == 0)) {
             return node;
         }
         if(child.compareTo(node) < 0) {
             return getParent(child, node.getLeft());
         } else {
             return getParent(child, node.getRight());
         }
     }
     return null;
 }
 /**
  * Searches for the target node we want to remove. Then searches for its parent
  * using the get parent method. Then the remove recursive method is called.
  * After we remove the node we will return its value.
  */
 public T remove(T target) {
     Node<T> targetNode = searchNode(target);
     if(targetNode == null) return null;
     Node<T> parent = getParent(targetNode);
     remove(root, parent, targetNode);
     return targetNode.getValue();
 }
 /**
  * First we will check the case that the node we want to remove has
  * two children. If the node has two children we will find the successor using the 
  * get successor method. We will then get the successors parent. Then we will copy
  * the value stored in the successor to the node we are trying to remove. Then we will
  * recursivly call the remove method passing in the successor and its parent. The next case
  * we will check is if we are removing the root with 1 or 0 children. If so we will set the root
  * equal to the its left child if it has a left child. Else we will set it equal to its right child.
  * The next case is the case where we are removing an internal node with a left child only. In this case
  * we will replace the the node with its left child. The final case is a internal node with a right child
  * or a leaf node. We will replace this node with its right child (which is null if it is a leaf node).
  */
 private void remove(Node<T> node, Node<T> parent, Node<T> targetNode) {
     if(targetNode != null) {
         // Two child nodes
         if(targetNode.getLeft() != null && targetNode.getRight() != null) {
             //Find successor and copy its value to the node to be removed
             Node<T> successor = getSuccessor(targetNode.getRight());
             Node<T> successorParent = getParent(successor);
             targetNode.setValue(successor.getValue());
             //Recursivly remove successor
             remove(node, successorParent, successor);
         // Root node with 1 or 0 children
         } else if(targetNode == root) {
             if(targetNode.getLeft() != null) {
                 root = targetNode.getLeft();
             } else {
                 root = targetNode.getRight();
             }
         //Internal node with left child only
         } else if(targetNode.getLeft() != null) {
             if(parent.getLeft() == targetNode) {
                 parent.setLeft(targetNode.getLeft());
             } else {
                 parent.setRight(targetNode.getLeft());
             }
         //Internal node with only right child or leaf
         } else {
             if(parent.getLeft() == targetNode) {
                 parent.setLeft(targetNode.getRight());
             } else {
                 parent.setRight(targetNode.getRight());
             }
         }
     }
 }
 /**
  * Pass in the node's right child. Recursivly go left until you get to a
  * leaf node.
  */
 private Node<T> getSuccessor(Node<T> node) {
     if(node.getLeft() == null) {
         return node;
     }
     return getSuccessor(node.getLeft());
 }
 /**
  * Creates a list and does a reverse in order traversal on the tree and
  * adds each node to the list. It is in reverse so we can have are degrees
  * ordered from highest to lowest.
  */
 public ArrayList<T> treeToList() {
     ArrayList<T> list = new ArrayList<T>();
     ReverseInOrderTrav(list, root);
     return list;
 }
 /**
  * Recursivly add nodes to the list by exploring right, adding the current node,
  * then exploring left
  */
 private void ReverseInOrderTrav(ArrayList<T> list, Node<T> node) {
     if(node != null) {
         //opposite of inorder traversal
         ReverseInOrderTrav(list, node.getRight());
         list.add(node.getValue());
         ReverseInOrderTrav(list, node.getLeft());
     }
 }
 /**
  * Print root to string. The root node will recursivly call every other nodes
  * toString.
  */
 @Override
 public String toString() {
     return root.toString();
 }
}