package robot;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.awt.ImageUtil;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by Paul on 10/11/2015.
 * I declare that this code is my own work.
 * prmacdonald1@sheffield.ac.uk
 * 1350155458
 */
public class Restaurant {
    private final int POINTS_X = 200, POINTS_Y = 200;
    private final double TILES_X = 1, TILES_Y = 1;
    private double height, width, depth;

    public Restaurant(GL2 gl, double h, double w, double d) {
        height = h;
        width = w;
        depth = d;
    }

    public void create(GL2 gl) {
    }

    public void render(GL2 gl) {
    }
}
