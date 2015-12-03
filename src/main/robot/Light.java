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

    public Light (int index, float[] position) {
        this.index = index;
        this.position = position.clone();
    }

    public void spotlight(float angle, float[] direction) {
        //Make sure light is set to positional
        if(position[3] != 1) {
            position[3] = 1;
        }
        //Set variables defining the spotlights behaviour
        spotAngle = angle;
        spotDirection = direction.clone();
        spotlight = true;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public void setEnable(GL2 gl,boolean enable) {
        if(enable) {
            gl.glEnable(index);
        } else {
            gl.glDisable(index);
        }
    }

    public void deploy(GL2 gl, GLUT glut, boolean visible) {
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
        } else {
            //Disable light
            gl.glDisable(index);
        }
    }
}
