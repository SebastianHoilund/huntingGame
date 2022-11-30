abstract class Animal {

    PVector location;
    PVector velocity;
    PVector acceleration;
    PVector desired = PVector.sub(target,location);

    float maxspeed;
    float maxforce;

    void seek(PVector target) {
    PVector desired = PVector.sub(target,location);
    desired.normalize();
    desired.mult(maxspeed);
    PVector steer = PVector.sub(desired,velocity);

    steer.limit(maxforce);

    PVector steer = PVector.sub(desired,velocity);
    applyForce(steer);
    }

}