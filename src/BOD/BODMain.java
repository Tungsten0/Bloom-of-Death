package BOD;
//hi
import java.awt.AWTException;

/* Copyright material for students working on assignments */

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.GraphicsConfiguration;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.picking.PickResult;
import org.jogamp.java3d.utils.picking.PickTool;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.java3d.utils.universe.ViewingPlatform;
import org.jogamp.vecmath.*;

import com.jogamp.nativewindow.util.Point;


public class BODMain extends JPanel implements KeyListener, MouseListener, MouseMotionListener {
	
	private static final long serialVersionUID = 1L;
	private static JFrame frame;

	private static final int OBJ_NUM = 8;		// New : changed 2 to 6 to accommodate six objects // NEW: changed from 6 to 8 since we want to add two more buttons 
	private static BODObjects[] object3D = new BODObjects[OBJ_NUM];
	
	// NEW: Added for A4 -----------------------------------------------------------------------------------------------------
	protected static String snd_bk = "Fan";                // specify the name of the background sound of the fanswitch
	protected static SoundUtilityJOAL soundJOAL;             // need for playing sound
	private static PickTool pickTool;	
	private Canvas3D canvas;                             // need for mouse picking
	//-------------------------------------------------------------------------------------------------------------------------
	// Needed for the user's movement
	private SimpleUniverse su;
    private TransformGroup viewTransformGroup;
    private Transform3D viewTransform = new Transform3D();
    private double moveSpeed = 1; // Speed of movement
    private double mouseSensitivity = 0.005; // Sensitivity for mouse look
    private int lastMouseX, lastMouseY; // Store the last mouse position
    private double rotationX = 0; // Rotation around the X-axis (up/down)
    private double rotationY = 0; // Rotation around the Y-axis (left/right)
    private Set<Integer> activeKeys = new HashSet<>(); // Track active keys   
    private double bounceTime = 0.0; // Tracks time for the bounce effect
    private final double bounceSpeed = 8.0; // Controls the speed of the bounce
    private final double bounceAmplitude = 0.05; // Controls the height of the bounce
    private boolean isMoving = false; // Tracks if the user is moving
    
    
    // Noor debugging part -----------------------------------------------------------------------------------------  
    private boolean isCrouching = false;  // used for croushing 
    private final double crouchOffset = -0.5; // Lower height by 0.5 units
    private static TransformGroup addReferenceCornerMarkers() {    //TEMPORARY FUNCTION%%%%%%%%%%%%%%%%%%%%%%%%%%%% DELETE ME LATER %%%%%
        TransformGroup cornerMarkersTG = new TransformGroup();

        // Define positions
        Vector3d backLeft = new Vector3d(-Room.roomWidth + 1, 0.5, -Room.roomLength + 1);
        Vector3d backRight = new Vector3d(Room.roomWidth - 1, 0.5, -Room.roomLength + 1);
        Vector3d frontRight = new Vector3d(Room.roomWidth - 1, 0.5, Room.roomLength - 1);
        Vector3d frontLeft = new Vector3d(-Room.roomWidth + 1, 0.5, Room.roomLength - 1);

        cornerMarkersTG.addChild(createColoredMarkerBox(frontLeft, new Color3f(1, 0, 0)));   // Red - back-right
        cornerMarkersTG.addChild(createColoredMarkerBox(backLeft, new Color3f(0, 1, 0)));    // Green - front-right
        cornerMarkersTG.addChild(createColoredMarkerBox(frontRight, new Color3f(0, 0, 1)));  // Blue - back-left
        cornerMarkersTG.addChild(createColoredMarkerBox(backRight, new Color3f(1, 1, 0)));   // Yellow - front-left


        return cornerMarkersTG;
    }

