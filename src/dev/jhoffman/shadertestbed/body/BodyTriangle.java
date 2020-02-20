package dev.jhoffman.shadertestbed.body;

import dev.jhoffman.shadertestbed.main.App;
import dev.jhoffman.shadertestbed.shader.core.ShaderCore;
import org.joml.Vector3f;
import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.GL20.*;
import org.lwjgl.system.MemoryStack;

/**
 * @author J Hoffman
 * Created: Feb 19, 2020
 */

public class BodyTriangle extends Body {

    private float angle;
    
    public BodyTriangle(Vector3f position) {
        super(position);
        
        try(MemoryStack stack = MemoryStack.stackPush()) {
            graphics.vertices = stack.mallocFloat(18);
            
            //(vec3 position), (vec3 color)
            graphics.vertices.put(-8).put(-8).put(0)   .put(1).put(0).put(0);
            graphics.vertices .put(0) .put(8).put(0)   .put(0).put(1).put(0);
            graphics.vertices .put(8).put(-8).put(0)   .put(0).put(0).put(1);
            
            graphics.vertices.flip();
        }
        
        graphics.bindBuffers();
        
        glVertexAttribPointer(0, 3, GL_FLOAT, false, (6 * Float.BYTES), 0);
        glVertexAttribPointer(2, 3, GL_FLOAT, false, (6 * Float.BYTES), (3 * Float.BYTES));
        
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(2);
    }
    
    @Override
    public void update() {
        angle += 1f;
        
        graphics.model.translation(position);
        graphics.model.rotateY((float) Math.toRadians(-angle));
    }

    @Override
    public void render() {
        ShaderCore.use("default");
        glBindVertexArray(graphics.vao);
        
        ShaderCore.setMat4("uModel", false, graphics.model);
        ShaderCore.setInt("uType", 0);
        
        glDrawArrays(GL_TRIANGLES, 0, 3);
        App.checkGLError();
    }
    
}