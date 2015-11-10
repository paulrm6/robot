package robot;

import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

/**
 * Created by Paul on 10/11/2015.
 * I declare that this code is my own work.
 * prmacdonald1@sheffield.ac.uk
 * 1350155458
 */
public class RobotRestaurantScene {
    private GLU glu = new GLU();
    private GLUT glut = new GLUT();

    public void render(GL2 gl, boolean withWorldLighting, boolean withRobotLight, boolean robotPerspective) {
        gl.glClear(GL.GL_COLOR_BUFFER_BIT|GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        //If perspective is from the robot
        if (robotPerspective) {
            glu.gluLookAt(5.0, 7.0, 9.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
        }
        //If perspective is arbitrary
        else {
            glu.gluLookAt(10.0, 7.0, 9.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
        }

        gl.glPushMatrix();
        gl.glScaled(20,0.01,20);
        glut.glutSolidSphere(1, 20, 20);
        gl.glPopMatrix();
    }

    public void update() {
    }
}
