/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FileSystem;

import DataStruct.LinkedList;
import Process.Proceso;

/**
 *
 * @author Jose
 */
public class Archivo {
    private String nombre;
    private int tamanioBloques;
    private int primerBloque;
    private Proceso owner;  // proceso que lo creó
    private Directorio dirPadre; 
    private LinkedList listaBloques;

    public Archivo(String nombre, int tamanioBloques, int primerBloque, Proceso owner, Directorio dirPadre, LinkedList listaBloques) {
        this.nombre = nombre;
        this.tamanioBloques = tamanioBloques;
        this.primerBloque = primerBloque;
        this.owner = owner;
        this.dirPadre = dirPadre;
        this.listaBloques = listaBloques;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPrimerBloque() {
        return primerBloque;
    }

    public int getTamanioBloques() {
        return tamanioBloques;
    }
    
    public Proceso getOwner() {
        return owner;
    }

    public void setNombre(String nuevoNombre) {
        this.nombre = nuevoNombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
    
    public Directorio getPadre() {
        return this.dirPadre;
    }
    
    public LinkedList getListaBloques() {
        return this.listaBloques;
    }
}
