/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author vivia
 */
public class Proceso {
    // PCB
    private int pid;
    private String name;
    private int totalInstructions;
    private int remainingInstructions;
    private int pc;
    private int mar;
    private boolean cpuBound;
    private int cyclesToException;
    private int exceptionServiceCycles;
    

    public enum Status {
        NEW, READY, RUNNING, BLOCKED, SUSPENDED, TERMINATED
    }
    
    private int arrivalTime;

   
    public Proceso(int pid, String name, int totalInstructions, boolean cpuBound,
               int cyclesToException, int exceptionServiceCycles, int arrivalTime) {

        this.pid = pid;
        this.name = name;
        this.totalInstructions = totalInstructions;
        this.remainingInstructions = totalInstructions;
        this.pc = 0;
        this.mar = 0;
        this.cpuBound = cpuBound;
        this.cyclesToException = cyclesToException;
        this.exceptionServiceCycles = exceptionServiceCycles;
        this.arrivalTime = arrivalTime;
    }
}
