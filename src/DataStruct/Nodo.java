/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataStruct;


/**
 *
 * @author VivianaPetit
 * @param <E>
 */
public class Nodo<E> {
    private E element;
    private Nodo next;
    
    /**
     * Constructor de la clase.
     * @param element informaci&oacute; para crear el nodo.
     */
    public Nodo (E element){
        this.next = null;
        this.element = element;
    }
    
    /**
     * Obtener el contenido de un nodo.
     * @return el contenido del nodo. 
     */
    public E getElement() {
        return element;
    }
    /**
     * Obtener el apuntador al nodo siguiente.
     * @return el apuntador al nodo siguiente.
     */
    public Nodo<E> getNext() {
        return next;
    }
    
    /**
     * Establecer el contenido de un nodo.
     * @param element contenido del nodo.
     */
    public void setElement(E element) {
        this.element = element;
    }
    
    /**
     * Establecer el apuntador al nodo siguiente.
     * @param next apuntador al nodo siguiente.
     */
    public void setNext(Nodo<E> next) {
        this.next = next;
    }
    
}

