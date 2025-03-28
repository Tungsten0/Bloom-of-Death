package BOD;
import java.io.FileNotFoundException;

import org.jogamp.java3d.*;
import org.jogamp.java3d.Clip;
import org.jogamp.java3d.loaders.*;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.geometry.Cylinder;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.*;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;


public abstract class ClockObjects4 {
	private Alpha rotationAlpha;                           // NOTE: keep for future use
	protected BranchGroup objBG;                           // load external object to 'objBG'
	protected TransformGroup objTG;                        // use 'objTG' to position an object
	protected TransformGroup objRG;                        // use 'objRG' to rotate an object
	protected double scale;                                // use 'scale' to define scaling
	protected Vector3f post;                               // use 'post' to specify location
	protected Shape3D obj_shape, obj_shape2 , obj_shape3, obj_shape4, obj_shape5;	//i am here adding the extra objects shapes for the FanBlades and the front side of the cylinder connecting them
	public abstract TransformGroup position_Object();      // need to be defined in derived classes
	public abstract void add_Child(TransformGroup nextTG);
	
	private static String snd_bk = "clockTicking";                // specify the name of the background sound
	private static SoundUtilityJOAL soundJOAL;             // need for playing sound

	
	public Alpha get_Alpha() { return rotationAlpha; };    // NOTE: keep for future use 
	public void setAlpha(Alpha newAlpha) {				   
		rotationAlpha = newAlpha ;	//here ia am adding the set up vslue for the alpha when i use them
	}
	//a function to load and return object shape from the file named 'obj_name'
	private Scene loadShape(String obj_name) {
		ObjectFile f = new ObjectFile(ObjectFile.RESIZE, (float) (60 * Math.PI / 180.0));
		Scene s = null;
		try {                                              // load object's definition file to 's'
			s = f.load("Clockimages/" + obj_name + ".obj");
		} catch (FileNotFoundException e) {
			System.err.println(e);
			System.exit(1);
		} catch (ParsingErrorException e) {
			System.err.println(e);
			System.exit(1);
		} catch (IncorrectFormatException e) {
			System.err.println(e);
			System.exit(1);
		}
		return s;                                          // return the object shape in 's'
	}
	
	//function to set 'objTG' and attach object after loading the model from external file
	protected void transform_Object(String obj_name) {
		//System.out.println("Scaling object: " + obj_name + " with scale: " + scale);
	   // System.out.println("Translating object: " + obj_name + " to position: " + post);
	    
	    
	    
		Transform3D scaler = new Transform3D();
		scaler.setScale(scale);                            // set scale for the 4x4 matrix
		scaler.setTranslation(post);                       // set translations for the 4x4 matrix
		objTG = new TransformGroup(scaler);                // set the translation BG with the 4x4 matrix
		objBG = loadShape(obj_name).getSceneGroup();       // load external object to 'objBG'
		
			if ( obj_name.equals("ClockDong")) {			  // Blades has multiple objects, therefore we should extract each of them 
				obj_shape = (Shape3D) objBG.getChild(0);//here it will extract first blade 
				obj_shape.setName(obj_name);   
				//obj_shape2 = (Shape3D) objBG.getChild(1);//here it will extract the second blade
				//obj_shape3 = (Shape3D) objBG.getChild(2);//here it will extract the third blade
				//obj_shape4 = (Shape3D) objBG.getChild(3);//here it will extract the fourth blade
				//obj_shape5 = (Shape3D) objBG.getChild(4);//here it will extract the Front surface of the cylinder that is connecting the blades 
			}else {											   
				obj_shape = (Shape3D) objBG.getChild(0);// extract only one object if we are not dealing with the Blades and here i am getting and cast the object to 'obj_shape'
				obj_shape.setName(obj_name);// use the name to identify the object 
			}
	}//end of the method

