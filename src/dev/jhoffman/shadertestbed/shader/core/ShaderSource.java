package dev.jhoffman.shadertestbed.shader.core;

import dev.jhoffman.shadertestbed.main.App;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import static org.lwjgl.opengl.GL20.*;

/**
 * @author J Hoffman
 * Created: Feb 19, 2020
 */

public final class ShaderSource {
    
    public final int handle;
    
    public ShaderSource(String filename, int type) {
        StringBuilder builder = new StringBuilder();
        InputStream file      = ShaderSource.class.getResourceAsStream(App.CLASSPATH + "/shader/source/" + filename);
        
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(file, "UTF-8"));) {
            String line;
            while((line = reader.readLine()) != null) builder.append(line).append("\n");
        } catch(IOException e) {
            System.err.println("Failed to parse GLSL file: \"" + filename + "\"");
            e.printStackTrace();
        }
        
        CharSequence src = builder.toString();
        
        handle = glCreateShader(type);
        glShaderSource(handle, src);
        glCompileShader(handle);
        
        if(glGetShaderi(handle, GL_COMPILE_STATUS) != GL_TRUE) {
            throw new RuntimeException("Failed to compile GLSL file: \"" + filename + "\" " + glGetShaderInfoLog(handle));
        }
    }
    
}