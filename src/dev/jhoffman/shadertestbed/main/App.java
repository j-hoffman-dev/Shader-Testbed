package dev.jhoffman.shadertestbed.main;

import dev.jhoffman.shadertestbed.shader.core.BufferType;
import dev.jhoffman.shadertestbed.shader.core.ShaderCore;
import dev.jhoffman.shadertestbed.shader.core.ShaderProgram;
import dev.jhoffman.shadertestbed.shader.core.ShaderSource;
import dev.jhoffman.shadertestbed.util.Camera;
import dev.jhoffman.shadertestbed.util.Color;
import java.util.ArrayList;
import java.util.HashMap;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL33.*;

/**
 * @author J Hoffman
 * Created: Feb 18, 2020
 */

public final class App {
    
    private static int tickCount = 0;
    private static int fps;
    
    private double delta = 0;
    
    private boolean ticked;
    private static boolean showFPS;
    private static boolean pause;
    private static boolean vSync = true;
    
    public static final String CLASSPATH = "/dev/jhoffman/shadertestbed";
    private Monitor monitor;
    private Window window;
    private Scene scene;
    private static Camera camera;
    
    void start() {
        glfwInit();
        
        monitor = new Monitor();
        window  = new Window("Shader Testbed.", monitor);
        
        glInit();
        glViewport(0, 0, monitor.width, monitor.height);
        
        window.show(monitor, camera);
        scene = new Scene();
        
        loop();
        
        ShaderCore.flush();
        GL.destroy();
        glfwTerminate();
    }
    
    private void glInit() {
        glfwMakeContextCurrent(window.handle);
        GL.createCapabilities();
        
        glEnable(GL_DEPTH_TEST);
        
        var shaderSources  = new ArrayList<ShaderSource>();
        var shaderPrograms = new HashMap<String, ShaderProgram>();
        
        {
            shaderSources.add(new ShaderSource("teapotVertex.glsl",   GL_VERTEX_SHADER));
            shaderSources.add(new ShaderSource("teapotFragment.glsl", GL_FRAGMENT_SHADER));
            
            ShaderProgram program = new ShaderProgram(shaderSources);
            
            shaderPrograms.put("teapot", program);
            glUseProgram(program.handle);
            
            program.addUniform(BufferType.MAT4, "uModel");
            program.addUniform(BufferType.MAT4, "uView");
            program.addUniform(BufferType.MAT4, "uProjection");
            program.addUniform(BufferType.MAT3, "uNormal");
            program.addUniform(BufferType.VEC3, "uLight.position");
            program.addUniform(BufferType.VEC3, "uLight.ambient");
            program.addUniform(BufferType.VEC3, "uLight.diffuse");
        }
        
        shaderSources.clear();
        
        {
            shaderSources.add(new ShaderSource("defaultVertex.glsl",   GL_VERTEX_SHADER));
            shaderSources.add(new ShaderSource("defaultFragment.glsl", GL_FRAGMENT_SHADER));
            
            ShaderProgram program = new ShaderProgram(shaderSources);
            
            shaderPrograms.put("default", program);
            glUseProgram(program.handle);
            
            program.addUniform(BufferType.MAT4, "uModel");
            program.addUniform(BufferType.MAT4, "uView");
            program.addUniform(BufferType.MAT4, "uProjection");
            program.addUniform(BufferType.INT,  "uType");
        }
        
        ShaderCore.init(shaderPrograms);
        ShaderCore.use("default");
        
        camera = new Camera(window.width, window.height, shaderPrograms);
    }
    
    private void loop() {
        int cycles = 0;
        
        final double TARGET_DELTA = 1 / 60.0;
        double currTime;
        double prevTime = glfwGetTime();
        
        while(!glfwWindowShouldClose(window.handle)) {
            glfwPollEvents();
            
            currTime = glfwGetTime();
            
            delta += currTime - prevTime;
            if(delta < TARGET_DELTA && vSync) delta = TARGET_DELTA;
            
            prevTime = currTime;
            ticked   = false;
            
            while(delta >= TARGET_DELTA) {
                delta     -= TARGET_DELTA;
                ticked    = true;
                tickCount = (tickCount == Integer.MAX_VALUE) ? 0 : tickCount + 1;
                
                camera.update();
                if(!pause) scene.update();
                
                if(tick(60)) {
                    fps = cycles;
                    cycles = 0;
                    if(showFPS) System.out.println("FPS: " + fps);
                }
            }
            
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            
            camera.render();
            scene.render();
            
            glfwSwapBuffers(window.handle);
            
            if(!ticked) {
                try {
                    Thread.sleep(1);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                cycles++;
            }
        }
    }
    
    public static void toggleShowFPS() {
        App.showFPS = !App.showFPS;
    }
    
    public static void togglePause() {
        pause = !pause;
        System.out.println("App: INFO (pause toggled \"" + pause + "\")");
    }
    
    public static void toggleVSync() {
        vSync = !vSync;
        
        if(vSync) glfwSwapInterval(1);
        else      glfwSwapInterval(0);
        
        System.out.println("App: INFO (vsync toggled \"" + vSync + "\")");
    }
    
    public static void checkGLError() {
        int glError = glGetError();
        
        if(glError != GL_NO_ERROR) {
            String desc = "";
            
            switch(glError) {
                case GL_INVALID_ENUM:      desc = "invalid enum"; break;
                case GL_INVALID_VALUE:     desc = "invalid value"; break;
                case GL_INVALID_OPERATION: desc = "invalid operation"; break;
                case GL_STACK_OVERFLOW:    desc = "stack overflow"; break;
                case GL_STACK_UNDERFLOW:   desc = "stack underflow"; break;
                case GL_OUT_OF_MEMORY:     desc = "out of memory"; break;
            }
            
            throw new RuntimeException("OpenGL: ERROR " + glError + " (" + desc + ")\n");
        }
    }
    
    public static boolean tick(int cycles) {
        return tickCount % cycles == 0;
    }
    
    public static void setClearColor(Color color) {
        glClearColor(color.r, color.g, color.b, 0);
    }
    
}