	protected Appearance app = new Appearance();
	private int shine = 6000;                                // specify common values for object's appearance
	protected Color3f[] mtl_clr = {new Color3f(1.000000f, 1.000000f, 1.000000f),
			new Color3f(0.772500f, 0.654900f, 0.000000f),	
			new Color3f(0.175000f, 0.175000f, 0.175000f),
			new Color3f(0.000000f, 0.000000f, 0.000000f)};
	
    //a function to define object's material and use it to set object's appearance 
	/*protected void obj_Appearance(String texturePath) {
	    // Load the texture image
	    TextureLoader textureLoader = new TextureLoader(texturePath, null);
	    Texture texture = textureLoader.getTexture();

	    // Define texture attributes
	    TextureAttributes textureAttributes = new TextureAttributes();
	    textureAttributes.setTextureMode(TextureAttributes.MODULATE);

	    // Define material's attributes
	    Material mtl = new Material();
	    mtl.setShininess(shine);
	    mtl.setAmbientColor(mtl_clr[0]);
	   // mtl.setDiffuseColor(mtl_clr[1]);
	   // mtl.setSpecularColor(mtl_clr[2]);
	   mtl.setEmissiveColor(mtl_clr[3]);
	    mtl.setLightingEnable(true);

	    // Set appearance's material and texture
	    app.setMaterial(mtl);
	    app.setTexture(texture);
	    app.setTextureAttributes(textureAttributes);

	    if (obj_shape.getName().equals("ClockDong")) {
	        obj_shape.setAppearance(app);
	        //obj_shape2.setAppearance(app);
	        //obj_shape3.setAppearance(app);
	        //obj_shape4.setAppearance(app);
	        //obj_shape5.setAppearance(app);
	    } else if (obj_shape.getName().equals("ClockDong")) {
	        for (int i = 0; i < objBG.numChildren(); i++) {
	            obj_shape = (Shape3D) objBG.getChild(i);
	            obj_shape.setAppearance(app);
	        }
	    } else {
	        obj_shape.setAppearance(app);
	    }
	}*/
	
	
	protected void obj_Appearance(String texturePath) {
	    // Load the texture image
	    TextureLoader textureLoader = new TextureLoader(texturePath, null);
	    Texture texture = textureLoader.getTexture();

	    // Define texture attributes
	    TextureAttributes textureAttributes = new TextureAttributes();
	    textureAttributes.setTextureMode(TextureAttributes.MODULATE);

	    // Define material's attributes
	    Material mtl = new Material();
	    mtl.setShininess(shine);
	    mtl.setAmbientColor(mtl_clr[0]);
	    mtl.setEmissiveColor(mtl_clr[3]);
	    mtl.setLightingEnable(true);

	    // Set appearance's material and texture
	    app.setMaterial(mtl);
	    app.setTexture(texture);
	    app.setTextureAttributes(textureAttributes);

	    // Apply appearance directly — don't mess with geometry
	    obj_shape.setAppearance(app);
	}


	
	
	
	
	
	
	
}//the end of the main code



class ClockHouse extends ClockObjects4 {
	public ClockHouse() {
		scale = 1d;                                        // use to scale up/down original size
		post = new Vector3f(0f, 1.25f, 0.02f);                   // use to move object for positioning
		transform_Object("ClockHouse");                      // set transformation to 'objTG' and load object file
		//mtl_clr[1] = new Color3f(-0.58f, 0.69f, -0.11f);     // set "FanStand" to a different color than the common  		                                              
		obj_Appearance("images/clockmaincolor.jpg");                                  // set appearance after converting object node to Shape3D
	}

	public TransformGroup position_Object() {              // attach object BranchGroup "FanStand" to 'objTG'
		Transform3D r_axis = new Transform3D();            // default: rotate around Y-axis
		//r_axis.rotY(Math.PI);        
		objRG = new TransformGroup(r_axis);                // allow "FanBlades" to rotate
		objTG.addChild(objRG);                            // position "FanStand" by attaching 'objRG' to 'objTG'

		objRG.addChild(objBG);                             // rotate "FanStand" by attaching 'objBG' to 'objRG'
		return objTG;                                      
	}

