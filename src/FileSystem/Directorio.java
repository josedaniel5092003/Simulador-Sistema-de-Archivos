/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FileSystem;

/**
 *
 * @author Jose
 */
import DataStruct.LinkedList;
import DataStruct.Nodo;

public class Directorio {
    private String nombre;
    private Directorio padre;
    private LinkedList<Directorio> subdirectorios;
    private LinkedList<Archivo> archivos;

    public Directorio(String nombre, Directorio padre) {
        this.nombre = nombre;
        this.padre = padre;
        this.subdirectorios = new LinkedList<>();
        this.archivos = new LinkedList<>();
    }
    
    public Archivo buscarArchivoPorNombre(String nombreArchivo) {
        Nodo<Archivo> nodo = archivos.getFirst();   // primer nodo de la lista
        int total = archivos.getLenght();           // número de elementos

        for (int i = 0; i < total && nodo != null; i++) {
            Archivo a = nodo.getElement();          // obtenemos el objeto Archivo
            if (a.getNombre().equalsIgnoreCase(nombreArchivo)) {
                return a;                           // encontrado ✅
            }
            nodo = nodo.getNext();                  // avanzar al siguiente
        }

        return null; // si no lo encontró
    }
    

    public void agregarArchivo(Archivo archivo) {
        archivos.insertFinal(archivo);
    }

    public void agregarSubdirectorio(Directorio dir) {
        subdirectorios.insertFinal(dir);
    }

    public LinkedList<Archivo> getArchivos() {
        return archivos;
    }

    public LinkedList<Directorio> getSubdirectorios() {
        return subdirectorios;
    }
    
    public String getNombre() {
        return this.nombre;
    }
}
