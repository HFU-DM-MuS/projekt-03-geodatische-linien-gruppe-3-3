package app;
import java.awt.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.*;
import utils.ApplicationTime;

class EarthAnimation extends JPanel {
    public static void setAlpha(double a) {alpha = Math.toRadians(a);}
    public static void setPos1XY(double X, double Y) {
        Pos1X = Math.toRadians(X);
        Pos1Y = Math.toRadians(Y);
    }
    public static void setPos2XY(double X, double Y) {
        Pos2X = Math.toRadians(X);
        Pos2Y = Math.toRadians(Y);
    }
    public static void startAnimation() {tDelta = 0;}

    private final ApplicationTime t;
    public EarthAnimation(ApplicationTime thread) {
        this.t = thread;
    }
    // set this panel's preferred size for auto-sizing the container JFrame
    public Dimension getPreferredSize() {
        return new Dimension(_0_Constants.WINDOW_WIDTH, _0_Constants.WINDOW_HEIGHT);
    }


    double phi_p ;


    double theta_p ;
    public int w = _0_Constants.WINDOW_WIDTH / 2;
    public int h = _0_Constants.WINDOW_HEIGHT / 2;
    public static double alpha = Math.toRadians(1);
    public static double Pos1X;
    public static double Pos1Y;
    public static double Pos2X;
    public static double Pos2Y;
    public static double tDelta = 0;
    public static double distance = 0;
    public static double scaleFactor = 200.0;
    public static double s1ScaleFactor = 1 / Math.sqrt(2);


    @Override
    protected void paintComponent(Graphics g){




        super.paintComponent(g);
        Graphics2D g2d;
        g2d = (Graphics2D) g;

        phi_p = Math.toDegrees(
                Math.atan(
                        s1ScaleFactor *
                                Math.sin(Math.toRadians(alpha))
                )
        );

        theta_p = Math.toDegrees(
                Math.atan(
                        -s1ScaleFactor *
                                Math.cos(Math.toRadians(alpha)) *
                                Math.cos(
                                        Math.atan(
                                                s1ScaleFactor *
                                                        Math.sin(Math.toRadians(alpha))
                                        )
                                )
                )
        );


        double[][] projectionMatrix = {
                {-s1ScaleFactor * Math.sin(alpha), 1.0, 0.0, w},
                {-s1ScaleFactor * Math.cos(alpha), 0.0, -1.0, h}
        };

        double[] e1H = {1.0, 0.0, 0.0 };
        double[] e2H = {0.0, 1.0, 0.0};
        double[] e3H = {0.0, 0.0, 1.0};
        double[] originH = {0.0, 0.0, 0.0, 1.0};

        double [] e1Hscale = {e1H[0]*scaleFactor,e1H[1]*scaleFactor,e1H[2]*scaleFactor};
        double [] e2Hscale = {e2H[0]*scaleFactor,e2H[1]*scaleFactor,e2H[2]*scaleFactor};
        double [] e3Hscale = {e3H[0]*scaleFactor,e3H[1]*scaleFactor,e3H[2]*scaleFactor};

        double [] e1Hhomoge = {e1Hscale[0],e1Hscale[1],e1Hscale[2],1};
        double [] e2Hhomoge = {e2Hscale[0],e2Hscale[1],e2Hscale[2],1};
        double [] e3Hhomoge = {e3Hscale[0],e3Hscale[1],e3Hscale[2],1};


        double[] xAxisEndPoint = Maths.multMatVec(projectionMatrix, e1Hhomoge);
        double[] yAxisEndPoint = Maths.multMatVec(projectionMatrix, e2Hhomoge);
        double[] zAxisEndPoint = Maths.multMatVec(projectionMatrix, e3Hhomoge);

        super.paintComponent(g);


        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());


        g2d.setStroke(new BasicStroke(3.0f));

        g.setColor(Color.GREEN);
        g.drawLine(w, h, (int) yAxisEndPoint[0], (int) yAxisEndPoint[1]);

        g.setColor(Color.BLUE);
        g.drawLine(w, h, (int) zAxisEndPoint[0], (int) zAxisEndPoint[1]);

        g.setColor(Color.RED);
        g.drawLine(w, h, (int) xAxisEndPoint[0], (int) xAxisEndPoint[1]);



        int step =2;
        double[] lats = {-30.0, -60.0, 0.0, 30.0, 60.0};
        double[] longs = {0.0, 45.0, 90.0, 135.0, 180.0, 225.0, 270.0, 315.0};

