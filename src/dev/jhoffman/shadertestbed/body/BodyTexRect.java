package dev.jhoffman.shadertestbed.body;

import dev.jhoffman.shadertestbed.graphics.Texture;
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

public class BodyTexRect extends Body {

    private Texture texture;
    
    public BodyTexRect(Vector3f position) {
        super(position);
        
        texture = new Texture("img_default.png");
        
        glBindTexture(GL_TEXTURE_2D, texture.handle);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glBindTexture(GL_TEXTURE_2D, 0);
        
        try(MemoryStack stack = MemoryStack.stackPush()) {
            graphics.vertices = stack.mallocFloat(20);
            graphics.indices  = stack.mallocInt(6);
            
            //(vec3 position), (vec2 tex coords)
            graphics.vertices.put(-16) .put(16).put(0) .put(0).put(0);
            graphics.vertices .put(16) .put(16).put(0) .put(1).put(0);
            graphics.vertices .put(16).put(-16).put(0) .put(1).put(1);
            graphics.vertices.put(-16).put(-16).put(0) .put(0).put(1);
            
            graphics.indices.put(0).put(1).put(2);
            graphics.indices.put(2).put(3).put(0);
            
            graphics.vertices.flip();
            graphics.indices.flip();
        }
        
        graphics.bindBuffers();
        
        glVertexAttribPointer(0, 3, GL_FLOAT, false, (5 * Float.BYTES), 0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, (5 * Float.BYTES), (3 * Float.BYTES));
        
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
    }

    @Override
    public void update() {
        graphics.model.translation(position);
    }

    @Override
    public void render() {
        ShaderCore.use("default");
        glBindTexture(GL_TEXTURE_2D, texture.handle);
        glBindVertexArray(graphics.vao);
        
        ShaderCore.setMat4("uModel", false, graphics.model);
        ShaderCore.setInt("uType", 1);
        
        glDrawElements(GL_TRIANGLES, graphics.indices.limit(), GL_UNSIGNED_INT, 0);
        App.checkGLError();
    }
    
}