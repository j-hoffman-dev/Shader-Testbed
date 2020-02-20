package dev.jhoffman.shadertestbed.body;

import dev.jhoffman.shadertestbed.graphics.Graphics;
import dev.jhoffman.shadertestbed.util.LogicLoop;
import org.joml.Vector3f;

/**
 * @author J Hoffman
 * Created: Feb 19, 2020
 */

public abstract class Body implements LogicLoop {

    protected Graphics graphics = new Graphics();
    protected Vector3f position;
    
    protected Body(Vector3f position) {
        this.position = position;
    }
    
    @Override
    public abstract void update();

    @Override
    public abstract void render();
    
}