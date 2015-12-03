package robot;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

/**
 * Created by Paul on 10/11/2015.
 * I declare that this code is my own work.
 * prmacdonald1@sheffield.ac.uk
 * 1350155458
 */
public class RobotRestaurantScene {
    private final double MAX_LEAN = 15;
    private GLU glu = new GLU();
    private GLUT glut = new GLUT();
    private RobotWaiter theRobot = new RobotWaiter();
    private Table table1,table2,table3;
    private Restaurant restaurant;
    private Light global1,global2,spot1,spot2,robotLight;
    private double rotate=0;
    private double frontLean=0,sideLean = 0;
    private double addTilt = 1;

    public RobotRestaurantScene(GL2 gl) {
        float[] position = {0,10,0,1};
        float[] direction = {-0,-10,-0};
        spot1 = new Light(GL2.GL_LIGHT2,position,20f,direction);
        float[] globalPos0 = {10,10,10,0};
        float[] globalPos1 = {-10,-10,-10,0};
        global1 = new Light(GL2.GL_LIGHT0,globalPos0);
        global2 = new Light(GL2.GL_LIGHT1,globalPos1);
        restaurant = new Restaurant(gl,20,20,20);
        restaurant.create(gl);
    }

    public void render(GL2 gl, boolean withWorldLighting, boolean withRobotLight, boolean robotPerspective) {
        gl.glClear(GL.GL_COLOR_BUFFER_BIT|GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        //If perspective is from the robot
        if (robotPerspective) {
            glu.gluLookAt(-5, 3.9, -5, 0.0, 0.0, 0.0, 0.0,1.0,0.0);
        }
        //If perspective is arbitrary
        else {
            glu.gluLookAt(30.0, 13.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
        }

        spot1.deploy(gl);
        global1.deploy(gl);
        global1.setOn(withWorldLighting);
        global2.deploy(gl);
        global2.setOn(withWorldLighting);


        gl.glPushMatrix();
            gl.glRotated(rotate,0,1,0);
            theRobot.draw(gl, glut);
        gl.glPopMatrix();
        restaurant.render(gl);
        drawAxes(gl);

    }

    public void drawAxes(GL2 gl) {
        gl.glDisable(GL2.GL_LIGHTING);
        double x = 1.5, y = 1.5, z = 3;
        gl.glLineWidth(4);
        gl.glBegin(GL2.GL_LINES);
        gl.glColor3d(1,0,0);
        gl.glVertex3d(0,0,0);
        gl.glVertex3d(x,0,0);
        gl.glColor3d(0,1,0);
        gl.glVertex3d(0,0,0);
        gl.glVertex3d(0,y,0);
        gl.glColor3d(0,0,1);
        gl.glVertex3d(0,0,0);
        gl.glVertex3d(0,0,z);
        gl.glEnd();
        gl.glLineWidth(1);
        gl.glEnable(GL2.GL_LIGHTING);
    }
    public void update() {
        rotate = (rotate+1)%360;
        if (sideLean == MAX_LEAN || sideLean == -MAX_LEAN) addTilt = -addTilt;
        sideLean += addTilt;
        theRobot.setRobotTilt(0.3,sideLean);
    }
}
