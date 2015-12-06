package robot;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import java.awt.*;
import java.awt.event.*;

/**
 * Created by Paul on 10/11/2015.
 * I declare that this code is my own work.
 * prmacdonald1@sheffield.ac.uk
 * 1350155458
 */

public class RobotRestaurant extends Frame implements GLEventListener, ActionListener, ItemListener, MouseMotionListener {
    public static final int FRAMES_PER_SEC = 30;
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 1000;
    private static final float NEAR_CLIP = 0.1f;
    private static final float FAR_CLIP = 100.0f;
    private Camera camera;
    private Point lastpoint;
    private RobotRestaurantScene scene;
    private GLCanvas canvas;
    private Checkbox worldLighting, robotLight, spotlight;
    private boolean withWorldLighting = true, withRobotLight = true, robotPerspective = false, withSpotlight = true;
    private boolean continuousAnimation;

    public RobotRestaurant() {
        //Set title of window
        super("Paul's Robot Restaurant");
        //Set size of the window
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        canvas = new GLCanvas(caps);
        add(canvas, "Center");

        //Add a menu bar with a quit option
        MenuBar menuBar = new MenuBar();
        this.setMenuBar(menuBar);
        Menu fileMenu = new Menu("File");
        MenuItem quitItem = new MenuItem("Quit");
        quitItem.addActionListener(this);
        fileMenu.add(quitItem);
        menuBar.add(fileMenu);
        //Add a panel at the bottom with various options
        Panel p = new Panel();
        //Checkbox for world lighting
        worldLighting = new Checkbox("World Lighting", withWorldLighting);
        worldLighting.addItemListener(this);
        p.add(worldLighting);
        //Checkbox for the robot light
        robotLight = new Checkbox("Robot Light", withRobotLight);
        robotLight.addItemListener(this);
        p.add(robotLight);
        //Checkbox for the spotlights
        spotlight = new Checkbox("Spotlights", withSpotlight);
        spotlight.addItemListener(this);
        p.add(spotlight);
        //Button to change perspective to and from the robot
        Button perspective = new Button("Change Perspective");
        perspective.addActionListener(this);
        p.add(perspective);
        //Button to start the animation
        Button startAnim = new Button("Start Animation");
        startAnim.addActionListener(this);
        p.add(startAnim);
        //Button to pause the animation
        Button stopAnim = new Button("Stop Animation");
        stopAnim.addActionListener(this);
        p.add(stopAnim);
        //Button to stop the animation
        Button resetAnim = new Button("Reset Animation");
        resetAnim.addActionListener(this);
        p.add(resetAnim);
        this.add(p, "South");
        //Add a window listener for the window being closed by the user
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        //Add a event listener to the canvas
        canvas.addGLEventListener(this);
        //Create and start an animator
        FPSAnimator animator = new FPSAnimator(canvas, FRAMES_PER_SEC);
        animator.start();
        canvas.addMouseMotionListener(this);
    }

    public static void main(String[] args) {
        RobotRestaurant robotRestaurant = new RobotRestaurant();
        robotRestaurant.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //If user clicks quit in File->Quit
        if (e.getActionCommand().equalsIgnoreCase("Quit")) {
            //Exit with code 0
            System.exit(0);
        }
        //If user clicks change perspective
        if (e.getActionCommand().equalsIgnoreCase("Change Perspective")) {
            //Inverse the robotPerspective variable
            robotPerspective = !robotPerspective;
            canvas.repaint();
        }
        //If user clicks change perspective
        if (e.getActionCommand().equalsIgnoreCase("Start Animation")) {
            continuousAnimation = true;
            scene.startAnim();
        }
        //If user clicks change perspective
        if (e.getActionCommand().equalsIgnoreCase("Stop Animation")) {
            continuousAnimation = false;
            scene.stopAnim();
        }
        //If user clicks change perspective
        if (e.getActionCommand().equalsIgnoreCase("Reset Animation")) {
            continuousAnimation = false;
            scene.resetAnim();
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        Object source = e.getSource();
        //If worldLighting is clicked
        if (source == worldLighting) {
            //Change the state of the withWorldLighting variable
            withWorldLighting = worldLighting.getState();
            canvas.repaint();
        }
        //If robotLight is clicked
        else if (source == robotLight) {
            //Change the state of the withRobotLight variable
            withRobotLight = robotLight.getState();
            canvas.repaint();
        }
        //If robotLight is clicked
        else if (source == spotlight) {
            //Change the state of the withRobotLight variable
            withSpotlight = spotlight.getState();
            canvas.repaint();
        }
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); //black background
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDisable(GL.GL_CULL_FACE);
        gl.glFrontFace(GL.GL_CCW);
        gl.glCullFace(GL.GL_BACK);
        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_NORMALIZE);
        double radius = 50.0; //distance of the camera from world origin
        double theta = Math.toRadians(-90); // theta rotates anticlockwise around y axis
        double phi = Math.toRadians(5);  // phi is inclination from ground plane
        camera = new Camera(theta, phi, radius);
        scene = new RobotRestaurantScene(gl, camera);
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        if (continuousAnimation) scene.update();
        scene.render(gl, withWorldLighting, withRobotLight, robotPerspective, withSpotlight);
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
        GL2 gl = glAutoDrawable.getGL().getGL2();

        float fAspect = (float) width / height;
        float fovy = 60.0f;

        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        float top = (float) Math.tan(Math.toRadians(fovy * 0.5)) * NEAR_CLIP;
        float bottom = -top;
        float left = fAspect * bottom;
        float right = fAspect * top;

        gl.glFrustum(left, right, bottom, top, NEAR_CLIP, FAR_CLIP);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    /**
     * The mouse is used to control the camera position.
     *
     * @param e instance of MouseEvent, automatically supplied by the system when the user drags the mouse
     */
    public void mouseDragged(MouseEvent e) {
        Point ms = e.getPoint();

        float dx = (float) (ms.x - lastpoint.x) / WINDOW_WIDTH;
        float dy = (float) (ms.y - lastpoint.y) / WINDOW_HEIGHT;

        if (e.getModifiers() == MouseEvent.BUTTON1_MASK) {
            camera.updateThetaPhi(-dx * 2.0f, dy * 2.0f);
        } else if (e.getModifiers() == MouseEvent.BUTTON3_MASK) {
            camera.updateRadius(-dy);
        }

        lastpoint = ms;
    }

    /**
     * The mouse is used to control the camera position.
     *
     * @param e instance of MouseEvent, automatically supplied by the system when the user moves the mouse
     */
    public void mouseMoved(MouseEvent e) {
        lastpoint = e.getPoint();
    }
}