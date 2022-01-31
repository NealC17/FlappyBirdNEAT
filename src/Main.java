import processing.core.PApplet;

import java.util.ArrayList;

public class Main extends PApplet {

    public static final int LINEAR_DIST_BETWEEN_OBSTACLES = 400;
    public static final int SCREEN_WIDTH = 1920;
    public static final int POP_SIZE = 50;

    Population p;
    ArrayList<Obstacle> obstacles;
    int generation, count;
    public static final int WIDTH = 1200, HEIGHT = 900;

    public void setup() {

        p = new Population(POP_SIZE);
        obstacles = new ArrayList<Obstacle>();
        obstacles.add(new Obstacle());
        generation = 1;

        //preTrain();



        //System.out.println("Red is negative blue is positive");
        //System.out.println("The greater the absolute value of the weight is, the thicker the line");

    }

    public void settings(){
        size(SCREEN_WIDTH, HEIGHT);
    }

    private void preTrain() {
        while(p.bestPlayer().getScore()<10000) {
            updateGame();
            applyGeneticAlgorithm();
        }
    }

    public void draw() {
        background(255);

        updateGame();
        applyGeneticAlgorithm();

        drawObstacles();
        drawBirds();

        drawLogistics();
        drawWall();

    }

    private void updateGame() {
        updateObstacles();
        p.update(this);
    }

    private void drawBirds() {
        p.draw(this);
        p.drawBestNet(this);
        count++;
    }

    private void drawWall() {
        stroke(0);
        strokeWeight(10);
        line(WIDTH, 0, WIDTH, HEIGHT);
        strokeWeight(1);
    }

    private void drawLogistics() {

        fill(0);
        textSize(25);
        text("Score: " + count, 30, HEIGHT - 20);
        text("Gen " + generation, 10, 20);

    }

    private void applyGeneticAlgorithm() {
        if (p.allPlayersDead()) {
            p.naturalSelection();
            obstacles = new ArrayList<Obstacle>();
            obstacles.add(new Obstacle());
            generation++;
            count = 0;
        }
    }

    private void drawObstacles() {
        for (Obstacle o : obstacles) {
            o.draw(this);
        }
    }

    private void updateObstacles() {
        Obstacle o;
        for (int i = 0; i < obstacles.size(); i++) {
            o = obstacles.get(i);
            o.update();
            p.isCollided(o);

            if (obstacles.get(i).getX() == LINEAR_DIST_BETWEEN_OBSTACLES) {
                obstacles.add(new Obstacle());
            }

            if (o.getX() < -o.getWidth()) {
                obstacles.remove(i);
                i--;
            }
        }
    }

    public Obstacle getClosestObstacle() {
        Obstacle closest = obstacles.get(0);
        for (Obstacle o : obstacles) {
            if (o.getX() < closest.getX()) closest = o;
        }

        return closest;
    }

    public static void main(String[] args) {
        PApplet.main("Main");
    }

}