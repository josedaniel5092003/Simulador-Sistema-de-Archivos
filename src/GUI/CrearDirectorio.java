/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import javax.swing.*;
import java.awt.*;


public class CrearDirectorio extends JDialog {

    @FunctionalInterface
    public interface Listener {
        void aceptar(String nombreDirectorio);
    }

    private Listener listener;

    public CrearDirectorio(Frame parent, Listener listener) {
        super(parent, "Crear Directorio", true);
        this.listener = listener;
        initUI(parent);
    }

    private void initUI(Frame parent) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblNombre = new JLabel("Nombre del directorio:");
        JTextField tfNombre = new JTextField(18);

        JButton btnCrear = new JButton("Crear");
        JButton btnCancelar = new JButton("Cancelar");

        // Primera fila - nombre
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblNombre, gbc);

        gbc.gridx = 1;
        panel.add(tfNombre, gbc);

        // Segunda fila - botones
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.add(btnCancelar);
        botones.add(btnCrear);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(botones, gbc);

        // Acciones
        btnCrear.addActionListener(e -> {
            String nombre = tfNombre.getText().trim();

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this, "Ingrese un nombre para el directorio.",
                        "Error", JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (listener != null) listener.aceptar(nombre);

            dispose();
        });

        btnCancelar.addActionListener(e -> dispose());

        // Enter en el campo → crear
        tfNombre.addActionListener(e -> btnCrear.doClick());

        setContentPane(panel);
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }
}

