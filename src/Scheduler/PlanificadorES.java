/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Scheduler;

import FileSystem.SistemaArchivos;
import Process.Proceso;
import DataStruct.Nodo;

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

        var cola = sistema.getColaProcesos(); // <--- ESTE SÍ EXISTE

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
}

