package simuladorsistemadearchivos;

import Disk.Bloque;
import FileSystem.Archivo;
import FileSystem.Directorio;
import FileSystem.SistemaArchivos;
import GUI.SimuladorSistArchivos;

public class SimuladorSistemaDeArchivos {

    public static void main(String[] args) {

        // Crear el sistema de archivos
        SistemaArchivos sistema = new SistemaArchivos(20);
        Directorio root = sistema.getRoot();

        // Crear algunos datos de prueba
        Directorio docs = new Directorio("Documentos", root);
        root.agregarSubdirectorio(docs);

        Directorio img = new Directorio("Imagenes", root);
        root.agregarSubdirectorio(img);

        sistema.crearArchivo("informe.pdf", 3, docs, "admin");
        sistema.crearArchivo("foto.png", 4, img, "admin");
        sistema.crearArchivo("logo.svg", 2, img, "admin");

        // Mostrar la interfaz gráfica directamente
        SimuladorSistArchivos simulador = new SimuladorSistArchivos(sistema);
        simulador.setVisible(true);
        
        // Mostrar archivos en consola
        System.out.println("Contenido del sistema de archivos:");
        mostrarArchivosRecursivo(root);
    }

    // Método auxiliar para mostrar el estado del disco
    private static void mostrarDisco(SistemaArchivos sistema) {
        Bloque[] bloques = sistema.getDisco().getBloques();
        System.out.print("Bloques: ");
        for (Bloque b : bloques) {
            System.out.print(b.estaLibre() ? "[ ]" : "[X]");
        }
        System.out.println();
    }

    // Método recursivo para mostrar todos los archivos de un directorio y sus subdirectorios
    private static void mostrarArchivosRecursivo(Directorio dir) {
        System.out.println("Directorio: " + dir.getNombre());

        // Archivos del directorio
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

        // Subdirectorios
        var nodoSub = dir.getSubdirectorios().getFirst();
        while (nodoSub != null) {
            Directorio sub = (Directorio) nodoSub.getElement();
            mostrarArchivosRecursivo(sub);
            nodoSub = nodoSub.getNext();
        }
    }
}
