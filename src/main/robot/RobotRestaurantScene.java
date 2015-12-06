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
    //Create some variables
    private GLU glu = new GLU();
    private GLUT glut = new GLUT();
    private Camera camera;
    private AnimationScene animationScene = new AnimationScene();
    private RobotWaiter theRobot = new RobotWaiter();
    private Restaurant restaurant;
    private boolean nearTable;
    //Create instances of the tables
    private Table[] tables = {
            new Table(2,10,2.0,3.5,3),
            new Table(-7,6,2.0,3.5,3),
            new Table(-3,-7,2.0,3.5,3)};
    //Create instances of the lights
    private Light[] globalLights = {
            new Light(GL2.GL_LIGHT0, new float[]{0.0f, 10.0f, 0.0f, 0.0f}),
            new Light(GL2.GL_LIGHT1, new float[]{-10.0f, 0.0f, -10.0f, 0.0f}),
            new Light(GL2.GL_LIGHT2, new float[]{10.0f, 0.0f, 10.0f, 0.0f})};
    private Light[] spots = {
            new Light(GL2.GL_LIGHT3,
                    new float[]{-3.0f, 9.0f, -7.0f, 1.0f}, 25f,
                    new float[]{0.0f, -9.0f, 0.0f}),
            new Light(GL2.GL_LIGHT4,
                    new float[]{-7.0f, 9.0f, 6.0f, 1.0f}, 25f,
                    new float[]{0.0f, -9.0f, 0.0f})};
    private Light robotLight = new Light(GL2.GL_LIGHT5,
                        new float[] {0.0f,4.6f,0.0f,1.0f},20f,
                        new float[] {0.0f,0.0f,1.0f});

    /**
     * Constructor. Sets the camera object and creates the world objects
     * @param gl openGL context
     * @param camera The camera object
     */
    public RobotRestaurantScene(GL2 gl, Camera camera) {
        //initiate and create the restaurant
        restaurant = new Restaurant(gl,9,30,30);
        restaurant.create(gl);
        //create the tables
        for(Table table: tables) {
            table.create(gl);
        }
        this.camera = camera;
    }

    /**
     * A method to start the animation
     */
    public void startAnim() {
        //Starts the animator
        animationScene.startAnimation();
    }

    /**
     * A method to stop/pause the animation
     */
    public void stopAnim() {
        //Pauses the animator
        animationScene.pauseAnimation();
    }

    /**
     * A method to reset the animation to the starting values
     */
    public void resetAnim() {
        //Resets the animator
        animationScene.reset();
        //Replaces the old robot waiter with a new one to clean params
        theRobot = new RobotWaiter();
    }

    public void render(GL2 gl, boolean withWorldLighting, boolean withRobotLight, boolean robotPerspective) {
        gl.glClear(GL.GL_COLOR_BUFFER_BIT|GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        //If perspective is from the robot
        if (robotPerspective) {
            glu.gluLookAt(theRobot.getX(), 3.4f, theRobot.getZ(),
                    theRobot.getX()+theRobot.getLookX(), 2.7f,  theRobot.getZ()+theRobot.getLookZ(),
                    0.0, 1.0, 0.0);
        } else {
            camera.view(glu);  //If perspective is not from the robot
        }
        //deploy the lights
        deployLights(gl,withWorldLighting,withRobotLight);
        //render the robot, restaurant and tables
        gl.glEnable(GL.GL_CULL_FACE);
        theRobot.draw(gl, glut, withRobotLight);
        restaurant.render(gl);
        gl.glDisable(GL.GL_CULL_FACE);
        for(Table table: tables) {
            table.render(gl);
        }
    }

    /**
     * A method to deploy and set the lights
     * @param gl openGL context
     * @param withWorldLight boolean value, true if world lights should be on
     * @param withRobotLight boolean value, true if robot light should be on
     */
    private void deployLights(GL2 gl,boolean withWorldLight, boolean withRobotLight) {
        //Iterate through the spotlights
        for(Light spot: spots) {
            //Deploy the spotlight
            spot.deploy(gl,glut,true);
        }
        //Iterate through the world lights
        for(Light light: globalLights) {
            //Set the world light on or off
            light.setOn(withWorldLight);
            //Deploy the world light
            light.deploy(gl,glut,false);
        }
        //Set the position of the robot light (based on the position of the robot
        robotLight.setPosition(new float[] {(float)theRobot.getX(),4.6f,(float)theRobot.getZ(),1f});
        //Set the direction of the robot light (based on the direction of the robot)
        robotLight.setSpotDirection(new float[] {(float)theRobot.getLookX(),-0.7f,(float)theRobot.getLookZ()});
        //Set the robot light on or off
        robotLight.setOn(withRobotLight);
        //Deploy the robot light
        robotLight.deploy(gl,glut,false);
    }

    public void update() {
        animationScene.update();
        theRobot.setRotate(Math.toRadians(animationScene.getParam(AnimationScene.ROBOT_ROTATE)));
        theRobot.setX(animationScene.getParam(AnimationScene.ROBOT_X_PARAM));
        theRobot.setZ(animationScene.getParam(AnimationScene.ROBOT_Z_PARAM));
        theRobot.setRobotTilt(animationScene.getParam(AnimationScene.ROBOT_LEAN_F),
                animationScene.getParam(AnimationScene.ROBOT_LEAN_S));
        theRobot.setHeadTilt(animationScene.getParam(AnimationScene.ROBOT_HEAD));
        theRobot.setTrayArm(animationScene.getParam(AnimationScene.ROBOT_TRAY_ARM));
        theRobot.updateLookXZ();
    }

    /**
     * A method to load textures.
     * @param gl openGL context
     * @param filename the filename to load
     * @return the texture as Texture
     */
    public static Texture loadTexture(GL2 gl, String filename) {
        Texture tex = null;
        // since file loading is involved, must use try...catch
        try {
            File f = new File(filename);
            BufferedImage img = ImageIO.read(f); // read file into BufferedImage
            ImageUtil.flipImageVertically(img); // flips image vertically
            // No mip-mapping.
            tex = AWTTextureIO.newTexture(GLProfile.getDefault(), img, false);
            tex.setTexParameteri(gl, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
            tex.setTexParameteri(gl, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
        }
        catch(Exception e) {
            System.out.println("Error loading texture " + filename);
        }
        return tex;
    }
}
