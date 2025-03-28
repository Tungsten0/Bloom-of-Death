package BOD;




// Copyright material for students working on assignments
import java.awt.Font;
import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.vecmath.*;
import org.jogamp.java3d.utils.geometry.Cylinder;
import org.jogamp.java3d.utils.geometry.Sphere;

public abstract class BaseShapesHS1 {//this is the abstruct class for creating and pos the 3d shapes that i will need tem 
	protected TransformGroup objTG = new TransformGroup(); // use 'objTG' to position an object
	protected abstract Node create_Object();               // allow derived classes to create different objects
	protected Appearance app;                              // allow each object to define its own appearance
	public TransformGroup position_Object() {	           // retrieve 'objTG' to which 'obj_shape' is attached
		return objTG;   //return the transform group that i cretaed 
	}
}//the end of the abstruct class 
class SquareShape1 extends BaseShapesHS1 {//the begining of the subclass that extract the absrtuct class 
	public SquareShape1() {//here i will represent a square base for the 3d model
		Transform3D translator = new Transform3D();//create the transform3d
		translator.setTranslation(new Vector3d(0.0, -0.54, 0.0));//set the translautions to be beloow the tower
		objTG = new TransformGroup(translator);            // down half of the tower and base's heights
		objTG.addChild(create_Object());                   // attach the object to 'objTG'
	}
	protected Node create_Object() {
		app = Commons.set_Appearance(Commons.White);   // set the appearance for the base
		return new Box(0.5f, 0.04f, 0.5f, Primitive.GENERATE_NORMALS, app);//create a square base
	}
}//the end of the SquareShape sub class
class CylinderTower extends BaseShapesHS1 {//here i am creating the cylindrical tower for the 3d model
    public CylinderTower() {
        objTG.addChild(create_Object());//here i am attaching the cylinder to transormGroup 
    }
    protected Node create_Object() {
        app = Commons.set_Appearance(new Color3f(1.0f, 0.5f, 0.0f));//setting the orange appearance
        return new Cylinder(0.12f, 1.0f, Primitive.GENERATE_NORMALS, 30, 30, app);//create a cylinder
    }
}//the end of the CylinderTower sub class
class SphereYawDrive extends BaseShapesHS1 {//here i am creating a sub class that repersent the spheriacal component for the yaw drive
    public SphereYawDrive() {
        Transform3D translator = new Transform3D();//creating the transform 3d to use it 
        translator.setTranslation(new Vector3d(0.0, 0.51, 0.0)); // here i am will osition the sphere on top of the tower
        objTG = new TransformGroup(translator);
        objTG.addChild(create_Object());
    }
    protected Node create_Object() {
        app = Commons.set_Appearance(Commons.Red); // Set the appearance to Red
        //app = Commons.set_Appearance(new Color3f(1.0f, 0.0f, 0.0f));
        return new Sphere(0.12f, Primitive.GENERATE_NORMALS,30, app); // Create a sphere
    }
}//the end of the sphereYawDeive sub class
class boxNacelleShape extends BaseShapesHS1 {//here i am creating the the nacelle shape on top of the sphere
    public boxNacelleShape() {
        Transform3D translator = new Transform3D();//creating the transform 3d to use it 
        translator.setTranslation(new Vector3d(0.0, 0.69, 0.12)); // Position the nacelle on top of the sphere
        Transform3D rotator = new Transform3D();//creating the transform 3d to use it  for the rotation 
        rotator.rotY(Math.PI / 2); //here it will rotate 90° along Y-axis
        translator.mul(rotator);
        objTG = new TransformGroup(translator);
        objTG.addChild(create_Object());
    }
    protected Node create_Object() {
        app = Commons.set_Appearance(Commons.Cyan); // Set the appearance to Cyan
       // app = Commons.set_Appearance(new Color3f(0.0f, 1.0f, 1.0f));
        return new Box(0.26f, 0.06f, 0.12f, Primitive.GENERATE_NORMALS, app); // Create a box for the nacelle
    }
}//the end of the boxNacelleShape sub class
class RotorBladeShape extends BaseShapesHS1 {//the begining  of the RotorBladeShape
    public RotorBladeShape() {
        objTG.addChild(create_Object());
    }
    protected Node create_Object() {
        TransformGroup bladeTG = new TransformGroup();
        app = Commons.set_Appearance(Commons.Magenta);
        Box rotorBlade = new Box(0.010f, 0.06f, 0.40f, Primitive.GENERATE_NORMALS, app);//here iam creating a balde
        Transform3D translator = new Transform3D();//creating the transform 3d to use it 
        translator.setTranslation(new Vector3d(0.0, 0.69, 0.443)); // Positioned in front of the nacelle    
        Transform3D rotator = new Transform3D();//create a new transform 3d 
        rotator.rotY(Math.PI / 2); // Rotate 90° around Y
        translator.mul(rotator); // Combine translation and rotation
        TransformGroup boxGroup = new TransformGroup(translator);
        boxGroup.addChild(rotorBlade);//add the new blade that i create as the child of the transform Group that deal with the box blade
        bladeTG.addChild(boxGroup);//add the new boxgroup that i create that have the blade as a cilde to the first transform group that i create it 
        return bladeTG;//retrun the group of the transform that i created
    }
}//the end of the RotorBladeShape subclass
class redSphere extends BaseShapesHS1{//this sub class that represent the red sphere 
	public redSphere() {
		Transform3D traslator = new Transform3D();
		traslator.setTranslation(new Vector3d(0, 0.69f, 0.38));//set the translations 
		objTG = new TransformGroup(traslator);
		objTG.addChild(create_Object());
	}
	protected Node create_Object() {
		app = Commons.set_Appearance(new Color3f(1.0f, 0.0f, 0.0f));
		return new Sphere(0.06f, Primitive.GENERATE_NORMALS, 30, app);
	}
}
class ColorString1 extends BaseShapesHS1 {// a derived class to create a string label and place it to the bottom of the self-made cone 
	private String str;
	private Color3f clr;
	private double scl;
	private Point3f pos;                                           // make the label adjustable with parameters
	public ColorString1(String str_ltrs, Color3f str_clr, double s, Point3f p) {
		str = str_ltrs;	
		clr = str_clr;
		scl = s;
		pos = p;

		Transform3D scaler = new Transform3D();
		scaler.setScale(scl);                              // scaling 4x4 matrix 
		Transform3D rotator = new Transform3D();           // 4x4 matrix for rotation
		rotator.rotY(Math.PI);
		Transform3D trfm = new Transform3D();              // 4x4 matrix for composition
		trfm.mul(rotator);                                 // apply rotation second
		trfm.mul(scaler);                                  // apply scaling first
		objTG = new TransformGroup(trfm);                  // set the combined transformation
		objTG.addChild(create_Object());                   // attach the object to 'objTG'		
	}
	
