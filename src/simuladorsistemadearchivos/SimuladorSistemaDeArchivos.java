package simuladorsistemadearchivos;

import Disk.Bloque;
import FileSystem.Archivo;
import FileSystem.Directorio;
import FileSystem.SistemaArchivos;
import GUI.SimuladorSistArchivos;
import Scheduler.PlanificadorES;

public class SimuladorSistemaDeArchivos {

    public static void main(String[] args) {

        SistemaArchivos sistema = new SistemaArchivos(25);
        Directorio root = sistema.getRoot();

        // --- CREACIÓN DE ESTRUCTURA BÁSICA ---
        sistema.crearProceso("p1","createDir", "Documentos", 0, root, "admin",1);
        sistema.crearProceso("p2","createDir", "Musica", 0, root, "admin",6);
        sistema.crearProceso("p3","createDir", "Fotos", 0, root, "user1",5);

        // Ejecutar procesos de creación de directorios de nivel 1
        while (sistema.hayProcesosPendientes()) {
            PlanificadorES planificador = new PlanificadorES(sistema);
            //planificador.ejecutarFIFO();
            //planificador.ejecutarLIFO();
            //planificador.ejecutarPA();
            //planificador.ejecutarSSTF();
            planificador.ejecutarPrioridad();
            
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
        sistema.crearProceso("p4","createDir", "Tareas", 0, dirDocumentos, "admin",8);
        sistema.crearProceso("p5","createDir", "Trabajos", 0, dirDocumentos, "admin",2);
        sistema.crearProceso("p6","createFile", "Tesis.docx", 5, dirDocumentos, "admin",1);
        sistema.crearProceso("p7","createFile", "Apuntes.txt", 1, dirDocumentos, "admin",10);

        // Ejecutar procesos de Documentos
        while (sistema.hayProcesosPendientes()) {
            PlanificadorES planificador = new PlanificadorES(sistema);
            //planificador.ejecutarFIFO();
            //planificador.ejecutarLIFO();
            //planificador.ejecutarPA();
            //planificador.ejecutarSSTF();
            planificador.ejecutarPrioridad();
        }

        // --- OBTENER REFERENCIAS DE SEGUNDO NIVEL ---
        Directorio dirTareas = dirDocumentos.buscarDirectorioPorNombre("Tareas");
        Directorio dirTrabajos = dirDocumentos.buscarDirectorioPorNombre("Trabajos");
        
        if (dirTareas == null || dirTrabajos == null) {
            System.err.println("¡ERROR! Falló la creación de subdirectorios en Documentos.");
            return; 
        }
        
        // --- CREACIÓN DE ARCHIVOS EN TAREAS Y TRABAJOS ---
        sistema.crearProceso("p8","createFile", "historia.pdf", 2, dirTareas, "admin",8);
        sistema.crearProceso("p9","createFile", "matematicas.xls", 3, dirTareas, "admin",5);
        sistema.crearProceso("p10","createFile", "proyecto_final.zip", 7, dirTrabajos, "admin",6);
        
        // --- CREACIÓN DE ARCHIVOS EN MUSICA ---
        sistema.crearProceso("p11","createFile", "track01.mp3", 4, dirMusica, "user1",1);
        sistema.crearProceso("p12","createFile", "playlist.m3u", 1, dirMusica, "user1",5);
        
        // --- CREACIÓN DE SUBDIRECTORIO EN FOTOS ---
        sistema.crearProceso("p13","createDir", "Viajes", 0, dirFotos, "user1",5);

        // Ejecutar los procesos restantes (p8 a p13)
        while (sistema.hayProcesosPendientes()) {
            PlanificadorES planificador = new PlanificadorES(sistema);
            //planificador.ejecutarFIFO();
            //planificador.ejecutarLIFO();
            //planificador.ejecutarPA();
            planificador.ejecutarPrioridad();
        }

        // --- VERIFICACIÓN DE SUB-SUBDIRECTORIO ---
        Directorio dirViajes = dirFotos.buscarDirectorioPorNombre("Viajes");
        if (dirViajes == null) {
            System.err.println("¡ERROR! Falló la creación del directorio Viajes.");
            return; 
        }

        // --- CREACIÓN DE ARCHIVO EN VIAJES ---
        sistema.crearProceso("p14","createFile", "verano_2024.jpg", 3, dirViajes, "user1",9);

        // Ejecutar el último proceso
        while (sistema.hayProcesosPendientes()) {
            PlanificadorES planificador = new PlanificadorES(sistema);
            //planificador.ejecutarFIFO();
            //planificador.ejecutarLIFO();
            //planificador.ejecutarPA();
            planificador.ejecutarPrioridad();
        }
        
        sistema.crearProceso("r1", "read", "Tesis.docx", 0, dirDocumentos, "admin",9);
sistema.crearProceso("r2", "read", "historia.pdf", 0, dirTareas, "admin",5);
sistema.crearProceso("r3", "read", "track01.mp3", 0, dirMusica, "user1",10);
sistema.crearProceso("r4", "delete", "playlist.m3u", 0, dirMusica, "user1",5);

while (sistema.hayProcesosPendientes()) {
    PlanificadorES planificador = new PlanificadorES(sistema);
    planificador.ejecutarPrioridad();
}

sistema.crearProceso("p99","createFile", "69.xxx", 1, dirViajes, "user1",8);

        // Ejecutar el último proceso
        while (sistema.hayProcesosPendientes()) {
            PlanificadorES planificador = new PlanificadorES(sistema);
            //planificador.ejecutarFIFO();
            //planificador.ejecutarLIFO();
            //planificador.ejecutarPA();
            planificador.ejecutarPrioridad();
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