        for (double latitude : lats) {
            for (double longitude = 0; longitude < 360; longitude += step){

                double [] cartesienVector = {
                        scaleFactor* Math.cos(Math.toRadians(latitude))* Math.cos(Math.toRadians(longitude)),
                        scaleFactor* Math.cos(Math.toRadians(latitude)) * Math.sin(Math.toRadians(longitude)),
                        scaleFactor*Math.sin(Math.toRadians(latitude))};

                double [] homogenV = {cartesienVector[0], cartesienVector[1], cartesienVector[2],1};

                double [] vector = Maths.multMatVec(projectionMatrix,homogenV);

                int dotSize =1;

                if(Math.cos(Math.toRadians(phi_p)) * Math.cos(Math.toRadians(theta_p)) * cartesienVector[0] +
                        Math.sin(Math.toRadians(phi_p)) * Math.cos(Math.toRadians(theta_p)) * cartesienVector[1] +
                        Math.sin(Math.toRadians(theta_p)) * cartesienVector[2] >= 0){

                    g.setColor(Color.white);
                    dotSize = 2;

                }else {
                    g.setColor(Color.darkGray);
                }

                g.fillRect((int) vector[0],(int) vector[1], dotSize,dotSize);

            }
        }

        for (double longitude : longs){
            for (int latitude = -90; latitude < 90; latitude += step){



                double [] cartesienVector = {scaleFactor* Math.cos(Math.toRadians(latitude))* Math.cos(Math.toRadians(longitude)),
                        scaleFactor* Math.cos(Math.toRadians(latitude)) * Math.sin(Math.toRadians(longitude)),
                        scaleFactor*Math.sin(Math.toRadians(latitude))};

                double [] homogenV = {cartesienVector[0], cartesienVector[1], cartesienVector[2],1};

                double [] vector = Maths.multMatVec(projectionMatrix,homogenV);

                int dotSize = 1;
                if(Math.cos(Math.toRadians(phi_p)) * Math.cos(Math.toRadians(theta_p)) * cartesienVector[0] +
                        Math.sin(Math.toRadians(phi_p)) * Math.cos(Math.toRadians(theta_p)) * cartesienVector[1] +
                        Math.sin(Math.toRadians(theta_p)) * cartesienVector[2] >= 0){


                    g.setColor(Color.white);
                    dotSize =2;

                }else {
                    g.setColor(Color.lightGray);
                }

                g.fillRect((int) vector[0], (int) vector[1], dotSize,dotSize);
            }
        }

        double [] startVec = {0.0, Math.cos(Math.toRadians(0)), Math.sin(Math.toRadians(0))};
        double [] [] rotateMatrixY = {
                {Math.cos(-theta_p), 0.0, Math.sin(-theta_p)},
                {0.0, 1.0, 0.0},
                {-Math.sin(-theta_p), 0.0, Math.cos(-theta_p)}
        };
        double [] [] rotateMatrixZ = {
                {Math.cos(phi_p), -Math.sin(phi_p), 0.0},
                {Math.sin(phi_p), Math.cos(phi_p), 0.0},
                {0.0, 0.0, 1.0}
        };
        //double [] rotatevetor = multMatVec(rotateMatrixY,startVec);
        //double [] rotatevetor2= multMatVec(rotateMatrixZ,rotatevetor);
        double [] scalevec = {startVec[0]*=scaleFactor, startVec[1]*=scaleFactor, startVec[2]*=scaleFactor};

        g.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for(int t = step; t<= 360; t += step){
            double [] startVec2 = {0.0, Math.cos(Math.toRadians(t)), Math.sin(Math.toRadians(t))};
            // double [] rotatevetor3 = multMatVec(rotateMatrixY,startVec2);
            //double [] rotatevetor4= multMatVec(rotateMatrixZ,rotatevetor3);
            double [] scalevec2 = {startVec2[0]*=scaleFactor, startVec2[1]*=scaleFactor, startVec2[2]*=scaleFactor};

            double [] homogenvec = {scalevec2[0], scalevec2[1], scalevec2[2], 1};
            double [] homogenvecstart = {scalevec[0], scalevec[1], scalevec[2], 1};

            double[] projektvec2 = Maths.multMatVec(projectionMatrix, homogenvec);
            double[] projektvec1 = Maths.multMatVec(projectionMatrix, homogenvecstart);

            g.drawLine((int) projektvec1[0],(int) projektvec1[1], (int) projektvec2[0], (int) projektvec2[1]);

            scalevec = scalevec2;
        }




        double pos1Horizontal_angle = Pos1X;
        double pos1Vertical_angle = Pos1Y;


