package dev.jhoffman.shadertestbed.graphics;

import dev.jhoffman.shadertestbed.util.LogicLoop;
import org.joml.Matrix3f;
import org.joml.Vector3f;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIVector3D;
import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.GL20.*;
import org.lwjgl.system.MemoryUtil;

/**
 * @author J Hoffman
 * Created: Feb 19, 2020
 */

public abstract class Mesh implements LogicLoop {
    
    protected Graphics meshGraphics = new Graphics();
    
    protected AIVector3D.Buffer vertices;
    
    public Matrix3f normal = new Matrix3f();
    public Vector3f lightPos = new Vector3f(0, 70, -50);
    public Vector3f ambient = new Vector3f(0.3f);
    public Vector3f diffuse = new Vector3f(0.6f);
    
    public Mesh(AIMesh mesh) {
        vertices = mesh.mVertices();
        meshGraphics.indices  = MemoryUtil.memAllocInt(mesh.mNumFaces() * 3);
        
        glBindVertexArray(meshGraphics.vao);
        
        glBindBuffer(GL_ARRAY_BUFFER, meshGraphics.vbo);
        nglBufferData(GL_ARRAY_BUFFER, AIVector3D.SIZEOF * vertices.remaining(), vertices.address(), GL_STATIC_DRAW);
        
        AIFace.Buffer faceBuf = mesh.mFaces();
        for(int i = 0; i < mesh.mNumFaces(); ++i) {
            AIFace face = faceBuf.get(i);
            if(face.mNumIndices() != 3) throw new RuntimeException("Assimp: ERROR (Invalid number of indices in face " + face.mNumIndices() + ")");
            meshGraphics.indices.put(face.mIndices());
        }
        
        meshGraphics.indices.flip();
        
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, meshGraphics.ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, meshGraphics.indices, GL_STATIC_DRAW);
        
        glVertexAttribPointer(0, 3, GL_FLOAT, false, (3 * Float.BYTES), 0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, (2 * Float.BYTES), 0);
        glVertexAttribPointer(2, 3, GL_FLOAT, false, (3 * Float.BYTES), 0);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
    }
    
}