/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI;

import DataStruct.LinkedList;
import FileSystem.Archivo;
import FileSystem.Directorio;
import FileSystem.SistemaArchivos;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import Disk.Bloque;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author vivia
 */
public class SimuladorSistArchivos extends javax.swing.JFrame {

    /**
     * Creates new form SimuladorSistArchivos
     */
    
    private SistemaArchivos sistema;
    public boolean isAdmin;
    JPopupMenu menuOpciones;
    
    public SimuladorSistArchivos(SistemaArchivos sistema) {
        initComponents();
        this.sistema = sistema;
        this.isAdmin = false;
        
        dibujarDisco();
        
        setTitle("Simulador de Sistema de Archivos");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        explorador.setSize(280,440);
        
        arbol.setRootVisible(true);
        arbol.setShowsRootHandles(true);
        arbol.putClientProperty("JTree.lineStyle", "Angled"); 
        arbol.setSize(260,420);

        // Fuente y colores modernos
        arbol.setFont(new java.awt.Font("Corbel", java.awt.Font.PLAIN, 13));
        arbol.setRowHeight(24);
        arbol.setForeground(new java.awt.Color(50, 50, 50));
        aplicarRendererConIconos();
         
        // Estilo general del toolbar
        bar.setFloatable(false);                    // Evita que se mueva
        bar.setRollover(true);                      // Efecto hover
        bar.setOpaque(true);
        bar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Márgenes
        
        add.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 25));
        add.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add.setToolTipText("Agregar archivo");
        add.disable();

        delete.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 25));
        delete.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        delete.setToolTipText("Eliminar");
        delete.disable();
        
        directorio.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        directorio.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        directorio.setToolTipText("Crear directorio");
        directorio.disable();

        editar.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        editar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        editar.setToolTipText("Editar");
        editar.disable();
        
        
        menu.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 25));
        menu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        menu.setToolTipText("Más opciones");
        
        bar.add(Box.createHorizontalGlue());
        userType.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        userType.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 35));
        userType.setToolTipText("Tipo de usuario");
        bar.add(userType);
        
        separador.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 25));
        separador1.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 25));
        
        menuOpciones = new JPopupMenu();

        JMenuItem itemGuardar = new JMenuItem("Guardar configuración");
        JMenuItem itemCargar = new JMenuItem("Cargar configuración");

        menuOpciones.add(itemGuardar);
        menuOpciones.add(itemCargar);
        
        DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Archivo", "Bloques", "Primer Bloque", "Proceso"}, 
            0
        );
        tablaAsignacion.setModel(modelo);
        
        actualizarArbol();
        llenarTablaAsignacion(sistema);
       
    }
    
    private void cambiarModo(){
        isAdmin = !isAdmin;
        if (isAdmin) {
            userType.setIcon(new ImageIcon(getClass().getResource("/GUI/admin.png")));
            add.setIcon(new ImageIcon(getClass().getResource("/GUI/plus.png")));
            add.enable();
            delete.setIcon(new ImageIcon(getClass().getResource("/GUI/eliminar.png")));
            delete.enable();
            editar.setIcon(new ImageIcon(getClass().getResource("/GUI/editar.png")));
            editar.enable();
            directorio.setIcon(new ImageIcon(getClass().getResource("/GUI/carpeta-vacia.png")));
            directorio.enable();
        } else {
            userType.setIcon(new ImageIcon(getClass().getResource("/GUI/noadmin.png")));
            add.setIcon(new ImageIcon(getClass().getResource("/GUI/plus-opaco.png")));
            add.disable();
            delete.setIcon(new ImageIcon(getClass().getResource("/GUI/eliminar-opaco.png")));
            delete.disable();
            editar.setIcon(new ImageIcon(getClass().getResource("/GUI/editar-opaco.png")));
            editar.disable();
            directorio.setIcon(new ImageIcon(getClass().getResource("/GUI/carpeta-vacia-opaca.png")));
            directorio.disable();
        }

    }
    
    // Construye el árbol a partir del sistema de archivos
    public void actualizarArbol() {
        Directorio rootDir = sistema.getRoot();

        // Crea el nodo raíz
        DefaultMutableTreeNode nodoRaiz = new DefaultMutableTreeNode(rootDir);

        // Llama al método recursivo para llenar los subdirectorios
        construirArbol(nodoRaiz, rootDir);

        // Asigna el modelo al árbol
        DefaultTreeModel modelo = new DefaultTreeModel(nodoRaiz);
        arbol.setModel(modelo);
    }

    // Recorre recursivamente los directorios y archivos
    private void construirArbol(DefaultMutableTreeNode nodoPadre, Directorio dir) {
        
        // Añadir subdirectorios
        var nodoSub = dir.getSubdirectorios().getFirst();
        while (nodoSub != null) {
            Directorio sub = (Directorio) nodoSub.getElement();
            DefaultMutableTreeNode nodoDir = new DefaultMutableTreeNode(sub);
            nodoPadre.add(nodoDir);
            construirArbol(nodoDir, sub); // recursión
            nodoSub = nodoSub.getNext();
        }

        // Añadir archivos
        var nodoArchivo = dir.getArchivos().getFirst();
        while (nodoArchivo != null) {
            Archivo a = (Archivo) nodoArchivo.getElement();
            DefaultMutableTreeNode nodoA = new DefaultMutableTreeNode(a);
            nodoPadre.add(nodoA);
            nodoArchivo = nodoArchivo.getNext();
        }
    }
    
    private void recorrerDirectorio(Directorio dir, DefaultTableModel modelo) {
        // Archivos
        LinkedList archivos = dir.getArchivos();
        for (int i = 0; i < archivos.getLength(); i++) {
            Archivo archivo = (Archivo) archivos.getElementIn(i);

            modelo.addRow(new Object[]{
                archivo.getNombre(),
                archivo.getTamanioBloques(),
                archivo.getPrimerBloque(),
                archivo.getOwner().getNombreProceso()
                   
            });
            System.out.println(archivo.getOwner().getNombreProceso());
        }

        // Subdirectorios
        LinkedList subs = dir.getSubdirectorios();
        for (int i = 0; i < subs.getLength(); i++) {
            recorrerDirectorio((Directorio) subs.getElementIn(i), modelo);
        }
    }
    
    private void inicializarPanelDisco() {
    // Creamos el panel para los bloques
    jPanel1 = new JPanel();
    jPanel1.setLayout(null); // layout absoluto para posicionar bloques

    // Creamos el scroll pane que contendrá el panel
    JScrollPane scrollPanel = new JScrollPane(jPanel1);
    scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

    // Agregamos el scroll pane al contenedor principal
    getContentPane().add(scrollPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 60, 320, 390));
}
    
    public void dibujarDisco() {
    jPanel1.removeAll();
    jPanel1.setLayout(null); // importante

    Bloque[] bloques = sistema.getDisco().getBloques();
    int totalBloques = bloques.length;

    int ancho = 60;
    int alto = 60;
    int margen = 10;
    int bloquesPorFila = 5;
    int x = 10;
    int y = 20;

    for (int i = 0; i < totalBloques; i++) {
        JLabel lbl = new JLabel();
        lbl.setBounds(x, y, ancho, alto);
        lbl.setOpaque(true);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setVerticalAlignment(SwingConstants.CENTER);
        lbl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        lbl.setFont(new Font("Arial", Font.BOLD, 9));

        if (bloques[i].estaLibre()) {
            lbl.setBackground(Color.LIGHT_GRAY);
            lbl.setText("");
        } else {
            lbl.setBackground(new Color(155, 197, 255));
            Archivo ocupante = buscarArchivoPorBloque(sistema.getRoot(), i);
            if (ocupante != null) {
                lbl.setText(ocupante.getNombre());
            }
        }

        jPanel1.add(lbl);

        x += ancho + margen;
        if ((i + 1) % bloquesPorFila == 0) {
            x = 10;
            y += alto + margen;
        }
    }

    int filas = (int) Math.ceil((double) totalBloques / bloquesPorFila);
    jPanel1.setPreferredSize(new Dimension(bloquesPorFila*(ancho+margen), filas*(alto+margen)));
    jPanel1.revalidate();
    jPanel1.repaint();
    

}