	public void add_Child(TransformGroup nextTG) {
		objRG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
}

class ClockBase extends ClockObjects4 {
	public ClockBase() {
		scale = 1.2d;                                      // actual scale is 0.3 = 1.0 x 0.3
		post = new Vector3f(-0.02f, -0.77f, -0.7f);         // location to connect "FanSwitch" with "FanStand"
		transform_Object("ClockBase");                     // set transformation to 'objTG' and load object file
		obj_Appearance("images/one.jpg");                                  // set appearance after converting object node to Shape3D
	}

	public TransformGroup position_Object() {
		objTG.addChild(objBG);                             // attach "FanSwitch" to 'objTG'
		return objTG;                                      // use 'objTG' to attach "FanSwitch" to the previous TG
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
}


/*class ClockCenter extends ClockObjects4 {
	public ClockCenter() {
		scale = 0.50d;                                      // actual scale is 0.3 = 1.0 x 0.3
		post = new Vector3f(0.01f, -0.40f, 0.3f);         // location to connect "FanSwitch" with "FanStand"
		transform_Object("ClockCenter");                     // set transformation to 'objTG' and load object file
		obj_Appearance("images/treecolor.jpg");                                  // set appearance after converting object node to Shape3D
	}

	public TransformGroup position_Object() {
		objTG.addChild(objBG);                             // attach "FanSwitch" to 'objTG'
		return objTG;                                      // use 'objTG' to attach "FanSwitch" to the previous TG
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
}*/

class ClockCenter extends ClockObjects4 {
	private TransformGroup circleTG;  // TransformGroup for center circle

	public ClockCenter() {
		scale = 0.50d;
		post = new Vector3f(0.01f, -0.40f, 0.3f);
		transform_Object("ClockCenter");
		obj_Appearance("images/CC2.jpg");

		add_CircleSurface();  // add the circle surface in the center
	}

	// Add a flat circular surface to the center of the clock
	private void add_CircleSurface() {
		// Create a thin cylinder to represent a circle
		float radius = 0.1f;
		float height = 0.01f;
		Cylinder circle = new Cylinder(radius, height, createCircleAppearance());

		// Set transformation for the circle
		Transform3D transform = new Transform3D();
		transform.setTranslation(new Vector3f(0.0f, 0.0f, 0.02f)); // slightly above center surface

		circleTG = new TransformGroup(transform);
		circleTG.addChild(circle);

		objTG.addChild(circleTG);  // attach to main object TransformGroup
	}

	// Custom appearance for the center circle (can be adjusted)
	private Appearance createCircleAppearance() {
		Appearance appear = new Appearance();
		Color3f ambientColor = new Color3f(0.6f, 0.6f, 0.6f);
		Color3f emissiveColor = new Color3f(0.1f, 0.1f, 0.1f);
		Color3f diffuseColor = new Color3f(0.9f, 0.9f, 0.9f);
		Color3f specularColor = new Color3f(1.0f, 1.0f, 1.0f);
		Material material = new Material(ambientColor, emissiveColor, diffuseColor, specularColor, 100f);
		appear.setMaterial(material);
		return appear;
	}

	public TransformGroup position_Object() {
		objTG.addChild(objBG);
		return objTG;
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);
	}
}




class ClockTop extends ClockObjects4 {
	public ClockTop() {
		scale = 1.2d;                                      // actual scale is 0.3 = 1.0 x 0.3
		post = new Vector3f(0.02f, 0.77f, -0.1f);         // location to connect "FanSwitch" with "FanStand"

		transform_Object("ClockTop");                     // set transformation to 'objTG' and load object file
		obj_Appearance("images/clockTop.jpg");                                 // set appearance after converting object node to Shape3D
	}

