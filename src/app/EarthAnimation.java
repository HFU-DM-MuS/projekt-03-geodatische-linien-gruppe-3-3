package app;

import javax.swing.*;
import java.awt.*;


import utils.ApplicationTime;


public class EarthAnimation extends JPanel {

    private final ApplicationTime t;
    private final int w = Constants.WINDOW_WIDTH;
    private final int h = Constants.WINDOW_HEIGHT;
    private Matrix projectionMatrix;

    private Graphics g;
    private Graphics2D g2d;

    private double phi_p;
    private double theta_p;


    public EarthAnimation(ApplicationTime thread) {
        this.t = thread;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        this.g = g;
        g2d = (Graphics2D) g;

        ProjectionOnImagePlane();
        paintCoordinateSystem();
        paintGlobeWireframe();
        paintOutlineEllipse();

    }

    private void ProjectionOnImagePlane() {
        //  Diese Matrix enthält Werte, die für die Projektion einer 3D-Szene auf eine 2D-Fläche verwendet wird.
        projectionMatrix = new Matrix(new double[][]{
                {-Constants.PROJECTION_S1 * Math.sin(Math.toRadians(Constants.PROJECTION_ALPHA)), 1.0, 0.0, Constants.WINDOW_WIDTH / 2.},
                {-Constants.PROJECTION_S1 * Math.cos(Math.toRadians(Constants.PROJECTION_ALPHA)), 0.0, -1.0, Constants.WINDOW_HEIGHT / 2.}
        });
        // Die Variablen phi_p und theta_p werden berechnet und enthalten Winkelwerte, die für die Berechnung der Perspektive der 3D-Szene verwendet wird.
        phi_p = Math.toDegrees(Math.atan(Constants.PROJECTION_S1 * Math.sin(Math.toRadians(Constants.PROJECTION_ALPHA))));

        theta_p = Math.toDegrees(Math.atan(-Constants.PROJECTION_S1 * Math.cos(Math.toRadians(Constants.PROJECTION_ALPHA)) *
                                Math.cos(Math.atan(Constants.PROJECTION_S1 * Math.sin(Math.toRadians(Constants.PROJECTION_ALPHA))))
                )
        );
    }
    //Koordinatensystem wird gezeichnet
    private void paintCoordinateSystem() {

        g.setColor(Color.WHITE);
        g.drawLine(0, h / 2, w, h / 2);
        g.drawLine(w / 2, 0, w / 2, h);

        // axis labels
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString("x-axis", w - 50, h / 2 + 10);
        g.drawString("y-axis", w / 2 + 10, 10);

    }

    //Wireframe wird gezeichnet
    //Für jede Breiten- und Längengrad-Iteration werden die Koordinaten des entsprechenden Punkts auf dem Globus in einen 3D-Vektor umgewandelt,
    // um ihn für die Projektion auf die 2D-Ebene zu verwenden.
    private void paintGlobeWireframe() {
        try {
            double[] lats = {-90.0, -60.0, -30.0, 0.0, 30.0, 60.0, 90.0};
            double[] longs = {0.0, 30.0, 60.0, 90.0, 120.0, 150.0, 180.0, 210.0, 240.0, 270.0, 300.0, 330.0};

            // horizontal lines
            for (double latitude : lats) {
                int lastX = -1;
                int lastY = -1;
                for (int longitude = 0; longitude < 360; longitude ++) {
                    Coordinates c = new Coordinates(latitude, longitude);
                    Vector cv = c.toCartesian();

                    // Rotation um Z-Achse
                    cv.rotateWorldZ(Constants.GLOBE_ROTATION);

                    //Vektor wird auf dem 2D-Bildschirmraum projektziert.
                    Vector sv = projectionMatrix.doScreenProjection(cv);


                    //Prüfung aufgrund Positon sichtbar wenn ja Weiß nein Dunkelgrau
                    if (isVisible(cv)) {
                        g.setColor(Color.WHITE);
                    } else {
                        g.setColor(Color.darkGray);
                    }

                    if (lastX != -1 && lastY != -1) {
                        g.drawLine(lastX, lastY, (int) sv.x(), (int) sv.y());
                    }

                    lastX = (int) sv.x();
                    lastY = (int) sv.y();
                }
            }

            // vertical lines
            for (double longitude : longs) {
                int lastX = -1;
                int lastY = -1;
                for (int latitude = -90; latitude < 90; latitude++) {
                    Coordinates c = new Coordinates(latitude, longitude);
                    Vector cv = c.toCartesian();

                    // Rotation um Z-Achse
                    cv.rotateWorldZ(Constants.GLOBE_ROTATION);

                    Vector sv = projectionMatrix.doScreenProjection(cv);

                    if (isVisible(cv)) {
                        g.setColor(Color.WHITE);
                    } else {
                        g.setColor(Color.darkGray);
                    }

                    if (lastX != -1 && lastY != -1) {
                        g.drawLine(lastX, lastY, (int) sv.x(), (int) sv.y());
                    }

                    lastX = (int) sv.x();
                    lastY = (int) sv.y();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
    Drahtgittermodell mit Umrissellipse
    Vector [0 | r * cos(0) | r * sin(0)]
    (Schritt 1)Vector die Drehung(en) D(z^, phi)◦ D(y^, −theta) auf den Großkreis anwenden, der in der x2-x3-Ebene
    liegt (denn für theta = 0 und phi = 0 ist die x1-Achse die Projektionsachse, diese steht senkrecht auf dem genannten Großkreis),
    (Schritt 2) die resultierende Kurve mit der Projektionsmatrix in die x2-x3-Ebene projizieren.
     */


    public void paintOutlineEllipse() {
        try {
            Vector startVec = new Vector(0.0, Math.cos(Math.toRadians(0)), Math.sin(Math.toRadians(0)));
            startVec.rotateWorldY(-theta_p).rotateWorldZ(phi_p).scale(Constants.GLOBE_SCALE);

            g.setColor(Color.BLUE);
            g2d.setStroke(new BasicStroke(2.0f));


            for (int t = 1; t <= 360; t++) {
                Vector rVec = new Vector(0.0, Math.cos(Math.toRadians(t)), Math.sin(Math.toRadians(t)));
                rVec.rotateWorldY(-theta_p).rotateWorldZ(phi_p).scale(Constants.GLOBE_SCALE);

                Vector oldVec = projectionMatrix.doScreenProjection(rVec);
                Vector newVec = projectionMatrix.doScreenProjection(startVec);

                g.drawLine((int) newVec.x(), (int) newVec.y(), (int) oldVec.x(), (int) oldVec.y());

                startVec = rVec;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Fenstergröße
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
    }

    //Die Berechnung durch die Skalarprodukte der 3 Vektoren
    // Wenn Skalarprodukt der 3 V Positiv dann befindet sich der Punkt vor der projizierten Oberfläche der Kugel und ist sichtbar
    // Wenn Negativ dann hinter der Oberfläche der Kugel und nicht Sichtbar
    // Die Methode verwendet die aktuellen Werte von phi_p und theta_p
    //phi_p --> Breitengrad, theta_p --> Längengrad
    private boolean isVisible(Vector point) {
        return (
                        Math.cos(Math.toRadians(phi_p)) * Math.cos(Math.toRadians(theta_p)) * point.x() +
                        Math.sin(Math.toRadians(phi_p)) * Math.cos(Math.toRadians(theta_p)) * point.y() +
                        Math.sin(Math.toRadians(theta_p)) * point.z()
        ) >= 0.0;
    }









}