    private static TransformGroup createColoredMarkerBox(Vector3d position, Color3f color) { //TEMPORARY FUNCTION%%%%%%%%%%%%%%%%%%%%%%%%%%%% DELETE ME LATER %%%%%
        Appearance app = new Appearance();
        Material mat = new Material(color, new Color3f(0, 0, 0), color, color, 80f);
        mat.setLightingEnable(true);
        app.setMaterial(mat);

        Box box = new Box(1.0f, 1.0f, 1.0f, app);  // 2x2x2 box marker

        Transform3D transform = new Transform3D();
        transform.setTranslation(position);

        TransformGroup tg = new TransformGroup(transform);
        tg.addChild(box);
        return tg;
    }
    
    private void toggleCrouch() {
        Transform3D transform = new Transform3D();
        viewTransformGroup.getTransform(transform);
        Vector3d currentPosition = new Vector3d();
        transform.get(currentPosition);

        if (isCrouching) {
            currentPosition.y -= crouchOffset; // stand up
        } else {
            currentPosition.y += crouchOffset; // crouch down
        }

        transform.setTranslation(currentPosition);
        viewTransformGroup.setTransform(transform);
        isCrouching = !isCrouching;
    }
    
    private void addMiniMap(JFrame frame) {//TEMPORARY FUNCTION%%%%%%%%%%%%%%%%%%%%%%%%%%%% DELETE ME LATER %%%%%
        Canvas3D minimapCanvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        SimpleUniverse miniUniverse = new SimpleUniverse(minimapCanvas);
        
        Transform3D miniViewTransform = new Transform3D();
        miniViewTransform.lookAt(
            new Point3d(250, 100.0, 0),    // High and far back corner
            new Point3d(50, 15, 0),        // Look at room center
            new Vector3d(0, 1, 0)        // Up vector
        );
        miniViewTransform.invert();
        miniUniverse.getViewingPlatform().getViewPlatformTransform().setTransform(miniViewTransform);


        // Add a simple minimap scene
        BranchGroup miniScene = create_MiniMapScene();
        miniUniverse.addBranchGraph(miniScene);

        // Add minimap using absolute positioning on top-right
        minimapCanvas.setBounds(frame.getWidth() - 540, 20, 500, 200);
        minimapCanvas.setFocusable(false);

        JLayeredPane layeredPane = frame.getLayeredPane();
        layeredPane.add(minimapCanvas, JLayeredPane.PALETTE_LAYER);
        
        View miniView = miniUniverse.getViewer().getView();
        miniView.setBackClipDistance(500.0);
        miniView.setFrontClipDistance(0.1);

    }

    public static BranchGroup create_MiniMapScene() {//TEMPORARY FUNCTION%%%%%%%%%%%%%%%%%%%%%%%%%%%% DELETE ME LATER %%%%%
        BranchGroup miniSceneBG = new BranchGroup();
        TransformGroup sceneTG = new TransformGroup();

        // Add the room
        TransformGroup roomTG = Room.createEmptyRoom();
        sceneTG.addChild(roomTG);

        // Add only major objects
        //roomTG.addChild(TableLamp.create_TableLamp());

        // Add only major objects
        //sceneTG.addChild(addDebugCorners());
   
        // Add debug corner markers
        //sceneTG.addChild(addDebugCorners());
      

        Lights.setupSceneEffects(sceneTG);

        miniSceneBG.addChild(sceneTG);
        miniSceneBG.compile();
        return miniSceneBG;
    }
    
    private static TransformGroup addDebugCorners() {    //TEMPORARY FUNCTION%%%%%%%%%%%%%%%%%%%%%%%%%%%% DELETE ME LATER %%%%%
        TransformGroup debugCornersTG = new TransformGroup();

        // Corner positions based on roomWidth and roomLength
        double[][] corners = {
            { Room.roomWidth, 0.1, Room.roomLength },    // Front-right corner
            { -Room.roomWidth, 0.1, Room.roomLength },   // Front-left corner
            { Room.roomWidth, 0.1, -Room.roomLength },   // Back-right corner
            { -Room.roomWidth, 0.1, -Room.roomLength }   // Back-left corner
        };

        for (double[] c : corners) {
            Transform3D t3d = new Transform3D();
            t3d.setTranslation(new Vector3d(c[0], c[1], c[2]));
            TransformGroup cornerTG = new TransformGroup(t3d);

            // Create a small colored box for each corner
            Appearance app = new Appearance();
            Color3f color = new Color3f((float)Math.random(), (float)Math.random(), (float)Math.random());
            Material mtl = new Material(color, new Color3f(0,0,0), color, color, 80f);
            mtl.setLightingEnable(true);
            app.setMaterial(mtl);

            cornerTG.addChild(new Box(0.5f, 0.5f, 0.5f, app));
            debugCornersTG.addChild(cornerTG);
        }

        return debugCornersTG;
    }
// ---------------------------------------------------------------------------------------------------------------
    
   

