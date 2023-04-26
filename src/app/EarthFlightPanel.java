package app;

import utils.ApplicationTime;

import utils.FrameUpdater;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.regex.*;

public class EarthFlightPanel {



    public EarthFlightPanel() {
        startAnimation();
    }

    // Diese Methode startet die Animation, indem sie einen neuen Thread für die Anwendungszeit erstellt,
    // Frame-Updater erstellt und einen Timer startet, um die Aktualisierung der Frames zu planen.
    public void startAnimation() {
        ApplicationTime applicationTimeThread = new ApplicationTime();
        applicationTimeThread.start();
        FrameUpdater frameUpdater = new FrameUpdater(createFrames(applicationTimeThread));
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(frameUpdater, 100, (int) Constants.TPF);
    }

    // Diese Methode erstellt eine Liste von Frames, die in der Animation angezeigt werden sollen.
    // Die Frames werden erstellt, indem jeweils ein GUI-Frame und ein Grafik-Frame erzeugt werden.
    // Die Methode gibt die Liste der erstellten Frames zurück.
    private ArrayList<JFrame> createFrames(ApplicationTime applicationTimeThread) {
        ArrayList<JFrame> frames = new ArrayList<>();
        frames.add(createGUIFrame(applicationTimeThread));
        frames.add(createGraphicsFrame(applicationTimeThread));

        return frames;
    }



    //Window Frame
    private JFrame createGUIFrame(ApplicationTime thread){

        //Main Frame
        JFrame frame = new JFrame("Control-Window");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);

        //Create JPanel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(9, 1));
        panel.setPreferredSize(new Dimension(400, 400));

        //First Label
        JPanel inputPanel = new JPanel();
        JLabel labelInput = new JLabel("Input Coordinates");
        inputPanel.add(labelInput);


        // First Coordinate
        JPanel startCoor = new JPanel();
        startCoor.setLayout(new BorderLayout(0, 0));
        startCoor.setBorder(new EmptyBorder(10, 10, 0, 10));

        JLabel startCoorLabel = new JLabel("Start Coordinaten:   ");
        JTextField startCoorTextField = new JTextField();

        startCoorTextField.setText(" 48.0594021, 8.4640869");
        startCoorTextField.setHorizontalAlignment(JTextField.CENTER);

        startCoor.add(startCoorLabel, BorderLayout.LINE_START);
        startCoor.add(startCoorTextField);

        // Second Coordinate
        JPanel endCoor = new JPanel();
        endCoor.setLayout(new BorderLayout(0, 0));
        endCoor.setBorder(new EmptyBorder(10, 10, 0, 10));

        JLabel endCoorLabel = new JLabel("End Coordinaten:    ");
        JTextField endCoorTextField = new JTextField();

        endCoorTextField.setText("-48.4875236, 8.6459531");
        endCoorTextField.setHorizontalAlignment(JTextField.CENTER);

        endCoor.add(endCoorLabel, BorderLayout.LINE_START);
        endCoor.add(endCoorTextField);

        //Button
        JPanel startButton = new JPanel();
        startButton.setLayout(new BorderLayout(10, 0));
        startButton.setBorder(new EmptyBorder(10, 10, 25, 10));

        JButton runButton = new JButton("Start Flight");
        runButton.addActionListener(e -> {
            if(!Constants.COORD_SHOW_ON_GLOBE){
                String regex = "^(-?\\d+\\.\\d*),\\s?(-?\\d+\\.\\d*)$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcherStart = pattern.matcher(startCoorTextField.getText().trim());
                Matcher matcherEnd = pattern.matcher(endCoorTextField.getText().trim());

                if(matcherStart.find() && matcherEnd.find()){
                    Constants.COORD_START_LAT = Double.parseDouble(matcherStart.group(1));
                    Constants.COORD_START_LON = Double.parseDouble(matcherStart.group(2));

                    Constants.COORD_END_LAT = Double.parseDouble(matcherEnd.group(1));
                    Constants.COORD_END_LON = Double.parseDouble(matcherEnd.group(2));

                } else {
                    System.err.printf("Coordinates must fit pattern %s", regex);

                }
            }

            runButton.setText(Constants.COORD_SHOW_ON_GLOBE ? "Stop Flight" : "Start Flight");
        });

