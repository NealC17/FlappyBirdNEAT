import processing.core.PApplet;

import java.awt.*;

public class Obstacle {
    private int x, y;
    private int width, height;
    private static int gap;
    private int vel;

    static {
        gap = 150;
    }


    public Obstacle() {
        vel = 5;
        width = 60;
        x = Main.WIDTH-width;
        y = 0;
        int c = (int)(Math.random()*8);
        setHeight(c);
    }

    private void setHeight(int c) {
        switch (c) {
            case 7:
                height = 125;
                break;
            case 6:
                height = 150;
                break;
            case 5:
                height = 225;
                break;
            case 4:
                height = 300;
                break;
            case 3:
                height = 375;
                break;
            case 2:
                height = 450;
                break;
            case 1:
                height = 525;
                break;
            case 0:
                height = 600;
                break;
        }
    }

    public void draw(PApplet p) {
        p.fill(0,255,50);
        p.rect(x, y, width, height);
        p.rect(x, height + gap, width, Main.HEIGHT - (height + gap));
    }

    public void update() {
        x -= vel;
    }

    public int getWidth() {
        return width;
    }

    public int getX() {
        return x;
    }

    public int getHeight() {
        return height;
    }

    public static int getGap() {
        return gap;
    }

    public int getY() {
        return y;
    }

    public double getMidPoint() {

        return height + gap/2.0;
    }
}
