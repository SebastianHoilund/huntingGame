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

Animal hunter;

ArrayList<Animal> prey = new ArrayList<Animal>();
ArrayList<Float> close = new ArrayList<Float>();

int startingPrey = 5; 

int targetObject;

float oldD = width*height;
float newD;

boolean wasEaten = false;
int whoEaten; 

int timer = millis();
int counter = 15000;

public void setup() {

    // fullScreen();
    
    // size(640,480);
    frameRate(60);

    hunter = new Hunter(new PVector(250, 250), 0.9f, 3);
    
    for (int i = 0; i < startingPrey; ++i) {
        prey.add(new Prey(new PVector(random(width), random(height)), 0.9f, 3));
        close.add(0.1f);

    }


} 

public void draw() {

    background(200, 200, 200);

    fill(100);
    textAlign(CENTER);
    textSize(55);
    text("New prey in:", width/2, height/2-165); 
    text("Amount of preys:", width/2, height/2+15); 
    textSize(70);
    text(millis()/1000 - timer/1000 + " / " + counter/1000, width/2, height/2-100);
    text(prey.size() + " / 10", width/2, height/2+100); 

    for (int i = prey.size()-1; i >= 0; --i) {
        Animal newPrey = prey.get(i);

        newPrey.update();
        newPrey.move(hunter);
        newPrey.display(0, 255, 0);

        if (newPrey.isEaten(hunter)) {
            wasEaten = true; 
            whoEaten = i;
            print(" " + i + " blev spist ");
        }

        close.set(i, dist(hunter.location.x, hunter.location.y, newPrey.location.x, newPrey.location.y));
        newD = close.get(i);
        if (oldD > newD) {
            oldD = newD;
            targetObject = i;
        }

    }

    if (prey.size()-1 != 9) {
        if (millis() - timer > counter) {
            prey.add(new Prey(new PVector(random(width), random(height)), 0.9f, 3));
            print(" " + prey.size() + " ");
            close.add(0.1f);
            timer = millis(); 
        }
    }
    

    hunter.update();
    hunter.move(prey.get(targetObject));
    hunter.display(255, 0, 0);

    oldD = width*height;

    if (wasEaten) {
        wasEaten = false;
        int preySize = prey.size()-1;
        prey.set(whoEaten, prey.get(preySize));
        prey.remove(preySize); 
        close.remove(preySize); 
    }

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
    float oldmaxspeed;

    float range;

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

    public void seek(Animal target) {  }
    public void flee(Animal target) {  }
    public void move(Animal target) {  }
    public void wander() { }
    public boolean isEaten(Animal target) { return false; }

    public void display(int red, int green, int blue) {

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
class Hunter extends Animal {
    
    Hunter(PVector _location, float _maxforce, float _maxspeed) {
        
        location = _location;
        velocity = new PVector(0, 0);
        acceleration = new PVector(0, 0);

        range = 150;

        r = 5;
        maxforce = _maxforce;
        maxspeed = _maxspeed;
        oldmaxspeed = _maxspeed; 
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

        if (location.x < 100) {
            desired = new PVector(maxspeed, velocity.y);

        } else if (location.x > width-100) {
            desired = new PVector(-maxspeed, velocity.y);

        } else if (location.y < 100) {
            desired = new PVector(maxspeed, velocity.x);

        } else if (location.y > height-100) {
            desired = new PVector(-maxspeed, velocity.x);

        } else if (dist(location.x, location.y, target.location.x, target.location.y) < range) {
            seek(target);
            maxspeed *= 1.005f;

        } else if (millis() - timer > counter) {
            wander();
            timer = millis(); 

        } 

        noFill();
        circle(location.x, location.y, range);

        PVector steer = PVector.sub(desired,velocity);
        steer.limit(maxforce);
        applyForce(steer);

    }
    
    public void wander() {

        maxspeed = oldmaxspeed;
        
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

        range = 100;

        r = 5;
        maxforce = _maxforce;
        maxspeed = _maxspeed;
        oldmaxspeed = _maxspeed; 
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

        if (location.x < 100) {
            desired = new PVector(maxspeed, velocity.y);

        } else if (location.x > width-100) {
            desired = new PVector(-maxspeed, velocity.y);

        } else if (location.y < 100) {
            desired = new PVector(maxspeed, velocity.x);

        } else if (location.y > height-100) {
            desired = new PVector(-maxspeed, velocity.x);

        } else if (dist(location.x, location.y, target.location.x, target.location.y) < range) {
            flee(target);
            maxspeed *= 1.01f;

        } else if (millis() - timer > counter) {
            wander();
            timer = millis(); 

        } 

        if (dist(location.x, location.y, target.location.x, target.location.y) > target.range*2) {
            maxspeed = oldmaxspeed;
        }

        noFill();
        circle(location.x, location.y, range);

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

    public boolean isEaten(Animal target) {
        if (dist(target.location.x, target.location.y, location.x, location.y) <= 5) {
            return true;
        } else {
            return false;
        }
    }

}
  public void settings() {  size(1920,1080); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "huntingGame" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
