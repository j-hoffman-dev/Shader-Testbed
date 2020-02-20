package dev.jhoffman.shadertestbed.util;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;

/**
 * @author J Hoffman
 * Created: Feb 19, 2020
 */

public class Observable {
    
    private PropertyChangeSupport observable;
    public Map<String, Object> properties = new HashMap<>();
    
    public Observable(Object object) {
        observable = new PropertyChangeSupport(object);
    }
    
    public void addObserver(PropertyChangeListener observer) {
        observable.addPropertyChangeListener(observer);
    }
    
    public void removeObserver(PropertyChangeListener observer) {
        observable.removePropertyChangeListener(observer);
    }
    
    public void notifyObservers(String name, Object property) {
        observable.firePropertyChange(name, properties.get(name), property);
        properties.put(name, property);
    }
    
}