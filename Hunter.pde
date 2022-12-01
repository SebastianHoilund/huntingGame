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
    void seek(Animal target) {

        desired = PVector.sub(target.location,location);
        desired.normalize();
        desired.mult(maxspeed);

        PVector steer = PVector.sub(desired,velocity);
        steer.limit(maxforce);
        applyForce(steer);

    }

}