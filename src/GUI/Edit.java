/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import FileSystem.Archivo;
import FileSystem.Directorio;
import javax.swing.*;
import java.awt.*;


public class Edit extends JDialog {

    private JTextField txtNombre;
    private JButton btnGuardar, btnCancelar;
    private Object objeto; // Archivo o Directorio

    public interface OnNombreEditado {
        void nombreEditado(String nuevoNombre);
    }

    public Edit(Frame parent, Object objeto, OnNombreEditado callback) {
        super(parent, "Editar nombre", true);
        this.objeto = objeto;

        setSize(350, 170);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String nombreActual = (objeto instanceof Archivo)
                ? ((Archivo) objeto).getNombre()
                : ((Directorio) objeto).getNombre();

        panel.add(new JLabel("Nuevo nombre:"));
        txtNombre = new JTextField(nombreActual);
        panel.add(txtNombre);

        add(panel, BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
        botones.add(btnGuardar);
        botones.add(btnCancelar);
        add(botones, BorderLayout.SOUTH);

        // Eventos
        btnGuardar.addActionListener(e -> {
            String nuevo = txtNombre.getText().trim();
            if (nuevo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.");
                return;
            }

            callback.nombreEditado(nuevo);
            dispose();
        });

        btnCancelar.addActionListener(e -> dispose());
    }
}
