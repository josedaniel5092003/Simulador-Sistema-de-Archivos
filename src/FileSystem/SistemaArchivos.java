
package FileSystem;

import DataStruct.LinkedList;
import DataStruct.Nodo;
import DataStruct.Queue;
import Process.Proceso;
import Disk.Disco;
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

        // Operaciones que se ejecutan inmediatamente
        if (operacion.equals("createDir")) {
            // Crear directorio inmediatamente
            boolean creado = crearDirectorio(nombre, dir, usuario, pRoot);
            return;
        }

        if (operacion.equals("deleteDir")) {
            // Si el directorio existe y no tiene archivos ni subdirectorios, eliminar directamente
            Directorio dirAEliminar = dir.buscarDirectorioPorNombre(nombre);
            if (dirAEliminar != null 
                && dirAEliminar.getArchivos().getLength() == 0 
                && dirAEliminar.getSubdirectorios().getLength() == 0) {
                eliminarDirectorio(nombre, dir);
                return;
            }
            // Si no está vacío, crear proceso normalmente para planificador
        }

        // Crear proceso normalmente para las demás operaciones
        Proceso p = new Proceso(
            contadorProcesos++,
            nombreProc,
            operacion,
            nombre,
            tamanio,
            dir,
            usuario
        );

        if (nuevoNombre != null) {
            p.setNuevoNombre(nuevoNombre);
        }

