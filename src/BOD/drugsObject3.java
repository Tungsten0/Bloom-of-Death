package BOD;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.*;

public class drugsObject3 extends JPanel {
    private static final long serialVersionUID = 1L;
    private static JFrame frame;

    public static TransformGroup create_drugs() {
        TransformGroup sceneTG = new TransformGroup();

        // Create the pill bottle scene
        drugsHS pillBottleScene = new drugsHS();
        sceneTG.addChild(pillBottleScene.position_Object());

        // Optionally add lighting or behaviors to the same TransformGroup (if needed)
        // Note: Lights are usually added to a BranchGroup, not a TransformGroup
        // sceneTG.addChild(Commons.add_Lights(Commons.White, 1));

        return sceneTG;
    }

    public static void main(String[] args) {
        frame = new JFrame("Pill Bottle Scene");

        BranchGroup root = new BranchGroup();
        TransformGroup tg = create_drugs();
        root.addChild(tg);

        // Add lighting
        root.addChild(Commons.add_Lights(Commons.White, 1));

        frame.getContentPane().add(new drugsObject3(root));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public drugsObject3(BranchGroup sceneBG) {
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas = new Canvas3D(config);
        SimpleUniverse su = new SimpleUniverse(canvas);

        // Set the camera view
        Commons.define_Viewer(su, new Point3d(0.0d, 0.5d, 3.0d));

        // Compile and add the scene to the universe
        sceneBG.compile();
        su.addBranchGraph(sceneBG);

        // Set up the JFrame
        setLayout(new BorderLayout());
        add("Center", canvas);

        frame.setSize(800, 800);
        frame.setVisible(true);
    }

    
}