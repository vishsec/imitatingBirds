package boids;
class Bird {
    Vector position;
    Vector velocity;

    public Bird(Vector position, Vector velocity) {
        this.position = position;
        this.velocity = velocity;
    }
    public void updateVelocity(Bird[] birds, int xMax, int yMax, double cohesionCoefficient, int alignmentCoefficient, double separationCoefficient) {
        velocity = velocity.plus(cohesion(birds,  cohesionCoefficient))
                .plus(alignment(birds, alignmentCoefficient))
                .plus(separation(birds, separationCoefficient))
                .plus(boundPosition(xMax, yMax));
        limitVelocity();
    }
    public void updatePosition() {
        position = position.plus(velocity);
    }
    public Vector cohesion(Bird[] birds, double cohesionCoefficient) {
        Vector pcJ = new Vector(0,0);    // steer towards com(centre of mass)
        int length = birds.length;
        for (int i = 0; i < length; i++)
            pcJ = pcJ.plus(birds[i].position);
        pcJ = pcJ.div(length);
        return pcJ.minus(position).div(cohesionCoefficient);
    }

    //alignment - steer towards the average heading of local flockmates
    public Vector alignment(Bird[] birds, int alignmentCoefficient) {
        Vector pvJ = new Vector(0,0);
        int length = birds.length;
        for (int i = 0; i < length; i++)
            pvJ = pvJ.plus(birds[i].velocity);
        pvJ = pvJ.div(length);
        return pvJ.minus(velocity).div(alignmentCoefficient);
    }

    //separation - steer to avoid crowding local flockmates
    public Vector separation(Bird[] birds, double separationCoefficient) {
        Vector c = new Vector(0,0);
        int length = birds.length;
        for (int i = 0; i < length; i++)
            if ((birds[i].position.minus(position).magnitude()) < separationCoefficient)
                c = c.minus(birds[i].position.minus(position));
        return c;
    }

    //keep birds within a certain area
    public Vector boundPosition(int xMax, int yMax) {
        int x = 0;
        int y = 0;
        if (this.position.data[0] < 0)                x = 10;
        else if (this.position.data[0]  > xMax)       x = -10;
        if (this.position.data[1]  < 0)               y = 10;
        else if (this.position.data[1]  > yMax)       y = -10;
        return new Vector(x,y);
    }

    //limit the magnitude of the boid's  velocity
    public void limitVelocity() {
        int vlim = 100;
        if (this.velocity.magnitude() > vlim) {
            this.velocity = this.velocity.div(this.velocity.magnitude());
            this.velocity = this.velocity.times(vlim);
        }
    }

    public String toString() {
        return new String("Position: " + this.position + " Velocity: " + this.velocity);
    }
}