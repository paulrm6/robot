package robot;

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
    private double x = 0;
    private double z = 0;
    private double lookX = 0;
    private double lookZ = 1;
    private double rotate = 0;
    private double sideTilt=0, frontTilt=0;
    private double headTilt = 0;
    private double trayArm = 0;
    public RobotWaiter() {
    }
    public void draw(GL2 gl, GLUT glut, boolean withRobotLight) {
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, AMBIENT, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, COLOUR, 0);

        gl.glPushMatrix();
            gl.glTranslated(x,0,z);
            gl.glRotated(rotate,0,1,0);
            gl.glRotated(270,0,1,0);
            drawCone(gl,glut,1,1.2);
            gl.glTranslated(0,1,0);
            gl.glPushMatrix();
                gl.glRotated(frontTilt,0,0,1);
                gl.glRotated(sideTilt,1,0,0);
                drawCylinder(gl,glut,2.5,0.7); //draw body
                //draw left arm
                drawArm(gl,glut,true,-15,(-60-sideTilt),true);
                //draw right arm
                drawArm(gl,glut,false,-50,(-40-sideTilt),false);
                //draw neck
                gl.glPushMatrix();
                    gl.glTranslated(0.15,2.5,0);
                    drawCylinder(gl,glut,0.2,0.2);
                    //draw head
                    gl.glPushMatrix();
                        gl.glTranslated(0,0.2,0);
                        gl.glRotated(headTilt,0,0,1);
                        drawRectangle(gl,glut,1,0.9,0.9,0);
                        //set eye emission
                        if(withRobotLight) {
                            float[] yellowish = {1.0f, 1.0f, 0.6f, 1.0f};
                            gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, yellowish, 0);
                        }
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

    private void drawArm(GL2 gl, GLUT glut, boolean leftSide, double rotateArm, double rotateForearm, boolean closedHand) {
        double angle;
        int side;
        if (leftSide) {
            angle = 90;
            side = 1;
            rotateArm=rotateArm+0.5*trayArm-sideTilt;

        } else {
            angle = 270;
            side = -1;
        }
        int clawAngle;
        if (closedHand) {
            clawAngle = -5;
        } else {
            clawAngle = 10;
        }
        gl.glPushMatrix();
            gl.glTranslated(0,2,side*0.6);
            if(leftSide) {
                gl.glRotated(trayArm - frontTilt, 0, 0, 1);
            }
            gl.glRotated(rotateArm,1,0,0); // rotate arm
            drawRectangle(gl,glut,1,0.25,0.25,angle);
            //draw forearm
            gl.glPushMatrix();
                gl.glTranslated(0,0,side*0.9);
                gl.glRotated(rotateForearm,1,0,0); // rotate forearm
                drawRectangle(gl,glut,1,0.2,0.2,angle);
                //draw claw
                gl.glTranslated(0,0,side*0.95);
                gl.glPushMatrix();
                    gl.glTranslated(0,side*0.05,0);
                    gl.glRotated(clawAngle,1,0,0);
                    drawRectangle(gl,glut,0.3,0.05,0.05,angle);
                gl.glPopMatrix();
                gl.glPushMatrix();
                    gl.glTranslated(0,-side*0.05,0);
                    gl.glRotated(-clawAngle,1,0,0);
                    drawRectangle(gl,glut,0.3,0.05,0.05,angle);
                gl.glPopMatrix();
                gl.glPushMatrix();
                if(leftSide) {
                    gl.glTranslated(0,0,0.3);
                    gl.glRotated(rotateArm+rotateForearm,-1,0,0);
                    gl.glRotated(trayArm,0,0,-1);
                    drawTray(gl,glut);
                }
                gl.glPopMatrix();
            gl.glPopMatrix();
        gl.glPopMatrix();
    }

    private void drawCone(GL2 gl, GLUT glut, double h, double r) {
        gl.glPushMatrix();
            gl.glRotated(270,1,0,0);
            glut.glutSolidCone(h,r,RESOLUTION,RESOLUTION);
        gl.glPopMatrix();
    }

    private void drawTray(GL2 gl, GLUT glut) {
        gl.glMaterialfv(GL2.GL_FRONT,GL2.GL_DIFFUSE, new float[] {0,0,0,1},0);//black
        drawCylinder(gl,glut,0.1,0.7);
        gl.glTranslated(0,0.1,0);
        gl.glPushMatrix();
            gl.glMaterialfv(GL2.GL_FRONT,GL2.GL_DIFFUSE, new float[] {0.7f,0.7f,1,1},0);//bluey
            drawCylinder(gl,glut,0.3,0.05);
            gl.glTranslated(0,0.6,0);
            gl.glRotated(180,0,0,1);
            drawCone(gl,glut,0.2,0.3);
        gl.glPopMatrix();
        gl.glMaterialfv(GL2.GL_FRONT,GL2.GL_DIFFUSE, COLOUR,0);
    }

    private void drawRectangle(GL2 gl, GLUT glut, double l, double w, double d, double r) {
        gl.glPushMatrix();
            gl.glRotated(r,1,0,0);
            gl.glScaled(w,l,d);
            gl.glTranslated(0,0.5,0);
            glut.glutSolidCube(1);
        gl.glPopMatrix();
    }
    private void drawCylinder(GL2 gl, GLUT glut, double h, double r) {
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

    public double getRotate() {
        return rotate;
    }

    public void setRotate(double rotate) {
        this.rotate = Math.toDegrees(rotate);
    }

    public double getLookX() {
        return lookX;
    }

    public double getLookZ() {
        return lookZ;
    }

    public void setHeadTilt(double headTilt) {
        this.headTilt = headTilt;
    }

    public void setTrayArm(double trayArm) {
        this.trayArm = trayArm;
    }

    public void updateLookXZ() {
        double radAngle = Math.toRadians(rotate);
        lookX = Math.sin(radAngle);
        lookZ = Math.cos(radAngle);
    }

    public void setRobotTilt(double front, double side) {
        frontTilt = front;
        sideTilt = side;
    }

}
