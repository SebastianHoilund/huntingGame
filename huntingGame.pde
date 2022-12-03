Animal hunter;

ArrayList<Animal> prey = new ArrayList<Animal>();
ArrayList<Float> close = new ArrayList<Float>();

int targetObject;

float oldD = width*height;
float newD;

boolean wasEaten = false;
int whoEaten; 

int timer = millis();
int counter = 5000;

int startingPrey = 5; 

int maxPrey = 15;
int hunterstartspeed = 3;
int preystartspeed = 3;

void setup() {

    fullScreen();
    // size(1920,1080);
    // size(640, 480);

    hunter = new Hunter(new PVector(width/2, height/2-350), 0.9, hunterstartspeed);
    
    for (int i = 0; i < startingPrey; ++i) {

        prey.add(new Prey(new PVector(random(width), random(height)), 0.9, preystartspeed));
        close.add(0.1);

    }

} 

void draw() {

    background(200, 200, 200);

    fill(100);
    textAlign(CENTER);
    textSize(70);
    text("New prey in:", width/2, height/2-190); 
    text("Amount of preys:", width/2, height/2+15);
    textSize(55);
    text(millis()/1000 - timer/1000 + " / " + counter/1000, width/2, height/2-100);
    text(prey.size() + " / " + maxPrey, width/2, height/2+100); 

    for (int i = prey.size()-1; i >= 0; --i) {
        Animal newPrey = prey.get(i);

        newPrey.update();
        newPrey.move(hunter);
        newPrey.display(0, 255, 0);

        if (newPrey.isEaten(hunter)) {
            wasEaten = true; 
            whoEaten = i;
            // print(" " + i + " blev spist ");
        }

        close.set(i, dist(hunter.location.x, hunter.location.y, newPrey.location.x, newPrey.location.y));
        newD = close.get(i);
        if (oldD > newD) {
            oldD = newD;
            targetObject = i;

        }

    }

    if (prey.size() != maxPrey) {

        if (millis() - timer > counter) {

            prey.add(new Prey(new PVector(random(width), random(height)), 0.9, preystartspeed));

            // print(" " + prey.size() + " ");
            close.add(0.1);
            timer = millis(); 

        }

    } else {
        timer = millis(); 
        int counter = 0;

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