//        if (operacion.equals("read") || operacion.equals("deleteFile")) {
//            Archivo a = dir.buscarArchivoPorNombre(nombre);
//
//            if (a != null) {
//                p.setBloqueObjetivo(a.getPrimerBloque());
//            } else {
//                // si el archivo no existe se deja -1, SSTF lo ignorará
//                p.setBloqueObjetivo(-1);
//            }
//        }

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

        LinkedList listaBloques = disco.asignarBloques(bloques);
        int inicio = (int) listaBloques.getElementIn(0);

        if (inicio < 0) {
            System.out.println("No hay espacio disponible.");
            return false;
        }

        // ACTUALIZAR POSICIÓN DEL CABEZAL PARA SSTF
        disco.setHeadPosition(inicio);

        Archivo nuevo = new Archivo(nombre, bloques, inicio, proceso, dirActual, listaBloques);
        dirActual.agregarArchivo(nuevo);

        System.out.println(
            "Archivo creado: " + nombre +
            " | Cant. Bloques: " + bloques +
            " | Primer bloque: " + inicio +
            " | Lista de Bloques: " + listaBloques.toString()
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

        String contenido = java.nio.file.Files.readString(archivoJson.toPath());
        com.google.gson.JsonObject json = gson.fromJson(contenido, com.google.gson.JsonObject.class);

        int tamDisco = json.get("tamanioDisco").getAsInt();
        this.disco = new Disk.Disco(tamDisco);

        this.root = new Directorio("root", null, pRoot);

        // Mapa de rutas → directorios
        java.util.Map<String, Directorio> mapaDirs = new java.util.HashMap<>();
        mapaDirs.put("/", root);

        com.google.gson.JsonArray dirs = json.getAsJsonArray("directorios");

        for (int i = 0; i < dirs.size(); i++) {

            var d = dirs.get(i).getAsJsonObject();

            String nombre = d.get("nombre").getAsString();
            String padreRuta = d.get("padre").getAsString();

            Directorio padre = mapaDirs.getOrDefault(padreRuta, root);

            Directorio nuevo = new Directorio(nombre, padre, pRoot);
            padre.agregarSubdirectorio(nuevo);

            String ruta = padreRuta.equals("/") ? "/" + nombre : padreRuta + "/" + nombre;
            mapaDirs.put(ruta, nuevo);
        }

        com.google.gson.JsonArray archivos = json.getAsJsonArray("archivos");
        Disk.Bloque[] bloques = disco.getBloques();

        for (int i = 0; i < archivos.size(); i++) {

            var a = archivos.get(i).getAsJsonObject();

            String nombre = a.get("nombre").getAsString();
            int tam = a.get("tamanioBloques").getAsInt();
            int primerBloque = a.get("primerBloque").getAsInt();
            String rutaDir = a.get("directorio").getAsString();

            Directorio d = mapaDirs.get(rutaDir);


            com.google.gson.JsonArray arrBloques = a.getAsJsonArray("bloques");

            DataStruct.LinkedList listaBloques = new DataStruct.LinkedList();

            for (int b = 0; b < arrBloques.size(); b++) {
                int nro = arrBloques.get(b).getAsInt();
                listaBloques.insertFinal(nro);   
                bloques[nro].ocupar();
            }

            Nodo<Integer> nodo = listaBloques.getFirst();
            while (nodo != null && nodo.getNext() != null) {
                int actual = nodo.getElement();
                int siguiente = nodo.getNext().getElement();
                bloques[actual].setSiguiente(siguiente);
                nodo = nodo.getNext();
            }

            // último bloque →
            if (nodo != null) {
                bloques[nodo.getElement()].setSiguiente(-1);
            }

            Archivo nuevo = new Archivo(
                    nombre,
                    tam,
                    primerBloque,
                    pRoot,
                    d,
                    listaBloques           
            );

            d.agregarArchivo(nuevo);
        }

        System.out.println("CARGA DESDE JSON COMPLETADA");
    }
    
    public void guardarEnJson(File archivoDestino) throws Exception {

        com.google.gson.JsonObject json = new com.google.gson.JsonObject();

        // Tamaño del disco
        json.addProperty("tamanioDisco", disco.getBloques().length);

        com.google.gson.JsonArray listaDirs = new com.google.gson.JsonArray();

        guardarDirectoriosRecursivo(root, "/", listaDirs);

        json.add("directorios", listaDirs);

        com.google.gson.JsonArray listaArchivos = new com.google.gson.JsonArray();

        guardarArchivosRecursivo(root, "/", listaArchivos);

        json.add("archivos", listaArchivos);

        String contenido = new com.google.gson.GsonBuilder()
                .setPrettyPrinting()
                .create()
                .toJson(json);

        java.nio.file.Files.writeString(archivoDestino.toPath(), contenido);

        System.out.println("SISTEMA GUARDADO CORRECTAMENTE");
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

            // === NUEVO: Guardar lista de bloques ===
            com.google.gson.JsonArray bloquesJson = new com.google.gson.JsonArray();

            LinkedList<Integer> lista = a.getListaBloques();

            if (lista != null) {
                for (int i = 0; i < lista.getLength(); i++) {
                    int bloque = (int) lista.getElementGeneric(i);
                    bloquesJson.add(bloque);
                }
            }

            obj.add("bloques", bloquesJson);

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
    
    public void cargarLoteDesdeJson(File archivoJson) {
        try {
            String contenido = java.nio.file.Files.readString(archivoJson.toPath());
            com.google.gson.Gson gson = new com.google.gson.Gson();
            com.google.gson.JsonArray arr = gson.fromJson(contenido, com.google.gson.JsonArray.class);

            for (int i = 0; i < arr.size(); i++) {
                com.google.gson.JsonObject pj = arr.get(i).getAsJsonObject();
                if (pj == null) continue; // evita elementos nulos

                // Leer campos del JSON
                String nombreProceso = pj.has("nombreProceso") ? pj.get("nombreProceso").getAsString() : "ProcesoDesconocido";
                String operacion = pj.has("operacion") ? pj.get("operacion").getAsString() : "read";
                String nombre = pj.has("nombre") ? pj.get("nombre").getAsString() : "ElementoDesconocido";
                int tamBloques = pj.has("tamBloques") ? pj.get("tamBloques").getAsInt() : 0;
                String destinoNombre = pj.has("destinoNombre") ? pj.get("destinoNombre").getAsString() : "/";
                String usuario = pj.has("usuario") ? pj.get("usuario").getAsString() : "system";
                String nuevoNombre = pj.has("nuevoNombre") && !pj.get("nuevoNombre").isJsonNull()
                     ? pj.get("nuevoNombre").getAsString()
                     : null; // 


                // Buscar directorio destino
                Directorio destino = root.buscarDirectorioPorNombre(destinoNombre);
                if (destino == null) {
                    System.out.println("No se encontró el directorio destino: " + destinoNombre + ". Se omite este proceso.");
                    continue;
                }

                // Crear proceso según operación
                if (operacion.equalsIgnoreCase("createDir")) {
                    // crear inmediatamente
                    crearDirectorio(nombre, destino, usuario, new Proceso(contadorProcesos++, nombreProceso, operacion, nombre, tamBloques, destino, usuario));
                } else if (operacion.equalsIgnoreCase("deleteDir")) {
                    Directorio dirAEliminar = destino.buscarDirectorioPorNombre(nombre);
                    if (dirAEliminar != null && dirAEliminar.getArchivos().getLength() == 0) {
                        // eliminar directamente
                        eliminarDirectorio(nombre, destino);
                    } else {
                        // encolar si tiene archivos o subdirectorios
                        crearProceso(nombreProceso, operacion, nombre, tamBloques, destino, usuario, nuevoNombre);
                    }
                } else {
                    // operaciones normales: createFile, read, delete, update
                    crearProceso(nombreProceso, operacion, nombre, tamBloques, destino, usuario, nuevoNombre);
                }
            }

            System.out.println("Carga de lote desde JSON completada.");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al cargar el lote desde JSON.");
        }
    }



    // Agrega todos los procesos al sistema: createDir y deleteDir se ejecutan, el resto se encola
    public void agregarLoteProcesos(LinkedList<Proceso> lote) {
        Nodo<Proceso> nodo = lote.getFirst();
        while (nodo != null) {
            Proceso p = nodo.getElement();

            switch (p.getOperacion()) {
                case "createDir":
                    crearDirectorio(p.getNombre(), p.getDestino(), p.getUsuario(), p);
                    break;
                case "deleteDir":
                    Directorio dir = p.getDestino().buscarSubdirectorio(p.getNombre());
                    if (dir != null && dir.getArchivos().getLength() == 0) {
                        eliminarDirectorio(p.getNombre(), p.getDestino());
                    } else {
                        colaProcesos.enqueue(p);
                    }
                    break;
                default:
                    colaProcesos.enqueue(p);
                    break;
            }

            nodo = nodo.getNext();
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
