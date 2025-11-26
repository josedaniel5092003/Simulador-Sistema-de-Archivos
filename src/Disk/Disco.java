/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Disk;

import DataStruct.LinkedList;

/**
 *
 * @author Jose
 */
        

public class Disco {
    private Bloque[] bloques;
    
    private final int tamBloqueKB = 4;

    public Disco(int tam) {
        bloques = new Bloque[tam];
        for (int i = 0; i < tam; i++) {
            bloques[i] = new Bloque(i);
        }
    }
    private int headPosition = 10; 

    public LinkedList<Integer> asignarBloques(int cantidad) {

        LinkedList<Integer> listaBloques = new LinkedList<>();

        int primerBloque = -1;
        int anterior = -1;
        int asignados = 0;

        for (int i = 0; i < bloques.length && asignados < cantidad; i++) {

            if (bloques[i].estaLibre()) {
                bloques[i].ocupar();

                if (primerBloque == -1) primerBloque = i;

                if (anterior != -1) {
                    bloques[anterior].setSiguiente(i);
                }

                anterior = i;
                asignados++;

                // ➜ Agregarlo a tu LinkedList personalizada
                listaBloques.insertFinal(i);
            }
        }

        if (asignados < cantidad) {
            // revertir bloques marcados
            liberarBloques(primerBloque);
            return null; // ERROR → no hay memoria suficiente
        }

        return listaBloques; // ➜ Devuelves TODOS los bloques usados
    }

    public void liberarBloques(int inicio) {
        int actual = inicio;
        while (actual != -1) {
            int next = bloques[actual].getSiguiente();
            bloques[actual].liberar();
            actual = next;
        }
    }

    public Bloque[] getBloques() {
        return bloques;
    }
    
    public int getTamBloqueKB() {
        return tamBloqueKB;
    }
    
    public int getHeadPosition() { 
        return headPosition; 
    }
    
    public void setHeadPosition(int pos) { 
        headPosition = pos; 
    }
    
    public int getTotalBloques() {
        return bloques.length;
    }

    public int getBloquesUsados() {
        int usados = 0;
        for (Bloque b : bloques) {
            if (!b.estaLibre()) {
                usados++;
            }
        }
        return usados;
    }

    public int getBloquesDisponibles() {
        return getTotalBloques() - getBloquesUsados();
    }

}

