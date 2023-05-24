package app;

import utils.ApplicationTime;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class AnimationWindowPanel extends Animation {
    static JButton start = new JButton("Start Flight");
    static JLabel distanceP1P2 = new JLabel();

    private static void createControlFrame(ApplicationTime thread) {
        // Erstellen eines neuen Frames
        JFrame controlFrame = new JFrame("Mathematik und Simulation");
        controlFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Erstellen eines Panels für die Steuerelemente
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        controlFrame.add(panel);
        controlFrame.setVisible(true);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);

        // Label für die Eingabeüberschrift
        JLabel labelInput = new JLabel("Input Coordinates");
        labelInput.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(labelInput, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 10, 10);

        // Eingabefeld für Start Latitude
        JLabel pos1X = new JLabel("Start Latitude:");
        panel.add(pos1X, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3; // Eingabefeld über 3 Spalten erweitern

        JTextField pos1XInput = new JTextField("48.050144");
        pos1XInput.setHorizontalAlignment(JTextField.CENTER);
        panel.add(pos1XInput, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;

        // Eingabefeld für Start Longitude
        JLabel pos1Y = new JLabel("Start Longitude:");
        panel.add(pos1Y, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3; // Eingabefeld über 3 Spalten erweitern

        JTextField pos1YInput = new JTextField("8.201419");
        pos1YInput.setHorizontalAlignment(JTextField.CENTER);
        panel.add(pos1YInput, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;

        // Eingabefeld für End Latitude
        JLabel pos2Y = new JLabel("End Latitude:");
        panel.add(pos2Y, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3; // Eingabefeld über 3 Spalten erweitern

        JTextField pos2YInput = new JTextField("52.520007");
        pos2YInput.setHorizontalAlignment(JTextField.CENTER);
        panel.add(pos2YInput, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;

        // Eingabefeld für End Longitude
        JLabel pos2X = new JLabel("End Longitude:");
        panel.add(pos2X, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3; // Eingabefeld über 3 Spalten erweitern

        JTextField pos2XInput = new JTextField("13.404954");
        pos2XInput.setHorizontalAlignment(JTextField.CENTER);
        panel.add(pos2XInput, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(20, 0, 20, 0);

        // Anweisungen und Start-Button
        gbc.fill = GridBagConstraints.CENTER;
        panel.add(start, gbc);

        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 20, 0);

        // Label für die Distanz
        JLabel distance = new JLabel("Distance is:");
        distance.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(distance, gbc);

        panel.add(new JLabel());
        gbc.gridy = 6;
        gbc.gridx = 1;

        distanceP1P2.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(distanceP1P2, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, 10, 10);

        // Label für die Bewegungssteuerung
        JLabel movementLabel = new JLabel("Movement control");
        movementLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(movementLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;

        // Slider zur Anpassung von Alpha
        JLabel adjustAlpha = new JLabel("α");
        panel.add(adjustAlpha, gbc);

        gbc.gridx = 1;

        JSlider adjustAlphaSlider = new JSlider(0, 360, (int) Math.toDegrees(EarthAnimation.alpha));
        adjustAlphaSlider.addChangeListener(e -> {
            EarthAnimation.setAlpha(adjustAlphaSlider.getValue());
        });
        panel.add(adjustAlphaSlider, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;

        // Slider zur Anpassung von S1
        JLabel adjustS1 = new JLabel("S1");
        panel.add(adjustS1, gbc);

        gbc.gridx = 1;

        JSlider adjustS1Slider = new JSlider(0, 100, (int) (EarthAnimation.s1ScaleFactor * 100));
        adjustS1Slider.addChangeListener(e -> {
            EarthAnimation.s1ScaleFactor = (double) adjustS1Slider.getValue() /100;
        });
        panel.add(adjustS1Slider, gbc);

        controlFrame.pack();

        // Wenn "Start" gedrückt wird, die Werte aus den Textfeldern abrufen
        start.addActionListener(e -> {
            double pos1XDouble = Double.parseDouble(pos1XInput.getText());
            double pos1YDouble = Double.parseDouble(pos1YInput.getText());
            double pos2XDouble = Double.parseDouble(pos2XInput.getText());
            double pos2YDouble = Double.parseDouble(pos2YInput.getText());
            EarthAnimation.setPos1XY(pos1XDouble, pos1YDouble);
            EarthAnimation.setPos2XY(pos2XDouble, pos2YDouble);
            EarthAnimation.startAnimation();
        });
    }

    @Override
    protected ArrayList<JFrame> createFrames(ApplicationTime applicationTimeThread) {
        // Eine Liste aller Frames (Fenster), die angezeigt werden sollen
        ArrayList<JFrame> frames = new ArrayList<>();

        // Hauptframe (Fenster) erstellen
        JFrame frame = new JFrame("Mathematik und Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new EarthAnimation(applicationTimeThread);
        frame.add(panel);
        frame.pack(); // Größe des JFrame an die Größe seiner Komponenten anpassen
        frame.setVisible(true);

        frames.add(frame);
        createControlFrame(applicationTimeThread);
        return frames;
    }
}