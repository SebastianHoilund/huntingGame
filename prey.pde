class Prey extends Animal {
    
    Prey(PVector _location, float _maxforce, float _maxspeed) {
        
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
    void flee(Animal target) {

        desired = PVector.sub(target.location,location).mult(-1);
        desired.normalize();
        desired.mult(maxspeed);

    }

    void move(Animal target) {

        if (location.x < 25) {
            desired = new PVector(maxspeed, velocity.y);

        } else if (location.x > width-25) {
            desired = new PVector(-maxspeed, velocity.y);

        } else if (location.y < 25) {
            desired = new PVector(maxspeed, velocity.x);

        } else if (location.y > height-25) {
            desired = new PVector(-maxspeed, velocity.x);

        } else if (dist(location.x, location.y, target.location.x, target.location.y) < range) {
            flee(target);
            maxspeed *= 1.001;

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
    
    void wander() {

        float wradius = 25; 
        float wx = wradius*cos(random(360));
        float wy = wradius*sin(random(360));

        desired.sub(new PVector(wx, wy));
        desired.normalize();
        desired.mult(maxspeed);

    }

    boolean isEaten(Animal target) {
        if (dist(target.location.x, target.location.y, location.x, location.y) <= 5) {
            return true;
        } else {
            return false;
        }
    }

}