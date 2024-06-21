package IntegralCalc;

//Name: Thomas Zeller Net ID: TJZ230000
public class Node<T extends Comparable<T>> implements Comparable<Node<T>>{
 private T value;
 private Node<T> left;
 private Node<T> right;
 //Default constructor
 public Node(){
 }
 //Overloaded Constructor pass in value
 public Node(T value) {
     this.value = value;
 }
 //Overloaded constructor pass in value and both nodes
 public Node(T value, Node<T> left, Node<T> right) {
     this.value = value;
     this.left = left;
     this.right = right;
 }
 //Left node getter
 public Node<T> getLeft() {
     return left;
 }
 //Right node getter
 public Node<T> getRight() {
     return right;
 }
 //Left node setter
 public void setLeft(Node<T> left) {
     this.left = left;
 }
 //Right node setter
 public void setRight(Node<T> right) {
     this.right = right;
 }
 //Value getter
 public T getValue() {
     return value;
 }
 //Value setter
 public void setValue(T value) {
     this.value = value;
 }
 //Compare values
 @Override
 public int compareTo(Node<T> node) {
     return value.compareTo(node.getValue());
 }
 /**
  * Print the value and recursively print the nodes left and right values
  */
 @Override
 public String toString() {
     String leftStr = "";
     String rightStr = "";
     if(left != null) {
         leftStr = getLeft().toString();
     }
     if(right != null) {
         rightStr = getRight().toString();
     }
     return getValue().toString() + "(" + leftStr + ", " + rightStr + ")";
 }
 
}