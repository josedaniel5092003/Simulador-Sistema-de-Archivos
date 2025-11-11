/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataStruct;

import Model.Process;

/**
 *
 * @author VivianaPetit
 * @param <E>
 */
public class LinkedList<E> {

    private Nodo<E> first;
    private Nodo<E> last;
    private int length;

    /**
     * Constructor de la clase Lista
     */
    public LinkedList() {
        this.first = null;
        this.last = null;
        this.length = 0;
    }

    /**
     * Evalua si la lista est&aacute; vac&iacute;a.
     *
     * @return <code>true</code> si la lista esta vacia.
     */
    public boolean esVacio() {
        return first == null;
    }

    /**
     * Inserta un nuevo nodo al final de la lista.
     *
     * @param info almacena la info para crear el nodo.
     */
    public void insertFinal(E info) {
        Nodo<E> nuevo = new Nodo<>(info);
        if (esVacio()) {
            first = nuevo;
            last = nuevo;
        } else {
            Nodo<E> aux = last;
            aux.setNext(nuevo);
            last = nuevo;
        }
        length++;
    }
    
     public Process getElementIn(int posicion) {
        if (posicion < 0 || posicion >= length) {
            System.out.println("La posición debe estar entre 0 y " + (length - 1));
        }
         
        Nodo<Process> actualNodo = (Nodo<Process>) first;
        
        for (int i = 0; i < posicion; i++) {
            actualNodo = actualNodo.getNext();
        }
        return actualNodo.getElement();
    }
     
     public E getElementGeneric(int index) {
    if (index < 0 || index >= length) {
        throw new IndexOutOfBoundsException(
            "La posición debe estar entre 0 y " + (length - 1)
        );
    }

    Nodo<E> actual = first;
    for (int i = 0; i < index; i++) {
        actual = actual.getNext();
    }
    return actual.getElement();
}

    /**
     * Devuelve los elementos de una lista en una cadena.
     *
     * @return un <code>String</code> con los elementos de la lista.
     */
    @Override
    public String toString() {
        String cadena = "";
        if (esVacio()) {
            cadena = "La lista está vacía.";
        } else {
            Nodo<E> aux = first;
            for (int i = 0; i < length; i++) {
                cadena = cadena + aux.getElement();
                if (i != length - 1) {
                    cadena = cadena + ", ";
                }
                aux = aux.getNext();
            }
        }
        return cadena;
    }
    
    /**
     * Devuelve el valor del nodo en la posición especificada en minúsculas y sin espacios en blanco.
     * 
     * @param index La posición del nodo.
     * @return El valor del nodo como <code>String</code>.
     */
    public String toStringAt(int index) {
        String valor = "";
        Nodo<E> aux = first;
        for (int i = 0; i < length; i++) {
            if (i == index) {
                valor = (String) aux.getElement();
            }
            aux = aux.getNext();
        }
        valor = valor.toLowerCase().trim();
        return valor;
    }

     /**
     * Verifica si la lista contiene un valor especificado.
     * 
     * @param valor El valor a buscar en la lista.
     * @return <code>true</code> si la lista contiene el valor, <code>false</code> en caso contrario.
     */
    public boolean contains(E valor) {
        boolean encontrado = false;
        String aux;
        for (int i = 0; i < length; i++) {
            aux = this.toStringAt(i);
            if (aux.equals(valor)) {
                encontrado = true;
            }
        }
        return encontrado;
    }
    
    /**
     * Verifica si existe un nodo con el valor especificado en la lista.
     * 
     * @param valor El valor a buscar en la lista.
     * @return <code>true</code> si existe un nodo con el valor, <code>false</code> en caso contrario.
     */
    public boolean existe(E valor) {
        boolean encontrado = false;
        Nodo<E> actual = first;
        while (actual != null) {
            if (actual.getElement().equals(valor)) {
                encontrado = true;
            }
            actual = actual.getNext();
        }
        return encontrado;
    }
    
    
    /**
     * Ordena una lista de cadenas alfabéticamente utilizando el algoritmo de ordenamiento burbuja.
     * 
     * @param lista La lista de cadenas a ordenar.
     * @return Una lista ordenada de cadenas.
     */
    public LinkedList<String> ordenarLista(LinkedList<String> lista) {
        String[] arr = new String[lista.getLenght()];
        Nodo<String> actual = lista.getFirst();
        int index = 0;
        while (actual != null) {
            arr[index++] = actual.getElement();
            actual = actual.getNext();
        }
        // Ordenamiento burbuja
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j].compareTo(arr[j + 1]) > 0) {
                    String temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
        LinkedList<String> sortedList = new LinkedList<>();
        for (String titulo : arr) {
            sortedList.insertFinal(titulo);
        }
        return sortedList;
    }
    
    /**
    * Reemplaza el elemento en la posición especificada
    * @param index posición del elemento a reemplazar (0-based)
    * @param element nuevo elemento a colocar en esa posición
    * @return true si se pudo reemplazar, false si el índice es inválido
    */
    public boolean setElementIn(int index, E element) {
        if (index < 0 || index >= length) {
            System.out.println("La posición debe estar entre 0 y " + (length - 1));
            return false;
        }

        Nodo<E> actual = first;
        for (int i = 0; i < index; i++) {
            actual = actual.getNext();
        }

        actual.setElement(element);
        return true;
    }
    

    /**
     * Obtener el primer nodo.
     *
     * @return el primer nodo de la lista.
     */
    public Nodo<E> getFirst() {
        return first;
    }

    /**
     * Establece el primer nodo.
     *
     * @param first primer nodo de la lista.
     */
    public void setFirst(Nodo<E> first) {
        this.first = first;
    }

    /**
     * Obtener el &uacute;ltimo nodo.
     *
     * @return el &uacute;ltimo nodo de la lista.
     */
    public Nodo<E> getLast() {
        return last;
    }

    /**
     * Establecer el &uacute;ltimo nodo.
     *
     * @param last &uacute;ltimo nodo de la lista.
     */
    public void setLast(Nodo<E> last) {
        this.last = last;
    }

    /**
     * Obtener la longitud de la lista.
     *
     * @return un <code>int</code> con la longitud de la lista.
     */
    public int getLenght() {
        return length;
    }

    /**
     * Establecer la longitud de la lista.
     *
     * @param length longitud de la lista.
     */
    public void setLenght(int length) {
        this.length = length;
    }
    
    
}
