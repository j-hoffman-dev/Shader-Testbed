package dev.jhoffman.shadertestbed.body;

import dev.jhoffman.shadertestbed.graphics.Mesh;
import dev.jhoffman.shadertestbed.graphics.Model;
import dev.jhoffman.shadertestbed.main.App;
import dev.jhoffman.shadertestbed.shader.core.ShaderCore;
import org.joml.Vector3f;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIMesh;
import static org.lwjgl.assimp.Assimp.aiProcess_Triangulate;
import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glEnable;

/**
 * @author J Hoffman
 * Created: Feb 19, 2020
 */

public class BodyTeapot extends Body {

    private Model model3D;
    
    public BodyTeapot(Vector3f position) {
        super(position);
        
        model3D = new Model("mod_teapot.obj", aiProcess_Triangulate);
        
        PointerBuffer meshBuf = model3D.scene.mMeshes();
        
        for(int i = 0; i < model3D.scene.mNumMeshes(); ++i) {
            AIMesh aiMesh = AIMesh.create(meshBuf.get(i));
            
            Mesh mesh = new Mesh(aiMesh) {
                @Override
                public void update() {
                    meshGraphics.model.translation(position);
                    meshGraphics.model.scale(10);
                }

                @Override
                public void render() {
                    glEnable(GL_CULL_FACE);
                    glBindVertexArray(meshGraphics.vao);

                    ShaderCore.setMat4("uModel", false, meshGraphics.model);
                    ShaderCore.setMat3("uNormal", true, normal);
                    ShaderCore.setVec3("uLight.position", lightPos);
                    ShaderCore.setVec3("uLight.ambient", ambient);
                    ShaderCore.setVec3("uLight.diffuse", diffuse);

                    glDrawElements(GL_TRIANGLES, meshGraphics.indices.limit(), GL_UNSIGNED_INT, 0);
                    glDisable(GL_CULL_FACE);

                    App.checkGLError();
                }
            };
            
            model3D.meshes.add(mesh);
        }
    }
    
    @Override
    public void update() {
        model3D.update();
    }

    @Override
    public void render() {
        ShaderCore.use("teapot");
        model3D.render();
    }
    
}