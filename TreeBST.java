package org.example.algoritmica.tree.treebinary;

import lombok.Getter;

import java.util.*;

/**
 * TreeHeap
 *
 * @author Marcos Quispe
 * @since 1.0
 */
public class TreeBST implements TBPrint {

    private Node root;

    @Getter
    private int size;

    private boolean flag;

    private int[] values;

    public TreeBST() {
        root = null;
        size = 0;
        values = new int[10000]; // capacidad inicial
    }

    public boolean isEmpty() {
        return root == null;
    }


    private Node getNode(Node node, int valueToSearch) {
        if (node == null)
            return null;

        if (node.getValue() == valueToSearch)
            return node;

        Node izq = getNode(node.getLeft(), valueToSearch);
        if (izq != null) {
            return izq;
        } else {
            Node der = getNode(node.getRight(), valueToSearch);
            return der;
        }
    }
    public void add(int ...values) {
        for (int value : values) {
            add(value);
        }
    }
    public void add(int value) {
        if (isEmpty()) {
            root = new Node(value);
            size = 1;
            return;

        }
        Node current = root;
        do {
            if (value > current.getValue()) {
                if (current.getRight() == null) {
                    current.setRight(new Node(value));
                    return;
                }
                else {current = current.getRight();}
            }
            if (value < current.getValue()) {
                if (current.getLeft() == null) {
                    current.setLeft(new Node(value));
                    return;
                }
                else {current = current.getLeft();}
            }
            else {return;}
        } while (true);

    }

    public void addInArray(int value) {
        values[size] = value;
        size++;
        heapify();
    }

    public void heapify() {
        int pos = size;
        int posParent;
        while (pos > 1) {
            posParent = pos / 2;
            if (values[pos - 1] > values[posParent - 1]) {
                swap(values, pos - 1, posParent - 1);
            }
            pos = posParent;
        }
    }

    public void printArray() {
        System.out.println(Arrays.toString
                (Arrays.copyOfRange(values, 0, size)));
    }

    // funcion que permite intercambiar elementos de un array
    public void swap(int[] values, int i, int j) {
        int aux = values[i];
        values[i] = values[j];
        values[j] = aux;
    }

    private void addMaxHeap(Node node, int value, int nivelFinal, int nivelActual, Node parent) {
        if (flag)
            return;
        if (nivelActual == nivelFinal) {
            if (node == null) { // inserta nuevo nodo
                if (parent.getLeft() == null) {
                    parent.setLeft(new Node(value));
                } else {
                    parent.setRight(new Node(value));
                }
                size++;
                flag = true;
                return;
            } else {
                return;
            }
        }

        addMaxHeap(node.getLeft(), value, nivelFinal, nivelActual + 1, node);
        swapHeap(node, node.getLeft());

        addMaxHeap(node.getRight(), value, nivelFinal, nivelActual + 1, node);
        swapHeap(node, node.getRight());
    }

    /**
     * Obtiene el nodo mas a la derecha del ultimo nivel y se desenlaza de su padre
     * El ultimo nodo ocupa el lugar de la raiz y se  acomoda su valor comparando hacia abajo
     * Al final se retorna el valor original de la raiz antes de ser reemplazada
     * @return valor original de la raiz
     */


    private Node deleteLast(Node node, int level, int levelFinal, Node parent) {
        if (flag)
            return null;
        if (level == levelFinal) {
            if (node != null) { // ultimo valor
                if (parent.getLeft() == node) {
                    parent.setLeft(null);
                } else {
                    parent.setRight(null);
                }
                size--;
                //System.out.println("ultimo: "+node.getValue());
                flag = true;
                return node;
            } else {
                return null;
            }
        }

        Node der = deleteLast(node.getRight(), level + 1, levelFinal, node);
        if (der != null)
            return der;
        Node izq = deleteLast(node.getLeft(), level + 1, levelFinal, node);
        if (izq != null)
            return izq;
        return null;
    }

