import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import static java.lang.Math.toRadians;

public class DataViewer extends JFrame
        implements ActionListener, ChangeListener {

    private static final int SLIDER_MIN = 0;
    private static final int SLIDER_MAX = 360;
    private static final int SLIDER_INIT = 0;
    // Minimum dimensions of JFrame
    private static final int MIN_FRAME_WIDTH = 800;
    private static final int MIN_FRAME_HEIGHT = 900;
    private static final int DIALOG_WIDTH = 400;
    private static final int DIALOG_HEIGHT = 200;
    private static final double INITIAL_SCALE_FACTOR = 1.0;
    private static Point VIEW_VECTOR = new Point(0, 0, 1); // Set view vector in z direction
    // Buttons to change the size of the figure
    private final JButton biggerButton;
    private final JButton smallerButton;
    // Sliders for rotation angles
    private final JSlider sliderXY;
    private final JSlider sliderXZ;
    private final JSlider sliderYZ;
    private final DisplayPanel displayPanel; // Container to draw image inside
    // Menu items
    private JMenuItem openItem;
    private JMenuItem quitItem;
    private JMenuItem helpItem;
    private double scaleFactor;

    private DataViewer() {
        super("WireFrame Viewer");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(MIN_FRAME_WIDTH, MIN_FRAME_HEIGHT));
        this.scaleFactor = INITIAL_SCALE_FACTOR;

        makeMenu();

        // Get a reference to the JFrames content pane to which
        // JPanels will be added
        Container content = this.getContentPane();
        content.setLayout(new BorderLayout());

        // Make a control panel for the sliders and buttons using a JPanel
        JPanel controlP = new JPanel();
        content.add(controlP, BorderLayout.NORTH);
        sliderXY = makeSlider(controlP, "XY Plane");
        sliderYZ = makeSlider(controlP, "YZ Plane");
        sliderXZ = makeSlider(controlP, "XZ Plane");
        biggerButton = new JButton("Bigger");
        smallerButton = new JButton("Smaller");
        controlP.add(biggerButton);
        controlP.add(smallerButton);
        biggerButton.addActionListener(this);
        smallerButton.addActionListener(this);

        this.displayPanel = new DisplayPanel(); // Add the WireFrame panel
        content.add(this.displayPanel, BorderLayout.CENTER);

        this.setVisible(true);
    }

    public static void main(String[] args) {
        new DataViewer();
    }

    public void popup(String title, String message) { // Display a dialog when there's an error
        JDialog window = new JDialog(this, title, true);
        window.setLocation((int) (this.getWidth() / 2 - DIALOG_WIDTH / 2), (int) (this.getHeight() / 2 - DIALOG_HEIGHT / 2));
        window.add(new JLabel("<html><p style='margin:10px;'>" + message + "</p></html>"));
        window.setMinimumSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
        window.setVisible(true);
    }

    // Need this for the menu items and buttons
    public void actionPerformed(ActionEvent event) {

        JComponent source = (JComponent) event.getSource();
        if (source == openItem) {
            JFileChooser chooser = new JFileChooser("./");
            int retVal = chooser.showOpenDialog(this);
            if (retVal == JFileChooser.APPROVE_OPTION) {
                try {
                    this.displayPanel.wireframe = WireFrame.readShapesFromFile(chooser.getSelectedFile(), 3);
                } catch (IOException e) {
                    String errorMessage = e.getMessage() + " Please see the help menu for information on how to use this program.";
                    this.popup("Error", errorMessage);
                }
                this.scaleFactor = INITIAL_SCALE_FACTOR;
                this.sliderXY.setValue(SLIDER_INIT);
                this.sliderYZ.setValue(SLIDER_INIT);
                this.sliderXZ.setValue(SLIDER_INIT);
                this.displayPanel.repaint();
            }
        } else if (source == quitItem) {
            System.out.println("Quitting ...");
            System.exit(0);
        } else if (source == helpItem) {
            this.popup("Help", "This program displays wire frame images from a data file. The file " +
                    "chosen must contain first a line specifying the number of vertices that define a shape, " +
                    "then a line for each vertex, n specifying its (x,y,z) coordinates. Then a line containing " +
                    "the number of triangles that compose the image, followed by a line for each specifying which " +
                    "of the previously specified vertices it uses (1-n).");
        } else if (source == biggerButton) {
            this.scaleFactor *= 2;
            this.displayPanel.repaint();
        } else if (source == smallerButton) {
            this.scaleFactor /= 2;
            this.displayPanel.repaint();
        }
    }

    // Need this for the sliders
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        if (source.getValueIsAdjusting()) {
            this.displayPanel.repaint();
        }
    }

    private void makeMenu() {
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        openItem = new JMenuItem("Open");
        quitItem = new JMenuItem("Quit");
        fileMenu.add(openItem);
        fileMenu.add(quitItem);

        JMenu helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);

        helpItem = new JMenuItem("Help");
        helpMenu.add(helpItem);

        openItem.addActionListener(this);
        quitItem.addActionListener(this);
        helpItem.addActionListener(this);
    }

    private JSlider makeSlider(JPanel panel, String heading) {
        JSlider slider = new JSlider(SLIDER_MIN, SLIDER_MAX, SLIDER_INIT);
        slider.setBorder(BorderFactory.createTitledBorder(heading));
        slider.addChangeListener(this);
        slider.setMajorTickSpacing(90);
        slider.setMinorTickSpacing(30);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        panel.add(slider);
        return slider;
    }

    // An inner class to handle the final rendering of the figure
    class DisplayPanel extends JPanel {

        WireFrame wireframe = new WireFrame();

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.translate(getWidth() / 2, getHeight() / 2); // Set origin to centre of panel
            // Calculate rotation/scale matrices
            double XY = toRadians(sliderXY.getValue());
            double YZ = toRadians(sliderYZ.getValue());
            double XZ = toRadians(sliderXZ.getValue());
            Matrix transform = Matrix.getRotateXY(XY).multiply(Matrix.getRotateYZ(YZ)).multiply(Matrix.getRotateXZ(XZ)).multiply(Matrix.getScaleMatrix(scaleFactor));
            this.wireframe.draw(g2d, transform, VIEW_VECTOR);
        }
    }
}