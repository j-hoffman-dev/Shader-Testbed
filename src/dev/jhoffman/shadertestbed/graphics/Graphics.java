package dev.jhoffman.shadertestbed.graphics;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.joml.Matrix4f;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL30.*;

/**
 * @author J Hoffman
 * Created: Feb 19, 2020
 */

public class Graphics {
    
    public final int vao = glGenVertexArrays();
    public final int vbo = glGenBuffers();
    public final int ibo = glGenBuffers();
    
    public FloatBuffer vertices;
    public IntBuffer indices;
    
    public Matrix4f model = new Matrix4f();
    
    public void bindBuffers() {
        glBindVertexArray(vao);
        
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        
        if(indices != null) {
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
        }
    }
    
    public void freeBuffers() {
        glDeleteBuffers(vao);
        glDeleteBuffers(vbo);
        glDeleteBuffers(ibo);
    }
    
}