	protected Node create_Object() {
		Font my2DFont = new Font("Arial", Font.PLAIN, 1);  // font's name, style, size
		FontExtrusion myExtrude = new FontExtrusion();
		Font3D font3D = new Font3D(my2DFont, myExtrude);	
		Text3D text3D = new Text3D(font3D, str, pos);      // create 'text3D' for 'str' at position of 'pos'
		
		Appearance app = Commons.set_Appearance(clr);    // use appearance to specify the string color
		return new Shape3D(text3D, app);                   // return a string label with the appearance
	}
}
class colourString extends BaseShapesHS1 {//here i am doing the colored string label in the sub class that i modified it from the prof code
    private String text;
    private Color3f color;
    
    public colourString(String text, Color3f color) {//the constructor 
        this.text = text;
        this.color = color;
        Transform3D translator = new Transform3D();
        translator.setTranslation(new Vector3d(0.12, 0.66, 0.30)); // here i positioned on top of the blue box
        Transform3D rotator = new Transform3D();
        rotator.rotY(Math.PI / 2); //here iam rotate the text for alignment with blue box
        translator.mul(rotator);
        Transform3D scaler = new Transform3D();
        scaler.setScale(0.1); //here i am scaling thhe reduce text size
        translator.mul(scaler);//here i am multplying 
        objTG = new TransformGroup(translator);//create a new tronsform group 
        objTG.addChild(create_Object());
    }
    @Override
    protected Node create_Object() {//here i am overriding the crete_object 
        Font font = new Font("Arial", Font.PLAIN, 1);//create the font of the sting
        FontExtrusion extrude = new FontExtrusion();//here i am creating the extraction of the font
        Font3D font3D = new Font3D(font, extrude);
        Text3D text3D = new Text3D(font3D, text, new Point3f(0.0f, 0.0f, 0.0f));
        app = Commons.set_Appearance(color);//here i am setting the appearnce for the text
        return new Shape3D(text3D, app);//here  i am creating a text shape 
    }
}

class coordinateSystem extends BaseShapesHS1{//here is the sub class the  optional for Assignment 1
	public coordinateSystem() {//the constuctor 
		objTG.addChild(create_Object());
	}
	protected Node create_Object() {// the coordinate system with X, Y, and Z axessssss
		LineArray xy= new LineArray(6, LineArray.COORDINATES | LineArray.COLOR_3);//here i am defining the  new lineArrage  XY and give it the option the color and corrdinates
		xy.setCoordinate(0, new Point3f(0.0f, 0.0f, 0.0f));//the x axis orgin
		xy.setCoordinate(1, new Point3f(1.0f, 0.0f, 0.0f)); //x-axis positive direction
		xy.setColor(0, new Color3f(0.3f, 0.0f, 0.0f)); //here i am Softer red color  for X-axis
        xy.setColor(1, new Color3f(0.3f, 0.0f, 0.0f));
        xy.setCoordinate(2, new Point3f(0.0f, 0.0f, 0.0f)); //y-axis origin
        xy.setCoordinate(3, new Point3f(0.0f, 1.0f, 0.0f)); //y-axis positive direction
        xy.setColor(2, new Color3f(0.0f, 0.3f, 0.0f)); // here i an giving softer green color  for Y-axis
        xy.setColor(3, new Color3f(0.0f, 0.3f, 0.0f));
        xy.setCoordinate(4, new Point3f(0.0f, 0.0f, 0.0f)); //the z-axis origin
        xy.setCoordinate(5, new Point3f(0.0f, 0.0f, 1.0f)); //the z-axis positive direction
        xy.setColor(4, new Color3f(0.0f, 0.0f, 0.3f)); //the softer blue  color for Z-axis
        xy.setColor(5, new Color3f(0.0f, 0.0f, 0.3f));
        return new Shape3D(xy);	
	}
}