    public void print() {
        print(root);
    }
    private void print(Node node) { // metodo mascara
        if (node == null)
            return;
        System.out.println(node.getValue());
        print(node.getLeft());
        print(node.getRight());
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public int depth() {
        return depth(root);
    }

    /**
     * Retorna la longitud del camino mas largo
     * @param node
     * @return
     */
    public int depth(Node node) {
        if (node == null)
            return 0;
        if (node.isLeaf())
            return 1;

        int izq = depth(node.getLeft());
        int der = depth(node.getRight());
        return Math.max(izq, der) + 1;
    }

    /**
     * Retorna true si el arbol es perfecto
     * Un arbol es perfecto si todos sus niveles estan llenos
     * @return
     */
    public boolean isPerfect() {
        return Math.pow(2d, depth()) - 1 == size;
    }

    /**
     * Intercambia los valores de los nodos node1 y node2
     * @param node1
     * @param node2
     */
    private void swap(Node node1, Node node2) {
        int aux = node1.getValue();
        node1.setValue(node2.getValue());
        node2.setValue(aux);
    }
    private void swapHeap(Node nodeParent, Node nodeChild) {
        if (nodeChild != null && nodeChild.getValue() > nodeParent.getValue()) {
            int aux = nodeParent.getValue();
            nodeParent.setValue(nodeChild.getValue());
            nodeChild.setValue(aux);
        }
    }

    public void remove(int value) {
        Node node = getNode(root,value);
        Node hijo;
        if (node.isLeaf()) {
            Node parent = getParent(root,node);
            if (parent.getLeft() == node) {
                parent.setLeft(null);
            }
            if (parent.getRight() == node) {
                parent.setRight(null);
            }
        }
        if (node.hasOneSon()) {
            if (node.getLeft() != null) {
                hijo = node.getLeft();
            } else hijo = node.getRight();
            int holder = node.getValue();
            node.setValue(hijo.getValue());
            hijo.setValue(holder);
            remove(hijo.getValue());
        }
        if (node.hasTwoSon()) {
            int holder = node.getValue();
            int holdmyvalue = getNextInOrder(node).getValue();
            remove(getNextInOrder(node).getValue());
            node.setValue(holdmyvalue);
        }
    }

    private Node getParent(Node currentNode, Node targetNode) {
        if (currentNode == null || targetNode == null) {
            return null;
        }
        // Check if the target node is a child of the current node
        if (currentNode.getLeft() == targetNode || currentNode.getRight() == targetNode) {
            return currentNode;
        }
        // Recursively search in left and right subtrees
        Node leftResult = getParent(currentNode.getLeft(), targetNode);
        if (leftResult != null) {
            return leftResult;
        }
        return getParent(currentNode.getRight(), targetNode);
    }

    public Node getNextInOrder(Node node) {
        LinkedList<Node> lista = new LinkedList<>();
        int indextosearch = InOrder(lista,root).indexOf(node);
        return InOrder(lista,root).get(indextosearch+1);
    }
    public LinkedList<Node> InOrder(LinkedList<Node> lista, Node node) {
        if (node == null)
            return lista;

        InOrder(lista,node.getLeft());
        lista.add(node);
        InOrder(lista,node.getRight());
        return lista;
    }

    public Node getNodeByValue(Node node,int valueToSearch) {
        if (node == null)
            return null;
        if (node.getValue() == valueToSearch)
            return node;

        Node izq = getNode(node.getLeft(), valueToSearch);
        if (izq != null) {
            return izq;
        } else {
            Node der = getNode(node.getRight(), valueToSearch);
            return der;
        }
    }


    public static void main(String[] args) {
        TreeBST tb = new TreeBST();
//        tb.putRoot(10);
//        tb.putLeft(10, 20);
//        tb.putRight(10, 30);
//
//        tb.putRight(20, 15);
////        tb.putLeft(30, 25);
////        tb.putLeft(30, 35);
//        tb.putLeft(20, 28);
        //tb.print();
//        tb.add(6);
//        tb.add(1);
//        tb.add(5);
//        tb.add(3);
//        tb.add(6);
//        tb.add(7);
        tb.add(21,15,18,10,32,5);
        TBPrintUtil.print(tb);
        tb.remove(10);
        TBPrintUtil.print(tb);
        //System.out.println(tb.depth(tb.root));
        //System.out.println(tb.isFull(tb.root));

//        tb.deleteHeap();
//        tb.deleteHeap();
//        System.out.println();
//        TBPrintUtil.print(tb);
        //tb.postOrden(tb.root);
        //tb.bfs();
//        System.out.println(tb.getSize2(tb.root));
//        System.out.println(tb.areSiblings(tb.root, 30, 28));

    }
}