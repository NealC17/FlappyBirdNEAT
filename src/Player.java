import processing.core.PApplet;
import processing.core.PVector;

public class Player {
    private static final int NUM_INPUT = 2;
    private static final int NUM_HIDDEN = 6;
    private static final int NUM_OUT = 1;

    private PVector pos, vel,jumpVel, acc;
    private static final float radius = 15;
    private boolean isJumpingUp, isJumpingDown;
    private boolean dead;
    private double score, fitness;
    private NeuralNet brain;
    private boolean isBestBird;

    public Player(){
        pos = new PVector(250,Main.HEIGHT/2);
        vel = new PVector(0,10);
        acc = new PVector(0,0.8f);
        jumpVel=new PVector(vel.x,vel.y);

        this.brain = new NeuralNet(NUM_INPUT,NUM_HIDDEN,NUM_OUT);

    }

    public void draw(PApplet p) {
        if(dead){
            return;
        }
        p.fill(255);

        if(isBestBird){
            p.fill(0);
        }

        p.ellipse(pos.x,pos.y,2*radius,2*radius);
    }

    public void update(Main p){

        if(dead){
            return;
        }
        score++;

        Obstacle closest = p.getClosestObstacle();
        double[] inputs = new double[2];
        inputs[0] = (double)((closest.getX()-pos.x)/Main.WIDTH);
        inputs[1] = (pos.y- closest.getMidPoint())/Main.HEIGHT;

        double[] outputs = brain.feedForward(inputs);
        if (outputs[0]>0.5&&!isJumpingUp) {
            isJumpingUp = true;
            isJumpingDown = false;
        }

        if(!isJumping()){
            pos.add(vel);
        }

        if (isJumpingUp) {
            jumpVel.set(0,-vel.y);
            jumpUp();
        } else if (isJumpingDown) {
            jumpDown();
        }

    }

    public void setFitness(double fitness){
        this.fitness=fitness;
    }

    public void jumpUp() {

        pos.add(jumpVel);
        if (jumpVel.y <= -10) {
            isJumpingDown = true;
            isJumpingUp = false;
        }
        jumpVel.add(acc);
    }

    public void jumpDown() {
        pos.add(jumpVel);
        if (jumpVel.y >= 10) {
            isJumpingDown = false;
        }
        jumpVel.add(acc);
    }
    
    public void isCollided(Obstacle o){
        if((pos.x>o.getX()&&pos.x<o.getX()+o.getWidth())&&!(pos.y-radius>o.getHeight()&&pos.y+radius<o.getHeight()+Obstacle.getGap())){
            dead = true;
        } else if(pos.y<0||pos.y>Main.HEIGHT){
            dead=true;
        }
    }
    private boolean isJumping() {
        return isJumpingUp||isJumpingDown;
    }
    public boolean isDead(){
        return dead;
    }

    public double getScore() {
        return score;
    }

    public Player clone(){
        Player output = new Player();
        output.brain=brain.clone();
        return output;
    }

    public Player crossover(Player parent2) {
        Player child = new Player();
        child.brain = brain.crossover(parent2.brain);
        return child;
    }

    public double getFitness() {
        return fitness;
    }


    public void thisIsTheBestBird(){
        isBestBird=true;
    }

    public void drawNet(Main p) {
        brain.draw(p);
    }
    public void mutate() {
        brain.mutate();
    }



}
