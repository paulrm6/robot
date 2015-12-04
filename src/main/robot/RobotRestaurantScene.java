package robot;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.awt.ImageUtil;
import com.jogamp.opengl.util.gl2.GLUT;
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

public class RobotRestaurantScene {
    private final double MAX_LEAN = 15;
    private GLU glu = new GLU();
    private GLUT glut = new GLUT();
    private RobotWaiter theRobot = new RobotWaiter();
    private Table table1,table2,table3;
    private Restaurant restaurant;
    private Light global1,global2,spot1,spot2,robotLight1;
    private double frontLean=0,sideLean = 0;
    private double addTilt = 1;

    public RobotRestaurantScene(GL2 gl) {
        float[] spot1position = {-3,10,-7,1};
        float[] spot1direction = {0,-10,0};
        float[] spot2position = {-7,10,6,1};
        float[] spot2direction = {0,-10,0};
        spot1 = new Light(GL2.GL_LIGHT2,spot1position,20f,spot1direction);
        spot2 = new Light(GL2.GL_LIGHT3,spot2position,20f,spot2direction);
        float[] globalPos0 = {10,10,10,0};
        float[] globalPos1 = {-10,-10,-10,0};
        global1 = new Light(GL2.GL_LIGHT0,globalPos0);
        global2 = new Light(GL2.GL_LIGHT1,globalPos1);
        float[] robotLight1Pos = {(float)theRobot.getX(),3.7f,(float)theRobot.getZ(),1f};
        float[] robotLight1Dir = {(float)theRobot.getLookX(),0,(float)theRobot.getLookZ()};
        robotLight1 = new Light(GL2.GL_LIGHT4,robotLight1Pos,20f,robotLight1Dir);
        restaurant = new Restaurant(gl,9,25,30);
        restaurant.create(gl);
        table1 = new Table(2,10,2.5,3.5,3);
        table2 = new Table(-7,6,2.5,3.5,3);
        table3 = new Table(-3,-7,2.5,3.5,3);
        table1.create(gl);
        table2.create(gl);
        table3.create(gl);
    }

    public void render(GL2 gl, boolean withWorldLighting, boolean withRobotLight, boolean robotPerspective) {
        gl.glClear(GL.GL_COLOR_BUFFER_BIT|GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        //If perspective is from the robot
        if (robotPerspective) {
            glu.gluLookAt(theRobot.getX(), 3.9, theRobot.getZ(), theRobot.getLookX(), 3.7, theRobot.getLookZ(), 0.0,1.0,0.0);
        }
        //If perspective is arbitrary
        else {
            glu.gluLookAt(0.0, 15.0, 40.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
        }

        spot1.deploy(gl);
        spot2.deploy(gl);
        global1.deploy(gl);
        global1.setOn(withWorldLighting);
        global2.deploy(gl);
        global2.setOn(withWorldLighting);
        float[] robotLight1Pos = {(float)theRobot.getX(),3.7f,(float)theRobot.getZ(),1f};
        float[] robotLight1Dir = {(float)theRobot.getLookX(),-0.2f,(float)theRobot.getLookZ()};
        robotLight1.setPosition(robotLight1Pos);
        robotLight1.setSpotDirection(robotLight1Dir);
        robotLight1.deploy(gl);
        robotLight1.setOn(withRobotLight);


        gl.glPushMatrix();
            theRobot.draw(gl, glut, withRobotLight);
        gl.glPopMatrix();
        restaurant.render(gl);
        table1.render(gl);
        table2.render(gl);
        table3.render(gl);
        drawAxes(gl);

    }

    public void drawAxes(GL2 gl) {
        gl.glDisable(GL2.GL_LIGHTING);
        double x = 1.5, y = 1.5, z = 3;
        gl.glLineWidth(4);
        gl.glBegin(GL2.GL_LINES);
        gl.glColor3d(1,0,0);
        gl.glVertex3d(0,0,0);
        gl.glVertex3d(x,0,0);
        gl.glColor3d(0,1,0);
        gl.glVertex3d(0,0,0);
        gl.glVertex3d(0,y,0);
        gl.glColor3d(0,0,1);
        gl.glVertex3d(0,0,0);
        gl.glVertex3d(0,0,z);
        gl.glEnd();
        gl.glLineWidth(1);
        gl.glEnable(GL2.GL_LIGHTING);
    }
    public void update() {
        theRobot.setRotate((theRobot.getRotate()+1)%360);
        theRobot.updateLookXZ();
        //if (sideLean == MAX_LEAN || sideLean == -MAX_LEAN) addTilt = -addTilt;
        //sideLean += addTilt;
        //theRobot.setRobotTilt(0.3,sideLean);
    }

    public static Texture loadTexture(GL2 gl, String filename) {
        Texture tex = null;
        // since file loading is involved, must use try...catch
        try {
            File f = new File(filename);

            // The following line results in a texture that is flipped vertically (i.e. is upside down)
            // due to OpenGL and Java (0,0) position being different:
            //tex = TextureIO.newTexture(new File(filename), false);

            // So, instead, use the following three lines which flip the image vertically:
            BufferedImage img = ImageIO.read(f); // read file into BufferedImage
            ImageUtil.flipImageVertically(img);

            // No mip-mapping.
            tex = AWTTextureIO.newTexture(GLProfile.getDefault(), img, false);

            // Different filter settings can be used to give different effects when the texture
            // is applied to a set of polygons.
            tex.setTexParameteri(gl, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
            tex.setTexParameteri(gl, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);

        }
        catch(Exception e) {
            System.out.println("Error loading texture " + filename);
        }
        return tex;
    } // end of loadTexture()
}
