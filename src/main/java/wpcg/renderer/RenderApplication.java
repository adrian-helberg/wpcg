package wpcg.renderer;

import com.jme3.math.Vector3f;
import wpcg.base.Logger;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.TimeZone;

/**
 * Main application class
 * Provides program window, UI elements, key-press-events, file import and
 * 2D-Canvas to be printed in
 * Reads configuration from config file
 */
public class RenderApplication extends JFrame {
    // Unmodifiable properties object for public access
    public static final Properties properties = new Properties();
    // Renderer to be created with imported triangle mesh
    private Renderer renderer;
    // 2D screen to be rendered in
    private RenderCanvas canvas;
    // Current value from sliders
    float rotationX, rotationY;
    // Flag to prevent too many events thrown by the UI
    private boolean ignoreEvents = false;
    // Changing ui elements
    private JPanel controlBox;
    private JLabel drawingAlgorithm;

    /**
     * Create RenderApplication with necessary configuration
     */
    private RenderApplication() {
        // Access property file
        readConfiguration();
        // Program window
        setupWindow();
        // Canvas
        setupCanvas();
        // UI elements
        setupUIElements();
        // Key press events
        setupKeyListeners();
    }

    /**
     * Load properties from properties file and pass it to global accessible
     * properties object
     */
    private void readConfiguration() {
        String propertiesFileName = "project.properties";
        // Access auto-closable input stream from file
        try (InputStream is = getClass()
                .getClassLoader()
                .getResourceAsStream(propertiesFileName)) {
            properties.load(is);
            log("Load properties from file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set JFrame window properties
     */
    private void setupWindow() {
        // Window title phrase
        setTitle(properties.getProperty("window.title"));
        // Window dimensions
        int w = Integer.parseInt(properties.getProperty("window.width"));
        int h = Integer.parseInt(properties.getProperty("window.height"));
        setSize(w, h);
        // Spawn window in the screen center
        setLocationRelativeTo(null);
        // Prevent resizing the window
        setResizable(false);
        // What to do on close
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // UI elements positioning
        setLayout(new BorderLayout());
    }

    /**
     * Set user interface elements
     */
    private void setupUIElements() {
        // Top aligned box
        controlBox = new JPanel(new GridBagLayout());
        controlBox.setPreferredSize(new Dimension(400,30));
        getContentPane().add(controlBox, BorderLayout.NORTH);
        // Menu bar
        setupMenuBar();
        // Rotation sliders
        setupSlider();
        // Setup controls
        setupControls();
        // Mouse wheel listener
        addMouseWheelListener(e -> {
            // Camera zoom
            renderer.getContext().getCamera().zoom(e.getWheelRotation() >= 0);
            renderer.processPipeline();
        });
    }

    /**
     * Create JFrame window menu bar to access application functionalities
     */
    private void setupMenuBar() {
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("File");
        // File import
        JMenuItem importItem = new JMenuItem("Import");
        importItem.addActionListener(e -> {
            // Preselect project directory with obj files in it
            File currentDirectory = new File(System.getProperty("user.dir") + "/src/main/resources/Models");
            JFileChooser fileChooser = new JFileChooser(currentDirectory);
            fileChooser.setDialogTitle("Choose an obj file to import");
            // Only accept obj files
            fileChooser.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    if (file.isDirectory()) return true;
                    String fileName = file.getName().toLowerCase();
                    return fileName.endsWith(".obj");
                }

                @Override
                public String getDescription() {
                    return "Object files (*.obj)";
                }
            });
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                String selectedFileName = fileChooser.getSelectedFile().getName();
                // Create Renderer and pass imported file
                renderer = new Renderer(selectedFileName, canvas);
                setTitle(properties.getProperty("window.title") + " - "  + selectedFileName);
            }
        });
        file.add(importItem);

        // About - Provides information
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(actionEvent -> showAboutBox());
        file.add(aboutItem);

        // Exit - Closes application
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(actionEvent -> {
            System.exit(0);
            log("Exit");
        });
        file.add(exitItem);

