package simuladorsistemadearchivos;

import FileSystem.Directorio;
import FileSystem.SistemaArchivos;
import GUI.SimuladorSistArchivos;
import Scheduler.PlanificadorES;

public class SimuladorSistemaDeArchivos {

    public static void main(String[] args) {

        SistemaArchivos sistema = new SistemaArchivos(20);
        Directorio root = sistema.getRoot();

        // ----- CREAR 2 DIRECTORIOS -----
        sistema.crearProceso("p1", "createDir", "Documentos", 0, root, "admin", 5);
        sistema.crearProceso("p2", "createDir", "Fotos", 0, root, "admin", 3);

        ejecutarTodo(sistema);

        // Obtener referencias
        Directorio dirDocs = root.buscarDirectorioPorNombre("Documentos");
        Directorio dirFotos = root.buscarDirectorioPorNombre("Fotos");

        // ----- CREAR 2 ARCHIVOS -----
        sistema.crearProceso("p3", "createFile", "resume.pdf", 3, dirDocs, "admin", 7);
        sistema.crearProceso("p4", "createFile", "selfie.jpg", 2, dirFotos, "admin", 4);

        ejecutarTodo(sistema);

        // Mostrar interfaz
        SimuladorSistArchivos ui = new SimuladorSistArchivos(sistema);
        ui.setVisible(true);
    }

    // Ejecuta todos los procesos pendientes por prioridad
    private static void ejecutarTodo(SistemaArchivos sistema) {
        PlanificadorES planificador = new PlanificadorES(sistema);
        while (sistema.hayProcesosPendientes()) {
            planificador.ejecutarPrioridad();
        }
    }
}
