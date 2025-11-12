/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Disk;

/**
 *
 * @author Jose
 */
public class Bloque {
    private int id;
    private boolean libre;
    private int siguiente;

    public Bloque(int id) {
        this.id = id;
        this.libre = true;
        this.siguiente = -1; // sin enlace
    }

    public boolean estaLibre() {
        return libre;
    }

    public void ocupar() {
        this.libre = false;
    }

    public void liberar() {
        this.libre = true;
        this.siguiente = -1;
    }

    public int getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(int siguiente) {
        this.siguiente = siguiente;
    }

    public int getId() {
        return id;
    }
}


