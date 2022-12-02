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

    // fullScreen();
    frameRate(60);
    // size(1920,1080);
    

    hunter = new Hunter(new PVector(250, 250), 0.9f, 3);
    
    prey = new Prey(new PVector(width-250, height-250), 0.9f, 3.3f);

}

public void draw() {

    background(200, 200, 200);

    hunter.update();
    hunter.move(prey);
    hunter.display();

    prey.update();
    prey.move(hunter);
    prey.display();

}

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

    public void seek(Animal target) {    }
    public void flee(Animal target) {    }
    public void move(Animal target) {    }
    public void wander() {    }

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

        timer = millis();
        counter = 500;

    }

    // Our seek steering force algorithm
    public void seek(Animal target) {

        desired = PVector.sub(target.location,location);
        desired.normalize();
        desired.mult(maxspeed);

    }

    public void move(Animal target) {

        if (location.x < 25) {
            desired = new PVector(maxspeed, velocity.y);

        } else if (location.x > width-25) {
            desired = new PVector(-maxspeed, velocity.y);

        } else if (location.y < 25) {
            desired = new PVector(maxspeed, velocity.x);

        } else if (location.y > height-25) {
            desired = new PVector(-maxspeed, velocity.x);

        } else if (dist(location.x, location.y, target.location.x, target.location.y) < 50) {
            seek(target);
            print("Seek ");

        } else if (millis() - timer > counter) {
            wander();
            timer = millis(); 
            print("Wander Hunter ");

        } 

        PVector steer = PVector.sub(desired,velocity);
        steer.limit(maxforce);
        applyForce(steer);

    }
    
    public void wander() {
        
        float wradius = 25; 
        float wx = wradius*cos(random(360));
        float wy = wradius*sin(random(360));

        desired.sub(new PVector(wx, wy));
        desired.normalize();
        desired.mult(maxspeed);

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

        timer = millis();
        counter = 500;

    }
    
    // Our seek steering force algorithm
    public void flee(Animal target) {

        desired = PVector.sub(target.location,location).mult(-1);
        desired.normalize();
        desired.mult(maxspeed);

    }

    public void move(Animal target) {

        if (location.x < 25) {
            desired = new PVector(maxspeed, velocity.y);

        } else if (location.x > width-25) {
            desired = new PVector(-maxspeed, velocity.y);

        } else if (location.y < 25) {
            desired = new PVector(maxspeed, velocity.x);

        } else if (location.y > height-25) {
            desired = new PVector(-maxspeed, velocity.x);

        } else if (dist(location.x, location.y, target.location.x, target.location.y) < 50) {
            flee(target);
            print("Flee ");

        } else if (millis() - timer > counter) {
            wander();
            timer = millis(); 
            print("Wander Prey ");

        } 

        PVector steer = PVector.sub(desired,velocity);
        steer.limit(maxforce);
        applyForce(steer);

    }
    
    public void wander() {
        
        float wradius = 25; 
        float wx = wradius*cos(random(360));
        float wy = wradius*sin(random(360));

        desired.sub(new PVector(wx, wy));
        desired.normalize();
        desired.mult(maxspeed);

    }

}
  public void settings() {  size(640,480); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "huntingGame" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
