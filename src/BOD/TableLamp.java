package BOD;

import org.jogamp.java3d.BoundingSphere;
import org.jogamp.java3d.Node;
import org.jogamp.java3d.PointLight;
import org.jogamp.java3d.PolygonAttributes;
import org.jogamp.java3d.Switch;
import org.jogamp.java3d.TextureAttributes;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.TransparencyAttributes;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Point3f;
import org.jogamp.vecmath.Vector3d;
import org.jogamp.vecmath.Vector3f;

public class TableLamp {
	
	private static BODObjects[] TLObjects = new BODObjects[6];				// TableLamp objects
	
	/* a function to create the desk fan */
	protected static TransformGroup create_TableLamp() {
		
		TransformGroup TableLampTG = new TransformGroup();		  // Initializing sceneTG (TableLampTG)
				
		
		// Create the base of the TableLamp
		TLObjects[0] = new TLBase(); 
		TableLampTG = TLObjects[0].position_Object();             // set TableLampTG to TLBase's objTG   	
						
		// Create the Body of the TableLamp and attach it to TLBase objTG
		TLObjects[1] = new TLBody(); 
		TLObjects[0].add_Child(TLObjects[1].position_Object());
		
		// Create other Parts of the Table Lamp and attach it to TLBodyTG
		TLObjects[2] = new TLOtherP(); 
		TLObjects[1].add_Child(TLObjects[2].position_Object());
		
		// Create the table lamp's Bulb and attach it's objTG to TLothers objTG 
		TLObjects[3] = new TLBulb(); 
		TLObjects[2].add_Child(TLObjects[3].position_Object());
		
		// Create the table lamp's shade and attach it's objTG to TLothers objTG
		TLObjects[4] = new TLShade(); 
		TLObjects[2].add_Child(TLObjects[4].position_Object());
		
		
		TLObjects[5] = new TLShadeFitting(); 
		TLObjects[4].add_Child(TLObjects[5].position_Object());
		
		
		
		
		return TableLampTG;
	}

}



//Class responsible for making the TabeLamp's base  
class TLBase extends BODObjects{
	public TLBase() {
		scale = 1d;                                        	// use to scale up/down original size
		post = new Vector3f(0f, -1f, -1f);                   // use to move object for positioning
		transform_Object("TLBase");                      	// set transformation to 'objTG' and load object file
		create_Appearance() ;								// Create the Full appearance of the body, including the colors, and texture
	}

	protected void create_Appearance() {
		// Defining colors, and  setting them on the shape
		mtl_clr[0]= new Color3f(0.2f, 0.2f, 0.2f) ;  
		mtl_clr[1] = new Color3f(0.3f, 0.3f, 0.35f); 		// Darker grey diffuse color
		mtl_clr[2] = new Color3f(1.0f, 1.0f, 1.0f) ;
		mtl_clr[3] = new Color3f(0.0f, 0.0f, 0.0f) ; 
		obj_Appearance();									// Applying colors on the object                                 
		
		// Enable texture attributes
      TextureAttributes texAttr = new TextureAttributes();
      texAttr.setTextureMode(TextureAttributes.MODULATE); // MODULATE, REPLACE, BLEND, etc.
      
      // Scale the Texture if needed 
      float scl = 0.6f;
	    Transform3D transMap = new Transform3D();  			// to scale it 
      Vector3d scale = new Vector3d(scl, scl, scl); 
	    transMap.setScale(scale);							// apply scaling
      texAttr.setTextureTransform(transMap);
      
      // Set the Texture to the appearance 
      app.setTextureAttributes(texAttr);
	  app.setTexture(textured_App("TLOtherP"));     		
	}
	
	
	public TransformGroup position_Object() {              // attach object BranchGroup "LTBody" to 'objTG'
		objTG.addChild(objBG);                             // position "FanStand" by attaching 'objRG' to 'objTG'		
		return objTG;                                      
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
	
}


//Class responsible for making the TabeLamp's Body 
class TLBody extends BODObjects{
	public TLBody() {
		scale = 1.4d;                                        	// use to scale up/down original size
		post = new Vector3f(0f, 1.55f, 0f);                   	// use to move object for positioning
		transform_Object("TLBody");                      	// set transformation to 'objTG' and load object file
		create_Appearance() ;								// Create the Full appearance of the body, including the colors, and texture
	}

