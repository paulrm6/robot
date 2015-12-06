package robot;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

/**
 * Created by Paul on 02/12/2015.
 * I declare that this code is my own work.
 * Class and functions have been modelled from
 * Dr Steve Maddock's Light class from labs.
 * prmacdonald1@sheffield.ac.uk
 * 1350155458
 */
public class Light{
    //Initiate some final variables
    private final float[] WHITE = {1.0f,1.0f,1.0f};
    private final float[] BLACK = {0.0f,0.0f,0.0f};
    //Initiate some variables
    private int index;
    private boolean on = true,
            spotlight = false;
    private float[] position,
            ambient = BLACK,
            diffuse = WHITE,
            specular = WHITE,
            spotDirection;
    private float spotAngle;

    /**
     * Constructor.
     * Index should be in range GL2.GL_LIGHT0 to GL2.GL_LIGHT8.
     * @param index Index value for the openGL light
     * @param position Position of the light source
     */
    public Light (int index, float[] position) {
        this.index = index;
        this.position = position.clone();
    }

    /**
     * Constructor for a spotlight.
     * @param index Index value for the openGL light
     * @param position Position of the light source position[3] should be 1
     * @param angle Angle of the cut-off (a smaller angle means a tighter spotlight)
     * @param direction Direction that the spotlight is pointing in
     */
    public Light (int index, float[] position, float angle, float[] direction) {
        this.index = index;
        this.position = position.clone();
        //Make sure light is set to positional
        if(this.position[3] != 1) {
            this.position[3] = 1;
        }
        //Set variables defining the spotlights behaviour
        spotAngle = angle;
        spotDirection = direction.clone();
        spotlight = true;
    }

    /**
     * A function to turn on and off the current object
     * @param on True to turn light on, false to turn light off
     */
    public void setOn(boolean on) {
        this.on = on;
    }

    /**
     * A function to enable or disable the current openGL light
     * @param gl openGL context
     * @param enable True to enable light, false to disable
     */
    public void setEnable(GL2 gl,boolean enable) {
        if(enable) {
            gl.glEnable(index);
        } else {
            gl.glDisable(index);
        }
    }

    /**
     * If the light is on, then call all the relevant openGL commands
     * to enable and set the params for this light
     * @param gl openGL context
     */
    public void deploy(GL2 gl, GLUT glut, boolean show) {
        //If light is on
        if(on) {
            //Enable light and set properties
            gl.glEnable(index);
            gl.glLightfv(index,GL2.GL_POSITION,position,0);
            gl.glLightfv(index,GL2.GL_AMBIENT,ambient,0);
            gl.glLightfv(index,GL2.GL_DIFFUSE,diffuse,0);
            gl.glLightfv(index,GL2.GL_SPECULAR,specular,0);
            //If the light is a spotlight
            if (spotlight) {
                //Set extra properties
                gl.glLightf(index,GL2.GL_SPOT_CUTOFF, spotAngle);
                gl.glLightfv(index,GL2.GL_SPOT_DIRECTION, spotDirection,0);
            }
            if (show) {
                float[] yellowish = {1.0f, 1.0f, 0.6f, 1.0f};
                float[] black = {0.0f, 0.0f, 0.0f, 1.0f};
                gl.glPushMatrix();
                    gl.glTranslated(position[0],position[1],position[2]);
                    gl.glPushMatrix();
                        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, black, 0);
                        gl.glRotated(90,1,0,0);
                        glut.glutSolidCylinder(0.05,1,20,20);
                    gl.glPopMatrix();
                    gl.glTranslated(0,-1,0);
                    gl.glPushMatrix();
                        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, yellowish, 0);
                        gl.glScaled(0.2,0.2,0.2);
                        glut.glutSolidSphere(1,20,20);
                    gl.glPopMatrix();
                gl.glPopMatrix();
                gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, black, 0);
            }
        } else {
            //Disable light
            gl.glDisable(index);
        }
    }

    public void setPosition(float[] position) {
        this.position = position.clone();
    }

    public void setSpotDirection(float[] spotDirection) {
        this.spotDirection = spotDirection.clone();
    }
}