	/* a function to build the content branch, including the fan and other environmental settings */
	public static BranchGroup create_Scene() {
		BranchGroup sceneBG = new BranchGroup();
		TransformGroup sceneTG = new TransformGroup();
			
		// Create the room 
	    TransformGroup roomTG = Room.createEmptyRoom();	    
	    // Add the room to the sceneTG
	     sceneTG.addChild(roomTG);
	    
		// Create the lamp to the room 
	    roomTG.addChild(TableLamp.create_TableLamp());
		
		// Create the room's ceiling lamp 
		//roomTG.addChild(CeilingLamp.create_CeilingLamp());
	    

		
		// Setting up the light resource, and applying it to the TableLamp sceneBG
		// Add ambient light
		AmbientLight ambientLight = new AmbientLight(new Color3f(1.0f, 1.0f, 1.0f));
		ambientLight.setInfluencingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
		sceneBG.addChild(ambientLight);
		// Add directional light
		DirectionalLight directionalLight = new DirectionalLight(
		    new Color3f(1.0f, 1.0f, 1.0f), // Light color
		    new Vector3f(-1.0f, -1.0f, -1.0f) // Light direction
		);
		directionalLight.setInfluencingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
		sceneTG.addChild(directionalLight);

		
		
		
	    // write your codes here ------------------------------------------------------------------------
	    // exp : roomTG.addChild(something)
	    
		// Adding Noor's objects 
		TransformGroup sofaTG = Sofa.create_Sofa();
		roomTG.addChild(sofaTG);																						 
		TransformGroup corpseTG = Corpse.create_Corpse();
		 roomTG.addChild(corpseTG);																					    
		 TransformGroup carpetTG = Carpet.create_Carpet();
		 roomTG.addChild(carpetTG);
		 TransformGroup table5TG = Table5.create_Table5();
		 roomTG.addChild(table5TG);		
		    
		 //Adding Hanan's Objects 
		 
		 TransformGroup ChairTG = ChairHS.create_ChairHS();
		 roomTG.addChild(ChairTG);
		 
		 
		     
		    
		 
		 TransformGroup DrugsTG = drugsObject3.create_drugs();
		 roomTG.addChild(DrugsTG);
		 //Adding Kabir's objects here 
		  
		  
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
	    //-------------------------------------------------------------------------------------------------------------
	   
		
	   // Set up lighting for sceneTG 
	   Lights.setupSceneEffects(sceneTG);
	   
	   // Noor's debugging------------------------------------------------------------------------------------------------------------------
	   sceneTG.addChild(addReferenceCornerMarkers()); //TEMPORARY FUNCTION CALL%%%%%%%%%%%%%%%%%%%%%%%%%%%% DELETE ME LATER %%%%%
	   //------------------------------------------------------------------------------------------------------------
        
	   //sceneTG.addChild(Commons.rotate_Behavior(9000, sceneTG));
       sceneBG.addChild(sceneTG);                        
		
		
			
		return sceneBG;
	}

	
	/* NOTE: Keep the constructor for each of the labs and assignments */
	public BODMain(BranchGroup sceneBG) {
	    GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
	    canvas = new Canvas3D(config);

	    SimpleUniverse su = new SimpleUniverse(canvas); // Create a SimpleUniverse

	    // Adjust the clipping planes of the View
	    View view = su.getViewer().getView();
	    view.setBackClipDistance(100.0); // Set the far clipping plane to 100 units
	    view.setFrontClipDistance(0.1);  // Set the near clipping plane to 0.1 units

	    Commons.define_Viewer(su, new Point3d(0.25d, 0.25d, 90.0d)); // Set the viewer's location

	    sceneBG.compile(); // Optimize the BranchGroup
	    su.addBranchGraph(sceneBG); // Attach the scene to SimpleUniverse

	    setLayout(new BorderLayout());
	    add("Center", canvas);

	    frame.setSize(800, 800); // Set the size of the JFrame
	    frame.setVisible(true);

	    // Part related to the user's movement in the room
	    // Get the ViewingPlatform and its TransformGroup
	    ViewingPlatform viewingPlatform = su.getViewingPlatform();
	    viewTransformGroup = viewingPlatform.getViewPlatformTransform();
	    viewTransformGroup.getTransform(viewTransform);

	    // Initialize last mouse position
	    lastMouseX = canvas.getWidth() / 2;
	    lastMouseY = canvas.getHeight() / 2;

	    // Add mouse and key listeners
	    canvas.addKeyListener(this);
	    canvas.addMouseListener(this);
	    canvas.addMouseMotionListener(this);

	    // Add focus listener to ensure the canvas retains focus
	    canvas.addFocusListener(new FocusListener() {
	        @Override
	        public void focusGained(FocusEvent e) {
	            // Do nothing
	        }

	        @Override
	        public void focusLost(FocusEvent e) {
	            // Request focus back to the canvas
	            canvas.requestFocus();
	        }
	    });

	    // Make the canvas focusable
	    canvas.setFocusable(true);
	    canvas.requestFocus();

	    // Hide the mouse cursor
	    canvas.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

	    // Lock the mouse to the center
	    PointerInfo pointerInfo = MouseInfo.getPointerInfo();
	    Point centerPoint = new Point(canvas.getWidth() / 2, canvas.getHeight() / 2);
	    Robot robot = null;
	    try {
	        robot = new Robot();
	    } catch (AWTException e1) {
	        e1.printStackTrace();
	    }
	    robot.mouseMove(centerPoint.getX(), centerPoint.getY());
	    
	    // Noor's debugging steps, to add the minimap  -----------------------------------------------------	    
	    //addMiniMap(frame);    //minimap    killswitch
	    //-------------------------------------------------------------
	    
	}
	
	
	public static void main(String[] args) {
		frame = new JFrame("Project Bloom of Death");                   
		frame.getContentPane().add(new BODMain(create_Scene()));  // start the program
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
    
	
	
//------------------------------------------------------------------------------------------	
	
	@Override	
	public void keyPressed(KeyEvent e) {
		
	    int keyCode = e.getKeyCode();

	    // Get the current view transformation
	    Transform3D transform = new Transform3D();
	    viewTransformGroup.getTransform(transform);

	    // Extract the current position
	    Vector3d currentPosition = new Vector3d();
	    transform.get(currentPosition);

	    // Define movement direction in local (view) space
	    Vector3d movement = new Vector3d();

	    switch (keyCode) {
		    // Noor's debugging steps ------------------------
		    case KeyEvent.VK_C: // Toggle crouch
		        toggleCrouch();
		        break;
		    case KeyEvent.VK_ESCAPE: // Quit the application
		        System.exit(0);
		        break;
		    // ------------------------------------------
	        case KeyEvent.VK_UP: // Move forward
	            movement.set(0, 0, -moveSpeed);
	            isMoving = true; // Start tracking movement
	            break;
	        case KeyEvent.VK_DOWN: // Move backward
	            movement.set(0, 0, moveSpeed);
	            isMoving = true; // Start tracking movement
	            break;
	        case KeyEvent.VK_LEFT: // Move left
	            movement.set(-moveSpeed, 0, 0);
	            break;
	        case KeyEvent.VK_RIGHT: // Move right
	            movement.set(moveSpeed, 0, 0);
	            break;
	        case KeyEvent.VK_PAGE_UP: // Move up (optional, jump)
	            movement.set(0, moveSpeed, 0);
	            break;
	        case KeyEvent.VK_PAGE_DOWN: // Move down, but prevent going too low
	            movement.set(0, -moveSpeed, 0);
	            break;
	        case KeyEvent.VK_R: // Reset position and orientation
	            resetCamera();
	            return;
	    }

	    // Apply rotation to movement direction
	    Matrix3d rotationMatrix = new Matrix3d();
	    transform.get(rotationMatrix);
	    rotationMatrix.transform(movement);

	    // Update position
	    currentPosition.add(movement);
	    
	    // Noor's debugging, for the crouching thing ---------------------------
	    // Restrict movement: Prevent the user from going outside the room
        double minY = isCrouching ? -17.8 : 0.2; 
        double maxY = isCrouching ? -17.8 : 0.2;
        //---------------------------------------------------------------------------


	    // Restrict movement: Prevent the user from going outside the room
	   // double minY = 0.2; // Minimum walking height (adjust as needed)  Commented those to make noor's debugging works, we will remove the comments later on 
	   //double maxY = 0.2; // Maximum walking height (adjust as needed)
	    double maxX = Room.roomWidth - 5;
	    double minX = - (Room.roomWidth - 5 ) ; 
	    double minZ = - ( Room.roomLength - 5 ) ;
	    double maxZ = Room.roomLength - 5 ;
	    
	    
	    if (currentPosition.y < minY) {
	        currentPosition.y = minY; // Keep the user above the ground
	    }
	    if (currentPosition.y > maxY) {
	        currentPosition.y = maxY; // Keep the user above the ground
	    }
	    if (currentPosition.x >= maxX) {
	        currentPosition.x = maxX;
	    }
	    if (currentPosition.x <= minX) {
	        currentPosition.x = minX;
	    }
	    if (currentPosition.z >= maxZ) {
	        currentPosition.z = maxZ;
	    }
	    if (currentPosition.z <= minZ) {
	        currentPosition.z = minZ;
	    }
	   
	    

	    // Apply bouncy effect if the user is moving forward or backward
	    if (isMoving && (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN)) {
	        bounceTime += 0.1; // Increment time for the bounce effect
	        double bounceOffset = Math.sin(bounceTime * bounceSpeed) * bounceAmplitude;
	        currentPosition.y += bounceOffset; // Apply the bounce to the Y position
	    } else {
	        isMoving = false; // Stop tracking movement
	        bounceTime = 0.0; // Reset bounce time
	    }

	    // Apply new position while keeping the rotation
	    transform.setTranslation(currentPosition);
	    viewTransformGroup.setTransform(transform);
	}

	private void resetCamera() {
	    // Reset the viewTransform to the initial state
	    viewTransform.setIdentity(); // Reset the transform to the identity matrix
	    viewTransform.setTranslation(new Vector3d(0.25d, 0.25d, 4.0d)); // Set the initial position
	    rotationX = 0; // Reset X rotation
	    rotationY = 0; // Reset Y rotation

	    // Apply the reset transform to the viewTransformGroup
	    viewTransformGroup.setTransform(viewTransform);
	}
	
    @Override
    
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        // Stop the bouncy effect when the user releases the movement keys
        if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN) {
            isMoving = false;
            bounceTime = 0.0;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used, but required by KeyListener
    }

    
    
    
    
    
    
