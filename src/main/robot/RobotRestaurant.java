package robot;

import java.awt.*;
import java.awt.event.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.*;

/**
 * Created by Paul on 10/11/2015.
 * I declare that this code is my own work.
 * prmacdonald1@sheffield.ac.uk
 * 1350155458
 */

public class RobotRestaurant extends Frame implements GLEventListener, ActionListener, ItemListener {
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 1000;
    private static final float NEAR_CLIP = 0.1f;
    private static final float FAR_CLIP = 100.0f;
    public static final int FRAMES_PER_SEC = 30;
    private RobotRestaurantScene scene;
    private GLCanvas canvas;
    private Checkbox worldLighting, robotLight;
    private boolean withWorldLighting = true, withRobotLight = true, robotPerspective = false;

    public static void main(String[] args) {
        RobotRestaurant robotRestaurant =   new RobotRestaurant();
        robotRestaurant.setVisible(true);
    }

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
        //Button to change perspective to and from the robot
        Button perspective = new Button("Change Perspective");
        perspective.addActionListener(this);
        p.add(perspective);
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
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //If user clicks quit in File->Quit
        if (e.getActionCommand().equalsIgnoreCase("Quit")) {
            //Exit with code 0
            System.exit(0);
        }
        //If user clicks change perspective
        else if (e.getActionCommand().equalsIgnoreCase("Change Perspective")) {
            //Inverse the robotPerspective variable
            robotPerspective = !robotPerspective;
            canvas.repaint();
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
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); //black
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL.GL_CULL_FACE);
        gl.glFrontFace(GL.GL_CCW);
        gl.glCullFace(GL.GL_BACK);
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_LINE);

        scene = new RobotRestaurantScene();
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        scene.update();
        scene.render(gl, withWorldLighting, withRobotLight, robotPerspective);
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
}
