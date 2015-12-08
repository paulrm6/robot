package robot;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;

/**
 * Created by Paul on 02/12/2015.
 * I declare that this code is my own work.
 * prmacdonald1@sheffield.ac.uk
 * 1350155458
 *
 * A class to create and display a table
 */

public class Table {
    private double x, z, h, w, d;
    private RenderMesh cube, tableTop;

    /**
     * Constructor
     * @param x x-value (center of the table)
     * @param z z-value (center of the table)
     * @param h the height of the table
     * @param w the width of the table
     * @param d the depth of the table
     */
    public Table(double x, double z, double h, double w, double d) {
        this.x = x;
        this.z = z;
        this.h = h;
        this.w = w;
        this.d = d;
    }

    /**
     * A function to create each table
     * @param gl openGL context
     */
    public void create(GL2 gl) {
        //Load the table texture
        Texture table = RobotRestaurantScene.loadTexture(gl, "table.jpg");
        //Create the table top
        Mesh meshCube = ProceduralMeshFactory.createHardCube();
        cube = new RenderMesh(meshCube, table); //Create a new render object
        cube.initialiseDisplayList(gl,true);
        //Create a leg of the table
        Mesh meshTableTop = ProceduralMeshFactory.createPlane(w, d, 100, 100, 2, 2);
        tableTop = new RenderMesh(meshTableTop, table); //Create a new render object
        tableTop.initialiseDisplayList(gl, true);
    }

    /**
     * A function to render the table
     * @param gl openGL context
     */
    public void render(GL2 gl) {
        gl.glPushMatrix();
            gl.glTranslated(x, h, z);
            //draw table body
            gl.glPushMatrix();
                tableTop.renderDisplayList(gl);
            gl.glPopMatrix();
            //draw the table legs
            for (int i = -1; i < 2; i += 2) {
                for (int n = -1; n < 2; n += 2) {
                    gl.glPushMatrix();
                        gl.glTranslated(-w * (0.5 - 0.025) * i, -h * 0.5, -d * (0.5 - 0.025) * n);
                        gl.glScaled(0.05 * w, h, 0.05 * d);
                        cube.renderDisplayList(gl);
                    gl.glPopMatrix();
                }
            }
        gl.glPopMatrix();
    }
}
