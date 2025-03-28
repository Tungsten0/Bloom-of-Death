package BOD;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Canvas3D;
import org.jogamp.java3d.Material;
import org.jogamp.java3d.Node;
import org.jogamp.java3d.PointLight;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.Texture;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Point3f;
import org.jogamp.vecmath.Vector3d;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public class clock extends JPanel {
    private static final long serialVersionUID = 1L;
    private static JFrame frame;

    public static BranchGroup create_Scene() {
        BranchGroup sceneBG = new BranchGroup();
        TransformGroup sceneTG = new TransformGroup();
        sceneTG.addChild(loadClockModel());

        sceneBG.addChild(sceneTG);
        sceneBG.addChild(Commons.add_Lights(Commons.White, 1));

        return sceneBG;
    }

    private static TransformGroup loadClockModel() {
        return loadModel("models/clock.obj", "textures/clock.jpeg", new Vector3d(-0.5, 0.1, -0.3), 1.60, 0.90);
    }
    
    private static TransformGroup loadModel(String modelPath, String texturePath, Vector3d position, double scaleValue, double rotationAngle) {
        TransformGroup modelTG = new TransformGroup();

        try {
            ObjectFile loader = new ObjectFile(ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY);
            Scene modelScene = loader.load(new FileReader(modelPath));

            // Create Appearance with Texture
            Appearance modelAppearance = new Appearance();
            TextureLoader textureLoader = new TextureLoader(texturePath, "RGB", new Canvas3D(SimpleUniverse.getPreferredConfiguration()));
            modelAppearance.setTexture(textureLoader.getTexture());

            // Apply texture to all Shape3D objects
            Iterator<Node> children = modelScene.getSceneGroup().getAllChildren();
            while (children.hasNext()) {
                Node obj = children.next();
                if (obj instanceof Shape3D) {
                    ((Shape3D) obj).setAppearance(modelAppearance);
                }
            }

            // **Reuse a single Transform3D instance**
            Transform3D finalTransform = new Transform3D();
            finalTransform.setTranslation(position);
            finalTransform.setScale(scaleValue);

            Transform3D rotate = new Transform3D();
            rotate.rotY(rotationAngle);
            finalTransform.mul(rotate);

            TransformGroup transformGroup = new TransformGroup(finalTransform);
            transformGroup.addChild(modelScene.getSceneGroup());
            modelTG.addChild(transformGroup);

        } catch (IOException e) {
            System.out.println("Error loading " + modelPath + ": " + e.getMessage());
        }

        return modelTG;
    }

    public clock(BranchGroup sceneBG) {
        Canvas3D canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        SimpleUniverse su = new SimpleUniverse(canvas);
        Commons.define_Viewer(su, new Point3d(2.5d, 0.5d, 3.0d));

        sceneBG.compile();
        su.addBranchGraph(sceneBG);

        setLayout(new BorderLayout());
        add("Center", canvas);
        frame.setSize(800, 800);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        frame = new JFrame("Room with Placeholders, Double bass, Corpse, and Sofa & Pillow");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new clock(create_Scene()));
    }
}




