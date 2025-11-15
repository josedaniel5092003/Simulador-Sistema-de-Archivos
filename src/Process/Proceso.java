package Process;

import FileSystem.Directorio;

public class Proceso {

    private int id;
    private String operacion;       // createFile, createDir, delete...
    private String nombre;          // nombre del archivo o directorio
    private int tamBloques;         // solo para archivos (0 si es directorio)
    private Directorio destino;     // directorio donde se ejecuta
    private String usuario;         // quien solicita la operación
    private int bloqueObjetivo;  // para planificación de disco
    private String nombreProceso; 


    public Proceso(int id, String nombreProceso, String operacion, String nombre, int tamBloques, Directorio destino, String usuario) {
        this.id = id;
        this.nombreProceso = nombreProceso;
        this.operacion = operacion;
        this.nombre = nombre;
        this.tamBloques = tamBloques;
        this.destino = destino;
        this.usuario = usuario;
        this.bloqueObjetivo = -1;

    }

    public int getId() { return id; }
    public String getOperacion() { return operacion; }
    public String getNombre() { return nombre; }
    public int getTamBloques() { return tamBloques; }
    public Directorio getDestino() { return destino; }
    public String getUsuario() { return usuario; }
    public void setBloqueObjetivo(int b) { this.bloqueObjetivo = b; }
    public int getBloqueObjetivo() { return bloqueObjetivo; }
    public String getNombreProceso() { return nombreProceso; }


    @Override
    public String toString() {
        return "P" + id + " [" + operacion + "] " + nombre +
               (tamBloques > 0 ? (" (" + tamBloques + " bloques)") : "") +
               " -> " + destino.getNombre() +
               " [" + usuario + "]";
    }
}