	protected void create_Appearance() {
		// Defining colors, and  setting them on the shape
		mtl_clr[0]= new Color3f(0.2f, 0.2f, 0.2f) ;  
		mtl_clr[1] = new Color3f(0.3f, 0.3f, 0.35f); 		// Darker grey diffuse color
		mtl_clr[2] = new Color3f(1.0f, 1.0f, 1.0f) ;
		mtl_clr[3] = new Color3f(0.0f, 0.0f, 0.0f) ; 
		obj_Appearance();									// Applying colors on the object                                 
		
		// Enable texture attributes
      TextureAttributes texAttr = new TextureAttributes();
      texAttr.setTextureMode(TextureAttributes.MODULATE); // MODULATE, REPLACE, BLEND, etc.
      
      // Scale the Texture if needed 
      float scl = 0.6f;
	  Transform3D transMap = new Transform3D();  			// to scale it 
      Vector3d scale = new Vector3d(scl, scl, scl); 
	   transMap.setScale(scale);							// apply scaling
      texAttr.setTextureTransform(transMap);
      
      // Set the Texture to the appearance 
      app.setTextureAttributes(texAttr);
		app.setTexture(textured_App("TLBody"));     		
	}
	
	
	public TransformGroup position_Object() {              // attach object BranchGroup "LTBody" to 'objTG'
		objTG.addChild(objBG);                             // position "FanStand" by attaching 'objRG' to 'objTG'
		return objTG;                                      
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
	
}


//Class responsible for making the TabeLamp's other parts
class TLOtherP extends BODObjects{
	
	public TLOtherP() {
		scale = 1.5d;                                        // use to scale up/down original size
		post = new Vector3f(-0.01f, 2.5f, 0.25f);                 // use to move object for positioning
		transform_Object("TLOtherP");                      	// set transformation to 'objTG' and load object file
		create_Appearance() ;
	}

	protected void create_Appearance() {
		// Defining colors, and  setting them on the shape
		mtl_clr[0]= new Color3f(0.2f, 0.2f, 0.2f) ;  
		mtl_clr[1] = new Color3f (0.95f, 0.95f, 0.92f) ;
		mtl_clr[2] = new Color3f(1.0f, 1.0f, 1.0f) ;
		mtl_clr[3] = new Color3f(0.0f, 0.0f, 0.0f) ; 
		obj_Appearance();                                  
		
		// Enable texture attributes
      TextureAttributes texAttr = new TextureAttributes();
      texAttr.setTextureMode(TextureAttributes.MODULATE); 		// MODULATE, REPLACE, BLEND, etc.
      app.setTextureAttributes(texAttr);
		app.setTexture(textured_App("TLOtherP"));     			// set texture for the base	
	}
	
	public TransformGroup position_Object() {              		// attach object BranchGroup of "TLOthers" to 'objTG'
		objTG.addChild(objBG);                             
		return objTG;                                      
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            		// attach the next transformGroup to 'objTG'
	}
	
}


//Class responsible for making the TabeLamp's Bulb
class TLBulb extends BODObjects {
  private PointLight bulbLight;  								// Light source for the bulb
	private Switch bulbSwitch; 									// Needed to switch between two Bulbs, to control turning on and off the light

  public TLBulb() {
  	
      scale = 0.3d;                                            // use to scale up/down original size
      post = new Vector3f(0f, 0f, -0.15f);                    // use to move object for positioning
      
      bulbSwitch = new Switch();
  	bulbSwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);   // Allow Switch manipualtion during runtime    	
      transform_Object("TLBulb");                            // set transformation to 'objTG' and load object file
      create_Appearance();                                   // Create the Full appearance of the Bulb (its the colors)
      //bulbSwitch.addChild(obj_shape) ; 
      
      create_Light();                                        // Create and attach the light source to the Bulb objTG
     // bulbSwitch.addChild(obj_shape) ; 
   
		//bulbSwitch.setWhichChild(0);   
     	
		
		 objTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		objTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		objTG.setCapability(Node.ENABLE_PICK_REPORTING); // need for mouse picking
	    	
		
		// Attach objSwitch to objTG
		objTG.addChild(bulbSwitch);   
           
      
  }

  protected void create_Appearance() {
      // Defining colors, and setting them on the shape       
      mtl_clr[0] = new Color3f(0.2f, 0.2f, 0.2f);
      mtl_clr[1] = new Color3f(0.3f, 0.3f, 0.35f);        // Darker grey diffuse color
      mtl_clr[2] = new Color3f(1.0f, 1.0f, 1.0f);         // Specular color
      mtl_clr[3] = new Color3f(0.8f, 0.8f, 0.8f);         // Emissive color (makes the bulb appear to glow)
      obj_Appearance();                                   // Applying colors on the object
  }

