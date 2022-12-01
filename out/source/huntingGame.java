import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class huntingGame extends PApplet {

Animal hunter, prey;

public void setup() {

    
    frameRate(60);
    // size(1920,1080);

    hunter = new Hunter(new PVector(0, 0), 0.9f, 3);
    
    prey = new Prey(new PVector(100, 100), 0.9f, 5);

}

public void draw() {

    background(200, 200, 200);

    hunter.update();
    hunter.seek(prey);
    hunter.display();

    prey.update();
    prey.flee(hunter);
    prey.display();

}

abstract class Animal {


    PVector location;
    PVector velocity;
    PVector acceleration;
    PVector desired; 

    // Additional variable for size
    float r;
    float maxforce;
    float maxspeed;

    // Our standard “Euler integration” motion model
    public void update() {

        velocity.add(acceleration);
        velocity.limit(maxspeed);
        location.add(velocity);
        acceleration.mult(0);

    }

    // Newton’s second law; we could divide by mass if we wanted.
    public void applyForce(PVector force) {

        acceleration.add(force);

    }

    public void seek(Animal target) {}
    public void flee(Animal target) {}

    public void display() {

        // Vehicle is a triangle pointing in
        // the direction of velocity; since it is drawn
        // pointing up, we rotate it an additional 90 degrees.
        float theta = velocity.heading() + PI/2;
        fill(175);
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
class Hunter extends Animal {
    
    Hunter(PVector _location, float _maxforce, float _maxspeed) {
        
        location = _location;
        velocity = new PVector(0, 0);
        acceleration = new PVector(0, 0);

        r = 5;
        maxforce = _maxforce;
        maxspeed = _maxspeed;
        desired = new PVector(width/2, height/2);

    }

    // Our seek steering force algorithm
    public void seek(Animal target) {

        desired = PVector.sub(target.location,location);
        desired.normalize();
        desired.mult(maxspeed);

        PVector steer = PVector.sub(desired,velocity);
        steer.limit(maxforce);
        applyForce(steer);

    }

}
class Prey extends Animal {
    
    Prey(PVector _location, float _maxforce, float _maxspeed) {
        
        location = _location;
        velocity = new PVector(0, 0);
        acceleration = new PVector(0, 0);

        r = 5;
        maxforce = _maxforce;
        maxspeed = _maxspeed;
        desired = new PVector(width/2, height/2);

    }
    
    // Our seek steering force algorithm
    public void flee(Animal target) {

        desired = PVector.sub(target.location,location).mult(-1);
        desired.normalize();
        desired.mult(maxspeed);

        PVector steer = PVector.sub(desired,velocity);
        steer.limit(maxforce);
        applyForce(steer);

    }
}
  public void settings() {  fullScreen(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "huntingGame" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
