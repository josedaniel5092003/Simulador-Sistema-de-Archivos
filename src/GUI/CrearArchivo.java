/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import java.awt.Frame;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author vivia
 */
public class CrearArchivo extends JDialog {

    public interface Listener {
        void aceptar(String nombre, int tam);
    }

    public CrearArchivo(Frame parent, Listener listener) {
        super(parent, "Crear Archivo", true);

        JTextField tfNombre = new JTextField(15);
        JTextField tfTam = new JTextField(5);
        JButton btn = new JButton("Crear");

        btn.addActionListener(e -> {
            String n = tfNombre.getText();
            int t = Integer.parseInt(tfTam.getText());
            listener.aceptar(n, t);
            dispose();
        });

        JPanel panel = new JPanel();
        panel.add(new JLabel("Nombre:"));
        panel.add(tfNombre);
        panel.add(new JLabel("Tamaño (bloques):"));
        panel.add(tfTam);
        panel.add(btn);

        setContentPane(panel);
        pack();
        setLocationRelativeTo(parent);
    }
}