  private void create_Light() {
      // Create a PointLight to simulate the bulb emitting light
      bulbLight = new PointLight();
      bulbLight.setColor(new Color3f(3.0f, 1.5f, 1.0f)); 			
      bulbLight.setPosition(new Point3f(post.x, post.y, post.z));  // Position the light at the bulb's location
      bulbLight.setInfluencingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 10.0));  // Set the light's influence bounds
      // Add the light to the bulb's TransformGroup
      objTG.addChild(bulbLight);
  }
  
  /*
  private void createSwitch(String obj_name){	
  	Transform3D scaler = new Transform3D();
		scaler.setScale(scale);                           // set scale for the 4x4 matrix
		scaler.setTranslation(post);                      // set translations for the 4x4 matrix
		objTG = new TransformGroup(scaler);               // set the translation BG with the 4x4 matrix
		objBG = loadShape(obj_name).getSceneGroup();      // load external object to 'objBG'
		obj_shape = (Shape3D) objBG.getChild(0);          // Get and cast the object to 'obj_shape'
		obj_shape.setName(obj_name);                      // Use the name to identify the object 
		bulbSwitch.addChild(obj_shape) ; 
		bulbSwitch.setWhichChild(1);                     // start with green box
				
  }*/

  public TransformGroup position_Object() {
      objTG.addChild(objBG);  // position "TLBulb" by attaching 'objBG' to 'objTG'
      return objTG;
  }

  public void add_Child(TransformGroup nextTG) {
      objTG.addChild(nextTG);  // attach the next transformGroup to 'objTG'
  }
}



//Class responsible for making the TabeLamp's shade  
class TLShade extends BODObjects {
	    private PointLight bulbLight;  // Light source inside the lampshade

	    public TLShade() {
	        scale = 1.14d;                                        // Scale the object
	        post = new Vector3f(0f, 0.14f, -0.14f);             // Position the object
	        transform_Object("TLShade");                         // Load and transform the 3D object
	        create_Appearance();                                 // Set up the appearance
	        create_Light();                                      // Add a light source inside the lampshade
	    }

	    protected void create_Appearance() {
	        // Define material colors
	        mtl_clr[0] = new Color3f(0.4f, 0.4f, 0.4f);         
	        mtl_clr[1] = new Color3f(0.8f, 0.8f, 0.8f);         
	        mtl_clr[2] = new Color3f(0.1f, 0.1f, 0.1f);        
	        mtl_clr[3] = new Color3f(0.8f, 0.8f, 0.8f); 
	        obj_Appearance();                                   // Apply the colors

	        // Enable texture attributes
	        TextureAttributes texAttr = new TextureAttributes();
	        texAttr.setTextureMode(TextureAttributes.MODULATE); // Combine texture with material colors

	        // Scale the texture
	        float scl = 0.6f;
	        Transform3D transMap = new Transform3D();           // Create a transformation for scaling
	        Vector3d scale = new Vector3d(scl, scl, scl);      // Define scaling factors
	        transMap.setScale(scale);                           // Apply scaling
	        texAttr.setTextureTransform(transMap);              // Set the texture transformation

	        // Apply the texture to the appearance
	        app.setTextureAttributes(texAttr);
	        app.setTexture(textured_App("TLShade"));           // Load and apply the texture

	        // Add transparency to simulate fabric
	        TransparencyAttributes transparency = new TransparencyAttributes();
	        transparency.setTransparencyMode(TransparencyAttributes.BLENDED); // Smooth transparency
	        transparency.setTransparency(0.4f);                // 40% transparency
	        app.setTransparencyAttributes(transparency);

	        // Enable two-sided lighting
	        PolygonAttributes polyAttrs = new PolygonAttributes();
	        polyAttrs.setCullFace(PolygonAttributes.CULL_NONE); // Disable culling to render both sides
	        app.setPolygonAttributes(polyAttrs);
	    }