	public TransformGroup position_Object() {
		objTG.addChild(objBG);                             // attach "FanSwitch" to 'objTG'
		return objTG;                                      // use 'objTG' to attach "FanSwitch" to the previous TG
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
}

class ClockSide1 extends ClockObjects4 {
	public ClockSide1() {
		scale = 0.4d;                                      // actual scale is 0.3 = 1.0 x 0.3
		post = new Vector3f(0.43f, -0.15f, 0.3f);         // location to connect "FanSwitch" with "FanStand"
		transform_Object("ClockSide1");                     // set transformation to 'objTG' and load object file
		obj_Appearance("images/dong3.jpg");                                  // set appearance after converting object node to Shape3D
	}

	public TransformGroup position_Object() {
		objTG.addChild(objBG);                             // attach "FanSwitch" to 'objTG'
		return objTG;                                      // use 'objTG' to attach "FanSwitch" to the previous TG
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
}


class ClockSide2 extends ClockObjects4 {
	public ClockSide2() {
		scale = 0.4d;                                      // actual scale is 0.3 = 1.0 x 0.3
		post = new Vector3f(-0.27f, -0.15f, 0.3f);         // location to connect "FanSwitch" with "FanStand"
		transform_Object("ClockSide2");                     // set transformation to 'objTG' and load object file
		obj_Appearance("images/dong3.jpg");                                  // set appearance after converting object node to Shape3D
	}

	public TransformGroup position_Object() {
		objTG.addChild(objBG);                             // attach "FanSwitch" to 'objTG'
		return objTG;                                      // use 'objTG' to attach "FanSwitch" to the previous TG
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
}

class ClockWindowobj extends ClockObjects4 {
	public ClockWindowobj() {
		scale = 0.3d;                                      // actual scale is 0.3 = 1.0 x 0.3
		post = new Vector3f(0.019f, 0.56f, 0.1f);         // location to connect "FanSwitch" with "FanStand"
		transform_Object("ClockWindowobj");                     // set transformation to 'objTG' and load object file
		obj_Appearance("images/why.jpg");                                  // set appearance after converting object node to Shape3D
	}

	public TransformGroup position_Object() {
		objTG.addChild(objBG);                             // attach "FanSwitch" to 'objTG'
		return objTG;                                      // use 'objTG' to attach "FanSwitch" to the previous TG
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
}

class ClockTree1 extends ClockObjects4 {
	public ClockTree1() {
		scale = 0.9d;                                      // actual scale is 0.3 = 1.0 x 0.3
		post = new Vector3f(-0.2f, 0.1f, -0.1f);         // location to connect "FanSwitch" with "FanStand"
		transform_Object("ClockTree1");                     // set transformation to 'objTG' and load object file
		obj_Appearance("images/greentree.jpg");                                  // set appearance after converting object node to Shape3D
	}

	public TransformGroup position_Object() {
		objTG.addChild(objBG);                             // attach "FanSwitch" to 'objTG'
		return objTG;                                      // use 'objTG' to attach "FanSwitch" to the previous TG
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
}

class TreeBase1 extends ClockObjects4 {
	public TreeBase1() {
		scale = 0.5d;                                      // actual scale is 0.3 = 1.0 x 0.3
		post = new Vector3f(-0.25f, -1.1f, 0.51f);         // location to connect "FanSwitch" with "FanStand"
		transform_Object("TreeBase1");                     // set transformation to 'objTG' and load object file
		obj_Appearance("images/treebase.jpg");                                  // set appearance after converting object node to Shape3D
	}

