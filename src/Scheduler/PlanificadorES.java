/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Scheduler;

import FileSystem.SistemaArchivos;
import Process.Proceso;

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
}

