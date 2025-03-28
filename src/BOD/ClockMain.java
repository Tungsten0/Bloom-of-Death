package BOD;
// Copyright material for students working on assignments
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.*;

class ClockMain extends JPanel {
	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	private static final int OBJ_NUM = 20;
	private static ClockObjects4[] object3D = new ClockObjects4[OBJ_NUM];
	private static String snd_bk = "clockTicking";                // specify the name of the background sound
	private static SoundUtilityJOAL soundJOAL;             // need for playing sound

	
	public static TransformGroup create_ClockHS() {
	    TransformGroup fanTG = new TransformGroup();

	    // Create and attach ClockBase
	    object3D[0] = new ClockBase();
	    fanTG = object3D[0].position_Object();  // Set 'fanTG' to ClockBase's 'objTG'

	    // Create and attach ClockHouse to ClockBase
	    object3D[1] = new ClockHouse();
	    object3D[0].add_Child(object3D[1].position_Object());

	    // Create and attach ClockCenter to ClockHouse
	    object3D[2] = new ClockCenter();
	    object3D[1].add_Child(object3D[2].position_Object());

	    // Create and attach ClockTop to ClockHouse
	    object3D[3] = new ClockTop();
	    object3D[1].add_Child(object3D[3].position_Object());

	    // Create and attach ClockSide1 to ClockTop
	    object3D[4] = new ClockSide1();
	    object3D[3].add_Child(object3D[4].position_Object());

	    // Create and attach ClockSide2 to ClockTop
	    object3D[5] = new ClockSide2();
	    object3D[3].add_Child(object3D[5].position_Object());
	    
	    object3D[6] = new ClockWindowobj();
	    object3D[1].add_Child(object3D[6].position_Object());
	    
	    object3D[7] = new ClockTree1();
	    object3D[1].add_Child(object3D[7].position_Object());
	    
	    
	    object3D[8] = new TreeBase1();
	    object3D[7].add_Child(object3D[8].position_Object());
	    

	    object3D[9] = new ClockTree2();
	    object3D[1].add_Child(object3D[9].position_Object());
	    
	    object3D[10] = new TreeBase2();
	    object3D[9].add_Child(object3D[10].position_Object());
	    
	    object3D[11] = new HourHand();
	    object3D[2].add_Child(object3D[11].position_Object());
	    
	    
	    object3D[12] = new MinuteHand();
	    object3D[2].add_Child(object3D[12].position_Object());
	    
	    
	    object3D[13] = new ClockDong();
	    object3D[0].add_Child(object3D[13].position_Object());
	    
	    

	    
	    
	    // Add coordinate system (optional)
	    coordinateSystem coordSystem = new coordinateSystem();
	    Transform3D coordTransform = new Transform3D();
	    coordTransform.setTranslation(new Vector3f(0.0f, 1.0f, 0.0f));
	    TransformGroup coordSystemTG = new TransformGroup(coordTransform);
	    coordSystemTG.addChild(coordSystem.position_Object());
	    fanTG.addChild(coordSystemTG);

	    return fanTG;
	}
	
	
	public static BranchGroup create_Scene() {	//a function to build the content branch, including the fan and other environmental settings
		BranchGroup sceneBG = new BranchGroup();
		TransformGroup sceneTG = new TransformGroup();	   // make 'sceneTG' continuously rotating
		
	//	sceneTG.addChild(Commons.rotate_Behavior(7500, sceneTG));
		sceneTG.addChild(create_ClockHS());                    // add the fan to the rotating 'sceneTG'
		sceneBG.addChild(sceneTG);                         // keep the following stationary
		sceneBG.addChild(Commons.add_Lights(Commons.White, 1));
		
		soundJOAL = new SoundUtilityJOAL();
		
		if (!soundJOAL.load(snd_bk, 0f, 0f, 10f, true))    // set 'snd_bk' at the fixed position
			System.out.println("Could not load " + snd_bk);
		else
			soundJOAL.play(snd_bk);                        // start playing 'snd_bk' in background
		
		return sceneBG;
	}

	public ClockMain(BranchGroup sceneBG) {//NOTE: Keep the constructor for each of the labs and assignments
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas = new Canvas3D(config);
		SimpleUniverse su = new SimpleUniverse(canvas);    // create a SimpleUniverse
		Commons.define_Viewer(su, new Point3d(0.25d, 0.25d, 10.0d));   // set the viewer's location
		
		//Commons.define_Viewer(su, new Point3d(1.25d, -1.5d, 3.0d));   // set the viewer's location from down

		
		sceneBG.compile();		                           // optimize the BranchGroup
		su.addBranchGraph(sceneBG);                        // attach the scene to SimpleUniverse
		setLayout(new BorderLayout());
		add("Center", canvas);
		frame.setSize(800, 800);                           // set the size of the JFrame
		frame.setVisible(true);
		
		/*HourHand hourHand = new HourHand();
		MinuteHand  minuteHand = new MinuteHand();

        // Start a thread to update the clock hands in real-time
        new Thread(() -> {
            while (true) {
                // Get the current time
                int hours = java.time.LocalTime.now().getHour();
                int minutes = java.time.LocalTime.now().getMinute();

                // Update the hour and minute hands
                hourHand.updateRotation(hours, minutes);
                minuteHand.updateRotation(minutes);

                // Wait for 1 second
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
		
		
		
		*/
		
	}
	
	
	public static void main(String[] args) {

		frame = new JFrame("HS's Assignment");                   // NOTE: change XY to student's initials
		frame.getContentPane().add(new ClockMain(create_Scene()));  // start the program
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}