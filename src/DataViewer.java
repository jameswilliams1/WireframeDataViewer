//
// 159.235, 2019 S2
// Startup code for Assignment 2
//
//

// Put in some package name if you like
// eg nz.ac.massey.graphics.wire

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.*;

// Here is a skeleton class that sets up a JFrame, some sliders and
// buttons

// You may add whatever features you see fit, in order to complete the
// assignment.

// Suggest you put your event listeners in the main program file
//   - this would be the "controller" part of the MVC design

// You can also change the class names too
public class DataViewer extends JFrame
        implements ActionListener, ChangeListener {

    // Menu items
    JMenuItem openItem, quitItem, helpItem;

    // Buttons to change the size of the figure
    JButton biggerButton, smallerButton;

    // Sliders for rotation angles
    JSlider sliderXY, sliderXZ, sliderYZ;
    static final int SLIDER_MIN = 0;
    static final int SLIDER_MAX = 360;
    static final int SLIDER_INIT = 0;

    // Dimensions of JFrame
    static final int FRAME_WIDTH = 800;
    static final int FRAME_HEIGHT = 900;
    static final double HALF_SIZE = 400.0;

    // Need this for the menu items and buttons
    public void actionPerformed(ActionEvent event) {

        JComponent source = (JComponent) event.getSource();

        if (source == openItem) {
            JFileChooser chooser = new JFileChooser("./");
            int retVal = chooser.showOpenDialog(this);
            if (retVal == JFileChooser.APPROVE_OPTION) {
                File myFile = chooser.getSelectedFile();
            }

        } else if (source == quitItem) {
            System.out.println("Quitting ...");
            System.exit(0);
        } else if (source == helpItem) {
            System.out.println("Help me!");
        } else if (source == biggerButton) {
        } else if (source == smallerButton) {
        }

    }

    // Need this for the sliders
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        double angle;

        if (source.getValueIsAdjusting()) {
            angle = Math.toRadians((double) source.getValue());
        }

    }

    public void makeMenu() {
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

    JSlider makeSlider(JPanel panel, String heading) {
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

    public DataViewer() {
        super("Wireframe Viewer");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));

        makeMenu();

        // Get a reference to the JFrames content pane to which
        // JPanels will be added
        Container content = this.getContentPane();
        content.setLayout(new BorderLayout());

        // Make a control panel for the sliders and buttons using a JPanel
        JPanel controlP = new JPanel();
//        controlP.setBounds(new Rectangle(0, 0, 800, 100));
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

        JPanel imageP = new DisplayPanel();
        content.add(imageP, BorderLayout.CENTER);

        this.setVisible(true);
    }

    // An inner class to handle the final rendering of the figure
    class DisplayPanel extends JPanel {

        public void paintComponent(Graphics g) {
            // Code to draw the transformed triangles

            // You may want to redefine the g2 object so
            // the coordinate system is centred in the
            // middle of the panel with the y-axis going
            // up the screen

//            super.paintComponent(g); // call superclass's paintComponent
            Graphics2D g2d = (Graphics2D) g; // cast g to Graphics2D


            g2d.setStroke(new BasicStroke(2.0f));
            g2d.setPaint(Color.BLACK);
            g2d.draw(new Line2D.Double(395, 30, 320, 150));
        }
    }

    // Program entry point
    public static void main(String[] args) {
        try {
            Wireframe wireframe = Wireframe.readShapesFromFile(new File("cuboid.dat"), 3);
            for (int i = 0; i < wireframe.shapes.length; ++i) {
                System.out.println(wireframe.shapes[i]);

            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        new DataViewer();
    }


}

