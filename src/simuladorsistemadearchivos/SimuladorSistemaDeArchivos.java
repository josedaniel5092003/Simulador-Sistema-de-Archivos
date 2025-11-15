package simuladorsistemadearchivos;

import Disk.Bloque;
import FileSystem.Archivo;
import FileSystem.Directorio;
import FileSystem.SistemaArchivos;
import GUI.SimuladorSistArchivos;

public class SimuladorSistemaDeArchivos {

    public static void main(String[] args) {

        // Crear el sistema de archivos
        SistemaArchivos sistema = new SistemaArchivos(85);
        Directorio root = sistema.getRoot();

        // Crear algunos datos de prueba
        Directorio escritorio = new Directorio("Escritorio", root);
        root.agregarSubdirectorio(escritorio);
        
        Directorio docs = new Directorio("Documentos", root);
        root.agregarSubdirectorio(docs);

        Directorio img = new Directorio("Imagenes", root);
        root.agregarSubdirectorio(img);

        sistema.crearArchivo("informe.pdf", 3, docs, "admin");
        sistema.crearArchivo("foto.png", 4, img, "admin");
        sistema.crearArchivo("logo.svg", 2, img, "admin");
        
        // Crear más directorios
        Directorio musica = new Directorio("Musica", root);
        root.agregarSubdirectorio(musica);

        Directorio videos = new Directorio("Videos", root);
        root.agregarSubdirectorio(videos);

        Directorio proyectos = new Directorio("Proyectos", escritorio);
        escritorio.agregarSubdirectorio(proyectos);

        Directorio compresos = new Directorio("Comprimidos", docs);
        docs.agregarSubdirectorio(compresos);

        Directorio clases = new Directorio("Clases", escritorio);
        proyectos.agregarSubdirectorio(clases);

        Directorio reportes = new Directorio("Reportes", docs);
        docs.agregarSubdirectorio(reportes);


        // Crear archivos según extensiones disponibles
        sistema.crearArchivo("cancion1.mp3", 5, musica, "admin");
        sistema.crearArchivo("cancion2.mp3", 4, musica, "admin");
        sistema.crearArchivo("foto_familia.jpg", 3, img, "admin");
        sistema.crearArchivo("wallpaper.png", 4, img, "admin");
        sistema.crearArchivo("video1.mp4", 10, videos, "admin");
        sistema.crearArchivo("video2.mp4", 8, videos, "admin");
        sistema.crearArchivo("datos.csv", 2, docs, "admin");
        sistema.crearArchivo("guia.doc", 3, docs, "admin");
        sistema.crearArchivo("manual.pdf", 4, reportes, "admin");
        sistema.crearArchivo("reporte_final.pdf", 5, reportes, "admin");
        sistema.crearArchivo("clase1.java", 2, clases, "admin");
        sistema.crearArchivo("clase2.java", 2, clases, "admin");
        sistema.crearArchivo("instalador.exe", 6, escritorio, "admin");
        sistema.crearArchivo("comprimido.rar", 3, compresos, "admin");
        sistema.crearArchivo("archivos.zip", 4, compresos, "admin");
        sistema.crearArchivo("presentacion.ppt", 3, proyectos, "admin");
        sistema.crearArchivo("hoja_estilos.xsl", 2, proyectos, "admin");
        sistema.crearArchivo("descripcion.txt", 1, escritorio, "admin");
        sistema.crearArchivo("notas.txt", 1, docs, "admin");
        sistema.crearArchivo("banner.svg", 2, img, "admin");

        // Archivo de extensión desconocida -> usará default.png
        sistema.crearArchivo("archivo.rtf", 2, docs, "admin");


        // Mostrar la interfaz gráfica directamente
        SimuladorSistArchivos simulador = new SimuladorSistArchivos(sistema);
        simulador.setVisible(true);
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
