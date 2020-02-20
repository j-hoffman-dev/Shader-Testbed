package dev.jhoffman.shadertestbed.shader.core;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * @author J Hoffman
 * Created: Feb 19, 2020
 */

public final class UniformVariable {
    
    final int location;
    private Buffer buffer;
    
    UniformVariable(int location, Buffer buffer) {
        this.location = location;
        this.buffer   = buffer;
    }
    
    IntBuffer asIntBuffer()     { return (IntBuffer) buffer; }
    FloatBuffer asFloatBuffer() { return (FloatBuffer) buffer; }
    
}