/*
package CodesHS2800;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public class clock extends JPanel {
    private static final long serialVersionUID = 1L;
    private static JFrame frame;
    private static Sound continuousSound; // Sound for continuous ticking
    private static Sound specialSound;   // Sound for 4:00
    private static int currentHour = 0;  // Simulated clock hour
    private static int currentMinute = 0; // Simulated clock minute
    private static boolean isSpecialSoundPlaying = false; // Track if the special sound is playing

    public static BranchGroup create_Scene() {
        BranchGroup sceneBG = new BranchGroup();
        TransformGroup sceneTG = new TransformGroup();
        sceneTG.addChild(loadClockModel()); // Load the clock model

        // Add continuous ticking sound
        continuousSound = createSound("sounds/clockTik.mav", new Point3f(-0.5f, -0.1f, 0.0f));
        sceneTG.addChild(continuousSound);

        // Add special sound for 4:00
        specialSound = createSound("sounds/clock12tik.mav", new Point3f(-0.5f, -0.1f, 0.0f));
        specialSound.setEnable(false); // Disable initially
        sceneTG.addChild(specialSound);

        sceneBG.addChild(sceneTG);
        sceneBG.addChild(Commons.add_Lights(Commons.White, 1)); // Add lights

        // Start the clock simulation
        startClockSimulation();

        return sceneBG;
    }

    private static TransformGroup loadClockModel() {
        return loadModel("models/clock.obj", "textures/clock.jpeg", new Vector3d(-0.5, -0.1, 0.0), 0.25, 0.90);
    }

    private static TransformGroup loadModel(String modelPath, String texturePath, Vector3d position, double scaleValue, double rotationAngle) {
        TransformGroup modelTG = new TransformGroup();

        try {
            ObjectFile loader = new ObjectFile(ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY);
            Scene modelScene = loader.load(new FileReader(modelPath));

            // Create Appearance with Texture
            Appearance modelAppearance = new Appearance();
            TextureLoader textureLoader = new TextureLoader(texturePath, "RGB", new Canvas3D(SimpleUniverse.getPreferredConfiguration()));
            modelAppearance.setTexture(textureLoader.getTexture());

            // Apply texture to all Shape3D objects
            Iterator<Node> children = modelScene.getSceneGroup().getAllChildren();
            while (children.hasNext()) {
                Node obj = children.next();
                if (obj instanceof Shape3D) {
                    ((Shape3D) obj).setAppearance(modelAppearance);
                }
            }

            // **Reuse a single Transform3D instance**
            Transform3D finalTransform = new Transform3D();
            finalTransform.setTranslation(position);
            finalTransform.setScale(scaleValue);

            Transform3D rotate = new Transform3D();
            rotate.rotY(rotationAngle);
            finalTransform.mul(rotate);

            TransformGroup transformGroup = new TransformGroup(finalTransform);
            transformGroup.addChild(modelScene.getSceneGroup());
            modelTG.addChild(transformGroup);

        } catch (IOException e) {
            System.out.println("Error loading " + modelPath + ": " + e.getMessage());
        }

        return modelTG;
    }

    // Function to create and add sound to the scene 
    private static Sound createSound(String soundFilePath, Point3f soundPosition) {
        // Load the sound file
        MediaContainer soundMedia = new MediaContainer(soundFilePath);
        soundMedia.setCacheEnable(true); // Enable caching for better performance

        // Create a PointSound to position the sound in 3D space
        PointSound sound = new PointSound();
        sound.setSoundData(soundMedia);
        sound.setInitialGain(1.0f); // Set volume (0.0f to 1.0f)
        sound.setLoop(Sound.INFINITE_LOOPS); // Loop the sound infinitely
        sound.setEnable(true); // Enable the sound
        sound.setPosition(soundPosition); // Set the position of the sound

        return sound;
    }

    //Function to simulate a clock
    private static void startClockSimulation() {
        Thread clockThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000); // Simulate 1 second passing
                    currentMinute++;
                    if (currentMinute == 60) {
                        currentMinute = 0;
                        currentHour++;
                        if (currentHour == 12) {
                            currentHour = 0;
                        }
                    }

                    // Check if it's 4:00
                    if (currentHour == 4 && currentMinute == 0 && !isSpecialSoundPlaying) {
                        isSpecialSoundPlaying = true;
                        specialSound.setEnable(true); // Play the special sound
                        new Thread(() -> {
                            try {
                                Thread.sleep(5000); // Let the special sound play for 5 seconds
                                specialSound.setEnable(false); // Stop the special sound
                                isSpecialSoundPlaying = false;
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }).start();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        clockThread.start();
    }

    public clock(BranchGroup sceneBG) {
        Canvas3D canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        SimpleUniverse su = new SimpleUniverse(canvas);
        Commons.define_Viewer(su, new Point3d(0.0d, 0.5d, 3.0d)); // Adjusted for a better view

        sceneBG.compile(); // Optimize the scene
        su.addBranchGraph(sceneBG); // Attach the scene to the universe

        setLayout(new BorderLayout());
        add("Center", canvas);
        frame.setSize(800, 800); // Set frame size
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        frame = new JFrame("Clock with Sound");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new clock(create_Scene())); // Add the 3D scene to the frame
    }
}*/