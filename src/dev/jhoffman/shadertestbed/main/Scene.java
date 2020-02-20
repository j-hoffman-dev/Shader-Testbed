package dev.jhoffman.shadertestbed.main;

import dev.jhoffman.shadertestbed.body.Body;
import dev.jhoffman.shadertestbed.body.BodyTeapot;
import dev.jhoffman.shadertestbed.body.BodyTexRect;
import dev.jhoffman.shadertestbed.body.BodyTriangle;
import dev.jhoffman.shadertestbed.util.Color;
import dev.jhoffman.shadertestbed.util.LogicLoop;
import java.util.ArrayList;
import java.util.List;
import org.joml.Vector3f;

/**
 * @author J Hoffman
 * Created: Feb 18, 2020
 */

final class Scene implements LogicLoop {

    private List<Body> bodyList = new ArrayList();
    
    public Scene() {
        App.setClearColor(new Color(92, 148, 252));
        
        bodyList.add(new BodyTriangle(new Vector3f(50, 0, -100)));
        bodyList.add(new BodyTexRect(new Vector3f(-50, 0, -100)));
        bodyList.add(new BodyTeapot(new Vector3f(0, -10, -100)));
        
        //TODO add more bodies here.
    }
    
    @Override
    public void update() {
        bodyList.forEach(body -> body.update());
    }

    @Override
    public void render() {
        bodyList.forEach(body -> body.render());
    }
    
}