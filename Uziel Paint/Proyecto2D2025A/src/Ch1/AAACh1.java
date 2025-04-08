// ****************************************************
// File: AAACh1.java
// Propósito: Ventana principal del proyecto de gráficos
// Con mejoras de UI profesional y organización
// ****************************************************
package Ch1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

public class AAACh1 extends JFrame {
    private JTabbedPane tabbedPane;
    private ImageIcon appIcon;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        SwingUtilities.invokeLater(() -> {
            new AAACh1().setVisible(true);
        });
    }

    public AAACh1() {
        configureWindow();
        initComponents();
        applyModernStyle();
    }

    private void configureWindow() {
        setTitle("Java Graphics Suite v1.0");
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        try {
            appIcon = new ImageIcon(getClass().getResource("/icons/app_icon.png"));
            setIconImage(appIcon.getImage());
        } catch (Exception e) {
            System.out.println("Icono no encontrado, usando icono por defecto");
        }
    }

    private void initComponents() {
        JPanel hello2DPanel = createTitledPanel(new Hello2D(), "Ejercicio 1: Primeros Pasos 2D");
        JPanel hello2DV2Panel = createTitledPanel(new Hello2DV2(), "Ejercicio 2: Transformaciones Básicas");
        JPanel ex3Panel = createTitledPanel(new Ex3EllipsesSlider(), "Ejercicio 3: Patrones con Elipses");
        JPanel drawShapesPanel = createTitledPanel(new DrawShapes(), "Ejercicio 4: Editor de Formas Avanzado");

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        addTabWithIcon("Ejercicio 1", "⚡", hello2DPanel);
        addTabWithIcon("Ejercicio 2", "🔄", hello2DV2Panel);
        addTabWithIcon("Ejercicio 3", "🎨", ex3Panel);
        addTabWithIcon("Uziel Paint", "✏️", drawShapesPanel);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
        JLabel statusLabel = new JLabel("Estado: Listo");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusPanel.add(statusLabel);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
        getContentPane().add(statusPanel, BorderLayout.SOUTH);

        createMenuBar();
    }

    private JPanel createTitledPanel(JComponent component, String title) {
        JPanel panel = new JPanel(new BorderLayout());

        // Corrección aplicada en el borde
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0, 153, 204)),
                title,
                TitledBorder.LEFT,
                TitledBorder.TOP
        );

        titledBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 14));
        titledBorder.setTitleColor(new Color(0, 102, 153));

        panel.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(15, 15, 15, 15),
                titledBorder
        ));

        panel.add(component);
        return panel;
    }

    private void addTabWithIcon(String title, String icon, JComponent component) {
        JLabel tabLabel = new JLabel(title + "  " + icon);
        tabLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabbedPane.addTab(title, component);
        tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, tabLabel);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("Archivo");
        fileMenu.setMnemonic('A');

        JMenuItem exitItem = new JMenuItem("Salir", KeyEvent.VK_S);
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        JMenu helpMenu = new JMenu("Ayuda");
        JMenuItem aboutItem = new JMenuItem("Acerca de...");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
                "<html><b>Java Graphics Suite v1.0</b><br>"
                        + "Proyecto de gráficacion 2D/3D<br>"
                        + "Desarrollado por Uziel Sosa<br>"
                        + "Tecnologías: Java2D<br>"
                        + "© 2025 Todos los derechos reservados</html>",
                "Acerca del Proyecto",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void applyModernStyle() {
        UIManager.put("nimbusBase", new Color(0, 102, 153));
        UIManager.put("nimbusBlueGrey", new Color(214, 217, 223));
        UIManager.put("control", new Color(240, 240, 240));

        SwingUtilities.updateComponentTreeUI(this);
    }
}