        double xPos1 =Math.cos(pos1Vertical_angle) * Math.cos(pos1Horizontal_angle);
        double yPos1 =Math.cos(pos1Vertical_angle) * Math.sin(pos1Horizontal_angle);
        double zPos1 = Math.sin(pos1Vertical_angle);


        double rPos1= Math.sqrt(Math.pow(xPos1,2)+Math.pow(yPos1,2) + Math.pow(zPos1,2));


        double[] pos1Cartesian = {
                rPos1*xPos1,
                rPos1*yPos1,
                rPos1*zPos1,
                1
        };


        double[] pos1 = Maths.multMatVec(projectionMatrix,pos1Cartesian);

        g2d.drawOval((int) pos1[0] , (int) pos1[1] , 5, 5);


        double pos2Horizontal_angle = Pos2X;
        double pos2Vertical_angle = Pos2Y;

        double xPos2 =Math.cos(pos2Vertical_angle) * Math.cos(pos2Horizontal_angle);
        double yPos2 =Math.cos(pos2Vertical_angle) * Math.sin(pos2Horizontal_angle);
        double zPos2 = Math.sin(pos2Vertical_angle);

        double rPos2= Math.sqrt(Math.pow(xPos2,2)+Math.pow(yPos2,2) + Math.pow(zPos2,2));

        double[] pos2Cartesian = {
                rPos2 * xPos2,
                rPos2 * yPos2,
                rPos2 * zPos2,
                1
        };

        double[] pos2 = Maths.multMatVec(projectionMatrix,pos2Cartesian);
        g2d.drawOval((int) pos2[0] , (int) pos2[1] , 5, 5);



        double[] vectorO_Pos1 = Maths.subtractVectors(pos1Cartesian,originH);
        double[] vectorO_Pos2 = Maths.subtractVectors(pos2Cartesian,originH);
        double delta = Math.acos(Maths.cosineOfAngleBetweenVectors(vectorO_Pos1,vectorO_Pos2)) ;

        int earthRadius = 6371; //km
        distance = Math.acos(Math.sin(Pos1X)*Math.sin(Pos2X)+Math.cos(Pos1X)*Math.cos(Pos2X)*Math.cos(Pos2Y-Pos1Y))*earthRadius;
        AnimationWindowPanel.distanceP1P2.setText(String.valueOf((int) distance) + "Km");

        g2d.setStroke(new BasicStroke(1.0f));


        double[] unitVectorP =  Maths.normalizeVector(vectorO_Pos1);
        double[] unitVectorN = Maths.divVecWithNumber(Maths.crossProduct(vectorO_Pos1, vectorO_Pos2),Maths.crossProductMagnitude(vectorO_Pos1,vectorO_Pos2));
        double[] unitVectorU = Maths.divVecWithNumber(Maths.crossProduct(unitVectorN, unitVectorP),Maths.crossProductMagnitude(unitVectorN,unitVectorP));

        double[][] transMatrixD = {
                {unitVectorP[0],unitVectorU[0],unitVectorN[0]},
                {unitVectorP[1],unitVectorU[1],unitVectorN[1]},
                {unitVectorP[2],unitVectorU[2],unitVectorN[2]}
        };

        tDelta += 0.01;
        double[] geodeticCurve =  {
                scaleFactor * Math.cos(tDelta),
                scaleFactor * Math.sin(tDelta),
                0
        };


        double[] turnedCurveVector = Maths.multMatVec(transMatrixD,geodeticCurve);
        double[] homogenTCVector = {turnedCurveVector[0],turnedCurveVector[1],turnedCurveVector[2],1};
        double [] twoDHTCVector = Maths.multMatVec(projectionMatrix,homogenTCVector);
        g2d.setColor(Color.BLUE);
        g2d.fillOval((int) twoDHTCVector[0] , (int) twoDHTCVector[1] , 10, 10);
        if(tDelta >= delta) {

            tDelta = delta;
            for(double i = 0; i<=delta;i+=0.1) {
                geodeticCurve = new double[]{
                        scaleFactor * Math.cos(i),
                        scaleFactor * Math.sin(i),
                        0
                };
                turnedCurveVector = Maths.multMatVec(transMatrixD, geodeticCurve);
                homogenTCVector = new double[]{turnedCurveVector[0], turnedCurveVector[1], turnedCurveVector[2], 1};
                twoDHTCVector = Maths.multMatVec(projectionMatrix, homogenTCVector);
                g2d.setColor(Color.YELLOW);
                g2d.fillOval((int) twoDHTCVector[0], (int) twoDHTCVector[1], 10, 10);
            }

        }

    }




}