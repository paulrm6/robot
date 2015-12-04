package robot;

import java.io.File;
import java.awt.image.*;
import javax.imageio.*;
import com.jogamp.opengl.util.awt.*;

import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.texture.*;
import com.jogamp.opengl.util.texture.awt.*;

import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

/**
 * Created by Paul on 02/12/2015.
 * I declare that this code is my own work.
 * prmacdonald1@sheffield.ac.uk
 * 1350155458
 */

public class Table {
    private double x, z, h, w, d;
    private Texture table;
    private Mesh meshCube;
    private RenderMesh cube;
    private Material mat;
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
        Material mat = meshCube.getMaterial();
        mat.setDiffuse(1.0f,1.0f,1.0f,1f);
        cube = new RenderMesh(meshCube, table);
    }

    public void render(GL2 gl) {
        gl.glPushMatrix();
            gl.glTranslated(x,h*1.05,z);
            //draw tablebody
            gl.glPushMatrix();
                gl.glScaled(w,0.1*h,d);
                cube.renderImmediateMode(gl,true);
            gl.glPopMatrix();
            for(int i =-1; i<2;i+=2){
                for(int n =-1; n<2;n+=2) {
                    gl.glPushMatrix();
                    gl.glTranslated(-w * (0.5 - 0.025) * i, -h * 0.475, -d * (0.5 - 0.025)* n);
                    gl.glScaled(0.05 * w, 0.9 * h, 0.05 * d);
                    cube.renderImmediateMode(gl, true);
                    gl.glPopMatrix();
                }
            }
        gl.glPopMatrix();
    }
}