        startButton.add(runButton, BorderLayout.NORTH);

        //Second Label
        JPanel movementPanel = new JPanel();
        movementPanel.setBorder(new MatteBorder(3, 0, 0, 0, Color.BLACK));
        JLabel movementLabel = new JLabel("Movement control");
        movementPanel.add(movementLabel);


        //Alpha Slider
        JPanel alphaSlider = new JPanel();
        alphaSlider.setLayout(new BorderLayout(10, 0));
        alphaSlider.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel AlphaLabel = new JLabel("Axonometric α:");
        JLabel AlphaValue = new JLabel(String.format("%d°", (int) GuiParameters.PROJECTION_ALPHA));
        JSlider AlphaSlider = new JSlider(0, 360, (int) GuiParameters.PROJECTION_ALPHA);
        AlphaSlider.addChangeListener(e -> {
            GuiParameters.PROJECTION_ALPHA = (double)AlphaSlider.getValue();
            AlphaValue.setText(String.format("%d", (int) GuiParameters.PROJECTION_ALPHA));
        });

        alphaSlider.add(AlphaLabel, BorderLayout.LINE_START);
        alphaSlider.add(AlphaSlider, BorderLayout.CENTER);
        alphaSlider.add(AlphaValue, BorderLayout.LINE_END);



        //s1 Slider
        JPanel s1Slider = new JPanel();
        s1Slider.setLayout(new BorderLayout(10, 0));
        s1Slider.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel S1Label = new JLabel("Axonometric s1");
        JLabel S1Value = new JLabel(String.format("%.2f", GuiParameters.PROJECTION_S1));
        JSlider S1Slider = new JSlider(0, 100, (int)(GuiParameters.PROJECTION_S1 * 100));
        S1Slider.addChangeListener(e -> {
            GuiParameters.PROJECTION_S1 = (double)S1Slider.getValue() / 100.0;
            S1Value.setText(String.format("%.2f", GuiParameters.PROJECTION_S1));
        });

        s1Slider.add(S1Label, BorderLayout.LINE_START);
        s1Slider.add(S1Slider, BorderLayout.CENTER);
        s1Slider.add(S1Value, BorderLayout.LINE_END);

        //Rotation Slider
        JPanel rotationSlider = new JPanel();
        rotationSlider.setLayout(new BorderLayout(10, 0));
        rotationSlider.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel RotationLabel = new JLabel("Rotation");
        JLabel RotationValue = new JLabel(String.format("%d°", (int) GuiParameters.PROJECTION_S1));
        JSlider RotationSlider = new JSlider(0, 360, (int)Constants.GLOBE_ROTATION);
        RotationSlider.addChangeListener(e -> {
            Constants.GLOBE_ROTATION = (double)RotationSlider.getValue();
            RotationValue.setText(String.format("%d", (int)Constants.GLOBE_ROTATION));
        });

        rotationSlider.add(RotationLabel, BorderLayout.LINE_START);
        rotationSlider.add(RotationSlider, BorderLayout.CENTER);
        rotationSlider.add(RotationValue, BorderLayout.LINE_END);





        //Add all in Window
        panel.add(inputPanel);
        panel.add(startCoor);
        panel.add(endCoor);
        panel.add(startButton);
        panel.add(movementPanel);
        panel.add(alphaSlider);
        panel.add(s1Slider);
        panel.add(rotationSlider);

        frame.add(panel);
        frame.setLocation(Constants.WINDOW_WIDTH, 250);
        frame.pack();
        frame.setVisible(true);

        return frame;
    }

    //WireFrame
    private JFrame createGraphicsFrame(ApplicationTime thread) {
        // Create main frame (window)
        JFrame frame = new JFrame("Projekt 3.3 ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new EarthAnimation(thread);
        panel.setBackground(Color.BLACK);

        frame.add(panel);
        frame.setLocation(1, 1);
        frame.pack();
        frame.setVisible(true);

        return frame;
    }

}


