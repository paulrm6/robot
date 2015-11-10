package attempt1;

import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

import java.math.BigDecimal;
import java.util.List;

public class RobotScene {

    private final List<TheRobot> robots;
    private GLU glu = new GLU();
    private GLUT glut = new GLUT();

    public RobotScene(List<TheRobot> robots) {
        this.robots = robots;
    }

    public void update() {
        for (TheRobot robot : robots) {
            robot.setX(newCoordinate(robot.getX(), robot.getCoord()[robot.getT()][0], robot.getDx()));
            robot.setY(newCoordinate(robot.getY(), robot.getCoord()[robot.getT()][1], robot.getDy()));
            robot.setZ(newCoordinate(robot.getZ(), robot.getCoord()[robot.getT()][2], robot.getDz()));
            if (bigDecimalEqualToInt(robot.getX(), robot.getCoord()[robot.getT()][0])
                    && bigDecimalEqualToInt(robot.getY(), robot.getCoord()[robot.getT()][1])
                    && bigDecimalEqualToInt(robot.getZ(), robot.getCoord()[robot.getT()][2])) {
                robot.setT(robot.getT() + 1);
                if (robot.getT() == robot.getCoord().length)
                    robot.setT(0);
                robot.setDifferences();
            }
        }
    }

    private boolean bigDecimalEqualToInt(BigDecimal bigDecimal, int integer) {
        return bigDecimal.compareTo(new BigDecimal(integer)) == 0;
    }

    private BigDecimal newCoordinate(BigDecimal bigDecimal, int integer, BigDecimal incrementValue) {
        if (!bigDecimalEqualToInt(bigDecimal, integer)) {
            bigDecimal = bigDecimal.add(incrementValue);
        }
        return bigDecimal;
    }

    public void render(GL2 gl) {
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        glu.gluLookAt(15, 15, 15, 0, 0, 0, 0, 1, 0);

        //drawAxes(gl);
        for (TheRobot robot : robots) {
            this.robot(gl, glut, robot);
        }
    }

    private void robot(GL2 gl, GLUT glut, TheRobot robot) {
        gl.glColor3d(1, 1, 1); //White
        gl.glPushMatrix();
        gl.glTranslated(robot.getX().doubleValue(), robot.getY().doubleValue(), robot.getZ().doubleValue()); //Move attempt1
        gl.glPushMatrix();
        gl.glScaled(1.5, 4, 1.3);
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslated(0, 2.9, 0);
        gl.glScaled(1.6, 1.7, 1);
        glut.glutSolidSphere(0.5, 10, 10);
        gl.glPopMatrix();
        gl.glPopMatrix();
    }

    private void drawAxes(GL2 gl) {
        double x = 1.5, y = 1.5, z = 1.5;
        gl.glLineWidth(4);
        gl.glBegin(GL2.GL_LINES);
        gl.glColor3d(1, 0, 0);
        gl.glVertex3d(0, 0, 0);
        gl.glVertex3d(x, 0, 0);
        gl.glColor3d(0, 1, 0);
        gl.glVertex3d(0, 0, 0);
        gl.glVertex3d(0, y, 0);
        gl.glColor3d(0, 0, 1);
        gl.glVertex3d(0, 0, 0);
        gl.glVertex3d(0, 0, z);
        gl.glEnd();
        gl.glLineWidth(1);
    }
}