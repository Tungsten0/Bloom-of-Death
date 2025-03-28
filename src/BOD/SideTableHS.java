package BOD;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Cylinder;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.*;

public class SideTableHS extends BaseShapesHS {

    public SideTableHS() {
        // Create the tabletop
        Transform3D tabletopTranslator = new Transform3D();
        tabletopTranslator.setTranslation(new Vector3d(0.0, 0.0, 0.0)); // Position the tabletop
        objTG = new TransformGroup(tabletopTranslator);
        objTG.addChild(create_Object());
    }

    @Override
    protected Node create_Object() {
        // Create a warm, natural brown appearance
        Appearance wickerApp = createWickerAppearance();

        // Create the circular tabletop (smaller radius)
        Cylinder tabletop = new Cylinder(0.4f, 0.05f, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, wickerApp);

        // Create the raised edge with a braided pattern (smaller radius)
        Cylinder edge = new Cylinder(0.45f, 0.02f, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, wickerApp);
        Transform3D edgeTransform = new Transform3D();
        edgeTransform.setTranslation(new Vector3d(0.0, 0.06, 0.0)); // Position the edge slightly above the tabletop
        TransformGroup edgeTG = new TransformGroup(edgeTransform);
        edgeTG.addChild(edge);

        // Create the legs (taller height)
        TransformGroup legsTG = new TransformGroup();
        double angleIncrement = 2 * Math.PI / 4; // 90 degrees between legs
        double legRadius = 0.35; // Distance from the center of the tabletop to the legs

        for (int i = 0; i < 4; i++) {
            double angle = i * angleIncrement;
            double x = legRadius * Math.cos(angle);
            double z = legRadius * Math.sin(angle);

            // Leg starting from the bottom of the tabletop
            Cylinder leg = new Cylinder(0.03f, 0.7f, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, wickerApp);
            Transform3D legTransform = new Transform3D();
            legTransform.setTranslation(new Vector3d(x, -0.35, z)); // Adjusted so legs start from the tabletop bottom
            TransformGroup legTG = new TransformGroup(legTransform);
            legTG.addChild(leg);
            legsTG.addChild(legTG);
        }


        // Create the lower shelf (adjusted position)
        Cylinder lowerShelf = new Cylinder(0.35f, 0.03f, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, wickerApp);
        Transform3D shelfTransform = new Transform3D();
        shelfTransform.setTranslation(new Vector3d(0.0, -0.5, 0.0)); // Position the shelf lower
        TransformGroup shelfTG = new TransformGroup(shelfTransform);
        shelfTG.addChild(lowerShelf);

        // Combine all parts into a single TransformGroup
        TransformGroup tableTG = new TransformGroup();
        tableTG.addChild(tabletop);
        tableTG.addChild(edgeTG);
        tableTG.addChild(legsTG);
        tableTG.addChild(shelfTG);

        return tableTG;
    }

    private Appearance createWickerAppearance() {
        Appearance appearance = new Appearance();

        // Load wicker texture (ensure the file is in the "images" folder)
        TextureLoader loader = new TextureLoader("images/why.jpg", null);
        Texture texture = loader.getTexture();
        if (texture != null) {
            appearance.setTexture(texture);
        }

        // Set material properties
        Material material = new Material();
        material.setDiffuseColor(new Color3f(0.6f, 0.4f, 0.2f)); // Warm brown color
        material.setSpecularColor(new Color3f(0.8f, 0.8f, 0.8f)); // White specular highlights
        material.setShininess(32.0f); // Moderate shininess
        appearance.setMaterial(material);

        return appearance;
    }
}