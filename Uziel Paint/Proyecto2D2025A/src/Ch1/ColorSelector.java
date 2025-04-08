package Ch1;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class ColorSelector extends JDialog {
    private Color selectedColor = Color.BLACK;
    private JColorChooser colorChooser;
    private JSlider hueSlider;
    private JSlider saturationSlider;
    private JSlider brightnessSlider;
    private JPanel previewPanel;
    private JTextField hexField;
    private JSpinner redSpinner;
    private JSpinner greenSpinner;
    private JSpinner blueSpinner;

    public ColorSelector(Frame parent) {
        super(parent, "Selector Avanzado de Color", true);
        setSize(600, 500);
        setLayout(new BorderLayout());

        // Panel principal con pestañas
        JTabbedPane tabbedPane = new JTabbedPane();

        // Pestaña 1: Selector estándar
        colorChooser = new JColorChooser();
        tabbedPane.addTab("Paleta", colorChooser);

        // Pestaña 2: Selector HSB
        tabbedPane.addTab("HSB", createHSBPanel());

        // Pestaña 3: Selector RGB
        tabbedPane.addTab("RGB", createRGBPanel());

        add(tabbedPane, BorderLayout.CENTER);

        // Panel inferior con previsualización y botones
        JPanel bottomPanel = new JPanel(new BorderLayout());

        previewPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(selectedColor);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        previewPanel.setPreferredSize(new Dimension(100, 50));
        previewPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        hexField = new JTextField(7);
        hexField.setEditable(false);

        JPanel previewContainer = new JPanel();
        previewContainer.add(new JLabel("Color seleccionado:"));
        previewContainer.add(previewPanel);
        previewContainer.add(new JLabel("HEX:"));
        previewContainer.add(hexField);

        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("Aceptar");
        okButton.addActionListener(e -> {
            selectedColor = colorChooser.getColor();
            dispose();
        });

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        bottomPanel.add(previewContainer, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        // Listeners para sincronización
        colorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                selectedColor = colorChooser.getColor();
                updateSlidersFromColor();
                updatePreview();
            }
        });
    }

    private JPanel createHSBPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1));

        hueSlider = new JSlider(0, 360, 0);
        saturationSlider = new JSlider(0, 100, 100);
        brightnessSlider = new JSlider(0, 100, 100);

        panel.add(createSliderPanel("Matiz (H):", hueSlider));
        panel.add(createSliderPanel("Saturación (S):", saturationSlider));
        panel.add(createSliderPanel("Brillo (B):", brightnessSlider));

        // Listener para los sliders HSB
        ChangeListener hsbListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                float h = hueSlider.getValue() / 360f;
                float s = saturationSlider.getValue() / 100f;
                float b = brightnessSlider.getValue() / 100f;
                selectedColor = Color.getHSBColor(h, s, b);
                colorChooser.setColor(selectedColor);
                updatePreview();
            }
        };

        hueSlider.addChangeListener(hsbListener);
        saturationSlider.addChangeListener(hsbListener);
        brightnessSlider.addChangeListener(hsbListener);

        return panel;
    }

    private JPanel createRGBPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2));

        SpinnerNumberModel redModel = new SpinnerNumberModel(0, 0, 255, 1);
        SpinnerNumberModel greenModel = new SpinnerNumberModel(0, 0, 255, 1);
        SpinnerNumberModel blueModel = new SpinnerNumberModel(0, 0, 255, 1);

        redSpinner = new JSpinner(redModel);
        greenSpinner = new JSpinner(greenModel);
        blueSpinner = new JSpinner(blueModel);

        panel.add(new JLabel("Rojo (R):"));
        panel.add(redSpinner);
        panel.add(new JLabel("Verde (G):"));
        panel.add(greenSpinner);
        panel.add(new JLabel("Azul (B):"));
        panel.add(blueSpinner);

        // Listener para los spinners RGB
        ChangeListener rgbListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int r = (Integer)redSpinner.getValue();
                int g = (Integer)greenSpinner.getValue();
                int b = (Integer)blueSpinner.getValue();
                selectedColor = new Color(r, g, b);
                colorChooser.setColor(selectedColor);
                updatePreview();
            }
        };

        redSpinner.addChangeListener(rgbListener);
        greenSpinner.addChangeListener(rgbListener);
        blueSpinner.addChangeListener(rgbListener);

        return panel;
    }

    private JPanel createSliderPanel(String label, JSlider slider) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(label), BorderLayout.WEST);
        panel.add(slider, BorderLayout.CENTER);

        JLabel valueLabel = new JLabel(String.valueOf(slider.getValue()));
        valueLabel.setPreferredSize(new Dimension(40, 20));

        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                valueLabel.setText(String.valueOf(slider.getValue()));
            }
        });

        panel.add(valueLabel, BorderLayout.EAST);
        return panel;
    }

    private void updateSlidersFromColor() {
        float[] hsb = Color.RGBtoHSB(
                selectedColor.getRed(),
                selectedColor.getGreen(),
                selectedColor.getBlue(),
                null
        );

        hueSlider.setValue((int)(hsb[0] * 360));
        saturationSlider.setValue((int)(hsb[1] * 100));
        brightnessSlider.setValue((int)(hsb[2] * 100));

        redSpinner.setValue(selectedColor.getRed());
        greenSpinner.setValue(selectedColor.getGreen());
        blueSpinner.setValue(selectedColor.getBlue());
    }

    private void updatePreview() {
        previewPanel.repaint();
        hexField.setText(String.format("#%02x%02x%02x",
                selectedColor.getRed(),
                selectedColor.getGreen(),
                selectedColor.getBlue()
        ));
    }

    public Color getSelectedColor() {
        return selectedColor;
    }
}