package Ch1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import javax.imageio.ImageIO;

public class DrawShapes extends JPanel implements ActionListener {
    private JavaDraw2DPanel drawPanel;
    private JComboBox<Float> strokeWidthComboBox;
    private JComboBox<String> colorComboBox;
    private JComboBox<String> shapeComboBox;
    private JComboBox<String> fillTypeComboBox;
    private JButton clearButton;
    private JButton saveButton;
    private JButton unionButton;
    private JButton intersectButton;
    private JButton subtractButton;
    private JButton xorButton;
    private JButton selectButton;

    public DrawShapes() {
        setLayout(new BorderLayout());
        drawPanel = new JavaDraw2DPanel();
        add(drawPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(9, 2));

        // Controles de grosor
        controlPanel.add(new JLabel("Grosor:"));
        Float[] strokeWidths = {1.0f, 2.0f, 3.0f, 5.0f, 10.0f};
        strokeWidthComboBox = new JComboBox<>(strokeWidths);
        strokeWidthComboBox.addActionListener(this);
        controlPanel.add(strokeWidthComboBox);

        // Controles de color
        controlPanel.add(new JLabel("Color:"));
        String[] colors = {"Negro", "Rojo", "Verde", "Azul", "Amarillo", "Naranja", "Rosa"};
        colorComboBox = new JComboBox<>(colors);
        colorComboBox.addActionListener(this);
        controlPanel.add(colorComboBox);

        // Controles de forma
        controlPanel.add(new JLabel("Forma:"));
        String[] shapes = {"Rectángulo", "Rectángulo Redondeado", "Elipse", "Arco", "Línea", "Curva Cuadrática", "Curva Cúbica", "Polígono"};
        shapeComboBox = new JComboBox<>(shapes);
        shapeComboBox.addActionListener(this);
        controlPanel.add(shapeComboBox);

        // Controles de relleno
        controlPanel.add(new JLabel("Relleno:"));
        String[] fillTypes = {"Sin Relleno", "Sólido", "Degradado", "Fondo Personalizado"};
        fillTypeComboBox = new JComboBox<>(fillTypes);
        fillTypeComboBox.addActionListener(this);
        controlPanel.add(fillTypeComboBox);

        // Botón de selección
        controlPanel.add(new JLabel("Modo Selección:"));
        selectButton = new JButton("Activar Selección");
        selectButton.addActionListener(this);
        controlPanel.add(selectButton);

        // Botones de operaciones booleanas
        controlPanel.add(new JLabel("Operaciones:"));
        JPanel boolPanel = new JPanel(new GridLayout(1, 4));
        unionButton = new JButton("Unión");
        intersectButton = new JButton("Intersección");
        subtractButton = new JButton("Diferencia");
        xorButton = new JButton("XOR");

        unionButton.addActionListener(this);
        intersectButton.addActionListener(this);
        subtractButton.addActionListener(this);
        xorButton.addActionListener(this);

        boolPanel.add(unionButton);
        boolPanel.add(intersectButton);
        boolPanel.add(subtractButton);
        boolPanel.add(xorButton);
        controlPanel.add(boolPanel);

        // Botón limpiar
        controlPanel.add(new JLabel("Limpiar:"));
        clearButton = new JButton("Limpiar Todo");
        clearButton.addActionListener(this);
        controlPanel.add(clearButton);

        // Botón guardar
        controlPanel.add(new JLabel("Guardar:"));
        saveButton = new JButton("Guardar PNG");
        saveButton.addActionListener(this);
        controlPanel.add(saveButton);

        add(controlPanel, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == clearButton) {
            drawPanel.clear();
        } else if (e.getSource() == saveButton) {
            saveToPNG();
        } else if (e.getSource() == selectButton) {
            drawPanel.toggleSelectionMode();
            selectButton.setText(drawPanel.isSelectionMode() ? "Desactivar Selección" : "Activar Selección");
        } else if (e.getSource() == unionButton) {
            drawPanel.applyBooleanOperation("Union");
        } else if (e.getSource() == intersectButton) {
            drawPanel.applyBooleanOperation("Intersection");
        } else if (e.getSource() == subtractButton) {
            drawPanel.applyBooleanOperation("Subtract");
        } else if (e.getSource() == xorButton) {
            drawPanel.applyBooleanOperation("XOR");
        } else {
            updateDrawingSettings();
        }
    }

    private void updateDrawingSettings() {
        drawPanel.setStrokeWidth((Float) strokeWidthComboBox.getSelectedItem());
        drawPanel.setCurrentColor(getColorFromName((String) colorComboBox.getSelectedItem()));
        drawPanel.setShapeType(getShapeTypeFromName((String) shapeComboBox.getSelectedItem()));

        String fillType = (String) fillTypeComboBox.getSelectedItem();
        if ("Fondo Personalizado".equals(fillType)) {
            loadBackgroundTexture();
        }
        drawPanel.setFillType(fillType);
    }

