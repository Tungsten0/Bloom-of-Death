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

public class TheBloomOfDeath extends JPanel {
    private static final long serialVersionUID = 1L;
    private static JFrame frame;

    public static BranchGroup create_Scene() {
        BranchGroup sceneBG = new BranchGroup();
        TransformGroup sceneTG = new TransformGroup();

        // Create room (3 walls, open front and ceiling)
        Appearance wallAppearance = Commons.set_Appearance(Commons.Grey);
        sceneTG.addChild(createWall(new Vector3d(0, -0.5, 0.2), new Vector3d(1.2, 0.05, 1.2), wallAppearance)); // Floor
        sceneTG.addChild(createWall(new Vector3d(-1.2, 0.05, 0.2), new Vector3d(0.05, 0.6, 1.2), wallAppearance)); // Left Wall
        sceneTG.addChild(createWall(new Vector3d(1.2, 0.05, 0.2), new Vector3d(0.05, 0.6, 1.2), wallAppearance)); // Right Wall
        sceneTG.addChild(createWall(new Vector3d(0, 0.05, -1.0), new Vector3d(1.2, 0.6, 0.05), wallAppearance)); // Back Wall

        // Load models
        sceneTG.addChild(loadClockModel());

        sceneBG.addChild(sceneTG);
        sceneBG.addChild(Commons.add_Lights(Commons.White, 1));

        return sceneBG;
    }

    private static TransformGroup createWall(Vector3d position, Vector3d scale, Appearance appearance) {
        Transform3D transform = new Transform3D();
        transform.setTranslation(position);
        transform.setScale(scale);

        TransformGroup wallTG = new TransformGroup(transform);
        wallTG.addChild(new Box(1.0f, 1.0f, 1.0f, Box.GENERATE_NORMALS, appearance));
        return wallTG;
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

    public TheBloomOfDeath(BranchGroup sceneBG) {
        Canvas3D canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        SimpleUniverse su = new SimpleUniverse(canvas);
        Commons.define_Viewer(su, new Point3d(0, 1.15, 4.5));

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
        frame.getContentPane().add(new TheBloomOfDeath(create_Scene()));
    }
}
