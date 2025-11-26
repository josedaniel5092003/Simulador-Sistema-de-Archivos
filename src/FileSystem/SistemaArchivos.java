
package FileSystem;

import DataStruct.Nodo;
import DataStruct.Queue;
import Process.Proceso;
import Disk.Disco;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import java.io.File;

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

    public void crearProceso(String nombreProc, String operacion, String nombre, int tamanio, Directorio dir, String usuario, String nuevoNombre) {

        Proceso p = new Proceso(
            contadorProcesos++,
            nombreProc,
            operacion,
            nombre,
            tamanio,
            dir,
            usuario
        );
        
        p.setNuevoNombre(nuevoNombre);

        if (operacion.equals("read") || operacion.equals("delete")) {

            Archivo a = dir.buscarArchivoPorNombre(nombre);

            if (a != null) {
                p.setBloqueObjetivo(a.getPrimerBloque());
            } else {
                // si el archivo no existe se deja -1, SSTF lo ignorará
                p.setBloqueObjetivo(-1);
            }
        }

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

    public boolean ejecutarOperacion(Proceso p) {

        switch (p.getOperacion()) {

            case "createFile":
            boolean creado = crearArchivo(
                p.getNombre(),
                p.getTamBloques(),
                p.getDestino(),
                p
            );

            // Actualizar posición del cabezal y bloque objetivo para SSTF
            if (creado) {
                Archivo a = p.getDestino().buscarArchivoPorNombre(p.getNombre());
                if (a != null) {
                    // establecer el bloque objetivo del proceso
                    p.setBloqueObjetivo(a.getPrimerBloque());
                    // mover la cabeza del disco al primer bloque asignado
                    disco.setHeadPosition(a.getPrimerBloque());
                }
            }

            return creado;

            case "createDir":
                return crearDirectorio(
                    p.getNombre(),
                    p.getDestino(),
                    p.getUsuario(),
                    p
                );

            case "read":
                Archivo a1 = p.getDestino().buscarArchivoPorNombre(p.getNombre());
                if (a1 != null) p.setBloqueObjetivo(a1.getPrimerBloque());
                System.out.println(leerArchivo(p.getNombre(), p.getDestino()));
                return true;

            case "deleteFile":
                Archivo a2 = p.getDestino().buscarArchivoPorNombre(p.getNombre());
                if (a2 != null) p.setBloqueObjetivo(a2.getPrimerBloque());
                return eliminarArchivo(p.getNombre(), p.getDestino());
                
            case "deleteDir":
                Directorio dirAEliminar = p.getDestino().buscarDirectorioPorNombre(p.getNombre());

                if (dirAEliminar != null) {
                    return eliminarDirectorio(p.getNombre(), p.getDestino());
                }
                
            case "update":
                // Buscar archivo o directorio
                Object objetivo = p.getDestino().buscarArchivoPorNombre(p.getNombre());
                if (objetivo == null) {
                    objetivo = p.getDestino().buscarDirectorioPorNombre(p.getNombre());
                }

                if (objetivo == null) {
                    System.out.println("No existe el elemento a actualizar.");
                    return false;
                }

                // Si es archivo → asignar bloque objetivo para SSTF
                if (objetivo instanceof Archivo arch) {
                    p.setBloqueObjetivo(arch.getPrimerBloque());
                    disco.setHeadPosition(arch.getPrimerBloque());

                    // Renombrar archivo
                    System.out.println(p.getNuevoNombre());
                    arch.setNombre(p.getNuevoNombre());
                    return true;
                }

                // Si es directorio
                if (objetivo instanceof Directorio dir) {
                    dir.setNombre(p.getNuevoNombre());
                    return true;
                }

                return false;
                
            default:
                System.out.println("Operación no reconocida");
                return false;
        }
    }

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

        // ACTUALIZAR POSICIÓN DEL CABEZAL PARA SSTF
        disco.setHeadPosition(inicio);

        Archivo nuevo = new Archivo(nombre, bloques, inicio, proceso, dirActual);
        dirActual.agregarArchivo(nuevo);

        System.out.println(
            "Archivo creado: " + nombre +
            " | Bloques: " + bloques +
            " | Primer bloque: " + inicio
        );

        return true;
    }

    public boolean crearDirectorio(String nombre, Directorio padre, String usuario, Proceso creador) {

        if (padre.buscarSubdirectorio(nombre) != null) {
            System.out.println("Ya existe un directorio con ese nombre.");
            return false;
        }

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

        // MOVER EL CABEZAL AL PRIMER BLOQUE DEL ARCHIVO
        disco.setHeadPosition(archivo.getPrimerBloque());

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

        // MOVER EL CABEZAL AL PRIMER BLOQUE DEL ARCHIVO ANTES DE ELIMINAR
        disco.setHeadPosition(archivo.getPrimerBloque());

        // Liberar bloques del disco
        disco.liberarBloques(archivo.getPrimerBloque());

        // Quitar el archivo del directorio
        dirActual.removerArchivo(archivo);

        System.out.println("Archivo eliminado: " + nombre);
        return true;
    }
    
    public boolean eliminarDirectorio(String nombre, Directorio dirActual) {

        Directorio dir = dirActual.buscarDirectorioPorNombre(nombre);

        if (dir == null) {
            System.out.println("El directorio no existe.");
            return false;
        }

        // Elimina todos los archivos dentro del directorio
        Nodo<Archivo> nodoA = dir.getArchivos().getFirst();
        while (nodoA != null) {
            Archivo archivo = nodoA.getElement();

            // eliminarArchivo() mueve el cabezal
            eliminarArchivo(archivo.getNombre(), dir);

            nodoA = nodoA.getNext();
        }

        // Elimina todos los subdirectorios recursivamente
        Nodo<Directorio> nodoD = dir.getSubdirectorios().getFirst();
        while (nodoD != null) {
            Directorio sub = nodoD.getElement();

            // Llamada recursiva
            eliminarDirectorio(sub.getNombre(), dir);

            nodoD = nodoD.getNext();
        }

        // Remover el directorio del padre
        dirActual.removerSubdirectorio(dir);

        System.out.println("Directorio eliminado: " + nombre);
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
    
    public void cargarDesdeJson(File archivoJson) throws Exception {

    com.google.gson.Gson gson = new com.google.gson.Gson();

    // leer archivo
    String contenido = java.nio.file.Files.readString(archivoJson.toPath());
    com.google.gson.JsonObject json = gson.fromJson(contenido, com.google.gson.JsonObject.class);

    // ================================
    // RECREAR DISCO
    // ================================
    int tamDisco = json.get("tamanioDisco").getAsInt();
    this.disco = new Disk.Disco(tamDisco);

    // ================================
    // RECREAR ROOT
    // ================================
    this.root = new Directorio("root", null, pRoot);

    // Mapa para encontrar directorios por ruta
    java.util.Map<String, Directorio> mapaDirs = new java.util.HashMap<>();
    mapaDirs.put("/", root);

    // ================================
    // CARGAR DIRECTORIOS
    // ================================
    com.google.gson.JsonArray dirs = json.getAsJsonArray("directorios");

    for (int i = 0; i < dirs.size(); i++) {

        var d = dirs.get(i).getAsJsonObject();

        String nombre = d.get("nombre").getAsString();
        String padreRuta = d.get("padre").getAsString();  // por ejemplo "/Documentos"

        Directorio padre = mapaDirs.getOrDefault(padreRuta, root);

        Directorio nuevo = new Directorio(nombre, padre, pRoot);
        padre.agregarSubdirectorio(nuevo);

        String ruta = padreRuta.equals("/") ? "/" + nombre : padreRuta + "/" + nombre;
        mapaDirs.put(ruta, nuevo);
    }

    // ================================
    // CARGAR ARCHIVOS
    // ================================
    com.google.gson.JsonArray archivos = json.getAsJsonArray("archivos");

    for (int i = 0; i < archivos.size(); i++) {

        var a = archivos.get(i).getAsJsonObject();

        String nombre = a.get("nombre").getAsString();
        int tam = a.get("tamanioBloques").getAsInt();
        int inicio = a.get("primerBloque").getAsInt();
        String rutaDir = a.get("directorio").getAsString();

        Directorio d = mapaDirs.get(rutaDir);

        Archivo nuevo = new Archivo(nombre, tam, inicio, pRoot, d);
        d.agregarArchivo(nuevo);

        Disk.Bloque[] bloques = disco.getBloques();

int actual = inicio;
for (int b = 0; b < tam - 1; b++) {
    int sig = actual + 1;              // si tus archivos en JSON están contiguos
    bloques[actual].setSiguiente(sig);
    bloques[actual].ocupar();          // marcar bloque como ocupado
    actual = sig;
}
// último bloque
bloques[actual].setSiguiente(-1);
bloques[actual].ocupar();
// si quieres puedes setear contenido del último bloque también
// bloques[actual].setContenido(nombre);
    }

    System.out.println(">>> CARGA DESDE JSON COMPLETADA");
}
    
    public void guardarEnJson(File archivoDestino) throws Exception {

    com.google.gson.JsonObject json = new com.google.gson.JsonObject();

    // Tamaño del disco
    json.addProperty("tamanioDisco", disco.getBloques().length);

    // ===========================
    // GUARDAR DIRECTORIOS
    // ===========================
    com.google.gson.JsonArray listaDirs = new com.google.gson.JsonArray();

    guardarDirectoriosRecursivo(root, "/", listaDirs);

    json.add("directorios", listaDirs);

    // ===========================
    // GUARDAR ARCHIVOS
    // ===========================
    com.google.gson.JsonArray listaArchivos = new com.google.gson.JsonArray();

    guardarArchivosRecursivo(root, "/", listaArchivos);

    json.add("archivos", listaArchivos);

    // ===========================
    // ESCRIBIR JSON AL ARCHIVO
    // ===========================
    String contenido = new com.google.gson.GsonBuilder()
            .setPrettyPrinting()
            .create()
            .toJson(json);

    java.nio.file.Files.writeString(archivoDestino.toPath(), contenido);

    System.out.println(">>> SISTEMA GUARDADO CORRECTAMENTE");
}
    
    private void guardarDirectoriosRecursivo(Directorio dir, String ruta, com.google.gson.JsonArray salida) {

    if (!ruta.equals("/")) { 
        com.google.gson.JsonObject obj = new com.google.gson.JsonObject();
        obj.addProperty("nombre", dir.getNombre());
        obj.addProperty("padre", ruta.substring(0, ruta.lastIndexOf("/")));
        if (obj.get("padre").getAsString().isEmpty()) obj.addProperty("padre", "/");

        salida.add(obj);
    }

    Nodo<Directorio> nodo = dir.getSubdirectorios().getFirst();
    while (nodo != null) {
        Directorio sub = nodo.getElement();
        guardarDirectoriosRecursivo(sub, ruta + "/" + sub.getNombre(), salida);
        nodo = nodo.getNext();
    }
}

private void guardarArchivosRecursivo(Directorio dir, String ruta, com.google.gson.JsonArray salida) {

    Nodo<Archivo> nodoA = dir.getArchivos().getFirst();
    while (nodoA != null) {
        Archivo a = nodoA.getElement();

        com.google.gson.JsonObject obj = new com.google.gson.JsonObject();
        obj.addProperty("nombre", a.getNombre());
        obj.addProperty("tamanioBloques", a.getTamanioBloques());
        obj.addProperty("primerBloque", a.getPrimerBloque());
        obj.addProperty("directorio", ruta);

        salida.add(obj);

        nodoA = nodoA.getNext();
    }

    Nodo<Directorio> nodoD = dir.getSubdirectorios().getFirst();
    while (nodoD != null) {
        Directorio sub = nodoD.getElement();
        guardarArchivosRecursivo(sub, ruta + "/" + sub.getNombre(), salida);
        nodoD = nodoD.getNext();
    }
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
