/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Process;

/**
 *
 * @author Jose
 */
public class Proceso {
    private int id;
    private String operacion; // create/read/update/delete
    private String objetivo;  // nombre archivo/directorio
    private String estado;

    public Proceso(int id, String operacion, String objetivo) {
        this.id = id;
        this.operacion = operacion;
        this.objetivo = objetivo;
        this.estado = "nuevo";
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "P" + id + " (" + operacion + " -> " + objetivo + ") [" + estado + "]";
    }
}
