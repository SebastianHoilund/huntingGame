Animal hunter, prey;

void setup() {

    // fullScreen();
    frameRate(60);
    size(1920,1080);
    // size(640,480);

    hunter = new Hunter(new PVector(250, 250), 0.9, 3);
    
    prey = new Prey(new PVector(width-250, height-250), 0.9, 3.3);

}

void draw() {

    background(200, 200, 200);

    hunter.update();
    hunter.move(prey);
    hunter.display();

    prey.update();
    prey.move(hunter);
    prey.display();

}

