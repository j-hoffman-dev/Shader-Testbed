package dev.jhoffman.shadertestbed.shader.core;

import java.util.HashMap;
import java.util.Map;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import static org.lwjgl.opengl.GL20.*;

/**
 * @author J Hoffman
 * Created: Feb 18, 2020
 */

public final class ShaderCore {
    
    private static boolean initialized;
    
    private static ShaderProgram shaderProgram;
    private static Map<String, ShaderProgram> shaderPrograms = new HashMap<>();
    
    public static void init(Map<String, ShaderProgram> programs) {
        if(!initialized) {
            shaderPrograms.putAll(programs);
            initialized = true;
        } else {
            System.err.println("App: WARNING (shader core may not be initialized twice.)");
        }
    }
    
    public static void use(String name) {
        if(shaderPrograms.containsKey(name)) {
            shaderProgram = shaderPrograms.get(name);
            glUseProgram(shaderProgram.handle);
        } else {
            throw new RuntimeException("OpenGL: ERROR (shader program \"" + name + "\" not found)\n");
        }
    }
    
    public static void flush() {
        shaderPrograms.forEach((name, program) -> glDeleteProgram(program.handle));
    }
    
    public static void setInt(String name, int value) {
        glUniform1i(
                shaderProgram.getUniform(name).location, 
                value);
    }
    
    public static void setFloat(String name, float value) {
        glUniform1f(
                shaderProgram.getUniform(name).location, 
                value);
    }
    
    public static void setVec2(String name, Vector2f value) {
        glUniform2fv(
                shaderProgram.getUniform(name).location,
                value.get(shaderProgram.getUniform(name).asFloatBuffer()));
    }
    
    public static void setVec3(String name, Vector3f value) {
        glUniform3fv(
                shaderProgram.getUniform(name).location,
                value.get(shaderProgram.getUniform(name).asFloatBuffer()));
    }
    
    public static void setMat3(String name, boolean transpose, Matrix3f value) {
        glUniformMatrix3fv(
                shaderProgram.getUniform(name).location,
                transpose,
                value.get(shaderProgram.getUniform(name).asFloatBuffer()));
    }
    
    public static void setMat4(String name, boolean transpose, Matrix4f value) {
        glUniformMatrix4fv(
                shaderProgram.getUniform(name).location,
                transpose,
                value.get(shaderProgram.getUniform(name).asFloatBuffer()));
    }
    
}