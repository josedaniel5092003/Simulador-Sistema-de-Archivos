/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FileSystem;

/**
 *
 * @author Jose
 */
import DataStruct.Queue;
import Process.Proceso;
import Disk.Disco;

public class SistemaArchivos {
    private Disco disco;
    private Directorio root;
    private Queue colaProcesos;
    private int contadorProcesos = 1;

    public SistemaArchivos(int tamDisco) {
        disco = new Disco(tamDisco);
        root = new Directorio("root", null);
        colaProcesos = new Queue();
    }

    public void crearArchivo(String nombre, int bloques, Directorio dirActual, String usuario) {
        int inicio = disco.asignarBloques(bloques);

        if (inicio == -1) {
            System.out.println("No hay espacio para crear el archivo");
            return;
        }

        Archivo nuevo = new Archivo(nombre, bloques, inicio, usuario);
        dirActual.agregarArchivo(nuevo);

        agregarProceso("create", nombre);
    }

    private void agregarProceso(String operacion, String objetivo) {
        Proceso p = new Proceso(contadorProcesos++, operacion, objetivo);
        colaProcesos.enqueue(p);
    }
    
    public Disco getDisco() {
    return disco;
}

    public Directorio getRoot() {
        return root;
    }

    public void mostrarColaProcesos() {
        if (colaProcesos.isEmpty()) {
            System.out.println("(sin procesos en la cola)");
            return;
        }

        var actual = colaProcesos.getHead();
        while (actual != null) {
            System.out.println(actual.getElement().toString());
            actual = actual.getNext();
        }
    }

}
