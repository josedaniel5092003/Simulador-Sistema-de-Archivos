/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Disk;

/**
 *
 * @author Jose
 */
        

public class Disco {
    private Bloque[] bloques;

    public Disco(int tam) {
        bloques = new Bloque[tam];
        for (int i = 0; i < tam; i++) {
            bloques[i] = new Bloque(i);
        }
    }
    private int headPosition = 0;  // inicia en 0 (o donde quieras)

    public int asignarBloques(int cantidad) {
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
            }
        }

        if (asignados < cantidad) {
            // no hay suficientes bloques → revertir
            liberarBloques(primerBloque);
            return -1;
        }

        return primerBloque;
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
    
    public int getHeadPosition() { return headPosition; }
    public void setHeadPosition(int pos) { headPosition = pos; }
}

