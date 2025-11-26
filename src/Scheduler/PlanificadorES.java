/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Scheduler;

import FileSystem.SistemaArchivos;
import Process.Proceso;
import DataStruct.Nodo;
import FileSystem.Archivo;

/**
 *
 * @author vivia
 */
public class PlanificadorES {

    private SistemaArchivos sistema;

    public PlanificadorES(SistemaArchivos sistema) {
        this.sistema = sistema;
    }

    public void ejecutarFIFO() {

        while (sistema.hayProcesosPendientes()) {

            Proceso p = sistema.getSiguienteProceso();

            System.out.println("\n[Planificador FIFO] Atendiendo: " + p);

            sistema.ejecutarOperacion(p);
        }
    }

    public void ejecutarLIFO() {

        while (sistema.hayProcesosPendientes()) {

            Proceso p = obtenerUltimoProceso();

            System.out.println("\n[Planificador LIFO] Atendiendo: " + p);

            sistema.ejecutarOperacion(p);
        }
    }

    private Proceso obtenerUltimoProceso() {

        var cola = sistema.getColaProcesos(); 

        if (cola.isEmpty()) return null;

        Nodo actual = cola.getHead();
        Nodo anterior = null;

        // Recorrer hasta el último nodo
        while (actual.getNext() != null) {
            anterior = actual;
            actual = actual.getNext();
        }

        // SOLO 1 elemento
        if (anterior == null) {
            Proceso p = (Proceso) cola.getHead().getElement();
            cola.dequeue();
            return p;
        }

        // MÁS DE 1
        Proceso p = (Proceso) actual.getElement();

        anterior.setNext(null);
        cola.setTail(anterior);
        cola.setSize(cola.getSize() - 1);

        return p;
    }
    
    public void ejecutarPA() {

    var cola = sistema.getColaProcesos();

    java.util.Random random = new java.util.Random();

    while (!cola.isEmpty()) {

        int size = cola.getSize();

        // índice aleatorio entre 0 y size-1
        int index = random.nextInt(size);

        Proceso p = obtenerProcesoEnPosicion(index);

        System.out.println("\n[Planificador PA] Atendiendo (índice " + index + "): " + p);

        sistema.ejecutarOperacion(p);
    }
}
    
    private Proceso obtenerProcesoEnPosicion(int index) {

    var cola = sistema.getColaProcesos();

    Nodo actual = cola.getHead();
    Nodo anterior = null;

    // recorrer hasta index
    for (int i = 0; i < index; i++) {
        anterior = actual;
        actual = actual.getNext();
    }

    // actual está en posición index
    Proceso p = (Proceso) actual.getElement();

    // CASO 1: es el primero
    if (anterior == null) {
        cola.dequeue();
    }
    // CASO 2: es otro nodo
    else {
        anterior.setNext(actual.getNext());

        if (actual == cola.getTail()) {
            cola.setTail(anterior);
        }

        cola.setSize(cola.getSize() - 1);
    }

    return p;
}
    
    public void ejecutarSSTF() {

    var cola = sistema.getColaProcesos();
    if (cola.isEmpty()) return;

    while (!cola.isEmpty()) {

        Proceso mejor = null;
        int mejorDist = Integer.MAX_VALUE;

        int head = sistema.getDisco().getHeadPosition();

        Nodo actual = cola.getHead();

        // recorrer cola
        while (actual != null) {

            Proceso p = (Proceso) actual.getElement();

            // ignorar procesos que NO tienen bloque objetivo
            if (p.getBloqueObjetivo() == -1) {
                actual = actual.getNext();
                continue;
            }

            int dist = Math.abs(p.getBloqueObjetivo() - head);

            if (dist < mejorDist) {
                mejorDist = dist;
                mejor = p;
            }

            actual = actual.getNext();
        }

        // si ningún proceso sirve para SSTF, procesamos el primero (caso CREATE)
        if (mejor == null) {
            mejor = (Proceso) cola.getHead().getElement();
        }

        System.out.println("\n[SSTF] Atendiendo: " + mejor);

        sistema.ejecutarOperacion(mejor);

        // actualizar posición del cabezal solo para procesos con bloque válido
        if (mejor.getBloqueObjetivo() != -1) {
            sistema.getDisco().setHeadPosition(mejor.getBloqueObjetivo());
        }

        // quitar de la cola
        cola.remove(mejor);
    }
}

    
public void prepararProcesoParaSSTF(Proceso p) {

    if (p.getOperacion().equals("read") || p.getOperacion().equals("delete")) {
        Archivo a = p.getDestino().buscarArchivoPorNombre(p.getNombre());
        if (a != null) {
            p.setBloqueObjetivo(a.getPrimerBloque());
        }
    }
}

//public void ejecutarPrioridad() {
//
//    var cola = sistema.getColaProcesos();
//    if (cola.isEmpty()) return;
//
//    while (!cola.isEmpty()) {
//
//        Proceso mejor = null;
//        int mejorPrioridad = Integer.MAX_VALUE;
//
//        Nodo actual = cola.getHead();
//
//        // recorrer toda la cola para encontrar el proceso con mayor prioridad
//        while (actual != null) {
//            Proceso p = (Proceso) actual.getElement();
//
//            if (p.getPrioridad() < mejorPrioridad) {
//                mejorPrioridad = p.getPrioridad();
//                mejor = p;
//            }
//
//            actual = actual.getNext();
//        }
//
//        // si por alguna razón no encontramos ninguno (no debería pasar), tomamos el primero
//        if (mejor == null) {
//            mejor = (Proceso) cola.getHead().getElement();
//        }
//
//        System.out.println("\n[Planificador Prioridad] Atendiendo: " + mejor);
//
//        // ejecutar operación
//        sistema.ejecutarOperacion(mejor);
//
//        // quitar de la cola
//        cola.remove(mejor);
//    }
//}



}

