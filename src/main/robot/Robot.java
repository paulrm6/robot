package robot;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.*;

public class Robot extends Frame implements GLEventListener, ActionListener {
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 1000;
    private static final float NEAR_CLIP = 0.1f;
    private static final float FAR_CLIP = 100.0f;

    private RobotScene scene;
    private GLCanvas canvas;

    public static void main(String[] args) {
        Robot robot = new Robot();
        robot.setVisible(true);
    }

    public Robot() {
        //Title
        super("Robot");
        setSize(WIDTH, HEIGHT);

        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        canvas = new GLCanvas(caps);
        add(canvas, "Center");

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        canvas.addGLEventListener(this);

        MenuBar menuBar = new MenuBar();
        this.setMenuBar(menuBar);
        Menu fileMenu = new Menu("File");
        MenuItem quitItem = new MenuItem("Quit");
        quitItem.addActionListener(this);
        fileMenu.add(quitItem);
        menuBar.add(fileMenu);

        Panel p = new Panel();
        Button worldLighting = new Button("World Lighting");
        worldLighting.addActionListener(this);
        p.add(worldLighting);
        Button robotLight = new Button("Robot Light");
        robotLight.addActionListener(this);
        p.add(robotLight);
        this.add(p, "South");

        FPSAnimator animator = new FPSAnimator(canvas, 30);
        animator.start();
    }

    private List<TheRobot> createRobots(){
        TheRobot robot = new TheRobot(new int[][]{{1, 2, 3}, {15, 11, 9},{5,2,4}});
        TheRobot robot2 = new TheRobot(new int[][] {{0, 2, 3}, {15, 11, 9},{5,2,4}});
        TheRobot robot3 = new TheRobot(new int[][] {{5, 2, 3}, {15, 11, 9},{5,2,4}});
        return new ArrayList<>(Arrays.asList(robot, robot2, robot3));
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equalsIgnoreCase("World Lighting")) {

        } else if (e.getActionCommand().equalsIgnoreCase("Robot Light")) {

        } else if (e.getActionCommand().equalsIgnoreCase("Quit"))
            System.exit(0);
    }

    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0, 0, 0, 1);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL.GL_CULL_FACE);
        gl.glFrontFace(GL.GL_CCW);
        gl.glCullFace(GL.GL_BACK);
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_LINE);
        scene = new RobotScene(createRobots());
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();

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

    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        scene.update();
        scene.render(gl);
    }

    public void dispose(GLAutoDrawable drawable) {
    }

}