package robot;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

/**
 * Created by Paul on 02/12/2015.
 * I declare that this code is my own work.
 * prmacdonald1@sheffield.ac.uk
 * 1350155458
 */
public class Table {
    private int x, z;
    private final double depth = 0.5;
    public Table(int x, int z) {
        this.x = x;
        this.z = z;
    }
    public void draw(GL2 gl, GLU glu) {
        gl.glPushMatrix();

        gl.glPopMatrix();
    }
    public void drawPart(GL2 gl, GLU glu, int height, int width) {
    }
}