	public TransformGroup position_Object() {
		objTG.addChild(objBG);                             // attach "FanSwitch" to 'objTG'
		return objTG;                                      // use 'objTG' to attach "FanSwitch" to the previous TG
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
}

class ClockTree2 extends ClockObjects4 {
	public ClockTree2() {
		scale = 0.9d;                                      // actual scale is 0.3 = 1.0 x 0.3
		post = new Vector3f(1.3f, 0.1f, -0.1f);         // location to connect "FanSwitch" with "FanStand"
		transform_Object("ClockTree1");                     // set transformation to 'objTG' and load object file
		obj_Appearance("images/greentree.jpg");                                  // set appearance after converting object node to Shape3D
	}

	public TransformGroup position_Object() {
		objTG.addChild(objBG);                             // attach "FanSwitch" to 'objTG'
		return objTG;                                      // use 'objTG' to attach "FanSwitch" to the previous TG
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
}

class TreeBase2 extends ClockObjects4 {
	public TreeBase2() {
		scale = 0.5d;                                      // actual scale is 0.3 = 1.0 x 0.3
		post = new Vector3f(-0.25f, -1.1f, 0.51f);         // location to connect "FanSwitch" with "FanStand"
		transform_Object("TreeBase1");                     // set transformation to 'objTG' and load object file
		obj_Appearance("images/treebase.jpg");                                  // set appearance after converting object node to Shape3D
	}

	public TransformGroup position_Object() {
		objTG.addChild(objBG);                             // attach "FanSwitch" to 'objTG'
		return objTG;                                      // use 'objTG' to attach "FanSwitch" to the previous TG
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
}


class HourHand extends ClockObjects4 {
    public HourHand() {
        scale = 0.4d; // Scale as per hint
        post = new Vector3f(0f, 0f, 0.1f); // Positioned above the motor
        transform_Object("HourHand");
        mtl_clr[1] = new Color3f(0f, 1f, 0f); // Set color
        obj_Appearance("images/hands.png");
    }

    public TransformGroup position_Object() {
        objRG = new TransformGroup();
        objRG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE); // Allow rotation

        // Align the hand flat on the X-Y plane
        Transform3D alignTransform = new Transform3D();
        alignTransform.rotX(Math.PI / 2); // Rotate 90 degrees around the X-axis to make it lie flat
        objRG.setTransform(alignTransform);

        // Set initial rotation to 12:00 position (aligned with the Y-axis)
        Transform3D initialRotation = new Transform3D();
        initialRotation.rotX(-Math.PI / 2); // Rotate to 12:00 position
        objRG.setTransform(initialRotation);

        objRG.addChild(objBG); // Attach the hour hand to the rotation group
        applyRotation(21600  * 1000); // Rotate every 12 hours (43200 seconds * 1000 ms)
        objTG.addChild(objRG); // Attach the rotation group to the transform group
        return objTG;
    }

    private void applyRotation(int rotationDuration) {
        Transform3D axisPosition = new Transform3D();
        axisPosition.rotX(-Math.PI / 2); // Rotate clockwise around the Z-axis
        setAlpha(new Alpha(-1, rotationDuration)); // Set rotation duration
        RotationInterpolator rotationInterpolator = new RotationInterpolator(
            get_Alpha(), objRG, axisPosition, 0.0f, (float) Math.PI * 2.0f // Negative angle for clockwise rotation
        );
        rotationInterpolator.setSchedulingBounds(Commons.twenty_BS);
        objTG.addChild(rotationInterpolator); // Attach the interpolator to the transform group
    }

    public void add_Child(TransformGroup nextTG) {
        objTG.addChild(nextTG); // Attach the next transform group
    }
}



class MinuteHand extends ClockObjects4 {
    public MinuteHand() {
        scale = 0.45d; // Adjust scale as needed
        post = new Vector3f(0f, 0f, 0.1f); // Positioned above the motor
        transform_Object("HourHand"); // Load the MinuteHand.obj file
        mtl_clr[1] = new Color3f(1f, 0f, 0f); // Set color
        obj_Appearance("images/hands.png"); // Set appearance
    }

 
    public TransformGroup position_Object() {
        objRG = new TransformGroup();
        objRG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE); // Allow rotation
        objRG.addChild(objBG); // Attach the minute hand to the rotation group
        applyRotation(90 * 1000); // Rotate every minute (60 seconds * 1000 ms)
        objTG.addChild(objRG); // Attach the rotation group to the transform group
        return objTG;
    }

