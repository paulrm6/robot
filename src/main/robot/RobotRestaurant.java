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
 *
 * Modelled off of the Lab class examples provided by
 * Dr Steve Maddock.
 *
 * A class to start the program and control the interface
 */

public class RobotRestaurant extends Frame implements GLEventListener, ActionListener, ItemListener, MouseMotionListener {
    //Create some variables
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

    /**
     * Constructor
     */
    public RobotRestaurant() {
        //Set title of window
        super("Paul's Robot Restaurant");
        //Set size of the window
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        //Set the capabilities and create the canvas
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
        FPSAnimator animator = new FPSAnimator(canvas, FRAMES_PER_SEC);//Create the animator
        animator.start(); //Start the animator
        canvas.addMouseMotionListener(this);//Listen for mouse events on the canvas
    }

    /**
     * Method defined by GLEventListener.
     * Responds to events such as button clicks
     * @param e Automatically supplied by the system when an action occurs
     */
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

    /**
     * Method defined by GLEventListener
     * Respons to changed to any Checkbox in the GUI
     * @param e Automatically supplied by the system when a checkbox changes it's state
     */
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

    /**
     * Methods defined by GLEventListener
     * Initialises the OpenGL context
     * @param glAutoDrawable drawable automatically supplied by the system
     */
    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); //black background
        gl.glEnable(GL.GL_DEPTH_TEST);//Enable depth testing using z-buffer
        gl.glDisable(GL.GL_CULL_FACE);//Disable face culling so no polygons are discarded
        gl.glCullFace(GL.GL_BACK);//For when face culling is enabled, discard polygons facing away from the camera
        gl.glShadeModel(GL2.GL_SMOOTH);//Colours computed at vertices are interpolated over the surface
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);//Front and back facing polygons should be filled
        gl.glEnable(GL2.GL_LIGHTING);//Lighting will be used
        gl.glEnable(GL2.GL_NORMALIZE);//Vectors with glNormal are scaled to unit length after transformation
        double radius = 35.0; //distance of the camera from world origin
        double theta = Math.toRadians(-90); // theta rotates anticlockwise around y axis
        double phi = Math.toRadians(10);  // phi is inclination from ground plane
        camera = new Camera(theta, phi, radius); //initiate a camera
        scene = new RobotRestaurantScene(gl, camera); //initiate a scene
    }

    /**
     * Method defined by GLEventListener
     * Called when the canvas is displayed
     * @param glAutoDrawable drawable automatically supplied by the system
     */
    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        if (continuousAnimation) scene.update(); //If continuous animation is enabled, update the scene
        scene.render(gl, withWorldLighting, withRobotLight, robotPerspective, withSpotlight); //Render the scene
    }

    /**
     * Method defined by GLEventListener
     * Called when the user resizes the window
     * @param glAutoDrawable drawable automatically supplied by the system
     * @param x x coordinate of top left of window (supplied by system)
     * @param y y coordinate of top left of window (supplied by system)
     * @param width width of the window (supplied by system)
     * @param height height of the window (supplied by system)
     */
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

    /**
     * Method defined by GLEventListener
     * Called when closing the openGL context
     * @param glAutoDrawable drawable automatically supplied by the system
     */
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
            camera.updateRadius(-dy*20);
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

    public static void main(String[] args) {
        System.out.println((long) (Math.random()*90000000+10000000));
        //RobotRestaurant robotRestaurant = new RobotRestaurant();
        //robotRestaurant.setVisible(true);
    }
}