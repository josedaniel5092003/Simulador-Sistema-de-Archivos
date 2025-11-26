package simuladorsistemadearchivos;

import FileSystem.Directorio;
import FileSystem.SistemaArchivos;
import GUI.SimuladorSistArchivos;
import Scheduler.PlanificadorES;



public class SimuladorSistemaDeArchivos {

    public static void main(String[] args) {

        SistemaArchivos sistema = new SistemaArchivos(25);
        Directorio root = sistema.getRoot();

        sistema.crearProceso("p1", "createDir", "Documentos", 0, root, "admin", null);
        sistema.crearProceso("p2", "createDir", "Fotos", 0, root, "admin", null);
        
        // Obtener referencias
        Directorio dirDocs = root.buscarDirectorioPorNombre("Documentos");
        Directorio dirFotos = root.buscarDirectorioPorNombre("Fotos");
//        
//        sistema.crearProceso("p3", "createFile", "resume.pdf", 3, dirDocs, "admin", null);
//        sistema.crearProceso("p4", "createFile", "selfie.jpg", 2, dirFotos, "admin", null);
        
        // Mostrar interfaz
        SimuladorSistArchivos ui = new SimuladorSistArchivos(sistema);
        ui.setVisible(true);
    }


    
    



}