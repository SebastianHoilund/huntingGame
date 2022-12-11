abstract class Animal {

    // Her specificeres de variabler der skal bruges
    PVector location;
    PVector velocity;
    PVector acceleration;
    PVector desired; 

    int timer;
    int counter;

    float r;
    float maxforce;
    float maxspeed;
    float oldmaxspeed;

    float range;

    boolean hunting;

    void update() {

        velocity.add(acceleration);
        velocity.limit(maxspeed);
        location.add(velocity);
        acceleration.mult(0);

    }

    // Her kaldes de funktioner subclassesne skal bruge 
    void wander() { }
    void seek(Animal target) {  }
    void flee(Animal target) {  }
    void move(Animal target) {  }
    void applyForce(PVector force) { acceleration.add(force); }
    boolean isEaten(Animal target) { return false; }

    // Her tegnes dyrne, og værdierne (red, green, blue) bliver specificeret i subclasesne. 
    void display(int red, int green, int blue) {

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