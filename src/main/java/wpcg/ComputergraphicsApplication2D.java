/**
 * Diese Datei ist Teil der Vorgabe zur Lehrveranstaltung Einführung in die Computergrafik der Hochschule
 * für Angewandte Wissenschaften Hamburg von Prof. Philipp Jenke (Informatik)
 */

package wpcg;

import wpcg.base.canvas2d.Canvas2D;
import wpcg.base.canvas2d.SimpleExampleCanvas2D;

import javax.swing.*;
import java.awt.*;

/**
 * Lecture support application for 2D scenes
 */
public class ComputergraphicsApplication2D extends JFrame {

    public ComputergraphicsApplication2D() {

        // Set the 2D canvas here
        Canvas2D curveCanvas2D = new SimpleExampleCanvas2D(400, 400);
        curveCanvas2D.setupListener();
        getContentPane().add(curveCanvas2D);

        // Layout
        setLayout(new BorderLayout());
        setTitle("WP Computergraphics (2D)");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(500, 500);
        setVisible(true);
    }

    public static void main(String[] args) {
        new ComputergraphicsApplication2D();
    }
}
