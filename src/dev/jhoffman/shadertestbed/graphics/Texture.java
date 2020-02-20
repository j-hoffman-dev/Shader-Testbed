package dev.jhoffman.shadertestbed.graphics;

import dev.jhoffman.shadertestbed.main.App;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

/**
 * @author J Hoffman
 * Created: Feb 19, 2020
 */

public final class Texture {
    
    public final int handle;
    private int width;
    private int height;
    private int channels;
    
    public Texture(String filename) {
        handle = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, handle);
        
        try(InputStream file = Texture.class.getResourceAsStream(App.CLASSPATH + "/assets/" + filename)) {
            loadTexture(file);
        } catch(Exception e) {
            System.err.println("OpenGL: WARNING (failed to load texture \"" + filename + "\")\n");
            e.printStackTrace();
            
            loadTexture(Texture.class.getResourceAsStream(App.CLASSPATH + "/assets/img_default.png"));
        }
        
        App.checkGLError();
    }
    
    private void loadTexture(InputStream file) {
        try(MemoryStack stack = MemoryStack.stackPush()) {
            byte[] data = file.readAllBytes();
        
            ByteBuffer imageBuf = MemoryUtil.memAlloc(data.length).put(data).flip();
            IntBuffer widthBuf  = stack.mallocInt(1);
            IntBuffer heightBuf = stack.mallocInt(1);
            IntBuffer chanBuf   = stack.mallocInt(1);
            
            ByteBuffer texture = stbi_load_from_memory(imageBuf, widthBuf, heightBuf, chanBuf, STBI_rgb_alpha);
            
            width    = widthBuf.get();
            height   = heightBuf.get();
            channels = chanBuf.get();
            
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, texture);
            
            stbi_image_free(texture);
            MemoryUtil.memFree(imageBuf);
        } catch (IOException ex) {
            throw new RuntimeException("OpenGL: ERROR (failed to load fallback texture)");
        }
    }
    
    public int getWidth()    { return width; }
    public int getHeight()   { return height; }
    public int getChannels() { return channels; }
    
}