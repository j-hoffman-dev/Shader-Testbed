package dev.jhoffman.shadertestbed.main;

import dev.jhoffman.shadertestbed.util.Camera;
import java.nio.IntBuffer;
import org.joml.Vector2i;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.system.MemoryStack;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * @author J Hoffman
 * Created: Feb 18, 2020
 */

final class Window {
    
    final long handle;
    
    final int width;
    final int height;
    private final Vector2i position;
    
    private boolean inputEnabled = true;
    private boolean firstMouse   = true;
    
    Window(String name, Monitor monitor) {
        width  = (int) (monitor.width * 0.6f);
        height = (int) (monitor.height * 0.6f);
        
        try(MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer x = stack.mallocInt(1);
            IntBuffer y = stack.mallocInt(1);
            
            glfwGetMonitorPos(monitor.handle, x, y);
            
            position = new Vector2i(
                    ((monitor.width - width) / 2) + x.get(), 
                    ((monitor.height - height) / 2) + y.get());
        }
        
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        
        handle = glfwCreateWindow(width, height, name, NULL, NULL);
    }
    
    void show(Monitor monitor, Camera camera) {
        glfwSetWindowMonitor(handle, monitor.handle, position.x, position.y, monitor.width, monitor.height, monitor.refreshRate);
        //glfwSetWindowPos(handle, position.x, position.y);
        glfwSwapInterval(1);
        glfwSetInputMode(handle, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        glfwShowWindow(handle);
        
        glfwSetKeyCallback(handle, (window, key, scancode, action, mods) -> {
            if(inputEnabled) {
                if(key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                    glfwSetInputMode(handle, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
                    inputEnabled = false;
                }
                
                if(key == GLFW_KEY_F1 && action == GLFW_PRESS) App.toggleShowFPS();
                if(key == GLFW_KEY_F2 && action == GLFW_PRESS) App.togglePause();
                if(key == GLFW_KEY_F3 && action == GLFW_PRESS) App.toggleVSync();

                if(key == GLFW_KEY_W) camera.pressed[0] = (action != GLFW_RELEASE);
                if(key == GLFW_KEY_A) camera.pressed[1] = (action != GLFW_RELEASE);
                if(key == GLFW_KEY_S) camera.pressed[2] = (action != GLFW_RELEASE);
                if(key == GLFW_KEY_D) camera.pressed[3] = (action != GLFW_RELEASE);

                camera.setSpeedBoostEnabled(mods == GLFW_MOD_SHIFT);
            }
        });
        
        glfwSetMouseButtonCallback(handle, (window, button, action, mods) -> {
            if(button == GLFW_MOUSE_BUTTON_LEFT) {
                glfwSetInputMode(handle, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
                inputEnabled = true;
                firstMouse   = true;
            }    
        });
        
        glfwSetCursorPosCallback(handle, (window, xpos, ypos) -> {
            if(inputEnabled) {
                if(firstMouse) {
                    camera.prevCursorPos.set(xpos, ypos);
                    firstMouse = false;
                }
                
                camera.setDirection(xpos, ypos);
            }
        });
    }
    
}