	    private void create_Light() {
	        // Create a PointLight to simulate the bulb inside the lampshade
	        bulbLight = new PointLight();
	        bulbLight.setColor(new Color3f(2.0f, 1.0f, 0.9f));  // Warm white light
	        bulbLight.setPosition(new Point3f(0, 0, -0 ));     // Position the light inside the lampshade
	        bulbLight.setInfluencingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));  // Set influence bounds

	        // Add the light to the lampshade's TransformGroup
	        objTG.addChild(bulbLight);
	    }

	    public TransformGroup position_Object() {
	        objTG.addChild(objBG);                             // Attach the object's BranchGroup to its TransformGroup
	        return objTG;                                      // Return the TransformGroup
	    }

	    public void add_Child(TransformGroup nextTG) {
	        objTG.addChild(nextTG);                            // Attach another TransformGroup to the object's TransformGroup
	    }
	}


//Class responsible for making the TabeLamp shade fitting 
class TLShadeFitting extends BODObjects{
	
	public TLShadeFitting() {
		scale = 1.04d;                                        // use to scale up/down original size
		post = new Vector3f(0f, -0.01f, 0f);                 // use to move object for positioning
		transform_Object("TLShadeFitting");                      	// set transformation to 'objTG' and load object file
		create_Appearance() ;
	}

	protected void create_Appearance() {
		// Defining colors, and  setting them on the shape
		 mtl_clr[0] = new Color3f(0.4f, 0.4f, 0.4f);         
	        mtl_clr[1] = new Color3f(0.8f, 0.8f, 0.8f);         
	        mtl_clr[2] = new Color3f(0.1f, 0.1f, 0.1f);        
	        mtl_clr[3] = new Color3f(0.8f, 0.8f, 0.8f);
		obj_Appearance();                                  
		
		// Enable texture attributes
      TextureAttributes texAttr = new TextureAttributes();
      texAttr.setTextureMode(TextureAttributes.MODULATE); 	// MODULATE, REPLACE, BLEND, etc.
      app.setTextureAttributes(texAttr);
      
   // Add transparency to simulate fabric
      TransparencyAttributes transparency = new TransparencyAttributes();
      transparency.setTransparencyMode(TransparencyAttributes.BLENDED); // Smooth transparency
      transparency.setTransparency(0.1f);                // 40% transparency
      app.setTransparencyAttributes(transparency);

          
   // Enable two-sided lighting
      PolygonAttributes polyAttrs = new PolygonAttributes();
      polyAttrs.setCullFace(PolygonAttributes.CULL_NONE); // Disable culling to render both sides
      
      
		app.setTexture(textured_App("TLShadeFitting"));     			// set texture for the base	
	}
	
	public TransformGroup position_Object() {              		// attach object BranchGroup of "TLOthers" to 'objTG'
		objTG.addChild(objBG);                             
		return objTG;                                      
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            		// attach the next transformGroup to 'objTG'
	}
	
	
}



/*
class SwitchObjectA4 extends A3ObjectsLH {
	private Switch objectSwitch;
	public SwitchObjectA4( Vector3f post , String name) {
		objectSwitch = new Switch();
		objectSwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);		// Allow Switch manipualtion during runtime 
		scale = 0.5d;                                      			// actual scale is 0.3 = 1.0 x 0.3      
		
		// Set up objTG with the suitable scale and position and capabilities
		Transform3D scaler = new Transform3D();
		scaler.setScale(scale);                           // set scale for the 4x4 matrix
		scaler.setTranslation(post);                      // set translations for the 4x4 matrix
		objTG = new TransformGroup(scaler);               // set the translation BG with the 4x4 matrix and created objTG of the button
		objTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		objTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		objTG.setCapability(Node.ENABLE_PICK_REPORTING); // need for mouse picking
		
		// Set up objSwitch with the two boxes, red and white 
		objectSwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);
		for (int i = 0; i < 2; i++) { 
			Color3f clr = (i == 0) ? CommonsLH.Red : CommonsLH.Green;
			Appearance app = CommonsLH.set_Appearance(clr);
			Box box = new Box(0.5f, 0.5f, 0.5f, Primitive.GENERATE_NORMALS, app);
			box.setUserData(i);                            // 'UserData' retrievable at picking
			box.setName(name);                            // NOTE: 'Name' is also retrievabl		
			objectSwitch.addChild(box);
		}
		objectSwitch.setWhichChild(1);                     // start with green box
		
		// Attach objSwitch to objTG
		objTG.addChild(objectSwitch);     
		
	}

	public TransformGroup position_Object() {			// Used to return objTG
		return objTG;                                     
	}

	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                            // attach the next transformGroup to 'objTG'
	}
	
	public Switch get_objectSwitch() {			          // used to return the Buttons objectSwitch
		return objectSwitch;                                     
	}
}
*/












