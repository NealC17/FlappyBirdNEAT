import processing.core.PApplet;

public class Population {

    private int size;
    private Player[] population;
    private double sum;

    public Population(int size) {
        this.size = size;
        population = new Player[size];

        for (int i = 0; i < population.length; i++) {
            population[i] = new Player();
        }
    }


    public void update(Main p) {
        for (Player pl : population) {
            pl.update(p);
        }
    }

    public void draw(Main p) {
        for (Player pl : population) {
            pl.draw(p);
        }


    }

    public void drawBestNet(Main p){
        bestPlayer().drawNet(p);
    }


    public void isCollided(Obstacle o) {
        for (Player pl : population) {
            pl.isCollided(o);
        }
    }

    public boolean allPlayersDead() {
        for (Player pl : population) {
            if (!pl.isDead()) {
                return false;
            }
        }
        return true;
    }

    public void naturalSelection() {

        Player[] nextGen = new Player[size];

        calculateFitness();

        nextGen[0] = bestPlayer();

        for (int i = 1; i < size; i++) {
            nextGen[i] = selectPlayer().clone();

            nextGen[i].mutate();
        }
        population = nextGen;
    }

    public Player bestPlayer() {
        Player p = population[0];
        for (Player pl : population) {
            if (pl.getFitness() > p.getFitness() || pl.getScore()>p.getScore()) {
                p = pl;
            }
        }
        //p.thisIsTheBestBird();



        return p;
    }


    private Player selectPlayer() {
        double rand = Math.random();
        int index = 0;

        while (rand > 0) {
            rand -= population[index].getFitness();
            index++;
        }
        index--;

        return population[index];
    }

    private void calculateFitness() {

        sum = 0;

        for (Player pl : population) {
            sum += pl.getScore();
        }


        for (Player pl : population) {
            pl.setFitness(pl.getScore() / sum);
        }
    }

}
