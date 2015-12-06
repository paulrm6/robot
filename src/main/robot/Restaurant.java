package robot;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;

/**
 * Created by Paul on 10/11/2015.
 * I declare that this code is my own work.
 * prmacdonald1@sheffield.ac.uk
 * 1350155458
 */

public class Restaurant {
    private final int POINTS_X = 300, POINTS_Y = 300;
    private final double TILES_X = 1, TILES_Y = 1;
    private double height, width, depth;
    private Mesh meshSides, meshEnds, meshTB;
    private RenderMesh sides, ends, top, bottom;
    private Texture wallpaper, ceiling, floor;

    public Restaurant(GL2 gl, double h, double w, double d) {
        height = h;
        width = w;
        depth = d;
    }

    public void create(GL2 gl) {
        wallpaper = RobotRestaurantScene.loadTexture(gl, "wallpaper.jpg");
        floor = RobotRestaurantScene.loadTexture(gl, "woodFloor.jpg");
        ceiling = RobotRestaurantScene.loadTexture(gl, "ceiling.jpg");
        meshSides = ProceduralMeshFactory.createPlane(height,depth,POINTS_X,POINTS_Y,TILES_X,TILES_Y);  // Create the mesh structure
        sides = new RenderMesh(meshSides, wallpaper);    // Create a new Render object for the mesh
        sides.initialiseDisplayList(gl, true);
        meshEnds = ProceduralMeshFactory.createPlane(width,height,POINTS_X,POINTS_Y,TILES_X,TILES_Y);  // Create the mesh structure
        ends = new RenderMesh(meshEnds, wallpaper);    // Create a new Render object for the mesh
        ends.initialiseDisplayList(gl, true);
        meshTB = ProceduralMeshFactory.createPlane(width,depth,POINTS_X,POINTS_Y,TILES_X,TILES_Y);  // Create the mesh structure
        top = new RenderMesh(meshTB, ceiling);    // Create a new Render object for the mesh
        top.initialiseDisplayList(gl, true);
        bottom = new RenderMesh(meshTB, floor);    // Create a new Render object for the mesh
        bottom.initialiseDisplayList(gl, true);
    }

    public void render(GL2 gl) {
        //left wall
        gl.glPushMatrix();
            gl.glTranslated(-width/2,height/2,0);
            gl.glRotated(270,0,0,1);
            sides.renderDisplayList(gl);
        gl.glPopMatrix();
        //right wall
        gl.glPushMatrix();
            gl.glTranslated(width/2,height/2,0);
            gl.glRotated(90,0,0,1);
            sides.renderDisplayList(gl);
        gl.glPopMatrix();
        //back wall
        gl.glPushMatrix();
            gl.glTranslated(0,height/2,-depth/2);
            gl.glRotated(90,1,0,0);
            ends.renderDisplayList(gl);
        gl.glPopMatrix();
        //front wall
        gl.glPushMatrix();
            gl.glTranslated(0,height/2,depth/2);
            gl.glRotated(270,1,0,0);
            ends.renderDisplayList(gl);
        gl.glPopMatrix();
        //floor
        gl.glPushMatrix();
            bottom.renderDisplayList(gl);
        gl.glPopMatrix();
        //ceiling
        gl.glPushMatrix();
            gl.glTranslated(0,height,0);
            gl.glRotated(180,1,0,0);
            top.renderDisplayList(gl);
        gl.glPopMatrix();
    }
}
