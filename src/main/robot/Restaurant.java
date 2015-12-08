package robot;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;

/**
 * Created by Paul on 10/11/2015.
 * I declare that this code is my own work.
 * prmacdonald1@sheffield.ac.uk
 * 1350155458
 *
 * A class to create and display the Restaurant
 */

public class Restaurant {
    private final double TILES_X = 4, TILES_Y = 4;
    private double h, w, d;
    private RenderMesh sides, ends, top, bottom;

    /**
     * Constructor.
     * @param h height of the room
     * @param w width of the room
     * @param d depth of the room
     */
    public Restaurant(double h, double w, double d) {
        this.h = h;
        this.w = w;
        this.d = d;
    }

    /**
     * A function to create the planes of the room
     * @param gl openGL context
     */
    public void create(GL2 gl) {
        //Set the textures
        Texture wallpaper = RobotRestaurantScene.loadTexture(gl, "texture/wallpaper.jpg");
        Texture floor = RobotRestaurantScene.loadTexture(gl, "texture/checkerfloor.jpg");
        Texture ceiling = RobotRestaurantScene.loadTexture(gl, "texture/ceiling.jpg");
        //Create the sides
        Mesh meshSides = ProceduralMeshFactory.createPlane(h, d, 250, 250, TILES_X, TILES_Y); //creates a mesh
        sides = new RenderMesh(meshSides, wallpaper); //creates a new render object
        sides.initialiseDisplayList(gl, true);
        //Create the ends
        Mesh meshEnds = ProceduralMeshFactory.createPlane(w, h, 250, 250, TILES_X, TILES_Y); //creates a mesh
        ends = new RenderMesh(meshEnds, wallpaper); //creates a new render object
        ends.initialiseDisplayList(gl, true);
        //Create the ceiling and floor
        Mesh meshTop = ProceduralMeshFactory.createPlane(w, d, 100, 100, TILES_X, TILES_Y); //creates a mesh
        top = new RenderMesh(meshTop, ceiling); //creates a new render object
        top.initialiseDisplayList(gl, true);
        Mesh meshFloor = ProceduralMeshFactory.createPlane(w, d, 500, 500, TILES_X, TILES_Y); //creates a mesh
        bottom = new RenderMesh(meshFloor, floor); //creates a new render object
        bottom.initialiseDisplayList(gl, true);
    }

    /**
     * A function to render each plane of the room and postition it correctly
     * @param gl openGL context
     */
    public void render(GL2 gl) {
        //left wall
        gl.glPushMatrix();
            gl.glTranslated(-w / 2, h / 2, 0);
            gl.glRotated(270, 0, 0, 1);
            sides.renderDisplayList(gl); //render the left wall
        gl.glPopMatrix();
        //right wall
        gl.glPushMatrix();
            gl.glTranslated(w / 2, h / 2, 0);
            gl.glRotated(90, 0, 0, 1);
            sides.renderDisplayList(gl); //render the right wall
        gl.glPopMatrix();
        //back wall
        gl.glPushMatrix();
            gl.glTranslated(0, h / 2, -d / 2);
            gl.glRotated(90, 1, 0, 0);
            ends.renderDisplayList(gl); //render the back wall
        gl.glPopMatrix();
        //front wall
        gl.glPushMatrix();
            gl.glTranslated(0, h / 2, d / 2);
            gl.glRotated(270, 1, 0, 0);
            ends.renderDisplayList(gl); //render the front wall
        gl.glPopMatrix();
        //floor
        gl.glPushMatrix();
            bottom.renderDisplayList(gl); //render the floor
        gl.glPopMatrix();
        //ceiling
        gl.glPushMatrix();
            gl.glTranslated(0, h, 0);
            gl.glRotated(180, 1, 0, 0);
            top.renderDisplayList(gl); //render the ceiling
        gl.glPopMatrix();
    }
}