    private void applyRotation(int rotationDuration) {
        Transform3D axisPosition = new Transform3D();
        axisPosition.rotX(Math.PI / 2); // Rotate around the Z-axis
        setAlpha(new Alpha(-1, rotationDuration)); // Set rotation duration
        RotationInterpolator rotationInterpolator = new RotationInterpolator(get_Alpha(), objRG, axisPosition, 0.0f, (float)- Math.PI * 2.0f);
        rotationInterpolator.setSchedulingBounds(Commons.twenty_BS);
        objTG.addChild(rotationInterpolator); // Attach the interpolator to the transform group
    }

    public void add_Child(TransformGroup nextTG) {
        objTG.addChild(nextTG); // Attach the next transform group
    }
}


class ClockDong extends ClockObjects4 {
    public ClockDong() {
        scale = 0.7d; // Adjust scale as needed
       // post = new Vector3f(0.1f, -0.5f, 0.5f); // Position the pendulum below the ClockHouse
        post = new Vector3f(-0.04f, -0.6f, -0.27f); // Position the pendulum below the ClockHouse

        transform_Object("ClockDong"); // Load the ClockDong.obj file
        mtl_clr[1] = new Color3f(0.5f, 0.5f, 0.5f); // Set color (e.g., gray)
        obj_Appearance("images/dong3.jpg"); // Set appearance
    }

    public TransformGroup position_Object() {
        objRG = new TransformGroup();
        objRG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE); // Allow rotation

        // Align the pendulum with the Z-axis (vertical alignment)
        Transform3D alignTransform = new Transform3D();
        alignTransform.rotX(Math.PI/2); // Rotate 90 degrees around the X-axis to make it hang vertically
        objRG.setTransform(alignTransform);

        objRG.addChild(objBG); // Attach the pendulum to the rotation group
        applySwingMotion(2000); // Swing every 2 seconds (2000 ms)
        objTG.addChild(objRG); // Attach the rotation group to the transform group
        return objTG;
    }

    private void applySwingMotion(int swingDuration) {
        // Define the swing angle range (e.g., 30 degrees to the left and right)
        float minAngle = (float) Math.toRadians(-10); // -30 degrees in radians
        float maxAngle = (float) Math.toRadians(30);  // 30 degrees in radians

        // Create an Alpha object for continuous oscillation (-1 means infinite loop)
        
        
        //Alpha swingAlpha = new Alpha(-1, Alpha.INCREASING_ENABLE, 0, 0, swingDuration / 2, 0, 0, swingDuration / 2, 0, 0);

        
        Alpha swingAlpha = new Alpha(
                -1, // Loop indefinitely
                Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE, // Enable both increasing and decreasing phases
                0, 0, swingDuration / 2, 0, 0, swingDuration / 2, 0, 0
            );
        
        // Define the swing rotation axis (around the Z-axis for pendulum swing)
        Transform3D swingAxis = new Transform3D();
        swingAxis.rotX(Math.PI / 2); // Rotate around the Z-axis

        // Create a RotationInterpolator for swinging motion
        RotationInterpolator swingInterpolator = new RotationInterpolator(
            swingAlpha, objRG, swingAxis, minAngle, maxAngle // Rotate between -30° and 30°
        );

        swingInterpolator.setSchedulingBounds(Commons.twenty_BS); // Set activation bounds
        objRG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);  // Ensure transformations are allowed
        objRG.addChild(swingInterpolator); // Attach interpolator to the rotation group
    }

    public void add_Child(TransformGroup nextTG) {
        objTG.addChild(nextTG); // Attach the next transform group
    }
}
