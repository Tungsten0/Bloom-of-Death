package BOD;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.*;

public class SideTableObject2 extends JPanel {
    private static final long serialVersionUID = 1L;
    private static JFrame frame;

    public static BranchGroup create_Scene() {
        BranchGroup sceneBG = new BranchGroup();
        TransformGroup sceneTG = new TransformGroup();
        sceneTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        // Create the side table
        SideTableHS sideTable = new SideTableHS(); // Create the side table
        sceneTG.addChild(sideTable.position_Object());

        // Add lighting
        sceneBG.addChild(Commons.add_Lights(Commons.White, 1));
		//sceneBG.addChild(Commons.rotate_Behavior(7500, sceneTG));	

        // Add the side table to the scene
        sceneBG.addChild(sceneTG);

        return sceneBG;
    }

    public SideTableObject2(BranchGroup sceneBG) {
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
        frame.getContentPane().add(new SideTableObject2(create_Scene()));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}