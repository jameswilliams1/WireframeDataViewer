import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class DataViewer extends JFrame
        implements ActionListener, ChangeListener {

    static final int SLIDER_MIN = 0;
    static final int SLIDER_MAX = 360;
    static final int SLIDER_INIT = 0;
    // Minimum dimensions of JFrame
    static final int FRAME_WIDTH = 800;
    static final int FRAME_HEIGHT = 900;
    // Menu items
    JMenuItem openItem, quitItem, helpItem;
    // Buttons to change the size of the figure
    JButton biggerButton, smallerButton;
    // Sliders for rotation angles
    JSlider sliderXY, sliderXZ, sliderYZ;
    DisplayPanel displayPanel;

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

        this.displayPanel = new DisplayPanel(); // Add the wireframe panel
        content.add(this.displayPanel, BorderLayout.CENTER);

        this.setVisible(true);
    }

    public static void main(String[] args) {
        new DataViewer();
    }

    // Need this for the menu items and buttons
    public void actionPerformed(ActionEvent event) {

        JComponent source = (JComponent) event.getSource();
        if (source == openItem) { // TODO reset sliders/size on load
            JFileChooser chooser = new JFileChooser("./");
            int retVal = chooser.showOpenDialog(this);
            if (retVal == JFileChooser.APPROVE_OPTION) {
                try {
                    this.displayPanel.wireframe = Wireframe.readShapesFromFile(chooser.getSelectedFile(), 3);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.displayPanel.repaint();
            }
        } else if (source == quitItem) {
            System.out.println("Quitting ...");
            System.exit(0);
        } else if (source == helpItem) {
            System.out.println("Help me!");
        } else if (source == biggerButton) {
            this.displayPanel.wireframe.scale(2.0);
            this.displayPanel.repaint();
        } else if (source == smallerButton) {
            this.displayPanel.wireframe.scale(0.5);
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

    // An inner class to handle the final rendering of the figure
    class DisplayPanel extends JPanel {

        Wireframe wireframe = new Wireframe();

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.translate(getWidth() / 2, getHeight() / 2); // Set origin to centre of panel

            // Calculate rotation matrix
            double XY = Math.toRadians(sliderXY.getValue());
            double YZ = Math.toRadians(sliderYZ.getValue());
            double XZ = Math.toRadians(sliderXZ.getValue());
            Matrix transform = Matrix.getRotateXY(XY).multiply(Matrix.getRotateYZ(YZ)).multiply(Matrix.getRotateXZ(XZ));
            this.wireframe.draw(g2d, transform);
        }

    }


}

