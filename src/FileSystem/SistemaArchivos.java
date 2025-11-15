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

    public String leerArchivo(String nombre, Directorio dirActual) {
    // 1️⃣ Buscar el archivo en el directorio actual
    Archivo archivo = dirActual.buscarArchivoPorNombre(nombre);

    if (archivo == null) {
        return " El archivo \"" + nombre + "\" no existe en este directorio.";
    }

    // 2️⃣ Obtener información básica
    StringBuilder info = new StringBuilder();
    info.append(" Archivo: ").append(archivo.getNombre()).append("\n");
    info.append(" Propietario: ").append(archivo.getOwner()).append("\n");
    info.append(" Tamano: ").append(archivo.getTamanioBloques()).append(" bloques\n");

    // 3️⃣ Obtener los bloques encadenados desde el disco
    info.append(" Bloques: ").append(obtenerCadenaBloques(archivo.getPrimerBloque())).append("\n");

    // 4️⃣ Mostrar por consola (opcional, para depuración)
    System.out.println(info.toString());

    // 5️⃣ Retornar la información (útil para mostrar en interfaz más adelante)
    return info.toString();
}

    
    private String obtenerCadenaBloques(int primerBloque) {
    if (primerBloque == -1) {
        return "[]";
    }

    StringBuilder sb = new StringBuilder("[");
    int actual = primerBloque;
    Disk.Bloque[] bloques = disco.getBloques(); // importa Disk.Bloque si hace falta
    boolean primero = true;

    while (actual != -1) {
        if (!primero) sb.append(" -> ");
        sb.append(actual);
        primero = false;
        actual = bloques[actual].getSiguiente();
    }

    sb.append("]");
    return sb.toString();
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
