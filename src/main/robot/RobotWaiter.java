package robot;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

/**
 * Created by Paul on 10/11/2015.
 * I declare that this code is my own work.
 * prmacdonald1@sheffield.ac.uk
 * 1350155458
 */
public class RobotWaiter {
    private final float[] AMBIENT = {0.2f,0.2f,0.2f,1.0f}; //dark grey
    private final float[] COLOUR = {0.8f,0.8f,0.8f,1.0f}; //grey
    private final int RESOLUTION = 100;
    private double x,z;
    private double sideTilt=0, frontTilt=0;
    public RobotWaiter() {
    }
    public void draw(GL2 gl, GLUT glut) {
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, AMBIENT, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, COLOUR, 0);

        gl.glPushMatrix();
            gl.glTranslated(x,0,z);
            gl.glPushMatrix();
                gl.glRotated(270,1,0,0);
                glut.glutSolidCone(1,1,RESOLUTION,RESOLUTION);
            gl.glPopMatrix();
            gl.glTranslated(0,1,0);
            gl.glPushMatrix();
                gl.glRotated(frontTilt,0,0,1);
                gl.glRotated(sideTilt,1,0,0);
                drawCylinder(gl,glut,3,1); //draw body
                //draw left arm
                drawArm(gl,glut,true,-15,(-60-sideTilt),true);
                //draw right arm
                drawArm(gl,glut,false,-50,(-40-sideTilt),false);
                //draw neck
                gl.glPushMatrix();
                    gl.glTranslated(0,3,0);
                    drawCylinder(gl,glut,0.2,0.2);
                    //draw head
                    gl.glPushMatrix();
                        gl.glTranslated(0,0.2,0);
                        drawRectangle(gl,glut,1,0.9,0.9,0);
                        //draw left eye
                        gl.glPushMatrix();
                            gl.glTranslated(0.45,0.7,0.2);
                            glut.glutSolidSphere(0.1,RESOLUTION,RESOLUTION);
                        gl.glPopMatrix();
                        //draw right eye
                        gl.glPushMatrix();
                            gl.glTranslated(0.45,0.7,-0.2);
                            glut.glutSolidSphere(0.1,RESOLUTION,RESOLUTION);
                        gl.glPopMatrix();
                    gl.glPopMatrix();
                gl.glPopMatrix();
            gl.glPopMatrix();
        gl.glPopMatrix();

    }

    public void drawArm(GL2 gl, GLUT glut, boolean leftSide, double rotateArm, double rotateForearm, boolean closedHand) {
        double angle;
        int side;
        if (leftSide) {
            angle = 90;
            side = 1;
        } else {
            angle = 270;
            side = -1;
        }
        gl.glPushMatrix();
            gl.glTranslated(0,2.7,side*0.85);
            gl.glRotated(rotateArm,1,0,0); // rotate arm
            drawRectangle(gl,glut,1,0.25,0.25,angle);
            //draw forearm
            gl.glPushMatrix();
                gl.glTranslated(0,0,side*0.9);
                gl.glRotated(rotateForearm,1,0,0); // rotate forearm
                drawRectangle(gl,glut,1,0.2,0.2,angle);
                //draw claw
                drawClaw(gl,glut,angle,side,1,closedHand);
                drawClaw(gl,glut,angle,side,-1,closedHand);
            gl.glPopMatrix();
        gl.glPopMatrix();
    }

    public void drawClaw(GL2 gl, GLUT glut, double angle, int side, int clawSide, boolean closedHand) {
        int clawAngle;
        if (closedHand) {
            clawAngle = -5*clawSide;
        } else {
            clawAngle = 10*clawSide;
        }
        gl.glPushMatrix();
            gl.glTranslated(0,-side*clawSide*0.05,side*0.95);
            gl.glRotated(clawAngle,1,0,0);
            drawRectangle(gl,glut,0.3,0.05,0.05,angle);
        gl.glPopMatrix();
    }

    public void drawRectangle(GL2 gl, GLUT glut, double l, double w, double d, double r) {
        gl.glPushMatrix();
            gl.glRotated(r,1,0,0);
            gl.glScaled(w,l,d);
            gl.glTranslated(0,0.5,0);
            glut.glutSolidCube(1);
        gl.glPopMatrix();
    }
    public void drawCylinder(GL2 gl, GLUT glut, double h, double r) {
        gl.glPushMatrix();
            gl.glScaled(r,h,r);
            gl.glRotated(270,1,0,0);
            glut.glutSolidCylinder(1,1,RESOLUTION,RESOLUTION);
        gl.glPopMatrix();
    }

    public double getX() {
        return x;
    }
    public double getZ() {
        return z;
    }
    public void setX(double x) {
        this.x = x;
    }
    public void setZ(double z) {
        this.z = z;
    }

    public void setRobotTilt(double front, double side) {
        frontTilt = front;
        sideTilt = side;
    }

}
