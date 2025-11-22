package simuladorsistemadearchivos;

import Disk.Bloque;
import FileSystem.Archivo;
import FileSystem.Directorio;
import FileSystem.SistemaArchivos;
import GUI.SimuladorSistArchivos;
import Scheduler.PlanificadorES;

public class SimuladorSistemaDeArchivos {

    public static void main(String[] args) {

        SistemaArchivos sistema = new SistemaArchivos(40);
        Directorio root = sistema.getRoot();

        // --- CREACIÓN DE ESTRUCTURA BÁSICA ---
        sistema.crearProceso("p1","createDir", "Documentos", 0, root, "admin");
        sistema.crearProceso("p2","createDir", "Musica", 0, root, "admin");
        sistema.crearProceso("p3","createDir", "Fotos", 0, root, "user1");

        // Ejecutar procesos de creación de directorios de nivel 1
        while (sistema.hayProcesosPendientes()) {
            PlanificadorES planificador = new PlanificadorES(sistema);
            planificador.ejecutarFIFO();
        }
        
        // --- OBTENER REFERENCIAS DE NIVEL 1 ---
        Directorio dirDocumentos = root.buscarDirectorioPorNombre("Documentos"); 
        Directorio dirMusica = root.buscarDirectorioPorNombre("Musica");
        Directorio dirFotos = root.buscarDirectorioPorNombre("Fotos");

        if (dirDocumentos == null || dirMusica == null || dirFotos == null) {
            System.err.println("¡ERROR! Falló la creación de directorios de primer nivel.");
            return; 
        }
        
        // --- CREACIÓN DE DIRECTORIOS DE SEGUNDO NIVEL Y ARCHIVOS EN DOCUMENTOS ---
        sistema.crearProceso("p4","createDir", "Tareas", 0, dirDocumentos, "admin");
        sistema.crearProceso("p5","createDir", "Trabajos", 0, dirDocumentos, "admin");
        sistema.crearProceso("p6","createFile", "Tesis.docx", 5, dirDocumentos, "admin");
        sistema.crearProceso("p7","createFile", "Apuntes.txt", 1, dirDocumentos, "admin");

        // Ejecutar procesos de Documentos
        while (sistema.hayProcesosPendientes()) {
            PlanificadorES planificador = new PlanificadorES(sistema);
            planificador.ejecutarFIFO();
        }

        // --- OBTENER REFERENCIAS DE SEGUNDO NIVEL ---
        Directorio dirTareas = dirDocumentos.buscarDirectorioPorNombre("Tareas");
        Directorio dirTrabajos = dirDocumentos.buscarDirectorioPorNombre("Trabajos");
        
        if (dirTareas == null || dirTrabajos == null) {
            System.err.println("¡ERROR! Falló la creación de subdirectorios en Documentos.");
            return; 
        }
        
        // --- CREACIÓN DE ARCHIVOS EN TAREAS Y TRABAJOS ---
        sistema.crearProceso("p8","createFile", "historia.pdf", 2, dirTareas, "admin");
        sistema.crearProceso("p9","createFile", "matematicas.xls", 3, dirTareas, "admin");
        sistema.crearProceso("p10","createFile", "proyecto_final.zip", 7, dirTrabajos, "admin");
        
        // --- CREACIÓN DE ARCHIVOS EN MUSICA ---
        sistema.crearProceso("p11","createFile", "track01.mp3", 4, dirMusica, "user1");
        sistema.crearProceso("p12","createFile", "playlist.m3u", 1, dirMusica, "user1");
        
        // --- CREACIÓN DE SUBDIRECTORIO EN FOTOS ---
        sistema.crearProceso("p13","createDir", "Viajes", 0, dirFotos, "user1");

        // Ejecutar los procesos restantes (p8 a p13)
        while (sistema.hayProcesosPendientes()) {
            PlanificadorES planificador = new PlanificadorES(sistema);
            planificador.ejecutarFIFO();
        }

        // --- VERIFICACIÓN DE SUB-SUBDIRECTORIO ---
        Directorio dirViajes = dirFotos.buscarDirectorioPorNombre("Viajes");
        if (dirViajes == null) {
            System.err.println("¡ERROR! Falló la creación del directorio Viajes.");
            return; 
        }

        // --- CREACIÓN DE ARCHIVO EN VIAJES ---
        sistema.crearProceso("p14","createFile", "verano_2024.jpg", 3, dirViajes, "user1");

        // Ejecutar el último proceso
        while (sistema.hayProcesosPendientes()) {
            PlanificadorES planificador = new PlanificadorES(sistema);
            planificador.ejecutarFIFO();
        }

        // Mostrar la interfaz gráfica directamente
        SimuladorSistArchivos simulador = new SimuladorSistArchivos(sistema);
        simulador.setVisible(true);
    }

    private static void mostrarDisco(SistemaArchivos sistema) {
        Bloque[] bloques = sistema.getDisco().getBloques();
        System.out.print("Bloques: ");
        for (Bloque b : bloques) {
            System.out.print(b.estaLibre() ? "[ ]" : "[X]");
        }
        System.out.println();
    }

    private static void mostrarArchivosRecursivo(Directorio dir) {
        System.out.println("Directorio: " + dir.getNombre());

        var nodoArchivo = dir.getArchivos().getFirst();
        if (nodoArchivo == null) {
            System.out.println("  (sin archivos)");
        } else {
            while (nodoArchivo != null) {
                Archivo a = (Archivo) nodoArchivo.getElement();
                System.out.println("  - " + a.getNombre());
                nodoArchivo = nodoArchivo.getNext();
            }
        }

        var nodoSub = dir.getSubdirectorios().getFirst();
        while (nodoSub != null) {
            Directorio sub = (Directorio) nodoSub.getElement();
            mostrarArchivosRecursivo(sub);
            nodoSub = nodoSub.getNext();
        }
    }
}