    private Color getColorFromName(String colorName) {
        switch (colorName) {
            case "Negro": return Color.BLACK;
            case "Rojo": return Color.RED;
            case "Verde": return Color.GREEN;
            case "Azul": return Color.BLUE;
            case "Amarillo": return Color.YELLOW;
            case "Naranja": return Color.ORANGE;
            case "Rosa": return Color.PINK;
            default: return Color.BLACK;
        }
    }

    private int getShapeTypeFromName(String shapeName) {
        switch (shapeName) {
            case "Rectángulo": return JavaDraw2DPanel.RECTANGLE;
            case "Rectángulo Redondeado": return JavaDraw2DPanel.ROUNDRECTANGLE2D;
            case "Elipse": return JavaDraw2DPanel.ELLIPSE2D;
            case "Arco": return JavaDraw2DPanel.ARC2D;
            case "Línea": return JavaDraw2DPanel.LINE2D;
            case "Curva Cuadrática": return JavaDraw2DPanel.QUADCURVE2D;
            case "Curva Cúbica": return JavaDraw2DPanel.CUBICCURVE2D;
            case "Polígono": return JavaDraw2DPanel.POLYGON;
            default: return JavaDraw2DPanel.RECTANGLE;
        }
    }

    private void saveToPNG() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".png")) {
                file = new File(file.getParentFile(), file.getName() + ".png");
            }

            try {
                BufferedImage image = new BufferedImage(
                        drawPanel.getWidth(),
                        drawPanel.getHeight(),
                        BufferedImage.TYPE_INT_ARGB
                );
                Graphics2D g2 = image.createGraphics();
                drawPanel.paintComponent(g2);
                g2.dispose();
                ImageIO.write(image, "PNG", file);
                JOptionPane.showMessageDialog(this, "¡Imagen guardada exitosamente!");
            } catch (IOException ex) {
                showError("Error al guardar: " + ex.getMessage());
            }
        }
    }

    private void loadBackgroundTexture() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                BufferedImage image = ImageIO.read(fileChooser.getSelectedFile());
                drawPanel.setBackgroundTexture(image);
            } catch (IOException | IllegalArgumentException ex) {
                showError("Error al cargar textura: " + ex.getMessage());
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Dibujor 2D con Operaciones Booleanas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.add(new DrawShapes());
        frame.setVisible(true);
    }
}

class JavaDraw2DPanel extends JPanel implements MouseListener, MouseMotionListener {
    private Vector<Shape> shapes = new Vector<>();
    private Vector<Color> colors = new Vector<>();
    private Vector<Float> strokeWidths = new Vector<>();
    private Vector<String> fillTypes = new Vector<>();
    private BufferedImage backgroundTexture;
    private Vector<Shape> selectedShapes = new Vector<>();
    private boolean selectionMode = false;

    public static final int RECTANGLE = 0;
    public static final int ROUNDRECTANGLE2D = 1;
    public static final int ELLIPSE2D = 2;
    public static final int ARC2D = 3;
    public static final int LINE2D = 4;
    public static final int QUADCURVE2D = 5;
    public static final int CUBICCURVE2D = 6;
    public static final int POLYGON = 7;

    private int currentShapeType = RECTANGLE;
    private Color currentColor = Color.BLACK;
    private float currentStrokeWidth = 1.0f;
    private String currentFillType = "Sin Relleno";
    private Vector<Point> points = new Vector<>();
    private Shape tempShape;

