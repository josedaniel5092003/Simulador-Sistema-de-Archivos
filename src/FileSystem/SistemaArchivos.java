
package FileSystem;

import DataStruct.Queue;
import Process.Proceso;
import Disk.Disco;

public class SistemaArchivos {

    private Disco disco;
    private Directorio root;
    private Queue colaProcesos;
    private int contadorProcesos = 1;

    // proceso del sistema para el root
    Proceso pRoot = new Proceso(
        0,
        "system proces",
        "systemInit",
        "root",
        0,
        null,
        "system"
    );

    public SistemaArchivos(int tamDisco) {
        disco = new Disco(tamDisco);
        root = new Directorio("root", null, pRoot);
        colaProcesos = new Queue();
    }

    // -----------------------------
    //   GESTIÓN DE PROCESOS
    // -----------------------------

    public void crearProceso(String nombreProc, String operacion, String nombre, int tamanio, Directorio dir, String usuario) {

        Proceso p = new Proceso(
            contadorProcesos++,
            nombreProc,
            operacion,     // createFile, createDir, read, delete...
            nombre,
            tamanio,       // 0 si no aplica
            dir,           // directorio destino
            usuario
        );

        colaProcesos.enqueue(p);
        System.out.println("Proceso agregado a la cola: " + p);
    }

    public Proceso getSiguienteProceso() {
        if (colaProcesos.isEmpty()) return null;
        return (Proceso) colaProcesos.dispatch();
    }

    public boolean hayProcesosPendientes() {
        return !colaProcesos.isEmpty();
    }

    public void mostrarColaProcesos() {
        if (colaProcesos.isEmpty()) {
            System.out.println("(sin procesos en la cola)");
            return;
        }

        var nodo = colaProcesos.getHead();
        while (nodo != null) {
            System.out.println(nodo.getElement());
            nodo = nodo.getNext();
        }
    }

    // ---------------------------------------
    //   EJECUCIÓN DE OPERACIONES DEL PROCESO
    // ---------------------------------------

    public boolean ejecutarOperacion(Proceso p) {

        switch (p.getOperacion()) {

            case "createFile":
                return crearArchivo(
                    p.getNombre(),
                    p.getTamBloques(),
                    p.getDestino(),
                    p
                );

            case "createDir":
                return crearDirectorio(
                    p.getNombreProceso(),
                    p.getNombre(),
                    p.getDestino(),
                    p.getUsuario()
                );

            case "read":
                System.out.println(leerArchivo(p.getNombre(), p.getDestino()));
                return true;

            case "delete":
                return eliminarArchivo(
                    p.getNombre(),
                    p.getDestino()
                );

            default:
                System.out.println("Operación no reconocida");
                return false;
        }
    }

    // -----------------------------
    //   CRUD REAL DEL SISTEMA
    // -----------------------------

    public boolean crearArchivo(String nombre, int bloques, Directorio dirActual, Proceso proceso) {

        if (dirActual.buscarArchivoPorNombre(nombre) != null) {
            System.out.println("Ya existe un archivo con ese nombre.");
            return false;
        }

        int inicio = disco.asignarBloques(bloques);

        if (inicio == -1) {
            System.out.println("No hay espacio disponible.");
            return false;
        }

        Archivo nuevo = new Archivo(nombre, bloques, inicio, proceso);
        dirActual.agregarArchivo(nuevo);

        System.out.println("Archivo creado: " + nombre +
                           " | Bloques: " + bloques +
                           " | Primer bloque: " + inicio);

        return true;
    }


    public boolean crearDirectorio(String nombreProc, String nombre, Directorio padre, String usuario) {

        if (padre.buscarSubdirectorio(nombre) != null) {
            System.out.println("Ya existe un directorio con ese nombre.");
            return false;
        }

        Proceso creador = new Proceso(
            -1, nombreProc, "createDir", nombre, 0, padre, usuario
        );

        Directorio nuevo = new Directorio(nombre, padre, creador);
        padre.agregarSubdirectorio(nuevo);

        System.out.println("Directorio creado: " + nombre);
        return true;
    }


    public String leerArchivo(String nombre, Directorio dirActual) {

        Archivo archivo = dirActual.buscarArchivoPorNombre(nombre);

        if (archivo == null) {
            return "El archivo \"" + nombre + "\" no existe.";
        }

        StringBuilder info = new StringBuilder();
        info.append("Archivo: ").append(archivo.getNombre()).append("\n");
        info.append("Propietario: ").append(archivo.getOwner()).append("\n");
        info.append("Tamaño: ").append(archivo.getTamanioBloques()).append(" bloques\n");
        info.append("Bloques: ").append(obtenerCadenaBloques(archivo.getPrimerBloque())).append("\n");

        return info.toString();
    }


    public boolean eliminarArchivo(String nombre, Directorio dirActual) {
        Archivo archivo = dirActual.buscarArchivoPorNombre(nombre);

        if (archivo == null) {
            System.out.println("El archivo no existe.");
            return false;
        }

        disco.liberarBloques(archivo.getPrimerBloque());
        dirActual.removerArchivo(archivo);

        System.out.println("Archivo eliminado: " + nombre);
        return true;
    }


    private String obtenerCadenaBloques(int primerBloque) {

        if (primerBloque == -1) return "[]";

        StringBuilder sb = new StringBuilder("[");
        int actual = primerBloque;
        Disk.Bloque[] bloques = disco.getBloques();
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

    public Disco getDisco() {
        return disco;
    }

    public Directorio getRoot() {
        return root;
    }
    
    public Queue getColaProcesos() {
    return colaProcesos;
}
}
