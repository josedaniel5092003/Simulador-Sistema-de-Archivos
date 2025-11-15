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
        int total = archivos.getLength();           // número de elementos

        for (int i = 0; i < total && nodo != null; i++) {
            Archivo a = nodo.getElement();          // obtenemos el objeto Archivo
            if (a.getNombre().equalsIgnoreCase(nombreArchivo)) {
                return a;                           // encontrado ✅
            }
            nodo = nodo.getNext();                  // avanzar al siguiente
        }

        return null; // si no lo encontró
    }
    
       public boolean removerArchivo(Archivo objetivo) {
        Nodo<Archivo> prev = null;
        Nodo<Archivo> curr = archivos.getFirst();
        int len = archivos.getLength();

        for (int i = 0; i < len && curr != null; i++) {
            if (curr.getElement().equals(objetivo)) {
                if (prev == null) {
                    archivos.setFirst(curr.getNext());
                    if (curr.getNext() == null) archivos.setLast(null);
                } else {
                    prev.setNext(curr.getNext());
                    if (curr.getNext() == null) archivos.setLast(prev);
                }
                archivos.setLength(archivos.getLength() - 1);
                return true;
            }
            prev = curr;
            curr = curr.getNext();
        }
        return false;
    }
       
       public Directorio buscarSubdirectorio(String nombreDir) {
        Nodo<Directorio> nodo = subdirectorios.getFirst();
        int total = subdirectorios.getLength();

        for (int i = 0; i < total && nodo != null; i++) {
            Directorio d = nodo.getElement();
            if (d.getNombre().equalsIgnoreCase(nombreDir)) {
                return d;
            }
            nodo = nodo.getNext();
        }
        return null;
    }
       
        /** Elimina un subdirectorio específico del actual */
    public boolean removerSubdirectorio(Directorio objetivo) {
        Nodo<Directorio> prev = null;
        Nodo<Directorio> curr = subdirectorios.getFirst();
        int len = subdirectorios.getLength();

        for (int i = 0; i < len && curr != null; i++) {
            if (curr.getElement().equals(objetivo)) {
                if (prev == null) {
                    subdirectorios.setFirst(curr.getNext());
                    if (curr.getNext() == null) subdirectorios.setLast(null);
                } else {
                    prev.setNext(curr.getNext());
                    if (curr.getNext() == null) subdirectorios.setLast(prev);
                }
                subdirectorios.setLength(subdirectorios.getLength() - 1);
                return true;
            }
            prev = curr;
            curr = curr.getNext();
        }
        return false;
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
    
    @Override
    public String toString() {
        return nombre;
    }

}
