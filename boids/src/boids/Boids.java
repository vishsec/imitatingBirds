package boids;

import edu.wlu.cs.levy.CG.KDTree;
import edu.wlu.cs.levy.CG.KeyDuplicateException;
import edu.wlu.cs.levy.CG.KeySizeException;

import java.awt.*;
import java.util.Random;

/* Boids are used to imitate the movement of a flock of birds
* initially boids have random coordinates within an area
* each boid is assigned position vector determined by width and height rpresenting x and y axis and zero velocity vector*/
class Boids
{
    KDTree kd;     //kd-tree structure is used to find bird's neighbours fast
    boids.Bird[] birds;
    int N;         //  number of boids to process
    int xRes;      // maximum x-coordinate of field
    int yRes;      // maximum y-coordinate of field

    public Boids(int amount, int width, int height)
    {
        N = amount;
        xRes = width;
        yRes = height;
        kd =  new KDTree(2);
        birds = new boids.Bird[N];
        Random rand = new Random();

        for (int i = 0; i < N - 1; i++)
        {
            birds[i] = new boids.Bird(new Vector(rand.nextInt(xRes),rand.nextInt(yRes)), new Vector(0,0));
            try{
                kd.insert(birds[i].position.data, birds[i]);
            } catch (Exception e) {

                System.out.println("Exception caught: " + e);
                e.printStackTrace();
            }
        }
    }
    public void move(int distance, double cohesionCoefficient, int alignmentCoefficient, double separationCoefficient)
    {
        try{
            for (int i = 0; i < N - 1; i++)
            {
                double[] coords = birds[i].position.data;
                boids.Bird[] nbrs = new boids.Bird[distance];
                kd.nearest(coords, distance).toArray(nbrs);
                try {
                    kd.delete(coords);
                } catch (Exception e) {
                    // we ignore this exception on purpose
                    System.out.println("KeyMissingException deleting caught: " + e + e.getMessage());
                }
                birds[i].updateVelocity(nbrs, xRes, yRes, cohesionCoefficient, alignmentCoefficient, separationCoefficient);
                birds[i].updatePosition();
                kd.insert(birds[i].position.data, birds[i]);
            }
            kd = new KDTree(2);
            for (int i = 0; i < N - 1; i++)
                kd.insert(birds[i].position.data, birds[i]);
        } catch (KeySizeException | KeyDuplicateException e) {
            System.out.println("KeySizeException/KeyDuplicateException caught: " + e + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Unknown exception caught: ");
            e.printStackTrace();
        }
    }
    public void draw(Graphics g)
    {
        for (int i = 0; i < N - 1; i++)
        {
            int x = (int) birds[i].position.data[0];
            int y = (int) birds[i].position.data[1];
            g.drawLine(x, y, x, y);
        }
    }
}