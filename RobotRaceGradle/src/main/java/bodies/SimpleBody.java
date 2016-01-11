/*
 * Course: Computer Graphics
 * Course Code: 2IV60
 * Assignment: RobotRace
 * Students: Arjan Boschman & Robke Geenen
 */
package bodies;

import Texture.ImplementedTexture;
import java.util.HashSet;
import java.util.Set;
import javax.media.opengl.GL2;

/**
 * A simple Body consisting of an arbitrary number or shapes. Use the builders
 * given by this class to obtain instances of SimpleBody.
 *
 * @author Arjan Boschman
 */
public class SimpleBody implements Body {

    private final Set<Shape> shapes = new HashSet<>();

    /**
     * Adds a shape to this SimpleBody. During the SimpleBody's draw phase,
     * shapes belonging to a SimpleBody will all be drawn in no particular
     * order. Shapes may belong to several SimpleBodies.
     *
     * @param shape The shape to be added.
     */
    public void addShape(Shape shape) {
        shapes.add(shape);
    }

    @Deprecated//No longer used or needed.
    public void changeTexture(ImplementedTexture textureOld, ImplementedTexture textureNew) {
        for (Shape shape : shapes) {
            if (shape.getTexture() != null) {
                if (shape.getTexture().getOriginalFilename() == null ? textureOld.getOriginalFilename() == null : shape.getTexture().getOriginalFilename().equals(textureOld.getOriginalFilename())) {
                    shape.setTexture(textureNew);
                }
            }
        }
    }

    @Override
    public void draw(GL2 gl) {
        shapes.stream().forEach((shape) -> shape.draw(gl));
    }

}