    //-------------------------------------------------
    
 // MouseListener implementation
    @Override
    public void mouseClicked(MouseEvent e) {
        // Request focus when the canvas is clicked
        canvas.requestFocus();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Request focus when the mouse is pressed
        canvas.requestFocus();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Do nothing
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Do nothing
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Do nothing
    }

    // MouseMotionListener implementation
    @Override
    public void mouseDragged(MouseEvent e) {
        int deltaX = e.getX() - lastMouseX;
        int deltaY = e.getY() - lastMouseY;

        // Update rotation angles
        rotationY += -deltaX * mouseSensitivity; // Left/right rotation (around Y-axis)
        rotationX += -deltaY * mouseSensitivity; // Up/down rotation (around X-axis)

        // Clamp the up/down rotation to avoid flipping
        rotationX = Math.max(-Math.PI / 2, Math.min(Math.PI / 2, rotationX));

        // Get the current view transformation
        Transform3D transform = new Transform3D();
        viewTransformGroup.getTransform(transform);

        // Extract the current position (prevent resetting position!)
        Vector3d currentPosition = new Vector3d();
        transform.get(currentPosition);

        // Create a new rotation transform
        Transform3D rotationTransform = new Transform3D();
        rotationTransform.rotY(rotationY); // Apply Y-axis rotation (left/right)

        Transform3D tempRot = new Transform3D();
        tempRot.rotX(rotationX); // Apply X-axis rotation (up/down)
        rotationTransform.mul(tempRot);

        // Extract the rotation matrix
        Matrix3d rotationMatrix = new Matrix3d();
        rotationTransform.get(rotationMatrix);

        // Apply rotation while keeping the current position
        transform.setRotation(rotationMatrix); // Set only the rotation
        transform.setTranslation(currentPosition); // Restore position

        // Apply the final transform to the viewTransformGroup
        viewTransformGroup.setTransform(transform);

        // Update last mouse position
        lastMouseX = e.getX();
        lastMouseY = e.getY();
    }