        // Add menu bar to window
        bar.add(file);
        setJMenuBar(bar);
    }

    /**
     * Set sliders for camera movement around a given triangle mesh
     */
    private void setupSlider() {
        // Rotate object around x axis
        JSlider sliderX = new JSlider(SwingConstants.VERTICAL, -180, 180, 0);
        sliderX.addChangeListener(e -> {
            if (ignoreEvents) return;
            ignoreEvents = true;
            rotationX = -sliderX.getValue();
            renderer.getContext().getMesh().setRotation(new Vector3f(rotationX, rotationY, 0));
            renderer.processPipeline();
            SwingUtilities.invokeLater(() -> ignoreEvents = false);
        });
        getContentPane().add(sliderX, BorderLayout.EAST);
        // Rotate object around y axis
        JSlider sliderY = new JSlider(SwingConstants.HORIZONTAL, -180, 180, 0);
        sliderY.addChangeListener(e -> {
            if (ignoreEvents) return;
            ignoreEvents = true;
            rotationY = sliderY.getValue();
            renderer.getContext().getMesh().setRotation(new Vector3f(rotationX, rotationY, 0));
            renderer.processPipeline();
            SwingUtilities.invokeLater(() -> ignoreEvents = false);
        });
        getContentPane().add(sliderY, BorderLayout.SOUTH);
    }

    /**
     * Set control ui elements to change properties
     */
    private void setupControls() {
        // Algorithm label
        JLabel enterLabel = new JLabel("Press 'Enter' to toggle drawing algorithm:  ");
        drawingAlgorithm = new JLabel(properties.getProperty("draw.algorithm"));
        drawingAlgorithm.setForeground(Color.BLUE);
        JPanel panel = new JPanel();
        panel.add(enterLabel);
        panel.add(drawingAlgorithm);
        controlBox.add(panel);
    }

    /**
     * Create canvas to be painted in
     */
    private void setupCanvas() {
        // Canvas dimensions
        int w = Integer.parseInt(properties.getProperty("canvas.width"));
        int h = Integer.parseInt(properties.getProperty("canvas.height"));
        canvas = new RenderCanvas(w, h);
        canvas.setBackground(Color.BLACK);
        // Add canvas
        getContentPane().add(canvas, BorderLayout.CENTER);
    }

    /**
     * Add key listener that catches specific keyPressed-events
     */
    private void setupKeyListeners() {
        canvas.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {}

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (renderer == null) return;
                switch (keyEvent.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        renderer.getContext().getCamera().moveX(false);
                        renderer.processPipeline();
                        break;
                    case KeyEvent.VK_RIGHT:
                        renderer.getContext().getCamera().moveX(true);
                        renderer.processPipeline();
                        break;
                    case KeyEvent.VK_UP:
                        renderer.getContext().getCamera().moveY(true);
                        renderer.processPipeline();
                        break;
                    case KeyEvent.VK_DOWN:
                        renderer.getContext().getCamera().moveY(false);
                        renderer.processPipeline();
                        break;
                    case KeyEvent.VK_W:
                        renderer.getContext().getCamera().moveZ(true);
                        renderer.processPipeline();
                        break;
                    case KeyEvent.VK_S:
                        renderer.getContext().getCamera().moveZ(false);
                        renderer.processPipeline();
                        break;
                    case KeyEvent.VK_A:
                        renderer.getContext().getCamera().rotateY(false);
                        renderer.processPipeline();
                        break;
                    case KeyEvent.VK_D:
                        renderer.getContext().getCamera().rotateY(true);
                        renderer.processPipeline();
                        break;
                    case KeyEvent.VK_Q:
                        renderer.getContext().getCamera().rotateX(false);
                        renderer.processPipeline();
                        break;
                    case KeyEvent.VK_E:
                        renderer.getContext().getCamera().rotateX(true);
                        renderer.processPipeline();
                        break;
                    case KeyEvent.VK_ENTER:
                        // Toggle drawing algorithm
                        String algorithm = properties.getProperty("draw.algorithm");
                        switch (algorithm) {
                            case "default":
                                algorithm = "Bresenham";
                                properties.setProperty("draw.algorithm", algorithm);
                                drawingAlgorithm.setText(algorithm);
                                drawingAlgorithm.setForeground(Color.BLUE);
                                renderer.processPipeline();
                                break;
                            case "Bresenham":
                                algorithm = "Scanline";
                                properties.setProperty("draw.algorithm", algorithm);
                                drawingAlgorithm.setText(algorithm);
                                drawingAlgorithm.setForeground(Color.GREEN);
                                renderer.processPipeline();
                                break;
                            default:
                                algorithm = "default";
                                properties.setProperty("draw.algorithm", algorithm);
                                drawingAlgorithm.setText(algorithm);
                                drawingAlgorithm.setForeground(Color.RED);
                                renderer.processPipeline();
                        }
                        break;
                    default: break;
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {}
        });
    }

    /**
     * Reveal some basic information about the project
     */
    private void showAboutBox() {
        // Prepare information message
        StringBuilder sb = new StringBuilder();
        sb.append("Implementierung der Kameratransformation zur Darstellung von 3D-Objekten");
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("Eingabe: TriangleMesh und virtuelle Kamera");
        sb.append(System.lineSeparator());
        sb.append(new String("Ausgabe: Rendering des Objektes als Drahtgittermodell auf 2D Zeichenfläche".getBytes(), StandardCharsets.UTF_8));
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("Teilaufgaben:");
        sb.append(System.lineSeparator());
        sb.append("* Kameratransformation");
        sb.append(System.lineSeparator());
        sb.append(new String("* Backface-Culling (nur Flächen, die zur Kamera zeigen) werden gezeichnet".getBytes(), StandardCharsets.UTF_8));
        sb.append(System.lineSeparator());
        sb.append("* (optional) Verdeckungsrechnung");
        sb.append(System.lineSeparator());
        sb.append("* (optional) Kamerasteuerung (Rotation, Zoom) + Rasterisierung");
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("'File > Import' zum Importieren einer *.obj-Datei");
        sb.append(System.lineSeparator());
        sb.append("'File > About' Informationen zum Programm");
        sb.append(System.lineSeparator());
        sb.append(new String("'File > Exit' Zum Schließen des Programms".getBytes(), StandardCharsets.UTF_8));
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("Autor: Adrian Helberg, HAW Hamburg (Informatik), Kurs Computebourgrafik");
        // Show dialog box
        JOptionPane.showMessageDialog(
                null,
                sb.toString(),
                "About",
                JOptionPane.INFORMATION_MESSAGE
        );
        log(sb.toString());
    }

    /**
     * Print text to the console
     * @param msg Text message to be printed
     */
    static void log(String msg) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat f = new SimpleDateFormat("hh:mm:ss:SSSS");
        String timestamp = "[" + f.format(c.getTime()) + "] ";
        Logger.getInstance().msg(timestamp + msg);
    }

    /**
     * Program entry point
     *
     * @param args Program parameters
     */
    public static void main(String[] args) {
        new RenderApplication().setVisible(true);
    }
}
