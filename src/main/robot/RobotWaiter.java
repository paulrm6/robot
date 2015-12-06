package robot;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

/**
 * Created by Paul on 10/11/2015.
 * I declare that this code is my own work.
 * prmacdonald1@sheffield.ac.uk
 * 1350155458
 *
 * A class the draw the robot
 */

public class RobotWaiter {
    //Create some variables
    private final float[] AMBIENT = {0.2f, 0.2f, 0.2f, 1.0f}; //dark grey
    private final float[] ROBOT_COLOUR = {0.8f, 0.8f, 0.8f, 1.0f}; //grey
    private final int RESOLUTION = 100;
    private double x = 0;
    private double z = 0;
    private double rotate = 0;
    private double sideTilt = 0, frontTilt = 0;
    private double headTilt = 0;
    private double trayArm = 0;
    private boolean perspective;

    public RobotWaiter() {
    }

    /**
     * A method to draw the robot
     * @param gl openGL context
     * @param glut glut
     * @param withRobotLight boolean, true if the robot light is on
     */
    public void draw(GL2 gl, GLUT glut, boolean withRobotLight) {
        //Set some material properties
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, AMBIENT, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, ROBOT_COLOUR, 0);

        gl.glPushMatrix();
            gl.glTranslated(x, 0, z);//change robot position
            gl.glRotated(rotate, 0, 1, 0);//change robot direction
            gl.glRotated(270, 0, 1, 0);
            drawCone(gl, glut, 1, 1.2);//draw the base
            gl.glTranslated(0, 1, 0);//move everything up by 1
            gl.glPushMatrix();
                gl.glRotated(frontTilt, 0, 0, 1);//Add tilt to the front
                gl.glRotated(sideTilt, 1, 0, 0);//Add tilt to the side
                drawCylinder(gl, glut, 2.5, 0.7); //draw body
                drawArm(gl, glut, true, -15, (-60 - sideTilt), true);//draw left arm
                drawArm(gl, glut, false, -50, (-40 - sideTilt), false);//draw right arm
                if (!perspective) { //if we're not looking from the robot perspective
                    //draw neck
                    gl.glPushMatrix();
                        gl.glTranslated(0.15, 2.5, 0);
                        drawCylinder(gl, glut, 0.2, 0.2);
                        //draw head
                        gl.glPushMatrix();
                            gl.glTranslated(0, 0.2, 0);
                            gl.glRotated(headTilt, 0, 0, 1);//add head tilt
                            drawRectangle(gl, glut, 1, 0.9, 0.9, 0);
                            //set eye emission if robot light is on
                            if (withRobotLight) {
                                float[] yellowish = {1.0f, 1.0f, 0.6f, 1.0f};
                                gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, yellowish, 0);
                            }
                            gl.glTranslated(0.45, 0.7, 0);
                            gl.glPushMatrix();//draw left eye
                                gl.glTranslated(0, 0, 0.2);
                                glut.glutSolidSphere(0.1, RESOLUTION, RESOLUTION);
                            gl.glPopMatrix();
                            gl.glPushMatrix();//draw right eye
                                gl.glTranslated(0, 0, -0.2);
                                glut.glutSolidSphere(0.1, RESOLUTION, RESOLUTION);
                            gl.glPopMatrix();
                        gl.glPopMatrix();
                    gl.glPopMatrix();
                }
            gl.glPopMatrix();
        gl.glPopMatrix();
    }

    /**
     * A method to draw an arm
     * @param gl openGL context
     * @param glut glut
     * @param leftSide true if arm is on the left
     * @param rotateArm angle of upper arm rotate
     * @param rotateForearm angle of lower arm rotate
     * @param closedHand true if hand is closed
     */
    private void drawArm(GL2 gl, GLUT glut, boolean leftSide, double rotateArm, double rotateForearm, boolean closedHand) {
        double angle;
        int side,clawAngle;
        if (leftSide) {//if left arm (tray arm)
            angle = 90;
            side = 1;
            rotateArm = rotateArm + 0.5 * trayArm;
        } else {//if right arm
            angle = 270;
            side = -1;
        }
        if (closedHand) {//If hand is closed
            clawAngle = -5;
        } else {//If hand is open
            clawAngle = 10;
        }
        gl.glPushMatrix();
            gl.glTranslated(0, 2, side * 0.6);
            if (leftSide) {
                gl.glRotated(trayArm - frontTilt, 0, 0, 1);
            }
            gl.glRotated(rotateArm, 1, 0, 0); // rotate arm
            drawRectangle(gl, glut, 1, 0.25, 0.25, angle);//draw upper arm
            gl.glPushMatrix();
                gl.glTranslated(0, 0, side * 0.9);
                gl.glRotated(rotateForearm, 1, 0, 0); // rotate forearm
                drawRectangle(gl, glut, 1, 0.2, 0.2, angle);//draw forearm
                //draw claws
                gl.glTranslated(0, 0, side * 0.95);
                gl.glPushMatrix();
                    gl.glTranslated(0, side * 0.05, 0);
                    gl.glRotated(clawAngle, 1, 0, 0);
                    drawRectangle(gl, glut, 0.3, 0.05, 0.05, angle);//draw claw
                gl.glPopMatrix();
                gl.glPushMatrix();
                    gl.glTranslated(0, -side * 0.05, 0);
                    gl.glRotated(-clawAngle, 1, 0, 0);
                    drawRectangle(gl, glut, 0.3, 0.05, 0.05, angle);//draw other claw
                gl.glPopMatrix();
                if (leftSide) {//If we are on the tray arm
                    gl.glPushMatrix();
                        gl.glTranslated(0, 0, 0.3);
                        gl.glRotated(rotateArm + rotateForearm + sideTilt, -1, 0, 0);//Keep it horizontal
                        gl.glRotated(trayArm, 0, 0, -1);//Keep it horizontal
                        drawTray(gl, glut);//Draw the tray
                    gl.glPopMatrix();
                }
            gl.glPopMatrix();
        gl.glPopMatrix();
    }

    /**
     * Draw the tray
     * @param gl openGL context
     * @param glut glut
     */
    private void drawTray(GL2 gl, GLUT glut) {
        gl.glPushMatrix();
            //change material for the tray
            gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, new float[]{0, 0, 0, 1}, 0);//black
            drawCylinder(gl, glut, 0.1, 0.7);//draw tray
            gl.glTranslated(0, 0.1, 0);
            //change material for the objects
            gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, new float[]{0.7f, 0.7f, 1, 1}, 0);//blueish tinge
            gl.glPushMatrix();//draw glass
                gl.glTranslated(0.2, 0, -0.2);
                drawCylinder(gl, glut, 0.3, 0.05);
                gl.glTranslated(0, 0.5, 0);
                gl.glRotated(180, 0, 0, 1);
                drawCone(gl, glut, 0.2, 0.3);
            gl.glPopMatrix();
            gl.glPushMatrix();//draw can
                gl.glTranslated(0, 0, 0.2);
                drawCylinder(gl, glut, 0.3, 0.1);
            gl.glPopMatrix();
            gl.glPushMatrix();//draw box
                gl.glTranslated(-0.2, 0, 0);
                drawRectangle(gl, glut, 0.4, 0.2, 0.2, 0);
            gl.glPopMatrix();
            //change material back
            gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, ROBOT_COLOUR, 0);//grey
        gl.glPopMatrix();
    }

    /**
     * Draw a cone
     * @param gl openGL context
     * @param glut glut
     * @param h height of the cone
     * @param r radius of the cone
     */
    private void drawCone(GL2 gl, GLUT glut, double h, double r) {
        gl.glPushMatrix();
            gl.glRotated(270, 1, 0, 0);
            glut.glutSolidCone(h, r, RESOLUTION, RESOLUTION);//draw cone
        gl.glPopMatrix();
    }

    /**
     * Draw a rectangle
     * @param gl openGL context
     * @param glut glut
     * @param l length of rectangle
     * @param w width of rectangle
     * @param d depth of rectangle
     * @param r rotation of rectangle
     */
    private void drawRectangle(GL2 gl, GLUT glut, double l, double w, double d, double r) {
        gl.glPushMatrix();
            gl.glRotated(r, 1, 0, 0);
            gl.glScaled(w, l, d);
            gl.glTranslated(0, 0.5, 0);
            glut.glutSolidCube(1);
        gl.glPopMatrix();
    }

    /**
     * Draw a cylinder
     * @param gl openGl context
     * @param glut glut
     * @param h height of cylinder
     * @param r radius of cylinder
     */
    private void drawCylinder(GL2 gl, GLUT glut, double h, double r) {
        gl.glPushMatrix();
            gl.glScaled(r, h, r);
            gl.glRotated(270, 1, 0, 0);
            glut.glutSolidCylinder(1, 1, RESOLUTION, RESOLUTION);
        gl.glPopMatrix();
    }

    /**
     * @return x
     */
    public double getX() {
        return x;
    }

    /**
     * @param x set x value
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return z
     */
    public double getZ() {
        return z;
    }

    /**
     * @param z set z value
     */
    public void setZ(double z) {
        this.z = z;
    }

    /**
     * @param rotate set rotate value (degrees)
     */
    public void setRotate(double rotate) {
        this.rotate = Math.toDegrees(rotate);
    }

    /**
     * @return lookX (radians)
     */
    public double getLookX() {
        return Math.sin(Math.toRadians(rotate));
    }

    /**
     * @return lookZ (radians)
     */
    public double getLookZ() {
        return Math.cos(Math.toRadians(rotate));
    }

    /**
     * @param headTilt set tilt of head (degrees)
     */
    public void setHeadTilt(double headTilt) {
        this.headTilt = headTilt;
    }

    /**
     * @param trayArm set angle of tray arm (degrees)
     */
    public void setTrayArm(double trayArm) {
        this.trayArm = trayArm;
    }

    /**
     * @param perspective set the perspective
     */
    public void setPerspective(boolean perspective) {
        this.perspective = perspective;
    }

    /**
     * @param front set front tilt
     * @param side set side tilt
     */
    public void setRobotTilt(double front, double side) {
        frontTilt = front;
        sideTilt = side;
    }
}
