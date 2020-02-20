package dev.jhoffman.shadertestbed.main;

import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWVidMode;

/**
 * @author J Hoffman
 * Created: Feb 18, 2020
 */

final class Monitor {
    
    final long handle;
    
    final int width;
    final int height;
    final int refreshRate;
    
    private GLFWVidMode vidMode;
    
    Monitor() {
        handle      = glfwGetPrimaryMonitor();
        vidMode     = glfwGetVideoMode(handle);
        width       = vidMode.width();
        height      = vidMode.height();
        refreshRate = vidMode.refreshRate();
    }
    
}