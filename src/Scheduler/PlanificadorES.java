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

    public enum Politica { FIFO, LIFO, SSTF, PA }

    private SistemaArchivos sistema;
    private Politica politicaActual = Politica.FIFO;

    public PlanificadorES(SistemaArchivos sistema) {
        this.sistema = sistema;
    }

    public void setPolitica(Politica politica) {
        this.politicaActual = politica;
        System.out.println("Política de E/S cambiada a: " + politica);
    }

    /** Ejecuta un solo proceso según la política actual */
    public void ejecutarUnProceso() {
        if (!sistema.hayProcesosPendientes()) return;

        switch (politicaActual) {
            case FIFO -> ejecutarUnProcesoFIFO();
            case LIFO -> ejecutarUnProcesoLIFO();
            case SSTF -> ejecutarUnProcesoSSTF();
            case PA -> ejecutarUnProcesoPA();
        }
    }

    private void ejecutarUnProcesoFIFO() {
        Proceso p = sistema.getSiguienteProceso();
        if (p != null) {
            System.out.println("\n[Planificador FIFO] Atendiendo: " + p);
            sistema.ejecutarOperacion(p);
        }
    }

    private void ejecutarUnProcesoLIFO() {
        Proceso p = obtenerUltimoProceso();
        if (p != null) {
            System.out.println("\n[Planificador LIFO] Atendiendo: " + p);
            sistema.ejecutarOperacion(p);
        }
    }

    private void ejecutarUnProcesoSSTF() {
        var cola = sistema.getColaProcesos();
        if (cola.isEmpty()) return;

        Proceso mejor = null;
        int mejorDist = Integer.MAX_VALUE;
        int head = sistema.getDisco().getHeadPosition();
        Nodo actual = cola.getHead();

        while (actual != null) {
            Proceso p = (Proceso) actual.getElement();
            if (p.getBloqueObjetivo() != -1) {
                int dist = Math.abs(p.getBloqueObjetivo() - head);
                if (dist < mejorDist) {
                    mejorDist = dist;
                    mejor = p;
                }
            }
            actual = actual.getNext();
        }

        if (mejor == null) mejor = (Proceso) cola.getHead().getElement();

        System.out.println("\n[SSTF] Atendiendo: " + mejor);
        sistema.ejecutarOperacion(mejor);

        if (mejor.getBloqueObjetivo() != -1) {
            sistema.getDisco().setHeadPosition(mejor.getBloqueObjetivo());
        }

        cola.remove(mejor);
    }

    private void ejecutarUnProcesoPA() {
        var cola = sistema.getColaProcesos();
        if (cola.isEmpty()) return;

        java.util.Random random = new java.util.Random();
        int index = random.nextInt(cola.getSize());
        Proceso p = obtenerProcesoEnPosicion(index);

        System.out.println("\n[Planificador PA] Atendiendo (índice " + index + "): " + p);
        sistema.ejecutarOperacion(p);
    }

    private Proceso obtenerUltimoProceso() {
        var cola = sistema.getColaProcesos();
        if (cola.isEmpty()) return null;

        Nodo actual = cola.getHead();
        Nodo anterior = null;

        while (actual.getNext() != null) {
            anterior = actual;
            actual = actual.getNext();
        }

        if (anterior == null) { // solo 1 elemento
            Proceso p = (Proceso) cola.getHead().getElement();
            cola.dequeue();
            return p;
        }

        Proceso p = (Proceso) actual.getElement();
        anterior.setNext(null);
        cola.setTail(anterior);
        cola.setSize(cola.getSize() - 1);
        return p;
    }

    private Proceso obtenerProcesoEnPosicion(int index) {
        var cola = sistema.getColaProcesos();
        Nodo actual = cola.getHead();
        Nodo anterior = null;

        for (int i = 0; i < index; i++) {
            anterior = actual;
            actual = actual.getNext();
        }

        Proceso p = (Proceso) actual.getElement();

        if (anterior == null) {
            cola.dequeue();
        } else {
            anterior.setNext(actual.getNext());
            if (actual == cola.getTail()) cola.setTail(anterior);
            cola.setSize(cola.getSize() - 1);
        }

        return p;
    }
}

