package DataStruct;

public class Queue {

    private Nodo head, tail;
    private int size;

    public Queue() {
        this.head = this.tail = null;
        size = 0;
    }

    public Nodo getHead() {
        return head;
    }

    public void setHead(Nodo head) {
        this.head = head;
    }

    public Nodo getTail() {
        return tail;
    }

    public void setTail(Nodo tail) {
        this.tail = tail;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void enqueue(Object element) {
        Nodo nodo = new Nodo(element);

        if (isEmpty()) {
            head = nodo;
            tail = nodo;
        } else {
            tail.setNext(nodo);
            tail = nodo;
        }
        size++;
    }

    public void dequeue() {
        if (isEmpty()) {
            return;
        }

        head = head.getNext();
        size--;

        if (head == null) {
            tail = null;   
        }
    }

    public Object dispatch() {
        if (isEmpty()) {
            return null;
        }

        Object element = head.getElement();

        head = head.getNext();
        size--;

        if (head == null) {
            tail = null;   
        }

        return element;
    }

    public boolean isEmpty() {
        return head == null;  
    }

    public boolean contains(Object obj) {
        Nodo current = head;

        while (current != null) {
            if (current.getElement().equals(obj)) {
                return true;
            }
            current = current.getNext();
        }

        return false;
    }

    public boolean remove(Object obj) {
        if (isEmpty()) return false;

        if (head.getElement().equals(obj)) {
            dequeue();
            return true;
        }

        Nodo prev = head;
        Nodo curr = head.getNext();

        while (curr != null) {
            if (curr.getElement().equals(obj)) {

                prev.setNext(curr.getNext());

                if (curr == tail) {
                    tail = prev;
                }

                size--;
                return true;
            }

            prev = curr;
            curr = curr.getNext();
        }

        return false;
    }

    public Object peek() {
        return head == null ? null : head.getElement();
    }

    public void print() {
        Nodo pointer = head;
        while (pointer != null) {
            System.out.println("[ " + pointer.getElement() + " ]");
            pointer = pointer.getNext();
        }
    }
    
    public DataStruct.LinkedList toCustomLinkedList() {

        DataStruct.LinkedList lista = new DataStruct.LinkedList();
        Nodo actual = head;

        while (actual != null) {
            lista.insertFinal(actual.getElement());
            actual = actual.getNext();
        }

        return lista;
    }

}
