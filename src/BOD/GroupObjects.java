package BOD;//my package
//Copyright material for students working on assignments and projects
import org.jogamp.java3d.Node;//my importation 
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.*;
import org.jogamp.vecmath.*;

public class GroupObjects {//the main class 
	public static BranchGroup scene_Group(BranchGroup a_BG, BranchGroup s_BG) {	//use the prof method 
		BranchGroup sceneBG = new BranchGroup();
		a_BG.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		a_BG.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		s_BG.setCapability(BranchGroup.ALLOW_DETACH);      // allow 'shapeBG' detached
		a_BG.addChild(s_BG);                               // add 'shapeBG' to 'alterableBG'
		TransformGroup sceneTG = new TransformGroup();     // introduce a TransformGroup for rotation 
		sceneTG.addChild(a_BG);                            // attach 'alterableBG' to 'sceneTG'
		sceneBG.addChild(Commons.rotate_Behavior(7500, sceneTG));
		Commons.control_Rotation(true);                 // make 'alterableBG' rotating by default
		sceneBG.addChild(sceneTG);
		return sceneBG;                                    // return the structured 'sceneBG'
	}//the end of the scene_Group method
	
	protected BranchGroup shapeBG = new BranchGroup();//the main circle outer 
	public BranchGroup get_ShapeBG() {	//use the prof method. a method to return 'shapeBG' as a BranchGroup 
		return shapeBG;
	}	
	/*public GroupObjects(Node p) {//the prof method 
		shapeBG.addChild(p);
	}//the end of the profs method
	*/
	protected BranchGroup ICircleBG = null; // it will be served when the optional inner circle will be chose by the user
	public GroupObjects(Node OCircle, Node ICircle) {//modify the constructor for the our class "GroupObjects" so i can hadle the circles innner or ouuuuter
		shapeBG.addChild(OCircle);//add the outerrrr circle to the shapbg
		if(ICircle != null) {//if the user chose to have the my inner circle then the new branch group will be created and display on the screen
			ICircleBG = new BranchGroup();//CREATE an speciall breach group and give it a name Icircle (I = Inner)
			ICircleBG.addChild(ICircle);//ADD it 
		}//the end of the if
	}//the end of the method
	public GroupObjects(BranchGroup bg) {//a consturctor that will assigne the given bg to the shapeBG 
		shapeBG = bg;//here i am doing the assignmentt of the bg
	}//end of the condtuctor
}//the end of the main class