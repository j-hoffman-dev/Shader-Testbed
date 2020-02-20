package dev.jhoffman.shadertestbed.util;

import dev.jhoffman.shadertestbed.shader.core.ShaderCore;
import dev.jhoffman.shadertestbed.shader.core.ShaderProgram;
import java.util.Map;
import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector3f;

/**
 * @author J Hoffman
 * Created: Feb 18, 2020
 */

public final class Camera implements LogicLoop {

    private int speed = 1;
    
    private float pitch;
    private float yaw         = -90f;
    private float sensitivity = 0.10f;
    
    public Vector2d prevCursorPos = new Vector2d();
    
    public boolean[] pressed = new boolean[4];
    
    private Vector3f position  = new Vector3f();
    private Vector3f direction = new Vector3f(0, 0, -1f);
    private Vector3f up        = new Vector3f(0, 1, 0);
    private Vector3f tempFront = new Vector3f();
    private Vector3f tempRight = new Vector3f();
    private Vector3f tempDirec = new Vector3f();
    private Matrix4f view      = new Matrix4f();
    private Matrix4f proj      = new Matrix4f();
    
    public Observable observable = new Observable(this);
    
    private Map<String, ShaderProgram> shaderPrograms;
    
    public Camera(int width, int height, Map<String, ShaderProgram> shaderPrograms) {
        this.shaderPrograms = shaderPrograms;
        
        for(int i = 0; i < 2; i++) {
            if(i == 0) {
                ShaderCore.use("teapot");
            } else {
                ShaderCore.use("default");
            }
            
            proj.setPerspective((float) Math.toRadians(45), (float) width / height, 0.1f, Float.POSITIVE_INFINITY);
            ShaderCore.setMat4("uProjection", false, proj);
        }
        
        /*
        shaderPrograms.forEach((name, program) -> {
            ShaderCore.use(name);
            proj.setPerspective((float) Math.toRadians(45), (float) width / height, 0.1f, Float.POSITIVE_INFINITY);
            ShaderCore.setMat4("uProjection", false, proj);
        });
        */
        observable.properties.put("position x", position.x);
        observable.properties.put("position y", position.y);
        observable.properties.put("position z", position.z);
        observable.properties.put("direction x", direction.x);
        observable.properties.put("direction y", direction.y);
        observable.properties.put("direction z", direction.z);
    }
    
    @Override
    public void update() {
        if(pressed[0]) position.add(direction.mul(speed, tempDirec));
        if(pressed[1]) position.sub(direction.cross(up, tempRight).normalize().mul(speed));
        if(pressed[2]) position.sub(direction.mul(speed, tempDirec));
        if(pressed[3]) position.add(direction.cross(up, tempRight).normalize().mul(speed));
        
        observable.notifyObservers("position x", position.x);
        observable.notifyObservers("position y", position.y);
        observable.notifyObservers("position z", position.z);
        observable.notifyObservers("direction x", direction.x);
        observable.notifyObservers("direction y", direction.y);
        observable.notifyObservers("direction z", direction.z);
    }

    @Override
    public void render() {
        shaderPrograms.forEach((name, program) -> {
            ShaderCore.use(name);
            view.setLookAt(position, position.add(direction, tempFront), up);
            ShaderCore.setMat4("uView", false, view);
        });
    }
    
    private float getChangeIntensity(double currValue, double prevValue) {
        return (float) (currValue - prevValue) * sensitivity;
    }
    
    public Vector3f getPosition()  { return position; }
    public Vector3f getDirection() { return direction; }
    
    public void setSpeedBoostEnabled(boolean enable) {
        speed = (enable) ? 3 : 1;
    }
    
    public void setDirection(double xpos, double ypos) {
        if(xpos != prevCursorPos.x || ypos != prevCursorPos.y) {
            yaw   += getChangeIntensity(xpos, prevCursorPos.x) * 2;
            pitch += getChangeIntensity(ypos, prevCursorPos.y) * 2;
            
            if(pitch > 89f)  pitch = 89;
            if(pitch < -89f) pitch = -89;
            
            direction.x = (float) (Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
            direction.y = (float) Math.sin(Math.toRadians(pitch)) * -1;
            direction.z = (float) (Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
            
            prevCursorPos.x = xpos;
            prevCursorPos.y = ypos;
        }
    }
    
}