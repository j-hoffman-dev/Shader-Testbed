package dev.jhoffman.shadertestbed.util;

/**
 * @author J Hoffman
 * Created: Feb 18, 2020
 */

public final class Color {
    
    public static final Color WHITE  = new Color(1);
    public static final Color SILVER = new Color(0.751f);
    public static final Color GRAY   = new Color(0.5f);
    public static final Color BLACK  = new Color(0);
    public static final Color RED    = new Color(255, 0, 0);
    public static final Color ORANGE = new Color(255, 128, 0);
    public static final Color YELLOW = new Color(255, 255, 0);
    public static final Color LIME   = new Color(0, 255, 0);
    public static final Color CYAN   = new Color(0, 255, 255);
    public static final Color BLUE   = new Color(0, 0, 255);
    public static final Color PINK   = new Color(255, 0, 255);
    public static final Color MAROON = new Color(128, 0, 0);
    public static final Color BROWN  = new Color(128, 64, 16);
    public static final Color OLIVE  = new Color(128, 128, 0);
    public static final Color GREEN  = new Color(0, 128, 0);
    public static final Color TEAL   = new Color(0, 128, 128);
    public static final Color NAVY   = new Color(0, 0, 128);
    public static final Color PURPLE = new Color(128, 0, 128);
    
    public final float r;
    public final float g;
    public final float b;
    
    public Color() {
        r = (int) (Math.random() * 255);
        g = (int) (Math.random() * 255);
        b = (int) (Math.random() * 255);
    }
    
    public Color(float scalar) {
        r = g = b = scalar;
    }
    
    public Color(int r, int g, int b) {
        this.r = (r / 255f);
        this.g = (g / 255f);
        this.b = (b / 255f);
    }
    
}