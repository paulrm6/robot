package robot;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;

/**
 * Created by Paul on 02/12/2015.
 * I declare that this code is my own work.
 * prmacdonald1@sheffield.ac.uk
 * 1350155458
 */

public class Table {
    private double x, z, h, w, d;
    private Texture table;
    private Mesh meshCube, meshTableTop;
    private RenderMesh cube, tableTop;

    public Table(double x, double z, double h, double w, double d) {
        this.x = x;
        this.z = z;
        this.h = h;
        this.w = w;
        this.d = d;
    }

    public void create(GL2 gl) {
        table = RobotRestaurantScene.loadTexture(gl, "table.jpg");
        meshCube = ProceduralMeshFactory.createHardCube();
        cube = new RenderMesh(meshCube, table);
        meshTableTop = ProceduralMeshFactory.createPlane(w, d, 100, 100, 1, 1);  // Create the mesh structure
        tableTop = new RenderMesh(meshTableTop, table);    // Create a new Render object for the mesh
        tableTop.initialiseDisplayList(gl, true);
    }

    public void render(GL2 gl) {
        gl.glPushMatrix();
        gl.glTranslated(x, h, z);
        //draw tablebody
        gl.glPushMatrix();
        tableTop.renderDisplayList(gl);
        gl.glPopMatrix();
        for (int i = -1; i < 2; i += 2) {
            for (int n = -1; n < 2; n += 2) {
                gl.glPushMatrix();
                gl.glTranslated(-w * (0.5 - 0.025) * i, -h * 0.5, -d * (0.5 - 0.025) * n);
                gl.glScaled(0.05 * w, h, 0.05 * d);
                cube.renderImmediateMode(gl, true);
                gl.glPopMatrix();
            }
        }
        gl.glPopMatrix();
    }

    public double getX() {
        return x;
    }

    public double getZ() {
        return z;
    }

    public double getW() {
        return w;
    }

    public double getD() {
        return d;
    }
}