// Función recursiva para buscar qué archivo ocupa un bloque
private Archivo buscarArchivoPorBloque(Directorio dir, int bloqueId) {
    LinkedList archivos = dir.getArchivos();
    for (int i = 0; i < archivos.getLength(); i++) {
        Archivo archivo = (Archivo) archivos.getElementIn(i);
        int actual = archivo.getPrimerBloque();
        int cont = 0;
        while (actual != -1 && cont < archivo.getTamanioBloques()) {
            if (actual == bloqueId) return archivo;
            actual = sistema.getDisco().getBloques()[actual].getSiguiente();
            cont++;
        }
    }

    // Subdirectorios
    LinkedList subs = dir.getSubdirectorios();
    for (int i = 0; i < subs.getLength(); i++) {
        Archivo encontrado = buscarArchivoPorBloque((Directorio) subs.getElementIn(i), bloqueId);
        if (encontrado != null) return encontrado;
    }

    return null; // no encontrado
}



    


    
    public void llenarTablaAsignacion(SistemaArchivos sistema) {
        DefaultTableModel modelo = (DefaultTableModel) tablaAsignacion.getModel();
        modelo.setRowCount(0); // Limpia la tabla antes

        recorrerDirectorio(sistema.getRoot(), modelo);
    }
    
    private void aplicarRendererConIconos() {

        // Mapa extensión → icono
        Map<String, Icon> iconos = new HashMap<>();

        iconos.put("png", new ImageIcon(getClass().getResource("/GUI/png.png")));
        iconos.put("jpg", new ImageIcon(getClass().getResource("/GUI/jpg.png")));
        iconos.put("mp3", new ImageIcon(getClass().getResource("/GUI/mp3.png")));
        iconos.put("mp4", new ImageIcon(getClass().getResource("/GUI/mp4.png")));
        iconos.put("pdf", new ImageIcon(getClass().getResource("/GUI/pdf.png")));
        iconos.put("ppt", new ImageIcon(getClass().getResource("/GUI/ppt.png")));
        iconos.put("csv", new ImageIcon(getClass().getResource("/GUI/csv.png")));
        iconos.put("txt", new ImageIcon(getClass().getResource("/GUI/txt.png")));
        iconos.put("doc", new ImageIcon(getClass().getResource("/GUI/doc.png")));
        iconos.put("java", new ImageIcon(getClass().getResource("/GUI/java.png")));
        //iconos.put("exe", new ImageIcon(getClass().getResource("/GUI/exe.png")));
        //iconos.put("rar", new ImageIcon(getClass().getResource("/GUI/rar.png")));
        iconos.put("zip", new ImageIcon(getClass().getResource("/GUI/zip.png")));
        iconos.put("xsl", new ImageIcon(getClass().getResource("/GUI/xsl.png")));
        Icon iconCarpeta = new ImageIcon(getClass().getResource("/GUI/carpeta.png"));
        Icon iconDefault = new ImageIcon(getClass().getResource("/GUI/default.png"));


        // ==== Custom Renderer ====
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer() {

            @Override
            public Component getTreeCellRendererComponent(
                    JTree tree, Object value, boolean sel,
                    boolean expanded, boolean leaf, int row,
                    boolean hasFocus) {

                super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

                DefaultMutableTreeNode nodo = (DefaultMutableTreeNode) value;
                Object userObj = nodo.getUserObject();

                // Si es directorio → icono de carpeta
                if (userObj instanceof Directorio) {
                    setIcon(iconCarpeta);
                }
                // Si es archivo → buscar extensión
                else if (userObj instanceof Archivo) {
                    Archivo archivo = (Archivo) userObj;
                    String nombre = archivo.getNombre();

                    String ext = "";
                    int idx = nombre.lastIndexOf(".");
                    if (idx != -1) {
                        ext = nombre.substring(idx + 1).toLowerCase();
                    }
                    

                    // Asignar icono según extensión
                    Icon icon = iconos.getOrDefault(ext, iconDefault);
                    setIcon(icon);
                }

                return this;
            }
        };

        // ==== Colores bonitos ====
        renderer.setTextSelectionColor(Color.WHITE);
        renderer.setBackgroundSelectionColor(new Color(51, 153, 255));
        renderer.setBorderSelectionColor(Color.WHITE);

        arbol.setCellRenderer(renderer);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        bar = new javax.swing.JToolBar();
        add = new javax.swing.JLabel();
        separador = new javax.swing.JSeparator();
        directorio = new javax.swing.JLabel();
        editar = new javax.swing.JLabel();
        delete = new javax.swing.JLabel();
        separador1 = new javax.swing.JSeparator();
        menu = new javax.swing.JLabel();
        userType = new javax.swing.JLabel();
        explorador = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        arbol = new javax.swing.JTree();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaAsignacion = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        jScrollPane1.setViewportView(jTree1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        bar.setRollover(true);

        add.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        add.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/plus.png"))); // NOI18N
        add.setText("Nuevo");
        add.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addMouseClicked(evt);
            }
        });
        bar.add(add);

        separador.setOrientation(javax.swing.SwingConstants.VERTICAL);
        bar.add(separador);

        directorio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/carpeta-vacia.png"))); // NOI18N
        bar.add(directorio);

        editar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/editar.png"))); // NOI18N
        bar.add(editar);

        delete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/eliminar.png"))); // NOI18N
        delete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                deleteMouseClicked(evt);
            }
        });
        bar.add(delete);

        separador1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        bar.add(separador1);

        menu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/menu.png"))); // NOI18N
        menu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                menuMousePressed(evt);
            }
        });
        bar.add(menu);

        userType.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/noadmin.png"))); // NOI18N
        userType.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                userTypeMousePressed(evt);
            }
        });
        bar.add(userType);

        getContentPane().add(bar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 40));

        explorador.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), "Explorador de archivos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(51, 51, 51))); // NOI18N
        explorador.setMinimumSize(new java.awt.Dimension(280, 440));
        explorador.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane2.setViewportView(arbol);

        explorador.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 260, 340));

        getContentPane().add(explorador, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 300, 390));

        tablaAsignacion.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tablaAsignacion.setEnabled(false);
        jScrollPane3.setViewportView(tablaAsignacion);

        getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 60, -1, 390));

        jScrollPane4.setViewportView(jPanel1);

        getContentPane().add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 60, 370, 390));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Sin título (1200 x 800 px).png"))); // NOI18N
        jLabel1.setText("jLabel1");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 800));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuMousePressed
        // TODO add your handling code here:
        menuOpciones.show(menu, 0, menu.getHeight());
    }//GEN-LAST:event_menuMousePressed

    private void userTypeMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_userTypeMousePressed
        // TODO add your handling code here:
        cambiarModo();
    }//GEN-LAST:event_userTypeMousePressed

    private void addMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addMouseClicked
        New ui = new New(sistema, this);
        ui.setVisible(true);
    }//GEN-LAST:event_addMouseClicked

    private void deleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deleteMouseClicked
        Delete ui = new Delete(sistema, this);
        ui.setVisible(true);
    }//GEN-LAST:event_deleteMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel add;
    private javax.swing.JTree arbol;
    private javax.swing.JToolBar bar;
    private javax.swing.JLabel delete;
    private javax.swing.JLabel directorio;
    private javax.swing.JLabel editar;
    private javax.swing.JPanel explorador;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTree jTree1;
    private javax.swing.JLabel menu;
    private javax.swing.JSeparator separador;
    private javax.swing.JSeparator separador1;
    private javax.swing.JTable tablaAsignacion;
    private javax.swing.JLabel userType;
    // End of variables declaration//GEN-END:variables
}
