/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FileSystem;

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
    private String color;  // opcional para GUI

    public Archivo(String nombre, int tamanioBloques, int primerBloque, Proceso owner) {
        this.nombre = nombre;
        this.tamanioBloques = tamanioBloques;
        this.primerBloque = primerBloque;
        this.owner = owner;
        this.color = null;
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

    public String getColor() {
        return color;
    }

    public void setNombre(String nuevoNombre) {
        this.nombre = nuevoNombre;
    }

    @Override
    public String toString() {
        return nombre;
    }

}
