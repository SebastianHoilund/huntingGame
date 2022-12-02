abstract class Animal {


    PVector location;
    PVector velocity;
    PVector acceleration;
    PVector desired; 

    int timer;
    int counter;

    // Additional variable for size
    float r;
    float maxforce;
    float maxspeed;
    float oldmaxspeed;

    float range;

    // Our standard “Euler integration” motion model
    void update() {

        velocity.add(acceleration);
        velocity.limit(maxspeed);
        location.add(velocity);
        acceleration.mult(0);

    }

    // Newton’s second law; we could divide by mass if we wanted.
    void applyForce(PVector force) {

        acceleration.add(force);

    }

    void seek(Animal target) {  }
    void flee(Animal target) {  }
    void move(Animal target) {  }
    void wander() { }
    boolean isEaten(Animal target) { return false; }

    void display(int red, int green, int blue) {

        // Vehicle is a triangle pointing in
        // the direction of velocity; since it is drawn
        // pointing up, we rotate it an additional 90 degrees.
        float theta = velocity.heading() + PI/2;
        fill(red, green, blue);
        stroke(0);
        pushMatrix();
        translate(location.x,location.y);
        rotate(theta);
        beginShape();
        vertex(0, -r*2);
        vertex(-r, r*2);
        vertex(r, r*2);
        endShape(CLOSE);
        popMatrix();

    }

}