    public JavaDraw2DPanel() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(800, 600));
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public boolean isSelectionMode() {
        return selectionMode;
    }

    public void toggleSelectionMode() {
        selectionMode = !selectionMode;
        if (!selectionMode) {
            selectedShapes.clear();
        }
        repaint();
    }

    public void applyBooleanOperation(String operation) {
        if (selectedShapes.size() < 2) {
            JOptionPane.showMessageDialog(this, "Selecciona al menos 2 formas", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Area result = new Area(selectedShapes.get(0));
        for (int i = 1; i < selectedShapes.size(); i++) {
            Area next = new Area(selectedShapes.get(i));
            switch (operation) {
                case "Union":
                    result.add(next);
                    break;
                case "Intersection":
                    result.intersect(next);
                    break;
                case "Subtract":
                    result.subtract(next);
                    break;
                case "XOR":
                    result.exclusiveOr(next);
                    break;
            }
        }

        shapes.add(result);
        colors.add(new Color(0, 0, 255, 150)); // Azul semitransparente
        strokeWidths.add(2.0f);
        fillTypes.add("Sólido");
        selectedShapes.clear();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar formas existentes
        for (int i = 0; i < shapes.size(); i++) {
            Shape shape = shapes.get(i);
            g2.setStroke(new BasicStroke(strokeWidths.get(i)));
            g2.setColor(colors.get(i));

            // Resaltar formas seleccionadas
            if (selectedShapes.contains(shape)) {
                g2.setColor(new Color(255, 255, 0, 100)); // Amarillo semitransparente
                g2.fill(shape);
                g2.setColor(colors.get(i));
            }

            String fillType = fillTypes.get(i);
            switch (fillType) {
                case "Sólido":
                    g2.fill(shape);
                    break;
                case "Degradado":
                    Rectangle bounds = shape.getBounds();
                    GradientPaint gp = new GradientPaint(
                            bounds.x, bounds.y, colors.get(i),
                            bounds.x + bounds.width, bounds.y + bounds.height, Color.WHITE);
                    g2.setPaint(gp);
                    g2.fill(shape);
                    break;
                case "Fondo Personalizado":
                    if (backgroundTexture != null) {
                        TexturePaint texture = new TexturePaint(
                                backgroundTexture,
                                new Rectangle(0, 0,
                                        backgroundTexture.getWidth(),
                                        backgroundTexture.getHeight())
                        );
                        g2.setPaint(texture);
                        g2.fill(shape);
                    }
                    break;
            }
            g2.draw(shape);
        }

        // Dibujar forma temporal
        if (tempShape != null) {
            g2.setStroke(new BasicStroke(currentStrokeWidth));
            g2.setColor(currentColor);
            g2.draw(tempShape);
        }
    }

    // ... (Mantener todos los demás métodos existentes sin cambios)
    public void setBackgroundTexture(BufferedImage image) {
        this.backgroundTexture = image;
    }

    public void setCurrentColor(Color color) {
        this.currentColor = color;
    }

    public void setStrokeWidth(float width) {
        this.currentStrokeWidth = width;
    }

    public void setShapeType(int type) {
        this.currentShapeType = type;
        points.clear();
    }

    public void setFillType(String type) {
        this.currentFillType = type;
    }

    public void clear() {
        shapes.clear();
        colors.clear();
        strokeWidths.clear();
        fillTypes.clear();
        selectedShapes.clear();
        backgroundTexture = null;
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (selectionMode) {
            for (int i = shapes.size() - 1; i >= 0; i--) {
                if (shapes.get(i).contains(e.getPoint())) {
                    if (selectedShapes.contains(shapes.get(i))) {
                        selectedShapes.remove(shapes.get(i));
                    } else {
                        selectedShapes.add(shapes.get(i));
                    }
                    repaint();
                    return;
                }
            }
        } else {
            points.add(e.getPoint());
            tempShape = null;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!selectionMode && !points.isEmpty()) {
            updateTempShape(e.getPoint());
            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!selectionMode && !points.isEmpty()) {
            Shape finalShape = createFinalShape(e.getPoint());
            if (finalShape != null) {
                shapes.add(finalShape);
                colors.add(currentColor);
                strokeWidths.add(currentStrokeWidth);
                fillTypes.add(currentFillType);
            }
            points.clear();
            tempShape = null;
            repaint();
        }
    }

    private void updateTempShape(Point endPoint) {
        Point start = points.firstElement();
        switch (currentShapeType) {
            case RECTANGLE:
                tempShape = new Rectangle2D.Double(
                        start.x, start.y,
                        endPoint.x - start.x,
                        endPoint.y - start.y);
                break;
            case ROUNDRECTANGLE2D:
                tempShape = new RoundRectangle2D.Double(
                        start.x, start.y,
                        endPoint.x - start.x,
                        endPoint.y - start.y,
                        20, 20);
                break;
            case ELLIPSE2D:
                tempShape = new Ellipse2D.Double(
                        start.x, start.y,
                        endPoint.x - start.x,
                        endPoint.y - start.y);
                break;
            case ARC2D:
                tempShape = new Arc2D.Double(
                        start.x, start.y,
                        endPoint.x - start.x,
                        endPoint.y - start.y,
                        0, 90, Arc2D.OPEN);
                break;
            case LINE2D:
                tempShape = new Line2D.Double(start, endPoint);
                break;
            case QUADCURVE2D:
                if (points.size() >= 2) {
                    Point ctrl = points.get(1);
                    tempShape = new QuadCurve2D.Double(
                            start.x, start.y,
                            ctrl.x, ctrl.y,
                            endPoint.x, endPoint.y);
                }
                break;
            case CUBICCURVE2D:
                if (points.size() >= 3) {
                    Point ctrl1 = points.get(1);
                    Point ctrl2 = points.get(2);
                    tempShape = new CubicCurve2D.Double(
                            start.x, start.y,
                            ctrl1.x, ctrl1.y,
                            ctrl2.x, ctrl2.y,
                            endPoint.x, endPoint.y);
                }
                break;
            case POLYGON:
                Polygon polygon = new Polygon();
                points.forEach(p -> polygon.addPoint(p.x, p.y));
                polygon.addPoint(endPoint.x, endPoint.y);
                tempShape = polygon;
                break;
        }
    }

    private Shape createFinalShape(Point endPoint) {
        return tempShape;
    }

    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
}