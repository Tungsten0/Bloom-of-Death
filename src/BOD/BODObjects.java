package BOD;

import java.io.File;

/* Copyright material for students working on assignments */

import java.io.FileNotFoundException;

import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.*;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.*;
import org.jogamp.java3d.utils.geometry.Primitive;

public abstract class BODObjects {
	private Alpha rotationAlpha;                           
	protected BranchGroup objBG;                           // load external object to 'objBG'
	protected TransformGroup objTG;                        // use 'objTG' to position an object
	protected TransformGroup objRG;                        // use 'objRG' to rotate an object
	protected double scale;                                // use 'scale' to define scaling
	protected Vector3f post;                               // use 'post' to specify location
	protected Shape3D obj_shape, obj_shape2 , obj_shape3, obj_shape4, obj_shape5;	// NEW: Added obj_shape2...5, for the FanBlades and the front side of the cylinder connecting them
	
	protected Appearance app = new Appearance();
	private float shine = 32;   						   // Default shininess value                            
	
	public abstract TransformGroup position_Object();      // need to be defined in derived classes
	public abstract void add_Child(TransformGroup nextTG);
	
	public Alpha get_Alpha() { return rotationAlpha; };    // NOTE: keep for future use 
	
	// NEW: Added to set up the value of Alpha------------
	public void setAlpha(Alpha newAlpha) {				   
		rotationAlpha = newAlpha ;	
	}
	//---------------------------------------------------

	/* a function to load and return object shape from the file named 'obj_name' */
	protected Scene loadShape(String obj_name) {
		ObjectFile f = new ObjectFile(ObjectFile.RESIZE, (float) (60 * Math.PI / 180.0));
		Scene s = null;
		try {                                              // load object's definition file to 's'
			s = f.load("objects/" + obj_name + ".obj");
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
	
	/* function to set 'objTG' and attach object after loading the model from external file */
	protected void transform_Object(String obj_name) {
		Transform3D scaler = new Transform3D();
		scaler.setScale(scale);                           // set scale for the 4x4 matrix
		scaler.setTranslation(post);                      // set translations for the 4x4 matrix
		objTG = new TransformGroup(scaler);               // set the translation BG with the 4x4 matrix
		objBG = loadShape(obj_name).getSceneGroup();      // load external object to 'objBG'
		// Method 1: Count immediate children
		int childCount = objBG.numChildren();
		System.out.println(obj_name+" has " + childCount + " children");
		obj_shape = (Shape3D) objBG.getChild(0);          // Get and cast the object to 'obj_shape'
		obj_shape.setName(obj_name);                      // Use the name to identify the object 
		
	}
	
	// Default colors values for objects, can be edited 
	protected Color3f[] mtl_clr = {
			new Color3f(1.000000f, 1.000000f, 1.000000f),
			new Color3f(0.772500f, 0.654900f, 0.000000f),	
			new Color3f(0.175000f, 0.175000f, 0.175000f),
			new Color3f(0.000000f, 0.000000f, 0.000000f)
   };
	
	// Method to set the shininess value 
   protected void setShininess(float shine) {
	   this.shine = shine ; 
   }

	
	protected void obj_Appearance() {
	    System.out.println("Applying appearance to " + obj_shape.getName());
	    Material mtl = new Material();
	    mtl.setShininess(shine);
	    mtl.setAmbientColor(mtl_clr[0]); 				// Ambient color
	    mtl.setDiffuseColor(mtl_clr[1]); 				// Diffuse color
	    mtl.setSpecularColor(mtl_clr[2]); 				// Specular color
	    mtl.setEmissiveColor(mtl_clr[3]); 				// Emissive color
	    mtl.setLightingEnable(true); 					// Enable lighting
	     
	    app.setMaterial(mtl); 							// Set the material to the appearance
	    obj_shape.setAppearance(app); 					// Apply the appearance to the shape
	}
	 protected static Texture textured_App(String name) {
	        String[] extensions = {".png", ".jpg", ".jpeg"};
	        for (String ext : extensions) {
	            String path = "textures/" + name + ext;
	            File file = new File(path);
	            System.out.println("Trying texture: " + file.getAbsolutePath());
	            if (file.exists()) {
	                TextureLoader loader = new TextureLoader(path, "RGB", new Canvas3D(SimpleUniverse.getPreferredConfiguration()));
	                Texture texture = loader.getTexture();
	                System.out.println("Load success for: " + path + " = " + (texture != null));
	                return texture;
	            }
	        }
	        System.out.println("Texture not found for " + name);
	        return null;
	    }
	
}

class BaseShape2 extends BODObjects {
	public BaseShape2() {
		Transform3D translator = new Transform3D();
		translator.setTranslation(new Vector3d(0.0, -0.54, 0));
		objTG = new TransformGroup(translator);            // down half of the tower and base's heights

		objTG.addChild(create_Object());                   // attach the object to 'objTG'
	}
	
	protected Node create_Object() {
		app = Commons.set_Appearance(Commons.White);  	    // set the appearance for the base
		app.setTexture(textured_App("MarbleTexture"));     // set texture for the base
		TransparencyAttributes ta =                        // value: FASTEST NICEST SCREEN_DOOR BLENDED NONE
				new TransparencyAttributes(TransparencyAttributes.SCREEN_DOOR, 0.5f);
		app.setTransparencyAttributes(ta);                 // set transparency for the base
		return new Box(0.5f, 0.04f, 0.5f, Box.GENERATE_NORMALS | Box.GENERATE_TEXTURE_COORDS, app);
	}

	public TransformGroup position_Object() {
		objTG.addChild(objBG);                             // attach "BaseShapeA" to 'objTG'
		return objTG;                                      // use 'objTG' to attach "BaseShapeA" to the previous TG
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
}
