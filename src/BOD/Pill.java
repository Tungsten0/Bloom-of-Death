package BOD;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Cylinder;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.vecmath.*;

public class Pill extends BaseShapesHS {

    public Pill() {
        // Create the pill
        Transform3D pillTranslator = new Transform3D();
        pillTranslator.setTranslation(new Vector3d(0.0, 0.0, 0.0)); // Position the pill
        objTG = new TransformGroup(pillTranslator);
        objTG.addChild(create_Object());
    }

    @Override
    protected Node create_Object() {
        // Create green and white appearances
        Appearance greenApp = new Appearance();
        Material greenMaterial = new Material();
        greenMaterial.setDiffuseColor(new Color3f(0.0f, 0.8f, 0.0f)); // Green color
        greenApp.setMaterial(greenMaterial);

        Appearance whiteApp = new Appearance();
        Material whiteMaterial = new Material();
        whiteMaterial.setDiffuseColor(new Color3f(1.0f, 1.0f, 1.0f)); // White color
        whiteApp.setMaterial(whiteMaterial);

        // Create the pill capsule
        Sphere leftEnd = new Sphere(0.05f, Primitive.GENERATE_NORMALS, greenApp);
        Sphere rightEnd = new Sphere(0.05f, Primitive.GENERATE_NORMALS, greenApp);
        Cylinder middle = new Cylinder(0.05f, 0.1f, Primitive.GENERATE_NORMALS, whiteApp);

        // Position the ends and middle
        Transform3D leftTransform = new Transform3D();
        leftTransform.setTranslation(new Vector3d(-0.1, 0.0, 0.0));
        TransformGroup leftTG = new TransformGroup(leftTransform);
        leftTG.addChild(leftEnd);

        Transform3D rightTransform = new Transform3D();
        rightTransform.setTranslation(new Vector3d(0.1, 0.0, 0.0));
        TransformGroup rightTG = new TransformGroup(rightTransform);
        rightTG.addChild(rightEnd);

        // Combine the parts
        TransformGroup pillTG = new TransformGroup();
        pillTG.addChild(leftTG);
        pillTG.addChild(rightTG);
        pillTG.addChild(middle);

        return pillTG;
    }
}