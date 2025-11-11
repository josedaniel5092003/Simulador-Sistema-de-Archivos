/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataStruct;
import Scheduler.Feedback;
import Model.Process;

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
            setHead(nodo);
            setTail(nodo);
        } else {
            getTail().setNext(nodo);
            setTail(nodo);
        }
        size++;
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

    public void dequeue() {
        if (isEmpty()) {
            System.out.println("La lista esta vacia");
        } else {
            Nodo pointer = getHead();
            setHead(pointer.getNext());
            pointer.setNext(null);
            size--;
        }
    }
    
    public Object dispatch() {
        if (isEmpty()) {
            return null; // evita NullPointerException
        }
        Nodo nodo = head;
        head = head.getNext();
        if (head == null) {
            tail = null;
        }
        return nodo.getElement();
    }

    public boolean isEmpty() {
        return getHead() == null && getTail() == null;
    }
    
    public void print() {
        Nodo pointer = getHead();
        while (pointer != null) {
            System.out.println("[ "+pointer.getElement() + " ]");
            pointer = pointer.getNext();
        }
    }
    
    private int findProcessLevel(Feedback fb, Process p) {
    for (int i = 0; i < fb.getQueues().getLenght(); i++) {
        Queue q = fb.getQueues().getElementGeneric(i);
        Nodo current = q.getHead();
        while (current != null) {
            Object elem = current.getElement();
            if (elem instanceof Process pcb && pcb == p) {
                return i; // nivel donde está el proceso
            }
            current = current.getNext();
        }
    }
    return 0; // por defecto nivel 0 si no lo encuentra
}
    
    public boolean remove(Object obj) {
        if (isEmpty()) {
            return false;
        }

        // Caso 1: el primer nodo es el que se elimina
        if (head.getElement().equals(obj)) {
            head = head.getNext();
            if (head == null) {
                tail = null; // la cola quedó vacía
            }
            size--;
            return true;
        }

        // Caso 2: buscar en el resto de la cola
        Nodo current = head;
        while (current.getNext() != null) {
            if (current.getNext().getElement().equals(obj)) {
                // si el nodo eliminado era el último, actualiza tail
                if (current.getNext() == tail) {
                    tail = current;
                }
                current.setNext(current.getNext().getNext());
                size--;
                return true;
            }
            current = current.getNext();
        }

        return false; // no se encontró
    }
    
    public Object peek() {
    if (head == null) {
        return null;
    }
    return head.getElement(); // Devuelve el dato del primer nodo sin eliminarlo
    }



    
}
