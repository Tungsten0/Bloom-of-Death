/*
package CodesHS2800;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.geometry.Cylinder;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.vecmath.*;

public class Chair3d extends BaseShapesHS {

    public Chair3d() {
        // Create the seat
        Transform3D seatTranslator = new Transform3D();
        seatTranslator.setTranslation(new Vector3d(0.0, 0.0, 0.0)); // Position the seat
        objTG = new TransformGroup(seatTranslator);
        objTG.addChild(create_Object());
    }

    @Override
    protected Node create_Object() {
        // Create the seat
        app = CommonsHS.set_Appearance(new Color3f(0.5f, 0.35f, 0.05f)); // Wood-like color
        Box seat = new Box(0.5f, 0.05f, 0.5f, Primitive.GENERATE_NORMALS, app);

        // Create the legs
        TransformGroup legsTG = new TransformGroup();
        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                Cylinder leg = new Cylinder(0.05f, 0.5f, Primitive.GENERATE_NORMALS, 30, 30, app);
                Transform3D legTransform = new Transform3D();
                legTransform.setTranslation(new Vector3d(i * 0.4, -0.3, j * 0.4)); // Position the legs
                TransformGroup legTG = new TransformGroup(legTransform);
                legTG.addChild(leg);
                legsTG.addChild(legTG);
            }
        }

        // Create the backrest
        Box backrest = new Box(0.5f, 0.4f, 0.05f, Primitive.GENERATE_NORMALS, app);
        Transform3D backrestTransform = new Transform3D();
        backrestTransform.setTranslation(new Vector3d(0.0, 0.3, -0.4)); // Position the backrest
        TransformGroup backrestTG = new TransformGroup(backrestTransform);
        backrestTG.addChild(backrest);

        // Combine all parts into a single TransformGroup
        TransformGroup chairTG = new TransformGroup();
        chairTG.addChild(seat);
        chairTG.addChild(legsTG);
        chairTG.addChild(backrestTG);

        return chairTG;
    }
}*/
package BOD;
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.*;
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;

import javax.swing.JFrame;

import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.Loader;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.geometry.Cone;
import org.jogamp.java3d.utils.geometry.Cylinder;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.*;
public class ChairHS extends JPanel{
	private static final long serialVersionUID = 1L;
    private static JFrame frame;

	
	/* a function to create the desk fan */
	public static TransformGroup create_ChairHS() {
        TransformGroup sceneTG = new TransformGroup();
        sceneTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        // Create the side table
        ChairHS2 chair = new ChairHS2(); // Create the side table
        sceneTG.addChild(chair.position_Object());

        // Add lighting
        sceneTG.addChild(Commons.add_Lights(Commons.White, 1));
		//sceneBG.addChild(CommonsHS.rotate_Behavior(7500, sceneTG));	

        // Add the side table to the scene

        return sceneTG;
    }
	public ChairHS(BranchGroup sceneBG) {
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas = new Canvas3D(config);
        SimpleUniverse su = new SimpleUniverse(canvas);

        // Set the camera view to see the side table from the front
        Commons.define_Viewer(su, new Point3d(0.0d, 0.5d, 3.0d)); // Adjusted for a better view

        // Compile and add the scene to the universe
        sceneBG.compile();
        su.addBranchGraph(sceneBG);

        // Set up the JFrame
        setLayout(new BorderLayout());
        add("Center", canvas);

        frame.setSize(800, 800);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        frame = new JFrame("Side Table Object 2");
      //  frame.getContentPane().add(new ChairObject1(create_ChairHS()));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


}




class ChairHS2 extends BaseShapesHS {

	public ChairHS2() {
	    // Create a hierarchy of TransformGroups
	    TransformGroup translationTG = new TransformGroup();
	    TransformGroup rotationTG = new TransformGroup();
	    TransformGroup scaleTG = new TransformGroup();
	    
	    // Set up the transformations
	    Transform3D translation = new Transform3D();
	    translation.setTranslation(new Vector3d(-43d, -12.5d, 30.0d));
	    translationTG.setTransform(translation);
	    
	    Transform3D rotation = new Transform3D();
	    rotation.rotY(Math.toRadians(90)); // Rotate around Y-axis
	    rotationTG.setTransform(rotation);
	    
	    Transform3D scaling = new Transform3D();
	    scaling.setScale(10.0);//before was 8
	    scaleTG.setTransform(scaling);
	    
	    // Build the hierarchy
	    translationTG.addChild(rotationTG);
	    rotationTG.addChild(scaleTG);
	    scaleTG.addChild(create_Object());
	    
	    objTG = translationTG;
	}

