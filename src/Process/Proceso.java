package Process;

import FileSystem.Directorio;

public class Proceso {

    private int id;
    private String operacion;       
    private String nombre;          
    private int tamBloques;         
    private Directorio destino;     
    private String usuario;
    
    private int bloqueObjetivo;     // primer bloque para SSTF  
    private String nombreProceso; 
    private int prioridad;          // menor valor = mayor prioridad

    // Constructor modificado para incluir prioridad
    public Proceso(int id, String nombreProceso, String operacion, String nombre,
                   int tamBloques, Directorio destino, String usuario, int prioridad) {
        
        this.id = id;
        this.nombreProceso = nombreProceso;
        this.operacion = operacion;
        this.nombre = nombre;
        this.tamBloques = tamBloques;
        this.destino = destino;
        this.usuario = usuario;
        this.prioridad = prioridad;

        this.bloqueObjetivo = -1;   // por defecto, sin asignar
    }

    // Constructor original por compatibilidad (prioridad = 0)
    public Proceso(int id, String nombreProceso, String operacion, String nombre,
                   int tamBloques, Directorio destino, String usuario) {
        this(id, nombreProceso, operacion, nombre, tamBloques, destino, usuario, 0);
    }

    public int getId() { return id; }
    public String getOperacion() { return operacion; }
    public String getNombre() { return nombre; }
    public int getTamBloques() { return tamBloques; }
    public Directorio getDestino() { return destino; }
    public String getUsuario() { return usuario; }
    public int getPrioridad() { return prioridad; }
    public void setPrioridad(int prioridad) { this.prioridad = prioridad; }

    public void setBloqueObjetivo(int bloque) { this.bloqueObjetivo = bloque; }
    public int getBloqueObjetivo() { return bloqueObjetivo; }

    public String getNombreProceso() { return nombreProceso; }

    @Override
    public String toString() {
        return "P" + id + " [" + operacion + "] " + nombre +
               (tamBloques > 0 ? (" (" + tamBloques + " bloques)") : "") +
               " -> " + destino.getNombre() +
               " [" + usuario + "]" +
               " | Prioridad: " + prioridad;
    }
}
