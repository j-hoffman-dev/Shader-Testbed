package dev.jhoffman.shadertestbed.shader.core;

import static dev.jhoffman.shadertestbed.shader.core.BufferType.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.lwjgl.opengl.GL20.*;
import org.lwjgl.system.MemoryStack;

/**
 * @author J Hoffman
 * Created: Feb 19, 2020
 */

public final class ShaderProgram {
    
    public final int handle;
    private Map<String, UniformVariable> uniforms = new HashMap<>();
    private Map<BufferType, Integer> bufferSizes  = new HashMap<>();
    
    public ShaderProgram(List<ShaderSource> shaders) {
        handle = glCreateProgram();
        
        shaders.forEach(shader -> glAttachShader(handle, shader.handle));
        
        glLinkProgram(handle);
        
        bufferSizes.put(VEC2, 2);
        bufferSizes.put(VEC3, 3);
        bufferSizes.put(MAT3, 3);
        bufferSizes.put(MAT4, 4);
    }
    
    public UniformVariable getUniform(String name) { return uniforms.get(name); }
    
    public void addUniform(BufferType type, String name) {
        if(glGetUniformLocation(handle, name) == -1) {
            throw new RuntimeException(
                    "OpenGL: ERROR (uniform variable \"" + name + "\" returned -1, " +
                    "check variable name or shader in which it's declared.)\n");
        } else {
            try(MemoryStack stack = MemoryStack.stackPush()) {
                switch(type) {
                    case INT:
                        uniforms.put(name, new UniformVariable(
                                glGetUniformLocation(handle, name),
                                stack.mallocInt(1)));
                        break;
                        
                    case FLOAT:
                        uniforms.put(name, new UniformVariable(
                                glGetUniformLocation(handle, name),
                                stack.mallocFloat(1)));
                        break;
                        
                    case VEC2: case VEC3:
                        uniforms.put(name, new UniformVariable(
                                glGetUniformLocation(handle, name),
                                stack.mallocFloat(bufferSizes.get(type))));
                        break;
                        
                    case MAT3: case MAT4:
                        uniforms.put(name, new UniformVariable(
                                glGetUniformLocation(handle, name),
                                stack.mallocFloat(bufferSizes.get(type) * Float.BYTES)));
                        break;
                }
            }
        }
    }
    
}