    @Override
    protected Node create_Object() {
        // Load the wood texture from the package
        String file_name = "textures/why.jpg"; // Path to the texture file in the package
        TextureLoader textureLoader = new TextureLoader(file_name, null);
        Texture texture = textureLoader.getTexture();

        // Create an appearance with the texture
        Appearance texturedApp = new Appearance();
        texturedApp.setTexture(texture);

        // Create the seat
        Box seat = new Box(0.5f, 0.05f, 0.5f, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, texturedApp);

        // Create the legs
        TransformGroup legsTG = new TransformGroup();
        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
               /* Cylinder leg = new Cylinder(0.05f, 0.5f, Primitive.GENERATE_NORMALS, 30, 30, texturedApp);
                Transform3D legTransform = new Transform3D();
                legTransform.setTranslation(new Vector3d(i * 0.4, -0.3, j * 0.4)); // Position the legs
                TransformGroup legTG = new TransformGroup(legTransform);
                legTG.addChild(leg);
                legsTG.addChild(legTG);	*/
            	// Example of loading a custom 3D model for the legs
            	// Example of creating lion paw legs
            	// Example of creating S-shaped legs
            	// Example of creating pyramid-shaped legs
            	// Example of creating tree branch legs
            	Cylinder branch = new Cylinder(0.05f, 0.5f, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, 30, 30, texturedApp);
            	Sphere knot = new Sphere(0.07f, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, texturedApp);

            	Transform3D branchTransform = new Transform3D();
            	branchTransform.setTranslation(new Vector3d(i * 0.4, -0.3, j * 0.4));
            	TransformGroup branchTG = new TransformGroup(branchTransform);
            	branchTG.addChild(branch);

            	Transform3D knotTransform = new Transform3D();
            	knotTransform.setTranslation(new Vector3d(i * 0.4, -0.1, j * 0.4));
            	TransformGroup knotTG = new TransformGroup(knotTransform);
            	knotTG.addChild(knot);

            	legsTG.addChild(branchTG);
            	legsTG.addChild(knotTG);
            	
            	
            	
        }
        }
    
        

        // Create the backrest
        Box backrest = new Box(0.5f, 0.4f, 0.05f, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, texturedApp);
        Transform3D backrestTransform = new Transform3D();
        backrestTransform.setTranslation(new Vector3d(0.0, 0.45, -0.4)); // Position the backrest
        TransformGroup backrestTG = new TransformGroup(backrestTransform);
        backrestTG.addChild(backrest);

        
        
        
        
        // Create armrests
     Box armrestLeft = new Box(0.05f, 0.1f, 0.5f, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, texturedApp);
        Box armrestRight = new Box(0.05f, 0.1f, 0.5f, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, texturedApp);

        Transform3D armrestLeftTransform = new Transform3D();
        armrestLeftTransform.setTranslation(new Vector3d(-0.55, 0.2, 0.0));
        TransformGroup armrestLeftTG = new TransformGroup(armrestLeftTransform);
        armrestLeftTG.addChild(armrestLeft);

        Transform3D armrestRightTransform = new Transform3D();
        armrestRightTransform.setTranslation(new Vector3d(0.55, 0.2, 0.0));
        TransformGroup armrestRightTG = new TransformGroup(armrestRightTransform);
        armrestRightTG.addChild(armrestRight);
         

        // Combine all parts into a single TransformGroup
        TransformGroup chairTG = new TransformGroup();
        chairTG.addChild(seat);
        chairTG.addChild(legsTG);
        chairTG.addChild(backrestTG);
        chairTG.addChild(armrestLeftTG);
       chairTG.addChild(armrestRightTG);
        

        return chairTG;
    }
}

