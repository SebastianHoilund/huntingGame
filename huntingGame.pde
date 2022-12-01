Animal hunter, prey;

void setup() {

    fullScreen();
    frameRate(60);
    // size(1920,1080);

    hunter = new Hunter(new PVector(0, 0), 0.9, 3);
    
    prey = new Prey(new PVector(100, 100), 0.9, 5);

}

void draw() {

    background(200, 200, 200);

    hunter.update();
    hunter.seek(prey);
    hunter.display();

    prey.update();
    prey.flee(hunter);
    prey.display();

}

