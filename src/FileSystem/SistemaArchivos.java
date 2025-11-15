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

    public boolean crearArchivo(String nombre, int bloques, Directorio dirActual, String usuario) {
    // 1️⃣ Validar nombre repetido
    if (dirActual.buscarArchivoPorNombre(nombre) != null) {
        System.out.println("Ya existe un archivo con ese nombre en este directorio.");
        return false;
    }

    // 2️⃣ Intentar asignar bloques en el disco
    int inicio = disco.asignarBloques(bloques);

    if (inicio == -1) {
        System.out.println("No hay espacio disponible para crear el archivo: " + nombre);
        return false;
    }

    // 3️⃣ Crear y agregar el archivo al directorio
    Archivo nuevo = new Archivo(nombre, bloques, inicio, usuario);
    dirActual.agregarArchivo(nuevo);

    // 4️⃣ Agregar proceso (opcional)
    agregarProceso("create", nombre);

    // 5️⃣ Mensaje informativo
    System.out.println("Archivo creado: " + nombre + 
                       " | Bloques: " + bloques + 
                       " | Primer bloque: " + inicio);

    return true;
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
