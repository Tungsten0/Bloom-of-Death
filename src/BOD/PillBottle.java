package BOD;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Cylinder;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.*;

public class PillBottle extends BaseShapesHS {

    public PillBottle() {
        // Create the bottle
        Transform3D bottleTranslator = new Transform3D();
        bottleTranslator.setTranslation(new Vector3d(0.0, 0.0, 0.0)); // Position the bottle
        objTG = new TransformGroup(bottleTranslator);
        objTG.addChild(create_Object());
    }

    @Override
    protected Node create_Object() {
        // Create a brown appearance for the bottle
        Appearance brownApp = createAppearance("images/drugs.jpg");

        // Create the bottle body
        Cylinder body = new Cylinder(0.2f, 0.6f, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, brownApp);

        // Create the rim (open top)
        Cylinder rim = new Cylinder(0.18f, 0.02f, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, brownApp);
        Transform3D rimTransform = new Transform3D();
        rimTransform.setTranslation(new Vector3d(0.0, 0.31, 0.0)); // Position the rim
        TransformGroup rimTG = new TransformGroup(rimTransform);
        rimTG.addChild(rim);

        // Combine the body and rim
        TransformGroup bottleTG = new TransformGroup();
        bottleTG.addChild(body);
        bottleTG.addChild(rimTG);

        return bottleTG;
    }

    private Appearance createAppearance(String texturePath) {
        Appearance appearance = new Appearance();

        // Load texture
        TextureLoader loader = new TextureLoader(texturePath, null);
        Texture texture = loader.getTexture();
        if (texture != null) {
            appearance.setTexture(texture);
        }

        // Set material properties
        Material material = new Material();
        material.setDiffuseColor(new Color3f(0.6f, 0.4f, 0.2f)); // Brown color
        material.setSpecularColor(new Color3f(0.8f, 0.8f, 0.8f)); // White specular highlights
        material.setShininess(32.0f); // Moderate shininess
        appearance.setMaterial(material);

        return appearance;
    }
}