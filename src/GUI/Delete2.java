/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import FileSystem.Archivo;
import FileSystem.Directorio;

import javax.swing.*;
import java.awt.*;

public class Delete2 extends JDialog {

    private JButton btnEliminar, btnCancelar;
    private Object objeto; // Archivo o Directorio

    // Callback para informar que se confirmó la eliminación
    public interface OnDeleteConfirm {
        void eliminar();
    }

    public Delete2(Frame parent, Object objeto, OnDeleteConfirm callback) {
        super(parent, "Eliminar", true);
        this.objeto = objeto;

        setSize(350, 150);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        String nombre = (objeto instanceof Archivo)
                ? ((Archivo) objeto).getNombre()
                : ((Directorio) objeto).getNombre();

        JLabel lblMsg = new JLabel(
                "<html>¿Seguro que deseas eliminar <b>" + nombre + "</b>?<br>" +
                "Esta acción no se puede deshacer.</html>"
        );
        lblMsg.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        add(lblMsg, BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnEliminar = new JButton("Eliminar");
        btnCancelar = new JButton("Cancelar");

        botones.add(btnEliminar);
        botones.add(btnCancelar);

        add(botones, BorderLayout.SOUTH);

        // Evento botón Eliminar
        btnEliminar.addActionListener(e -> {
            callback.eliminar();
            dispose();
        });

        // Evento Cancelar
        btnCancelar.addActionListener(e -> dispose());
    }
}