    @Override
    public void mouseMoved(MouseEvent e) {
        // Update last mouse position
        lastMouseX = e.getX();
        lastMouseY = e.getY();
    }
    
    
    private void updateViewTransform() {
        Transform3D transform = new Transform3D();
        viewTransformGroup.getTransform(transform);

        Vector3d currentPosition = new Vector3d();
        transform.get(currentPosition);

        Transform3D rotationTransform = new Transform3D();
        rotationTransform.rotY(rotationY);

        Transform3D tempRot = new Transform3D();
        tempRot.rotX(rotationX);
        rotationTransform.mul(tempRot);

        Matrix3d rotationMatrix = new Matrix3d();
        rotationTransform.get(rotationMatrix);

        transform.setRotation(rotationMatrix);
        transform.setTranslation(currentPosition);
        viewTransformGroup.setTransform(transform);
    }


	
    
    
	
	/*
	// NEW: KeyListener class to handle pressing x and z--------------------------------------------------
	static class KeyHandler extends KeyAdapter {	
	    @Override
	    public void keyTyped(KeyEvent e) {
	        char keyChar = e.getKeyChar(); 

	        // Access the Switch objects for the buttons
	        Switch RotateButton = ((SwitchObjectA4) object3D[6]).get_objectSwitch(); 
	        Switch PowerButton = ((SwitchObjectA4) object3D[7]).get_objectSwitch(); 

	        System.out.println("RotateButton state: " + RotateButton.getWhichChild()); // Debug
	        System.out.println("PowerButton state: " + PowerButton.getWhichChild());   // Debug

	        // Z will stop the movement of the shaft and turn on the blades        
	        if ((keyChar == 'z' || keyChar == 'Z') && FanOn) { // Only apply this when the fan is on                             
	            if (rotating) {								  
	                object3D[2].get_Alpha().pause();          // Pause the shaft animation  
	                RotateButton.setWhichChild(0);            // Set RotateButton to Red
	            } else {
	                object3D[2].get_Alpha().resume(); 
	                RotateButton.setWhichChild(1);            // Set RotateButton to Green
	            }
	            rotating = !rotating; 
	        } 
	        // X will turn on and off the whole fan
	        else if (keyChar == 'x' || keyChar == 'X') {                
	            if (FanOn) { // If Fan is on, we need to turn it off 
	                object3D[4].get_Alpha().pause();          // Stop blades
	                PowerButton.setWhichChild(0);             // Set PowerButton to Red (OFF)
                    soundJOAL.stop(snd_bk);       		      // Stop sound	                    
	                if (rotating) {
	                    object3D[2].get_Alpha().pause();
	                    RotateButton.setWhichChild(0);         // Set RotateButton to Red
	                }
	            } else {
	                if (rotating) {
	                    object3D[2].get_Alpha().resume(); 
	                    RotateButton.setWhichChild(1);         // Set RotateButton to Green
	                }
	                object3D[4].get_Alpha().resume();
	                PowerButton.setWhichChild(1);             // Set PowerButton to Green (ON)
                    soundJOAL.play(snd_bk);       		      // Stop sound	                    
	            }
	            FanOn = !FanOn; 
	        }     
	    }
	}
	// ----------------------------------------------------------------------------------------------------
	
	
	public void mouseClicked(MouseEvent event) {
		
		// Access the Switch objects for the buttons
        Switch RotateButton = ((SwitchObjectA4) object3D[6]).get_objectSwitch(); 
        Switch PowerButton = ((SwitchObjectA4) object3D[7]).get_objectSwitch(); 
        
		int x = event.getX(); int y = event.getY();        // mouse coordinates
		Point3d point3d = new Point3d(), center = new Point3d();
		canvas.getPixelLocationInImagePlate(x, y, point3d); // obtain AWT pixel in ImagePlate coordinates
		canvas.getCenterEyeInImagePlate(center);         // obtain eye's position in IP coordinates
		
		Transform3D transform3D = new Transform3D();       // matrix to relate ImagePlate coordinates~
		canvas.getImagePlateToVworld(transform3D);       // to Virtual World coordinates
		transform3D.transform(point3d);                    // transform 'point3d' with 'transform3D'
		transform3D.transform(center);                     // transform 'center' with 'transform3D'

		Vector3d mouseVec;
		mouseVec = new Vector3d();
		mouseVec.sub(point3d, center);
		mouseVec.normalize();

		pickTool.setShapeRay(point3d, mouseVec);           						// send a PickRay for intersection

	    // Check if an object was picked
	    PickResult pickResult = pickTool.pickClosest();							// Pick closest obejct that was clicked 
	    
	    if (pickResult != null) {
	    	
	        Node pickedNode = pickResult.getNode(PickResult.PRIMITIVE);
	        
	        if (pickedNode instanceof Box) {
	        	
	            Box pickedBox = (Box) pickedNode;
	            String objectName = pickedBox.getName(); // Get the name of the picked object shape

	            // Check if the picked object is a button
	            if ("RotateButton".equals(objectName) && FanOn) {	  //  and the Fan is On, means this button won't work unless the Fan is On  
	                int currentState = (int) pickedBox.getUserData(); // Get current state (0 or 1)	                               
	                if (currentState == 0 ) {		        		  // if the button is red 
	                	RotateButton.setWhichChild(1);  			  // Switch to Green 
						object3D[2].get_Alpha().resume();			  // resume rotating 
						rotating = true ; 
	                } else {										  // if the button is green 
	                	RotateButton.setWhichChild(0);      		  // Switch to Red (OFF)
						object3D[2].get_Alpha().pause(); 
						rotating = false ;                     
	                }
	            } else  if ("PowerButton".equals(objectName)) {
	                int currentState2 = (int) pickedBox.getUserData(); // Get current state (0 or 1)	           
	                if (currentState2 == 0) {				  			// If red ( OFF ) we need to turn it ON   
	                	// Switch both buttons to Green (ON)
	                	PowerButton.setWhichChild(1);  	  	  			
	                    soundJOAL.play(snd_bk);       		  			// Play sound	                    
	                    if (rotating) {
							object3D[2].get_Alpha().resume(); 			// Resume Shaft rotation 
		                	RotateButton.setWhichChild(1);  
						}
						object3D[4].get_Alpha().resume();	  			// Resume Blades rotation
						FanOn = true ; 
	                } else {							 				// If green, fan is ON and we need to turn it OFF
	                	// Switch both to Red (OFF)
	                	PowerButton.setWhichChild(0);   				
	                	RotateButton.setWhichChild(0); 
	                    soundJOAL.pause(snd_bk);        	    		// Stop sound
	                    object3D[4].get_Alpha().pause();				// Stop blades				
						if (rotating) {
							object3D[2].get_Alpha().pause();			// Pause the rotation of the shaft
						}
						FanOn = false ; 	                                       
	                }
	            	              	
	            }
	        }
	    }
	}

	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	*/
    
}
    
    




